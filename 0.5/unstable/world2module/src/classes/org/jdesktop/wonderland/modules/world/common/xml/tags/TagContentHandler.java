package org.jdesktop.wonderland.modules.world.common.xml.tags;

import java.io.FileFilter;
import javax.xml.stream.XMLStreamWriter;
import org.jdesktop.wonderland.modules.world.server.converter.ModuleMetaData;
import org.jdesktop.wonderland.modules.world.server.converter.ServerMetaData;
import org.jdesktop.wonderland.modules.world.common.xml.XmlStateAttributes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This interface represents an object which handles the content of certain tags. 
 * When tags are encountered which this TagContentHandler is registered to modify then
 * it will be informed of the opening and closing of that tag as well as any content
 * between the tags.
 *
 * @author Carl Jokl
 */
public interface TagContentHandler {

    /**
     * Due to the fact that TagContentHandlers may need to be implemented no-argument
     * constructors in order to load them dynamically from modules this method
     * is provided to perform any needed initialisation for the TagContentHandler
     * instead of the constructor (if any is needed).
     * 
     * If this method is not required by the implementation then an empty implementation
     * can be used just returning true.
     * 
     * @param moduleMetaData The meta-data about the Module to be created which may be needed by the TagContentHandler.
     * @param serverMetaData The meta-data about the Wonderland server on which may be needed by the TagContentHandler.
     * @return True if the TagContentHandler was successfully initialised.
     */
    public boolean init(ModuleMetaData moduleMetaData, ServerMetaData serverMetaData);

    /**
     * Set the DependentResourceNotificationListener to listen for notifications of resources
     * which are identified in an XML document as a dependent resource.
     *
     * @param dependencyListener The listener which is notified about dependent resources.
     * @return True if the DependentResourceNotificationListener was able to be set or false
     *         if this TagContentHandler does not support having a DependentResourceNotificationListener.
     */
    public boolean setDependencyListener(DependentResourceNotificationListener dependencyListener);

    /**
     * Get the XML root tags with which this tag handler is compatible. This is intended to cover scenarios
     * whereby the same unqualified XML tag is being used in different kinds of XML documents. This mechanism
     * allows multiple tag handlers to handle an XML tag with the same name as long as the root XML tag of
     * the document is different. The values returned from this method identifies which XML documents this tag
     * handler is intended to work with (using the root XML tag). This does not mean that the tag handler
     * will be informed whenever any of the root XML tags are encountered in a document unless the desired root
     * tag is also present in the array returned by getHandledTags().
     * 
     * @return Get an array of the XML root tags or documents supported by this XML tag handler. If this XML tag
     *         handler is not bound to any specific documents / root tags then null can be returned and if null
     *         is returned then tag event notifications will not be filtered by root tag.
     */
    public String[] getCompatibleRootTags();

    /**
     * Get the name of the tags which this TagContentHandler is setup to handle.
     *
     * @return An array of the local names or qualified names of tags which this TagContentHandler
     *         is intended to handle.
     */
    public String[] getHandledTags();

    /**
     * Get a FileFiter which is used to filter out those files for which this TagContentHandler is
     * setup to be able to handle.
     *
     * @return A FileFiter which filters out only those files which this TagHandler is setup to handle.
     */
    public FileFilter getHandledFiles();
    
    /**
     * Handle these characters which occur after the start tag of a tag which this tag handler
     * is specified to handle.
     *
     * @param ch The character buffer containing the characters to be handled.
     * @param start The start index of the characters to be handled.
     * @param length The number of characters to be handled.
     * @param destination The destination to which content processed by this TagContentHandler
     *                    can be sent.
     * @param state The XmlStateAttributes which is an object providing access to some of the parser state which might be useful for
     *              the tag handler.
     * @return Whether the parser / handler which called this method should continue with its own handling logic
     *         which would have run if no handler hand been found. This can be writing the characters to a destination
     *         if True is returned the normal logic will still be followed to process the characters. If false the
     *         parser / handler will delegate all handling of the data to the TagContentHandler.
     * @throws SAXException If an error occurs while handing the characters.
     */
    public boolean characters(char[] ch, int start, int length, XMLStreamWriter destination, XmlStateAttributes state) throws SAXException;

