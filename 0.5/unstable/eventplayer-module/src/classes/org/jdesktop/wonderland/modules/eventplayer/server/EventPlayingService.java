/**
 * Project Wonderland
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * Sun designates this particular file as subject to the "Classpath"
 * exception as provided by Sun in the License file that accompanied
 * this code.
 */
package org.jdesktop.wonderland.modules.eventplayer.server;

import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.impl.util.AbstractService;
import com.sun.sgs.kernel.ComponentRegistry;
import com.sun.sgs.impl.sharedutil.LoggerWrapper;
import com.sun.sgs.impl.util.TransactionContext;
import com.sun.sgs.impl.util.TransactionContextFactory;
import com.sun.sgs.kernel.KernelRunnable;
import com.sun.sgs.service.Transaction;
import com.sun.sgs.service.TransactionProxy;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.MultipleParentException;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.common.messages.MessagePacker.ReceivedMessage;
import org.jdesktop.wonderland.modules.eventplayer.server.EventPlayingManager.ChangeReplayingListener;
import org.jdesktop.wonderland.modules.eventplayer.server.wfs.RecordingLoaderUtils;
import org.jdesktop.wonderland.server.WonderlandContext;
import org.jdesktop.wonderland.server.cell.CellMO;
import org.jdesktop.wonderland.server.cell.CellMOFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A service to support the event player.
 * This class is responsible for replaying messages that have been parsed from the recording.
 * @author Bernard Horan
 */
public class EventPlayingService extends AbstractService {
    private static enum Action {
        COMPLETED,
        MESSAGE,
        LOAD_CELL,
        UNLOAD_CELL
        };


    /** The name of this class. */
    private static final String NAME = EventPlayingService.class.getName();

    /** The package name. */
    private static final String PKG_NAME = "org.jdesktop.wonderland.modules.eventplayer.server";

    /** The logger for this class. */
    private static final LoggerWrapper logger =
            new LoggerWrapper(Logger.getLogger(PKG_NAME));

    /** The name of the version key. */
    private static final String VERSION_KEY = PKG_NAME + ".service.version";

    /** The major version. */
    private static final int MAJOR_VERSION = 1;

    /** The minor version. */
    private static final int MINOR_VERSION = 0;
    
    /** manages the context of the current transaction */
    private TransactionContextFactory<EventPlayingTransactionContext> ctxFactory;

    /** executes the actual remote calls */
    private ExecutorService executor;

    /**Manages the replaying threads*/
    private Map<String, ReplayChanges> replayers = new HashMap<String, ReplayChanges>();



