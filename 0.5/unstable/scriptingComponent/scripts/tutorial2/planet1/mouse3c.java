
import java.lang.reflect.Method;
import javax.script.ScriptContext;

/*
 * SetScripts3.java
 *
 * Created on March 7, 2009, 1:14 PM
 */



/**
 *
 * @author  morris
 */
class SetScripts3 extends javax.swing.JFrame {
    
    private static ScriptContext myContext;
    private static Class sc = null;
    private static Object myClassObject = null;
    
    /** Creates new form SetScripts3 */
    public SetScripts3() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ScriptsTable = new javax.swing.JTable();
        jButtonFetch = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jLabelName = new javax.swing.JLabel();
        jTextName = new javax.swing.JTextField();
        jLabelURL = new javax.swing.JLabel();
        jTextURL = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ScriptsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Mouse Left", null, null},
                {"Mouse Middle", null, null},
                {"Mouse Right", null, null},
                {"Mouse Left + Shift", null, null},
                {"Mouse Middle + Shift", null, null},
                {"Mouse Right + Shift", null, null},
                {"Mouse Left + Control", null, null},
                {"Mouse Middle + Control", null, null},
                {"Mouse Right + Control", null, null},
                {"Mouse Left + Alt", null, null},
                {"Mouse Middle + Alt", null, null},
                {"Mouse Right + Alt", null, null},
                {"Timer", null, null},
                {"Startup", null, null},
                {"Proximity", null, null},
                {"Message 1", null, null},
                {"Message 2", null, null},
                {"Message 3", null, null},
                {"Message 4", null, null}
            },
            new String [] {
                "Event", "Name", "Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        ScriptsTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(ScriptsTable);

        jButtonFetch.setText("Fetch Settings");
        jButtonFetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFetchActionPerformed(evt);
            }
        });

        jButtonSave.setText("Save Settings");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonExit.setText("Exit");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jLabelName.setText("Name");

        jTextName.setText("default");

        jLabelURL.setText("Script URL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButtonExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jButtonFetch, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)))
                        .addGap(76, 76, 76)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelURL, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelName, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextURL, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addComponent(jTextName, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonFetch)
                    .addComponent(jLabelName)
                    .addComponent(jTextName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButtonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonExit))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelURL)
                            .addComponent(jTextURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonFetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFetchActionPerformed
        for(int i = 0; i < 19; i++)
            {
            ScriptsTable.setValueAt(getScriptName(i), i, 1);
            ScriptsTable.setValueAt(getScriptType(i), i, 2);
            }
        jTextName.setText(getScriptClump());
        jTextURL.setText(getScriptURL());

    }//GEN-LAST:event_jButtonFetchActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        for(int i = 0; i < 19; i++)
            {
            Object nameCell = ScriptsTable.getValueAt(i, 1);
            setScriptName(nameCell.toString(), i);
            Object typeCell = ScriptsTable.getValueAt(i, 2);
            setScriptType(typeCell.toString(), i);
            }
        setScriptClump(jTextName.getText());
        setScriptURL(jTextURL.getText());
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
       System.exit(0);
    }//GEN-LAST:event_jButtonExitActionPerformed

    public static void setScriptContext(ScriptContext ctx)
        {
        myContext = ctx;
        System.out.println("Inside setScriptContext");
        }
   
    private void testMethod(String message)
        {
// Do setup for testMethod(String)
        try
            {
            Class partypes[] = new Class[1];
            partypes[0] = message.getClass();
            Method meth = sc.getMethod("testMethod", partypes);
            
            Object arglist[] = new Object[1];
            arglist[0] = message;
            Object retobj = meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for testMethod");
            }
        }
    
    private String getScriptName(int which)
        {
// Do setup for testMethod(String)
        Object retobj = null;
        try
            {
            Class partypes[] = new Class[1];
            partypes[0] = Integer.TYPE;
            Method meth = sc.getMethod("getScriptName", partypes);
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(which);
            retobj = meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for getScriptName");
            e.printStackTrace();
            }
        return (String)retobj;
        }
    
    private String getScriptType(int which)
        {
// Do setup for testMethod(String)
        Object retobj = null;
        try
            {
            Class partypes[] = new Class[1];
            partypes[0] = Integer.TYPE;
            Method meth = sc.getMethod("getScriptType", partypes);
            
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(which);
            retobj = meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for getScriptType");
            }
        return (String)retobj;
        }
    
    
    private void setScriptName(String name, int which)
        {
// Do setup for testMethod(String)
        Object retobj = null;
        try
            {
            Class partypes[] = new Class[2];
            partypes[0] = name.getClass();
            partypes[1] = Integer.TYPE;
            Method meth = sc.getMethod("setScriptName", partypes);
            Object arglist[] = new Object[2];
            arglist[0] = name;
            arglist[1] = new Integer(which);
            meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for setScriptName");
            e.printStackTrace();
            }
        }
    
    private void setScriptType(String name, int which)
        {
// Do setup for testMethod(String)
        Object retobj = null;
        try
            {
            Class partypes[] = new Class[2];
            partypes[0] = name.getClass();
            partypes[1] = Integer.TYPE;
            Method meth = sc.getMethod("setScriptType", partypes);
            Object arglist[] = new Object[2];
            arglist[0] = name;
            arglist[1] = new Integer(which);
            meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for setScriptType");
            e.printStackTrace();
            }
        }
    
    private String getScriptClump()
        {
// Do setup for testMethod(String)
        Object retobj = null;
        try
            {
            Class partypes[] = new Class[0];
            Method meth = sc.getMethod("getScriptClump", partypes);
            Object arglist[] = new Object[0];
            retobj = meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for getScriptClump");
            e.printStackTrace();
            }
        return (String)retobj;
        }
    
    private String getScriptURL()
        {
// Do setup for testMethod(String)
        Object retobj = null;
        try
            {
            Class partypes[] = new Class[0];
            Method meth = sc.getMethod("getScriptURL", partypes);
            Object arglist[] = new Object[0];
            retobj = meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for getScriptURL");
            e.printStackTrace();
            }
        return (String)retobj;
        }
    
    private void setScriptClump(String message)
        {
// Do setup for testMethod(String)
        try
            {
            Class partypes[] = new Class[1];
            partypes[0] = message.getClass();
            Method meth = sc.getMethod("setScriptClump", partypes);
            
            Object arglist[] = new Object[1];
            arglist[0] = message;
            meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for setScriptClump");
            }
        }
    
    private void setScriptURL(String message)
        {
// Do setup for testMethod(String)
        try
            {
            Class partypes[] = new Class[1];
            partypes[0] = message.getClass();
            Method meth = sc.getMethod("setScriptURL", partypes);
            
            Object arglist[] = new Object[1];
            arglist[0] = message;
            meth.invoke(myClassObject, arglist);
            }
        catch(Exception e)
            {
            System.out.println("Exception for setScriptURL");
            }
        }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
        {
        java.awt.EventQueue.invokeLater(new Runnable() 
            {
            public void run() 
                {
// Get the ScriptingComponent reference of class that called us
                myClassObject = myContext.getAttribute("MyClass");
                sc = myClassObject.getClass();
                
                new SetScripts3().setVisible(true);
                }
            });
        }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable ScriptsTable;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonFetch;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelURL;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextName;
    private javax.swing.JTextField jTextURL;
    // End of variables declaration//GEN-END:variables
    
}
