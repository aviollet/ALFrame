package com.alv.alframe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static com.alv.alframe.Utils.readResource;

public class MakerSwing implements Maker {

    private ArrayList<Widget> widgets;
    private String projectName="";

    public MakerSwing(String project, ArrayList<Widget> widgets){
        this.widgets = widgets;
        this.projectName = project;
    }

    public void build(){

        String main = readResource(getClass(),"/swing/App.java");
        String buttonHandler = readResource(getClass(),"/swing/buttonHandler.java");

        if ((main!=null)&&(buttonHandler!=null)){
            try {
                String init="";
                String methods="";
                String fields="";
                String imports="";

                for (int i = 0; i < widgets.size() ; i++) {

                    if (widgets.get(i).getType().equals("Window")){
                        main = main.replace("0W",""+widgets.get(i).getWidth()).replace("0H",""+widgets.get(i).getHeight());
                    }
                    else
                    if (widgets.get(i).getType().equals("Button")){

                        init+="\t\tbutton_"+widgets.get(i).getName()+" = new javax.swing.JButton();\n";
                        init+="\t\tbutton_"+widgets.get(i).getName()+".setText(\""+widgets.get(i).getText()+"\");\n" +
                            "\t\tbutton_"+widgets.get(i).getName()+".addActionListener(new java.awt.event.ActionListener() {\n" +
                            "\t\t\tpublic void actionPerformed(java.awt.event.ActionEvent evt) {\n" +
                            "\t\t\t\thandleButton_"+widgets.get(i).getName()+"(evt);\n" +
                            "\t\t\t}\n" +
                            "\t\t});\n";
                        init+="\t\tbutton_"+widgets.get(i).getName()+".setBounds("+widgets.get(i).getX()+", "+widgets.get(i).getY()+", "+widgets.get(i).getWidth()+", "+widgets.get(i).getHeight()+");\n";
                        init+="\t\tgetContentPane().add(button_"+widgets.get(i).getName()+");\n\n";

                        methods+=buttonHandler.replace("buttonHandler","handleButton_"+widgets.get(i).getName());

                        fields += "\tprivate javax.swing.JButton button_"+widgets.get(i).getName()+";\n";
                    }
                    else
                    if (widgets.get(i).getType().equals("Check Box")){

                        init+="\t\tcheck_"+widgets.get(i).getName()+" = new javax.swing.JCheckBox();\n";
                        init+="\t\tcheck_"+widgets.get(i).getName()+".setText(\""+widgets.get(i).getText()+"\");\n";
                        init+="\t\tcheck_"+widgets.get(i).getName()+".setBounds("+widgets.get(i).getX()+", "+widgets.get(i).getY()+", "+widgets.get(i).getWidth()+", "+widgets.get(i).getHeight()+");\n";
                        init+="\t\tgetContentPane().add(check_"+widgets.get(i).getName()+");\n\n";

                        fields += "\tprivate javax.swing.JCheckBox check_"+widgets.get(i).getName()+";\n";
                    }
                    else
                    if (widgets.get(i).getType().equals("Drop Down Menu")){
                        //windowcontent+="<ComboBox layoutX=\""+widgets.get(i).getX()+"\" layoutY=\""+widgets.get(i).getY()+"\" prefHeight=\""+widgets.get(i).getHeight()+"\" prefWidth=\""+widgets.get(i).getWidth()+"\" text=\""+widgets.get(i).getText()+"\"/>\n";

                    }
                    /*else
                    if (widgets.get(i).getType().equals("Option")){

                        init+="\t\toption_"+widgets.get(i).getName()+" = new javax.swing.JRadioButton();\n";
                        init+="\t\toption_"+widgets.get(i).getName()+".setText(\""+widgets.get(i).getText()+"\");\n";
                        init+="\t\toption_"+widgets.get(i).getName()+".setBounds("+widgets.get(i).getX()+", "+widgets.get(i).getY()+", "+widgets.get(i).getWidth()+", "+widgets.get(i).getHeight()+");\n";
                        init+="\t\tgetContentPane().add(option_"+widgets.get(i).getName()+");\n\n";

                        fields += "\tprivate javax.swing.JRadioButton option_"+widgets.get(i).getName()+";\n";
                    }*/
                    else
                    if (widgets.get(i).getType().equals("Label")){

                        init+="\t\tlabel_"+widgets.get(i).getName()+" = new javax.swing.JLabel();\n";
                        init+="\t\tlabel_"+widgets.get(i).getName()+".setText(\""+widgets.get(i).getText()+"\");\n";
                        init+="\t\tlabel_"+widgets.get(i).getName()+".setBounds("+widgets.get(i).getX()+", "+widgets.get(i).getY()+", "+widgets.get(i).getWidth()+", "+widgets.get(i).getHeight()+");\n";
                        init+="\t\tgetContentPane().add(label_"+widgets.get(i).getName()+");\n\n";

                        fields += "\tprivate javax.swing.JLabel label_"+widgets.get(i).getName()+";\n";
                    }
                    else
                    if (widgets.get(i).getType().equals("Text Field")){

                        init+="\t\ttext_"+widgets.get(i).getName()+" = new javax.swing.JTextField();\n";
                        init+="\t\ttext_"+widgets.get(i).getName()+".setText(\""+widgets.get(i).getText()+"\");\n";
                        init+="\t\ttext_"+widgets.get(i).getName()+".setBounds("+widgets.get(i).getX()+", "+widgets.get(i).getY()+", "+widgets.get(i).getWidth()+", "+widgets.get(i).getHeight()+");\n";
                        init+="\t\tgetContentPane().add(text_"+widgets.get(i).getName()+");\n\n";

                        fields += "\tprivate javax.swing.JTextField text_"+widgets.get(i).getName()+";\n";
                    }
                    else
                    if (widgets.get(i).getType().equals("Text Area")){

                        init+="\t\tarea_"+widgets.get(i).getName()+" = new javax.swing.JTextArea();\n";
                        init+="\t\tscrollPane_"+widgets.get(i).getName()+" = new javax.swing.JScrollPane();\n";
                        init+="\t\tarea_"+widgets.get(i).getName()+".setText(\""+widgets.get(i).getText()+"\");\n";
                        init+="\t\tscrollPane_"+widgets.get(i).getName()+".setViewportView(area_"+widgets.get(i).getName()+");\n";
                        init+="\t\tscrollPane_"+widgets.get(i).getName()+".setBounds("+widgets.get(i).getX()+", "+widgets.get(i).getY()+", "+widgets.get(i).getWidth()+", "+widgets.get(i).getHeight()+");\n";
                        init+="\t\tgetContentPane().add(scrollPane_"+widgets.get(i).getName()+");\n\n";

                        fields += "\tprivate javax.swing.JTextArea area_"+widgets.get(i).getName()+";\n";
                        fields += "\tprivate javax.swing.JScrollPane scrollPane_"+widgets.get(i).getName()+";\n";
                    }
                }
                main= main.replace("//@INIT",init);
                main= main.replace("//@IMPORTS",imports);
                main= main.replace("//@METHODS",methods);
                main= main.replace("//@FIELDS",fields);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("viewer.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 900, 600);
                TextArea txtMain = (TextArea) loader.getNamespace().get("txt1");
                txtMain.setText(main);

                TabPane tabPane = (TabPane) loader.getNamespace().get("tabPane");
                tabPane.getTabs().get(0).setText("App.java");
                tabPane.getTabs().remove(2);
                tabPane.getTabs().remove(1);

                Stage stage = new Stage();
                stage.setTitle(projectName);
                stage.setMaximized(true);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