    /**
     * Handle these ignorable whitespace characters after the start tag of the tag which tag handler
     * is specified to handle.
     *
     * @param ch The character buffer containing the characters to be handled.
     * @param start The start index from which to start handling characters.
     * @param length The number of characters to be handled.
     * @param destination The destination to which content processed by this TagContentHandler
     *                    can be sent.
     * @param state The XmlStateAttributes which is an object providing access to some of the parser state which might be useful for
     *              the tag handler.
     * @return Whether the parser / handler which called this method should continue with its own handling logic
     *         which would have run if no handler hand been found. This can be writing the characters to a destination
     *         if True is returned the normal logic will still be followed to process the characters. If false the
     *         parser / handler will delegate all handling of the data to the TagContentHandler.
     * @throws SAXException If an error occurs while handling the characters.
     */
    public boolean ignorableWhitespace(char[] ch, int start, int length, XMLStreamWriter destination, XmlStateAttributes state) throws SAXException;

    /**
     * Handle the start of this specified tag (which this handler is registered to handle).
     *
     * @param uri The URI for the namespace of the tag.
     * @param localName The local tag name.
     * @param qName The qualified tag name.
     * @param atts The attributes of the tag.
     * @param destination The destination to which content processed by this TagContentHandler
     *                    can be sent.
     * @param state The XmlStateAttributes which is an object providing access to some of the parser state which might be useful for
     *              the tag handler.
     * @return Whether the parser / handler which called this method should continue with its own handling logic
     *         which would have run if no handler hand been found. This can be writing the start element to the destination
     *         if True is returned the normal logic will still be followed to process the start element. If false the
     *         parser / handler will delegate all handling of the data to the TagContentHandler.
     * @throws SAXException If an error occurs while handling this tag.
     */
    public boolean startElement(String uri, String localName, String qName, Attributes atts, XMLStreamWriter destination, XmlStateAttributes state) throws SAXException;

    /**
     * Handle the end of this specified tag (which this handler is registered to handle).
     *
     * @param uri The URI for the namespace of the tag.
     * @param localName The local tag name.
     * @param qName The qualified tag name.
     * @param destination The destination to which content processed by this TagContentHandler
     *                    can be sent.
     * @param state The XmlStateAttributes which is an object providing access to some of the parser state which might be useful for
     *              the tag handler.
     * @return Whether the parser / handler which called this method should continue with its own handling logic
     *         which would have run if no handler hand been found. This can be writing the end element to the destination
     *         if True is returned the normal logic will still be followed to process the end element. If false the
     *         parser / handler will delegate all handling of the data to the TagContentHandler.
     * @throws SAXException If an error occurs while handling this tag.
     */
    public boolean endElement(String uri, String localName, String qName, XMLStreamWriter destination, XmlStateAttributes state) throws SAXException;

    /**
     * This method is used to inform a TagContentHandler when XML content handling has switched to another
     * TagContentHandler. This normally happens if while inside an XML tag which is handled by this
     * TagContentHandler another XML tag is encountered for which a different TagContentHandler is registered 
     * to handle. When that happens this method is called and the argument is the TagContentHandler which
     * has taken over handing XML tag content.
     *
     * The Tag content handler is not required to do anything as a result of being notified but this
     * notification is given in case the logic of the TagContentHandler requires knowing when XML
     * content handling switches to another TagContentHandler.
     * 
     * @param handler The TagContentHandler which has taken over the loading of xml tag content.
     */
    public void switchedTo(TagContentHandler handler);

    /**
     * This method is used to inform a TagContentHandler when XML content handling has switched back to this
     * TagContentHandler from another TagContentHandler. This normally happens if while inside an XML tag which
     * is handled by this TagContentHandler another XML tag is encountered for which a different TagContentHandler
     * is registered to handle and then the end XML tag for which that TagContentHandler is registered to handle is
     * encountered switching handling back to this TagContentHandler.
     * When that happens this method is called and the argument is the TagContentHandler which has switch handing
     * back to this TagContentHandler to handle XML tag content.
     *
     * The Tag content handler is not required to do anything as a result of being notified but this
     * notification is given in case the logic of the TagContentHandler requires knowing when XML
     * content handling switches back from another TagContentHandler.
     *
     * @param handler The TagContentHandler which has has finished handling XML content content handling is being
     *                switched from.
     */
    public void switchedBackFrom(TagContentHandler handler);
}