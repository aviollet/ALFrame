package com.alv.alframe;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static com.alv.alframe.Utils.readResource;

public class MakerJavaFX implements Maker {

    private ArrayList<Widget> widgets;
    private String projectName="";

    public MakerJavaFX(String project, ArrayList<Widget> widgets){
        this.widgets = widgets;
        this.projectName = project;
    }

    public void build(){

        String window = readResource(getClass(),"/javafx/window.fxml");
        String main = readResource(getClass(),"/javafx/Main.java");
        String controller = readResource(getClass(),"/javafx/Controller.java");
        String buttonHandler = readResource(getClass(),"/javafx/buttonHandler.java");

        if ((window!=null)&&(main!=null)&&(controller!=null)&&(buttonHandler!=null)){
            try {
                String init="";
                String methods="";
                String windowcontent="";
                String fields="";
                String imports="";

                for (int i = 0; i < widgets.size() ; i++) {

                    if (widgets.get(i).getType().equals("Window")){
                        window = window.replace("0W",""+widgets.get(i).getWidth()).replace("0H",""+widgets.get(i).getHeight());
                    }
                    else
                    if (widgets.get(i).getType().equals("Button")){

                        windowcontent+="<Button mnemonicParsing=\"false\" onAction=\"#handleButton_"+widgets.get(i).getName()+"\" layoutX=\""+widgets.get(i).getX()+"\" layoutY=\""+widgets.get(i).getY()+"\" prefHeight=\""+widgets.get(i).getHeight()+"\" prefWidth=\""+widgets.get(i).getWidth()+"\" text=\""+widgets.get(i).getText()+"\"/>\n";

                        //init+="button_"+widgets.get(i).getName()+"();";
                        //<Button layoutX="311.0" layoutY="183.0" mnemonicParsing="false" text="Button" />
                        methods+=buttonHandler.replace("buttonHandler","handleButton_"+widgets.get(i).getName());

                    }
                    else
                    if (widgets.get(i).getType().equals("Check Box")){
                        windowcontent+="<CheckBox layoutX=\""+widgets.get(i).getX()+"\" layoutY=\""+widgets.get(i).getY()+"\" prefHeight=\""+widgets.get(i).getHeight()+"\" prefWidth=\""+widgets.get(i).getWidth()+"\" text=\""+widgets.get(i).getText()+"\"/>\n";

                    }
                    else
                    if (widgets.get(i).getType().equals("Drop Down Menu")){
                        windowcontent+="<ComboBox layoutX=\""+widgets.get(i).getX()+"\" layoutY=\""+widgets.get(i).getY()+"\" prefHeight=\""+widgets.get(i).getHeight()+"\" prefWidth=\""+widgets.get(i).getWidth()+"\" text=\""+widgets.get(i).getText()+"\"/>\n";

                    }
                    else
                    if (widgets.get(i).getType().equals("Option")){
                        windowcontent+="<RadioButton layoutX=\""+widgets.get(i).getX()+"\" layoutY=\""+widgets.get(i).getY()+"\" prefHeight=\""+widgets.get(i).getHeight()+"\" prefWidth=\""+widgets.get(i).getWidth()+"\" text=\""+widgets.get(i).getText()+"\"/>\n";

                    }
                    else
                    if (widgets.get(i).getType().equals("Label")){
                        windowcontent+="<Label layoutX=\""+widgets.get(i).getX()+"\" layoutY=\""+widgets.get(i).getY()+"\" prefHeight=\""+widgets.get(i).getHeight()+"\" prefWidth=\""+widgets.get(i).getWidth()+"\" text=\""+widgets.get(i).getText()+"\"/>\n";

                    }
                    else
                    if (widgets.get(i).getType().equals("Text Field")){
                        windowcontent+="<TextField layoutX=\""+widgets.get(i).getX()+"\" layoutY=\""+widgets.get(i).getY()+"\" prefHeight=\""+widgets.get(i).getHeight()+"\" prefWidth=\""+widgets.get(i).getWidth()+"\" text=\""+widgets.get(i).getText()+"\"/>\n";

                    }
                    else
                    if (widgets.get(i).getType().equals("Text Area")){
                        windowcontent+="<TextArea layoutX=\""+widgets.get(i).getX()+"\" layoutY=\""+widgets.get(i).getY()+"\" prefHeight=\""+widgets.get(i).getHeight()+"\" prefWidth=\""+widgets.get(i).getWidth()+"\" text=\""+widgets.get(i).getText()+"\"/>\n";

                    }
                }
                window= window.replace("<!--CONTENT-->",windowcontent);
                controller= controller.replace("//@INIT",init);
                controller= controller.replace("//@IMPORTS",imports);
                controller= controller.replace("//@METHODS",methods);
                controller= controller.replace("//@FIELDS",fields);


                FXMLLoader loader = new FXMLLoader(getClass().getResource("viewer.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 900, 600);
                TextArea txtMain = (TextArea) loader.getNamespace().get("txt1");
                txtMain.setText(main);
                TextArea txtController = (TextArea) loader.getNamespace().get("txt2");
                txtController.setText(controller);
                TextArea txtFxml = (TextArea) loader.getNamespace().get("txt3");
                txtFxml.setText(window);
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
