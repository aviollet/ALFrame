package com.alv.alframe;

public class App extends javax.swing.JFrame {

    public App() {
        initComponents();
    }

    private void handleButton_ok(java.awt.event.ActionEvent evt) {

        //processing...
    }
    private void handleButton_cancel(java.awt.event.ActionEvent evt) {

        //processing...
    }


    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("App");

        getContentPane().setLayout(null);

        button_ok = new javax.swing.JButton();
        button_ok.setText("OK");
        button_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleButton_ok(evt);
            }
        });
        button_ok.setBounds(278, 275, 102, 37);
        getContentPane().add(button_ok);

        button_cancel = new javax.swing.JButton();
        button_cancel.setText("Cancel");
        button_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleButton_cancel(evt);
            }
        });
        button_cancel.setBounds(394, 275, 100, 37);
        getContentPane().add(button_cancel);

        area_item_4 = new javax.swing.JTextArea();
        scrollPane_item_4 = new javax.swing.JScrollPane();
        area_item_4.setText("hello\n\nthis is a text!!");
        scrollPane_item_4.setViewportView(area_item_4);
        scrollPane_item_4.setBounds(15, 55, 479, 140);
        getContentPane().add(scrollPane_item_4);

        text_textfield1 = new javax.swing.JTextField();
        text_textfield1.setText("");
        text_textfield1.setBounds(187, 213, 307, 34);
        getContentPane().add(text_textfield1);

        label_currentlabel = new javax.swing.JLabel();
        label_currentlabel.setText("current label :");
        label_currentlabel.setBounds(15, 213, 156, 35);
        getContentPane().add(label_currentlabel);

        label_item_7 = new javax.swing.JLabel();
        label_item_7.setText("some kind of introductory text for the first line :");
        label_item_7.setBounds(15, 15, 479, 31);
        getContentPane().add(label_item_7);

        check_item_8 = new javax.swing.JCheckBox();
        check_item_8.setText("check me");
        check_item_8.setBounds(15, 272, 75, 33);
        getContentPane().add(check_item_8);

        check_item_11 = new javax.swing.JCheckBox();
        check_item_11.setText("check 2");
        check_item_11.setBounds(110, 272, 75, 33);
        getContentPane().add(check_item_11);



        setSize(509 + 10 + 10, 340 + 10 + 10);

        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx=0; idx<installedLookAndFeels.length; idx++)
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }

    private javax.swing.JButton button_ok;
    private javax.swing.JButton button_cancel;
    private javax.swing.JTextArea area_item_4;
    private javax.swing.JScrollPane scrollPane_item_4;
    private javax.swing.JTextField text_textfield1;
    private javax.swing.JLabel label_currentlabel;
    private javax.swing.JLabel label_item_7;
    private javax.swing.JCheckBox check_item_8;
    private javax.swing.JCheckBox check_item_11;

}
