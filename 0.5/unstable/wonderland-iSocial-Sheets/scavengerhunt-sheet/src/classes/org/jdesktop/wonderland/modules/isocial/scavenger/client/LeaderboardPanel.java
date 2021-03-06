/**
 * Copyright (c) 2012, WonderBuilders, Inc., All Rights Reserved
 */
package org.jdesktop.wonderland.modules.isocial.scavenger.client;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.wonderland.modules.isocial.scavenger.common.StudentRank;

/**
 *
 * @author Vladimir Djurovic
 */
public class LeaderboardPanel extends javax.swing.JPanel {

    /**
     * Creates new form LeaderboardPanel.
     */
    public LeaderboardPanel() {
        initComponents();
        rankTable.getColumn("Time").setCellRenderer(new TimeCellRenderer());
        DefaultTableCellRenderer centeredStringRenderer = new DefaultTableCellRenderer();
        centeredStringRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        rankTable.getColumn("Rank").setCellRenderer(centeredStringRenderer);
        rankTable.getColumn("Score").setCellRenderer(centeredStringRenderer);

    }
    
    
    /**
     * Updates ranking table to reflect current status.
     * 
     * @param ranking sorted ranks
     * @param currentUser  currently logged in user
     */
    public void updateRankings(StudentRank[] ranking, String currentUser){
        DefaultTableModel model = (DefaultTableModel)rankTable.getModel();
        int rowCount = model.getRowCount();
        for(int i = 0;i < ranking.length;i++){
            if(i <= (rowCount - 1)){
                // modify values
                model.setValueAt(i + 1, i, 0);
                model.setValueAt(ranking[i].getUsername(), i, 1);
                model.setValueAt(ranking[i].getScore(), i, 2);
                model.setValueAt(ranking[i].getTime(), i, 3);
            } else {
                model.addRow(new Object[]{rowCount + 1, ranking[i].getUsername(), ranking[i].getScore(), ranking[i].getTime()});
                rowCount++;
            }
            if(ranking[i].getUsername().equals(currentUser)){
                rankTable.setRowSelectionInterval(i, i);
            }
        }
//        rankTable.setModel(model);
    }
    
    /**
     * Renderer for cells that display time. This will show time in mm:ss or HH:mm:ss format,
     * depending on whether time is longer then hour or not.
     */
    private class TimeCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel timeLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            long time = (Long) value;
            timeLabel.setText(ScavengerHuntUtils.formatTime(time));
            return timeLabel;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rankTable = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Leaderboard");

        rankTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rank", "User", "Score", "Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Long.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        rankTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(rankTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(jLabel1)
                .addContainerGap(152, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable rankTable;
    // End of variables declaration//GEN-END:variables
}
