/**
 * Project Looking Glass
 *
 * $RCSfile$
 *
 * Copyright (c) 2004-2008, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * $Revision$
 * $Date$
 * $State$
 */
package org.jdesktop.lg3d.wonderland.videomodule.client.cell;

import java.awt.event.ActionListener;

/**
 *
 * @author  nsimpson
 */
public class VideoSourceDialog extends javax.swing.JDialog {

    /** Creates new form VideoSourceDialog */
    public VideoSourceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setVideoURL(String docURL) {
        sourceTextField.setText(docURL);
    }

    public String getVideoURL() {
        return sourceTextField.getText();
    }

    public void addActionListener(ActionListener listener) {
        okButton.addActionListener(listener);
        cancelButton.addActionListener(listener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        sourceLabel = new javax.swing.JLabel();
        sourceTextField = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load Video");

        okButton.setFont(new java.awt.Font("Dialog", 1, 12));
        okButton.setText("OK");

        cancelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        cancelButton.setText("Cancel");

        sourceLabel.setFont(new java.awt.Font("Dialog", 1, 12));
        sourceLabel.setText("Video URL:");

        sourceTextField.setFont(new java.awt.Font("DialogInput", 0, 12));
        sourceTextField.setText("file://");
        sourceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceTextFieldActionPerformed(evt);
            }
        });

        descriptionLabel.setFont(new java.awt.Font("Dialog", 1, 12));
        descriptionLabel.setText("Enter the URL of the video:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(6, 6, 6)
                                .add(sourceLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(sourceTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE))
                            .add(descriptionLabel)))
                    .add(layout.createSequentialGroup()
                        .add(122, 122, 122)
                        .add(okButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(descriptionLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sourceLabel)
                    .add(sourceTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(cancelButton)
                    .add(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sourceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceTextFieldActionPerformed
        okButton.doClick();
    }//GEN-LAST:event_sourceTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new VideoSourceDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel sourceLabel;
    private javax.swing.JTextField sourceTextField;
    // End of variables declaration//GEN-END:variables
}
