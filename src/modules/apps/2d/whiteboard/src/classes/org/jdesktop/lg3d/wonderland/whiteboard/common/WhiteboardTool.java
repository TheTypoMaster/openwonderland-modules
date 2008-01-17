/**
 * Project Looking Glass
 *
 * $RCSfile: WhiteboardTool.java,v $
 *
 * Copyright (c) 2004-2007, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision: 1.1 $
 * $Date: 2007/09/21 16:54:20 $
 * $State: Exp $
 */
package org.jdesktop.lg3d.wonderland.whiteboard.common;

/**
 * Whiteboard tool types
 * @author nsimpson
 */
public interface WhiteboardTool {
    public enum Tool {
        STROKE,
// not implemented yet
//        LINE,
//        RECTANGLE,
//        ELLIPSE
    };
    
    public static final Tool STROKE = Tool.STROKE;
//    public static final Tool LINE = Tool.LINE;    
//    public static final Tool RECTANGLE = Tool.RECTANGLE;
//    public static final Tool ELLIPSE = Tool.ELLIPSE;
}