    public EventPlayingService(Properties props,
            ComponentRegistry registry,
            TransactionProxy proxy) {
        super(props, registry, proxy, logger);


        logger.log(Level.CONFIG, "Creating EventPlayingService properties:{0}",
                props);

        // create the transaction context factory
        ctxFactory = new TransactionContextFactoryImpl(proxy);

        try {
            /*
             * Check service version.
             */
            transactionScheduler.runTask(new KernelRunnable() {

                public String getBaseTaskType() {
                    return NAME + ".VersionCheckRunner";
                }

                public void run() {
                    checkServiceVersion(
                            VERSION_KEY, MAJOR_VERSION, MINOR_VERSION);
                }
            }, taskOwner);
        } catch (Exception ex) {
            logger.logThrow(Level.SEVERE, ex, "Error creating service");
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void doReady() {
        // create the executor thread
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void doShutdown() {
        // stop the executor, attempting an orderly shutdown
        boolean shutdown = false;
        executor.shutdown();

        try {
           shutdown = executor.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            // ignore
        }

        if (!shutdown) {
            List<Runnable> leftover = executor.shutdownNow();
            logger.log(Level.WARNING, "Terminating executor with tasks in" +
                       "  progress: " + leftover);
        }
    }

    @Override
    protected void handleServiceVersionMismatch(Version oldVersion,
            Version currentVersion) {
        throw new IllegalStateException(
                "unable to convert version:" + oldVersion +
                " to current version:" + currentVersion);
    }

    void pauseChanges(String tapeName, ChangeReplayingListener listener) {
        logger.getLogger().info("tapename: " + tapeName);
        //Check to see if we've already started replaying these changes
        ReplayChanges rc = replayers.get(tapeName);
        if (rc == null) {
            logger.logThrow(Level.SEVERE, new RuntimeException("No changes of that name being played"), appName);
        } else {
            rc.pause();
        }
    }

    void replayChanges(String tapeName, ChangeReplayingListener listener) {
        logger.getLogger().info("tapename: " + tapeName);

        //Check to see if we've already started replaying these changes
        ReplayChanges rc = replayers.get(tapeName);
        if (rc != null) {
            //Just resume replaying
            rc.resume();
        } else {
            //create a new replayChanges instance and set it going
            if (!(listener instanceof ManagedObject)) {
                listener = new ManagedChangesReplayingWrapper(listener);
            }
            // create a reference to the listener
            ManagedReference<ChangeReplayingListener> scl =
                    dataService.createReference(listener);

            // now add the recording request to the transaction.  On commit
            // this request will be passed on to the executor for long-running
            // tasks
            rc = new ReplayChanges(tapeName, scl.getId());
            ctxFactory.joinTransaction().add(rc);
            replayers.put(tapeName, rc);
        }
    }

    /**
     * A task that replays a stream of changes
     * The results are then passed to a
     * NotifyChangesReplayingListener identified by managed reference.
     */
    private class ReplayChanges extends ChangeReplayer implements Runnable {
        private String tapeName;
        private BigInteger listenerID;
        /*
     * The time of the last message that was played or, if no message
     * has yet been played, the time of the start of the playback
     */
        private long recordingStartTime;
        private long playbackStartTime;
        private XMLEventReader xmlReader;
        private boolean paused = false;

        /** time elapsed from start to pause */
        private long timeElapsed = 0l;

        private ReplayChanges(String tapeName, BigInteger id) {
            this.tapeName = tapeName;
            this.listenerID = id;
        }

        public void run() {
            
            try {
                Reader recordingReader = RecordingLoaderUtils.getRecordingInputReader(tapeName);
                XMLInputFactory factory = XMLInputFactory.newInstance();
                xmlReader = factory.createXMLEventReader(recordingReader);
                EventHandler handler = new EventHandler(this);
                StaxEventDispatcher dispatcher = new StaxEventDispatcher(handler);
                Semaphore semaphore = new Semaphore(1, true);
                boolean oldPausedState = paused;

                //iterate as long as there are more events on the input
                while (xmlReader.hasNext()) {
                    if (oldPausedState != paused) {
                        logger.getLogger().info("PAUSED STATE CHANGED TO: " + paused);
                        oldPausedState = paused;
                    }
                    if (!paused) {
                        XMLEvent e = xmlReader.nextEvent();
                        /*
                         * This will end up calling one of the ChangeReplayer methods
                         * on myself, and thus creating a delayed task.
                         * The semaphore will be released when the task has been completed
                         */
                        dispatcher.dispatchEvent(e, semaphore);
                        logger.getLogger().info("waiting for semaphore");
                        semaphore.acquire();
                        logger.getLogger().info("aquired permit from semaphore");
                    }
                    
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(EventPlayingService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                logger.getLogger().log(Level.SEVERE, "failed due to SAX exception", ex);
            } catch (XMLStreamException ex) {
                logger.getLogger().log(Level.SEVERE, "Failed due to XML Streaming exception", ex);
            } catch (JAXBException ex) {
                logger.getLogger().log(Level.SEVERE, "failed due to JAXB exception", ex);
            } catch (IOException ex) {
                logger.getLogger().log(Level.SEVERE, "failed due to IO exception", ex);
            }
            //Remove myself from the map of replayers
            logger.getLogger().info("Completed run, removing myself");
            replayers.remove(tapeName);
        }



        @Override
        public void playMessage(ReceivedMessage rMessage, long timestamp, Semaphore semaphore) {
            long startTime = timestamp - recordingStartTime + playbackStartTime;
            // notify the listener
            NotifyChangesReplayingListener notify =
                    new NotifyChangesReplayingListener(rMessage, listenerID, semaphore);
            try {
                transactionScheduler.scheduleTask(notify, taskOwner, startTime);
            } catch (Exception ex) {
                logger.logThrow(Level.WARNING, ex, "Error calling listener");
            }
        }

        @Override
        public void startChanges(long startTime, Semaphore semaphore) {
            playbackStartTime = new Date(new java.util.Date().getTime()).getTime();
            recordingStartTime = startTime;
            logger.getLogger().info("releasing semaphore");
    }

        @Override
        public void endChanges(long timestamp, Semaphore semaphore) {
            long startTime = timestamp - recordingStartTime + playbackStartTime;
            // notify the listener
            NotifyChangesReplayingListener notify =
                    new NotifyChangesReplayingListener(listenerID, semaphore);
            try {
                transactionScheduler.scheduleTask(notify, taskOwner, startTime);
            } catch (Exception ex) {
                logger.logThrow(Level.WARNING, ex, "Error calling listener");
            }
        }

        @Override
        public void loadCell(String setupInfo, long timestamp, Semaphore semaphore) {
            //logger.getLogger().info("loadCell");
            long startTime = timestamp - recordingStartTime + playbackStartTime;
            //Need to remove the first line of the string, the processing instruction
            //Beats me, too.
            int index = setupInfo.indexOf('>');
            setupInfo = setupInfo.substring(index + 1);
            //logger.info("setupInfo: " + setupInfo);
            CellServerState setup;
            try {
                setup = CellServerState.decode(new StringReader(setupInfo));
            } catch (JAXBException ex) {
                logger.getLogger().log(Level.SEVERE, "failed to parse cell server state", ex);
                return;
            }
            // notify the listener
            NotifyChangesReplayingListener notify =
                    new NotifyChangesReplayingListener(setup, listenerID, semaphore);
            try {
                transactionScheduler.scheduleTask(notify, taskOwner, startTime);
            } catch (Exception ex) {
                logger.logThrow(Level.WARNING, ex, "Error calling listener");
            }
        }

        @Override
        public void unloadCell(CellID cellID, long timestamp, Semaphore semaphore) {
            //logger.getLogger().info("unloadCell: " + cellID);
            long startTime = timestamp - recordingStartTime + playbackStartTime;
            // notify the listener
            NotifyChangesReplayingListener notify =
                    new NotifyChangesReplayingListener(cellID, listenerID, semaphore);
            try {
                transactionScheduler.scheduleTask(notify, taskOwner, startTime);
            } catch (Exception ex) {
                logger.logThrow(Level.SEVERE, ex, "Error calling listener");
            }
        }

        private void resume() {
            logger.getLogger().info("Resuming playback for: " + tapeName);
            playbackStartTime = timeElapsed + new Date(new java.util.Date().getTime()).getTime();
            paused = false;
        }

        private void pause() {
            logger.getLogger().info("Pausing playback for: " + tapeName);
            timeElapsed = playbackStartTime - new Date(new java.util.Date().getTime()).getTime();
            paused = true;
        }
    }


    

    /**
     * A wrapper around the ChangeReplayingListener as a managed object.
     * This assumes a serializable ChangeReplayingListener
     */
    private static class ManagedChangesReplayingWrapper
            implements ChangeReplayingListener, ManagedObject, Serializable
    {
        private ChangeReplayingListener wrapped;

        public ManagedChangesReplayingWrapper(ChangeReplayingListener listener)
        {
            wrapped = listener;
        }

        public void playMessage(ReceivedMessage message) {
            wrapped.playMessage(message);
        }

        public void allChangesPlayed() {
            wrapped.allChangesPlayed();
        }

        public void unloadCell(CellID cellID) {
            wrapped.unloadCell(cellID);
        }

        public void loadedCell(CellID oldCellID, CellID newCellID) {
            wrapped.loadedCell(oldCellID, newCellID);
        }

        
    }

    /**
     * A task to notify a ChangeReplayingListener
     */
    private class NotifyChangesReplayingListener implements KernelRunnable {
        private ReceivedMessage message;
        private BigInteger listenerID;
        private Action actionType;
        private CellServerState setup;
        private CellID cellID;
        private Semaphore semaphore;

        private NotifyChangesReplayingListener(BigInteger listenerID, Semaphore semaphore) {
            this.listenerID = listenerID;
            this.semaphore = semaphore;
            actionType = Action.COMPLETED;
        }

        private NotifyChangesReplayingListener(ReceivedMessage message, BigInteger listenerID, Semaphore semaphore) {
            this.message = message;
            this.listenerID = listenerID;
            this.semaphore = semaphore;
            actionType = Action.MESSAGE;
        }

        private NotifyChangesReplayingListener(CellID cellID, BigInteger listenerID, Semaphore semaphore) {
            this.cellID = cellID;
            this.listenerID = listenerID;
            this.semaphore = semaphore;
            actionType = Action.UNLOAD_CELL;
        }

        private NotifyChangesReplayingListener(CellServerState setup, BigInteger listenerID, Semaphore semaphore) {
            this.listenerID = listenerID;
            this.setup = setup;
            this.semaphore = semaphore;
            actionType = Action.LOAD_CELL;
        }

        public String getBaseTaskType() {
            return NAME + ".REPLAY_CHANGES_LISTENER";
        }

        public void run() throws Exception {
            ManagedReference<?> lr =
                    dataService.createReferenceForId(listenerID);
            ChangeReplayingListener l =
                    (ChangeReplayingListener) lr.get();

            try {
                switch (actionType) {
                    case COMPLETED:
                        l.allChangesPlayed();
                        break;
                    case MESSAGE:
                        l.playMessage(message);
                        break;
                    case LOAD_CELL:
                        loadCell(l);
                        break;
                    case UNLOAD_CELL:
                        l.unloadCell(cellID);
                        break;
                    default:
                        throw new RuntimeException("Unknown case in switch, actionType: " + actionType);
                }
            } finally {
                // clean up
                if (l instanceof ManagedChangesReplayingWrapper) {
                    dataService.removeObject(l);
                }
                logger.getLogger().info("releasing semaphore");
                semaphore.release();
            }
        }

        private void loadCell(ChangeReplayingListener listener) {
            /*
             * Create the cell and pass it the setup information
             */
            String className = setup.getServerClassName();
            //logger.getLogger().info("className: " + className);
            CellMO cellMO = null;
            try {
                cellMO = CellMOFactory.loadCellMO(className);
            } catch (Exception e) {
                logger.getLogger().log(Level.SEVERE, "Failed to load cell: " + className, e);
                return;
            }
            //logger.getLogger().info("created cellMO: " + cellMO);
            if (cellMO == null) {
                /* Log a warning and move onto the next cell */
                logger.getLogger().severe("Unable to load cell MO: " + className);
                return;
            }
            /* Call the cell's setup method */
            try {
                cellMO.setServerState(setup);
            } catch (ClassCastException cce) {
                logger.getLogger().log(Level.WARNING, "Error setting up new cell " +
                        cellMO.getName() + " of type " +
                        cellMO.getClass(), cce);
                return;
            }
            try {
                WonderlandContext.getCellManager().insertCellInWorld(cellMO);
            } catch (MultipleParentException ex) {
                logger.getLogger().log(Level.SEVERE, "A cell cannot have multiple parents", ex);
            }
            String idString = setup.getMetaData().get("CellID");
            //logger.getLogger().info("Old cellID value: " + idString);
            long id = Long.valueOf(idString);
            //logger.getLogger().info("Old cellID id: " + id);
            CellID oldCellID = new CellID(id);
            //logger.getLogger().info("Old cellID: " + oldCellID);
            CellID newCellID = cellMO.getCellID();
            //logger.getLogger().info("New cellID: " + newCellID);
            listener.loadedCell(oldCellID, newCellID);
        }
    }

    

    /**
     * Transaction state
     */
    private class EventPlayingTransactionContext extends TransactionContext {
        List<Runnable> changes;

        public EventPlayingTransactionContext(Transaction txn) {
            super (txn);

            changes = new LinkedList<Runnable>();
        }

        public void add(Runnable change) {
            changes.add(change);
        }

        @Override
        public void abort(boolean retryable) {
            logger.getLogger().severe("retryable: " + retryable);
            changes.clear();
        }

        @Override
        public void commit() {
            for (Runnable r : changes) {
                executor.submit(r);
            }

            changes.clear();
            isCommitted = true;
        }
    }

    /** Private implementation of {@code TransactionContextFactory}. */
    private class TransactionContextFactoryImpl
            extends TransactionContextFactory<EventPlayingTransactionContext> {

        /** Creates an instance with the given proxy. */
        TransactionContextFactoryImpl(TransactionProxy proxy) {
            super(proxy, NAME);

        }

        /** {@inheritDoc} */
        protected EventPlayingTransactionContext createContext(Transaction txn) {
            return new EventPlayingTransactionContext(txn);
        }
    }

    private class StaxEventDispatcher {

        private EventHandler handler;

        public StaxEventDispatcher(EventHandler handler) {
            this.handler = handler;
        }

        public void dispatchEvent(XMLEvent event, Semaphore semaphore) throws SAXException {
            switch (event.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    dispatchStartElement(event.asStartElement(), semaphore);
                    break;
                case XMLEvent.END_ELEMENT:
                    dispatchEndElement(event.asEndElement(), semaphore);
                    break;
                case XMLEvent.CHARACTERS:
                    dispatchCharacters(event.asCharacters(), semaphore);
                    break;
                default:
                    //logger.getLogger().warning("Unhandled event type: " + getEventTypeString(event.getEventType()));
                    logger.getLogger().info("releasing semaphore");
                    semaphore.release();

            }
        }

        private void dispatchCharacters(Characters characters, Semaphore semaphore) throws SAXException {
            //logger.getLogger().info("dispatch characters: " + characters.getData());

            char[] data = characters.getData().toCharArray();
            if (characters.isIgnorableWhiteSpace()) {
                //handler.ignorableWhitespace(data, 0, data.length);
                return;
            }
            handler.characters(data, 0, data.length, semaphore);
        }

        private void dispatchEndElement(EndElement endElement, Semaphore semaphore) throws SAXException {
            //logger.getLogger().info("dispatch end element: " + endElement.getName());

            QName qName = endElement.getName();
            handler.endElement(qName.getNamespaceURI(), qName.getLocalPart(), qName.toString(),  semaphore);
        }

        private void dispatchStartElement(StartElement startElement, Semaphore semaphore) throws SAXException {
            //logger.getLogger().info("dispatch start element: " + startElement.getName());
            QName qName = startElement.getName();
            AttributesImpl attributes = new AttributesImpl();
            Iterator atttributeIterator = startElement.getAttributes();
            while (atttributeIterator.hasNext()) {
                Attribute attribute = (Attribute) atttributeIterator.next();
                QName attributeQName = attribute.getName();
                String type = attribute.getDTDType();
                if (type == null) {
                    type = "CDATA";
                }
                attributes.addAttribute(attributeQName.getNamespaceURI(), attributeQName.getLocalPart(), attributeQName.toString(), type, attribute.getValue());
            }
            handler.startElement(qName.getNamespaceURI(), qName.getLocalPart(), qName.toString(), attributes, semaphore);
        }

        private String getEventTypeString(int eventType) {
        switch (eventType) {
            case XMLEvent.START_ELEMENT:
                return "START_ELEMENT";
            case XMLEvent.END_ELEMENT:
                return "END_ELEMENT";
            case XMLEvent.PROCESSING_INSTRUCTION:
                return "PROCESSING_INSTRUCTION";
            case XMLEvent.CHARACTERS:
                return "CHARACTERS";
            case XMLEvent.COMMENT:
                return "COMMENT";
            case XMLEvent.START_DOCUMENT:
                return "START_DOCUMENT";
            case XMLEvent.END_DOCUMENT:
                return "END_DOCUMENT";
            case XMLEvent.ENTITY_REFERENCE:
                return "ENTITY_REFERENCE";
            case XMLEvent.ATTRIBUTE:
                return "ATTRIBUTE";
            case XMLEvent.DTD:
                return "DTD";
            case XMLEvent.CDATA:
                return "CDATA";
            case XMLEvent.SPACE:
                return "SPACE";
        }
        return "UNKNOWN_EVENT_TYPE " + "," + eventType;
    }
    }
}
