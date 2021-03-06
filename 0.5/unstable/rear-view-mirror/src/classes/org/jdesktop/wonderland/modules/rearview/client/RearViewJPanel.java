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
package org.jdesktop.wonderland.modules.rearview.client;

import java.awt.Dimension;
import java.awt.image.BufferedImage;


/**
 * A JPanel to be used as a HUD panel that displays a rear view mirror.
 *
 * @author Jordan Slott <jslott@dev.java.net>
 */
public class RearViewJPanel extends javax.swing.JPanel {

    // The width and height of the map image
    private static final int MAP_WIDTH = 600;
    private static final int MAP_HEIGHT = 100;

    // The buffered image in which to draw the camera scene
    private BufferedImage bufferedImage = null;

    // The component that displays the top map
    private CaptureJComponent mapComponent = null;

    /** Default constructor */
    public RearViewJPanel() {
        // Initialize the GUI
        initComponents();

        // Create the BufferedImage into which we will draw the camera scene
        bufferedImage = new BufferedImage(MAP_WIDTH, MAP_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        // Create and add a CaptureJPanel to the panel
        mapComponent = new CaptureJComponent(bufferedImage);
        mapComponent.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        rearViewPanel.add(mapComponent);
    }

    /**
     * Returns the CaptureJComponent which renders the map.
     *
     * @return The JComponent that renders the map
     */
    public CaptureJComponent getCaptureJComponent() {
        return mapComponent;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rearViewPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        rearViewPanel.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(rearViewPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel rearViewPanel;
    // End of variables declaration//GEN-END:variables
}
