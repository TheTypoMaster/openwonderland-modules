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
package org.jdesktop.wonderland.modules.metadata.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import org.jdesktop.wonderland.client.BaseClientPlugin;
import org.jdesktop.wonderland.client.jme.JmeClientMain;
import org.jdesktop.wonderland.client.login.ServerSessionManager;
import org.jdesktop.wonderland.common.annotation.Plugin;
import org.jdesktop.wonderland.modules.metadata.common.Metadata;
import org.jdesktop.wonderland.modules.metadata.common.SimpleMetadata;

/**
 * Client-size plugin for metadata module.
 * Stores list of metadata types
 *
 * Provides GUI access to
 * search interface in the form of an extra window.
 *
 * @author mabonner
 */
@Plugin
public class MetadataPlugin extends BaseClientPlugin
{
    private static Logger logger = Logger.getLogger(MetadataPlugin.class.getName());
    private static ArrayList<Metadata> metaTypes = new ArrayList<Metadata>();

    /* The single instance of the cell palette dialog */
    private WeakReference<MetadataSearchForm> searchFormRef = null;

    /* The menu item to add to the menu */
    private JMenuItem searchMI;

    static{
        // TODO
        // request metadata list from annotation registration system
        // for now, fill manually
        metaTypes.add(new Metadata(null, null));
        metaTypes.add(new SimpleMetadata(null, null));
    }

    @Override
    public void initialize(ServerSessionManager loginInfo) {
        // Create the metadata search menu The menu will be added when our
        // server becomes primary.
        // also create
        searchMI = new JMenuItem("Search Metadata");
        searchMI.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MetadataSearchForm searchFrame;
                if (searchFormRef == null || searchFormRef.get() == null) {
                    searchFrame = new MetadataSearchForm();
                    searchFormRef = new WeakReference(searchFrame);
                }
                else {
                    searchFrame = searchFormRef.get();
                }

                if (searchFrame.isVisible() == false) {
                    searchFrame.setVisible(true);
                }
            }
        });

        // Add the Palette menu and the Cell submenu and dialog that lets users
        // create new cells.
//        moduleMI = new JMenuItem("Module Art Palette");
//        moduleMI.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                ModulePalette modulePaletteFrame;
//                if (modulePaletteFrameRef == null || modulePaletteFrameRef.get() == null) {
//                    modulePaletteFrame = new ModulePalette();
//                    modulePaletteFrameRef = new WeakReference(modulePaletteFrame);
//                }
//                else {
//                    modulePaletteFrame = modulePaletteFrameRef.get();
//                }
//
//                if (modulePaletteFrame.isVisible() == false) {
//                    modulePaletteFrame.setVisible(true);
//                }
//            }
//        });

        super.initialize(loginInfo);
    }

    public static final ArrayList<Metadata> getMetadataTypes(){
        return metaTypes;
    }

    /**
     * Notification that our server is now the the primary server
     */
    @Override
    protected void activate() {
        // add menu item
        JmeClientMain.getFrame().addToWindowMenu(searchMI, -1);
    }

    @Override
    protected void deactivate() {
        // deactivate
        JmeClientMain.getFrame().removeFromInsertMenu(searchMI);
    }

    // context-menu level functionality begins here in palette plugin..
    // what to add besides tagging?
    // perhaps 'search for cells like this'

}