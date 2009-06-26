/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MetadataComponentProperties.java
 *
 * Created on Jun 10, 2009, 4:02:05 PM
 */

package org.jdesktop.wonderland.modules.metadata.client;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jdesktop.wonderland.client.cell.properties.CellPropertiesEditor;
import org.jdesktop.wonderland.client.cell.properties.annotation.CellComponentProperties;
import org.jdesktop.wonderland.client.cell.properties.spi.CellComponentPropertiesSPI;
import org.jdesktop.wonderland.common.cell.state.CellServerState;
import org.jdesktop.wonderland.modules.metadata.common.Metadata;
import org.jdesktop.wonderland.modules.metadata.common.MetadataComponentServerState;
import org.jdesktop.wonderland.modules.metadata.common.SimpleMetadata;

/**
 *
 * @author mabonner
 */

@CellComponentProperties
public class MetadataComponentProperties extends JPanel
    implements CellComponentPropertiesSPI {
  private CellPropertiesEditor editor = null;
  // used to map pieces of metadata to their appropriate table
  private HashMap<String, JTable> metatypeMap = new HashMap<String, JTable>();
  private static Logger logger = Logger.getLogger(MetadataComponent.class.getName());
  // TODO does this need to be synchronized? I don't think so
  private static ArrayList<Metadata> metaTypes = new ArrayList<Metadata>();

  // TODO could not add MetadataTypesTable to NetBeans GUI Builder
  // workaround: use customize code to make basicTabs instantiated as
  // a MTT. Cast basicTabs to an MTT and use the tabs reference instead.
  private MetadataTypesTable tabs;
  
  /**
   * initialize metadata types list
   */
  static {
      // eventually this will be initialized via the annotation system
      // will this still be the appropriate place to init it?

      // for now, hard-include types
      metaTypes.add(new Metadata(null, null));
      metaTypes.add(new SimpleMetadata(null, null));
  }

    /** Creates new form MetadataComponentProperties */
    public MetadataComponentProperties() {
        // this panel is dynamically populated
        super(new GridBagLayout());
        initComponents();

        // work-around for NetBeans GUI builder
        // see comment where tabs is declared
        tabs = (MetadataTypesTable) basicTabs;

        // add listeners
        tabs.registerListSelectionListener(new RemoveButtonSelectionListener());
        tabs.registerTableModelListener(new TableDirtyListener());
        
        System.out.println("constructor: size of map: " + metatypeMap.size());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    controls = new javax.swing.JPanel();
    addButton = new javax.swing.JButton();
    removeButton = new javax.swing.JButton();
    basicTabs = new MetadataTypesTable();

    jLabel1.setText("Metadata");

    controls.setBackground(new java.awt.Color(204, 204, 255));

    addButton.setText("+");
    addButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        addButtonActionPerformed(evt);
      }
    });

    removeButton.setText("-");
    removeButton.setEnabled(false);
    removeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        removeButtonActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout controlsLayout = new javax.swing.GroupLayout(controls);
    controls.setLayout(controlsLayout);
    controlsLayout.setHorizontalGroup(
      controlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(controlsLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(controlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(addButton)
          .addComponent(removeButton))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    controlsLayout.setVerticalGroup(
      controlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(controlsLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(addButton)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(removeButton)
        .addContainerGap(30, Short.MAX_VALUE))
    );

    basicTabs.setPreferredSize(new java.awt.Dimension(32767, 32767));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(36, 36, 36)
            .addComponent(jLabel1))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(basicTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(controls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(basicTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE))
      .addGroup(layout.createSequentialGroup()
        .addGap(40, 40, 40)
        .addComponent(controls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(211, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
      tabs.removeCurrentlySelectedRow();
}//GEN-LAST:event_removeButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
      tabs.createNewMetadataOnCurrentTab();
}//GEN-LAST:event_addButtonActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton addButton;
  private javax.swing.JTabbedPane basicTabs;
  private javax.swing.JPanel controls;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JButton removeButton;
  // End of variables declaration//GEN-END:variables
  
  public Class getServerCellComponentClass() {
    return MetadataComponentServerState.class;
  }

  public String getDisplayName() {
    return "Cell Metadata Component";
  }

  public JPanel getPropertiesJPanel(CellPropertiesEditor editor) {
    this.editor = editor;
    return this;
  }

  public <T extends CellServerState> void updateGUI(T cellServerState) {
    MetadataComponentServerState state =  (MetadataComponentServerState)
                cellServerState.getComponentServerState(MetadataComponentServerState.class);
    // do whatever you have to do based on server state (fill up our panel)
    logger.log(Level.INFO, "update GUI");

    // currently, this naively clears tabs and repopulates them
    tabs.clearTabs();
    tabs.addMetadata(state.getMetadata());
    
    tabs.repaint();
    repaint();
  }

  public <T extends CellServerState> void getCellServerState(T cellServerState) {
      logger.log(Level.INFO, "[METADATA COMPO PROPERTIES] get cell server state!");
      // Figure out whether there already exists a server state for the
      // component.

      MetadataComponentServerState state = (MetadataComponentServerState)
              cellServerState.getComponentServerState(MetadataComponentServerState.class);
      if (state == null) {
          state = new MetadataComponentServerState();
      }

      // TODO
      // naively clear this, in the future we could track which metadata
      // objects have been changed and adjust only them
      state.removeAllMetadata();
      // convert models back into ServerState
      for(int i = 0; i < tabs.getComponentCount(); i++){
        try {
          for (Metadata m : tabs.getMetadataFromTab(i)) {
            state.addMetadata(m);
          }
        } catch (Exception ex) {
             Logger.getLogger(MetadataComponentProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

      // TODO
      // this overwrites the old serverstate, which could result in needlessly
      // overwriting unchanged metadata elts
      // this is fairly lightweight.. could be improved by tracking what elts
      // (e.g. tabs and rows) were changed, and overwriting only them.
      cellServerState.addComponentServerState(state);
  }

  class RemoveButtonSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            boolean enabled = false;

            if (!e.getValueIsAdjusting()) {
                enabled = (tabs.getCurrentTable().getSelectedRow() >= 0);
            }

            removeButton.setEnabled(enabled);
        }
    }

  class TableDirtyListener implements TableModelListener {
        public void tableChanged(TableModelEvent tme) {
            editor.setPanelDirty(MetadataComponentProperties.class, true);
        }
  }

}