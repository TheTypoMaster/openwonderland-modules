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
package org.jdesktop.wonderland.modules.cmu.client.events.wonderland;

import org.jdesktop.wonderland.client.contextmenu.ContextMenuActionListener;
import org.jdesktop.wonderland.client.contextmenu.ContextMenuItemEvent;
import org.jdesktop.wonderland.modules.cmu.client.CMUCell;
import org.jdesktop.wonderland.modules.cmu.common.events.WonderlandResponse;

/**
 * Listener for context a context menu event, which sends an appropriate response.
 * @author kevin
 */
public class CMUContextListener extends CMUWonderlandEventListener
        implements ContextMenuActionListener {

    public CMUContextListener(CMUCell parent, WonderlandResponse response) {
        super(parent, response);
    }

    @Override
    public void actionPerformed(ContextMenuItemEvent event) {
        this.eventOccurred();
    }
}
