/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TimeSliderPanel.java
 *
 * Created on Aug 12, 2009, 10:07:15 AM
 */

package org.jdesktop.wonderland.modules.marbleous.client.ui;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import org.jdesktop.mtgame.Entity;
import org.jdesktop.mtgame.RenderComponent;
import org.jdesktop.mtgame.RenderUpdater;
import org.jdesktop.wonderland.client.input.Event;
import org.jdesktop.wonderland.client.jme.ClientContextJME;
import org.jdesktop.wonderland.modules.marbleous.client.SampleInfo;
import org.jdesktop.wonderland.modules.marbleous.client.SimTrace;
import org.jdesktop.wonderland.modules.marbleous.client.cell.TrackCell;
import org.jdesktop.wonderland.modules.marbleous.client.jme.TrackRenderer;
import org.jdesktop.wonderland.modules.marbleous.client.jme.TrackRenderer.MarbleMouseEventListener;

/**
 *
 * @author dj
 */
public class TimeSliderPanel extends javax.swing.JPanel {

    private TrackCell cell;
    private SimTrace trace;
    private MarbleMouseEventListener marbleMouseListener;

    /** Creates new form TimeSliderPanel */
    public TimeSliderPanel(TrackCell cell, SimTrace trace) {
        this.cell = cell;
        this.trace = trace;

        initComponents();

        float endTime = trace.getEndTime();
        jLabel3.setText(Float.toString(endTime));

        marbleMouseListener = new MarbleMouseListener();
        cell.addMarbleMouseListener(marbleMouseListener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        jSlider1.setPaintLabels(true);
        jSlider1.setPaintTicks(true);
        jSlider1.setValue(0);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel1.setText("Time (ms)");

        jLabel2.setText("0");

        jLabel3.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(232, 232, 232)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(32, 32, 32))
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        int value = jSlider1.getValue();
        float pct = (float)value / (float)(jSlider1.getMaximum() - jSlider1.getMinimum());
        System.err.println("trace.getEndTime() = " + trace.getEndTime());
        float t = pct * trace.getEndTime();

        //System.err.println("value = " + value);
        //System.err.println("pct = " + pct);
        System.err.println("t = " + t);

        SampleInfo sampleInfo = trace.getSampleInfo(t);
        System.err.println("sampleInfo = " + sampleInfo);
        final Vector3f position = sampleInfo.getPosition();
        System.err.println("position = " + position);

        // Assumes that the marble is still attached to the cell
        Entity marbleEntity = cell.getMarbleEntity();
        RenderComponent rc = marbleEntity.getComponent(RenderComponent.class);
        final Node marbleNode = rc.getSceneRoot();

        ClientContextJME.getWorldManager().addRenderUpdater(new RenderUpdater() {
            public void update(Object arg0) {
                marbleNode.setLocalTranslation(position);
                ClientContextJME.getWorldManager().addToUpdateList(marbleNode);
            }
        }, null);


    }//GEN-LAST:event_jSlider1StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables


    private class MarbleMouseListener implements TrackRenderer.MarbleMouseEventListener {
        public void commitEvent (Entity marbleEntity, Event event) {
            System.err.println("**** Mouse event on marble = " + event);
        }
    }
}
