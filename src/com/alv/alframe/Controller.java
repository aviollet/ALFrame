package com.alv.alframe;
import eu.hansolo.colors.MaterialDesign;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import static com.alv.alframe.Utils.tell;

public class Controller {

    private static final int TYPE_WINDOW = 0;
    private static final int TYPE_WIDGET = 1;
    private static int maxid =0;
    private static int selectedId =0;
    final double handleRadius = 10 ;

    @FXML
    private Pane pane;

    @FXML
    private AnchorPane rightpane;

    @FXML
    private TextField txtId;

    @FXML
    private TextField propName;

    @FXML
    private ComboBox propType;

    @FXML
    private TextField propText;

    @FXML
    private TextField propX;

    @FXML
    private TextField propY;

    @FXML
    private TextField propWidth;

    @FXML
    private TextField propHeight;

    @FXML
    private TextField propRight;

    @FXML
    private TextField propBottom;

    @FXML
    private TextField txtProj;

    @FXML
    private Button btnDel;

    @FXML
    private Button btnMake;

    @FXML
    private Button btnDup;
    private Rectangle window;

    ArrayList<Widget> widgets = new ArrayList<>();

    @FXML
    private ComboBox comboProjects;

    @FXML
    private ComboBox comboLang;
    private Stage primaryStage;
    private Paint COLOR_BUTTON = MaterialDesign.RED_700.get();
    private Paint COLOR_CHECK = MaterialDesign.PINK_700.get();
    private Paint COLOR_DROPDOWN = MaterialDesign.PURPLE_700.get();
    private Paint COLOR_LABEL = MaterialDesign.DEEP_PURPLE_700.get();
    private Paint COLOR_TEXTAREA = MaterialDesign.INDIGO_700.get();
    private Paint COLOR_TEXT = MaterialDesign.BLUE_700.get();
    private boolean wasModified = false;
    private boolean UIupdate = false;
    private int glueSide = 0;

    @FXML
    private void textAction(Event event) {

        //if (selectedId>1){
            Widget W = getCurrentWidget();
            String t = propText.getText();
            //t=t.replaceAll(" ","");
            W.setText(t);
        //}
    }

    @FXML
    private void nameAction(Event event) {

        //if (selectedId > 1) {
            Widget W = getCurrentWidget();
            if (W != null) {
                W.setName(propName.getText().replaceAll(" ",""));
            }
        //}
    }

    @FXML
    private void deleteProject(Event event) {

        if (txtProj.getText().length()==0) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete this project ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

            wasModified=false;
            UIupdate=true;
            //tell("deleting");

            pane.getChildren().clear();
            clearProperties();
            //comboProjects.setValue("");
            widgets.clear();
            maxid=0;
            selectedId=0;

            window = createWidget(TYPE_WINDOW);
            Widget W = new Widget();
            W.setId(Integer.parseInt(window.getId()));
            W.setType("Window");
            //W.setX((int) Math.floor(window.getX()));
            //W.setY((int) Math.floor(window.getY()));
            W.setWidth((int) Math.floor(window.getWidth()));
            W.setHeight((int) Math.floor(window.getHeight()));
            widgets.add(W);

            try {
                String p = System.getProperty("user.dir");
                File file = new File(p +"\\ALFrame\\" + txtProj.getText() + ".alf" );
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

            listProjects();
            txtProj.setText("");

            UIupdate=false;
        }
    }



    private void adjustWidgetsToWindow(){

        wasModified=true;

        for (Iterator<Node> it = pane.getChildren().iterator(); it.hasNext(); ) {
            Rectangle rect = (Rectangle) it.next();
            if (rect.getId() == null) continue;
            if (rect.getId() == "1") continue;
            if (rect.getId().contains("handle")) continue;
            if (rect.getId().contains("glue")) continue;

            for (int i = 1; i < widgets.size(); i++) {
                try {
                    if (widgets.get(i).getId()== Integer.parseInt(rect.getId())) {
                        widgets.get(i).setX((int) Math.floor(rect.getX() - window.getX()));
                        widgets.get(i).setY((int) Math.floor(rect.getY() - window.getY()));
                        widgets.get(i).setWidth((int) Math.floor(rect.getWidth()));
                        widgets.get(i).setHeight((int) Math.floor(rect.getHeight()));
                    }
                } catch (NumberFormatException e) {
                    tell(e.getLocalizedMessage());
                }
            }
        }
    }
    @FXML
    private void makeProject(Event event) {

        if (comboLang.getSelectionModel().getSelectedItem() == null) return;

        if (comboLang.getSelectionModel().getSelectedItem().toString().equals("java swing")){

            new MakerSwing(txtProj.getText(), widgets)
                    .build();
        } else
        if (comboLang.getSelectionModel().getSelectedItem().toString().equals("javafx")){

            new MakerJavaFX(txtProj.getText(), widgets)
                    .build();
        }
    }

    @FXML
    private void saveProject(ActionEvent event) {

        saveCurrentProject();

    }

    private void saveCurrentProject() {

        if ((txtProj.getText().trim().length()>0) && (!txtProj.getText().equals("import...")) && (widgets.size()>1)) {

            UIupdate=true;
            try {
                wasModified=false;
                String p = System.getProperty("user.dir");
                File file = new File(p +"\\ALFrame\\");
                file.mkdir();
                file = new File (file.getAbsolutePath()+"\\"+txtProj.getText().trim() + ".alf");

                if (file.exists())
                    file.delete();

                tell("saving");
                for (int i = 0; i < widgets.size() ; i++) {
                    String s=  ("id:" + widgets.get(i).getId()+
                            " t:"+widgets.get(i).getType()+
                            " x:"+widgets.get(i).getX()+
                            " y: "+ widgets.get(i).getY() +
                            " w: "+ widgets.get(i).getWidth() +
                            " h : " + widgets.get(i).getWidth());
                    tell(s);
                }

                FileOutputStream f = new FileOutputStream(file,false);
                ObjectOutputStream o = new ObjectOutputStream(f);

                o.writeObject(widgets);

                o.close();
                f.close();

                //tell("set selected project in combo");
                listProjects();
                comboProjects.setValue(txtProj.getText().trim());

            } catch (IOException e) {
                e.printStackTrace();
            }
            UIupdate=false;
        }
    }

    private void listProjects() {

        String p = System.getProperty("user.dir");
        File file = new File(p +"\\ALFrame\\");

        comboProjects.getItems().clear();

        if (file.exists()){
            File[] files = file.listFiles();
            for (File f : files){
                if (f.getName().toLowerCase().endsWith(".alf")){
                    comboProjects.getItems().add(f.getName().replaceAll("\\.alf",""));
                }
            }
        }
        comboProjects.getItems().add("import...");
    }

        @FXML
    private void deleteElement(Event event){

        if (selectedId>1) {

            for (int i = 0; i < widgets.size() ; i++) {
                if (widgets.get(i).getId()==selectedId){
                    widgets.remove(i);
                    break;
                }
            }
            clearProperties();
            deleteRect(selectedId + "");
            deleteRect(selectedId + "handle");
            selectedId=0;
        }
    }

    private Rectangle getCurrentRectangle(){

        Rectangle rectangleRef = null;

        for (Iterator<Node> it = pane.getChildren().iterator(); it.hasNext(); ) {
            Rectangle rec = (Rectangle) it.next();
            if (rec.getId() == null) continue;

            if (rec.getId().equals(String.valueOf(selectedId))) {
                rectangleRef = rec;
                break;
            }
        }
        return rectangleRef;
    }

    private Rectangle getRectangleForId(int selectedId){
        for (Iterator<Node> it = pane.getChildren().iterator(); it.hasNext(); ) {
            Rectangle rec = (Rectangle) it.next();
            if (rec.getId() == null) continue;

            if (rec.getId().equals(String.valueOf(selectedId))) {
                return rec;
            }
        }
        return null;
    }
    @FXML
    private void duplicateElement(Event event) {

        if (selectedId>1) {
            Rectangle rectangleRef = getRectangleForId(selectedId);//null;

            /*//Pane root = (Pane) loader.getNamespace().get("pane");
            for (Iterator<Node> it = pane.getChildren().iterator(); it.hasNext(); ) {
                Rectangle rec = (Rectangle) it.next();
                if (rec.getId() == null) continue;

                if (rec.getId().equals(String.valueOf(selectedId))) {
                    rectangleRef = rec;
                    break;
                }
            }*/
            if (rectangleRef!=null) {
                Rectangle rect = createWidget(TYPE_WIDGET);
                rect.setX(rectangleRef.getX()+rectangleRef.getWidth()+10);
                rect.setY(rectangleRef.getY());
                rect.setWidth(rectangleRef.getWidth());
                rect.setHeight(rectangleRef.getHeight());

                Widget W = getCurrentWidget().clone();
                W.setId(Integer.parseInt(rect.getId()));
                W.setName("item_"+rect.getId());
                W.setX((int)(rectangleRef.getX()+rectangleRef.getWidth()+10));
                widgets.add(W);

                rect.setFill(getColor(W.getType()));
            }
        }
    }

    @FXML
    private void createElement(){

        Rectangle rect = createWidget(TYPE_WIDGET);
        Widget W = new Widget();
        W.setType("Text Field");
        W.setId(Integer.parseInt(rect.getId()));
        W.setX((int)rect.getX());
        W.setY((int)rect.getY());
        W.setWidth((int)rect.getWidth());
        W.setHeight((int)rect.getHeight());
        W.setName("item_"+rect.getId());
        widgets.add(W);
    }

    private void deleteRect(String theId){
        //Pane root = (Pane) loader.getNamespace().get("pane");
        for (Iterator<Node> it = pane.getChildren().iterator(); it.hasNext(); ) {
            Rectangle rec = (Rectangle) it.next();
            if (rec.getId() == null) continue;

            if (rec.getId().equals(theId)) {
                ((Pane) rec.getParent()).getChildren().remove(rec);
                return;
            }
        }
    }


    private void clearProperties() {
        //javafx.scene.control.TextField txtId =  (javafx.scene.control.TextField) loader.getNamespace().get("txtId");
        txtId.setText("");
        propX.setText("");
        propY.setText("");
        propRight.setText("");
        propBottom.setText("");
        propWidth.setText("");
        propHeight.setText("");
        propText.setText("");
        propName.setText("");
        propType.setValue("");
    }

    @FXML
    private void onKeyX(){
        Widget W = getCurrentWidget();
        if (W!=null){
            try {
                int newValue = Integer.parseInt(propX.getText());
                W.setX(newValue);
                int val = (int)(newValue+window.getX());
                Rectangle rect = getCurrentRectangle();
                rect.setX(val);
                propRight.setText("" + (int) Math.floor(window.getX() + window.getWidth() - rect.getX() - rect.getWidth()));
            } catch (NumberFormatException e) {
                tell(e.getLocalizedMessage());
            }
        }
    }

    @FXML
    private void onKeyY(){
        Widget W = getCurrentWidget();
        if (W!=null){
            try {
                int newValue = Integer.parseInt(propY.getText());
                W.setY(newValue);
                int val = (int)(newValue+window.getY());
                Rectangle rect = getCurrentRectangle();
                rect.setY(val);
                propBottom.setText("" + (int) Math.floor(window.getY() + window.getHeight() - rect.getY() - rect.getHeight()));
            } catch (NumberFormatException e) {
                tell(e.getLocalizedMessage());
            }
        }
    }
    @FXML
    private void onKeyW(){
        Widget W = getCurrentWidget();
        if (W!=null){
            try {
                int newValue = Integer.parseInt(propWidth.getText());
                W.setWidth(newValue);
                Rectangle rect = getCurrentRectangle();
                rect.setWidth(newValue);
                propRight.setText("" + (int) Math.floor(window.getX() + window.getWidth() - rect.getX() - rect.getWidth()));
            } catch (NumberFormatException e) {
                tell(e.getLocalizedMessage());
            }
        }
    }

    @FXML
    private void onKeyH(){
        Widget W = getCurrentWidget();
        if (W!=null){
            try {
                int newValue = Integer.parseInt(propHeight.getText());
                W.setHeight(newValue);
                Rectangle rect = getCurrentRectangle();
                rect.setHeight(newValue);
                propBottom.setText("" + (int) Math.floor(window.getY() + window.getHeight() - rect.getY() - rect.getHeight()));
            } catch (NumberFormatException e) {
                tell(e.getLocalizedMessage());
            }
        }
    }

    @FXML
    private void onKeyR(){
        Widget W = getCurrentWidget();
        if (W!=null){
            try {
                int newValue = Integer.parseInt(propRight.getText());

                int newRectXValue = (int) Math.floor(window.getX() + window.getWidth() - W.getWidth() - newValue);
                Rectangle rect = getCurrentRectangle();
                rect.setX(newRectXValue);

                W.setX((int) (newRectXValue - window.getX()));
                propX.setText("" + (int) (newRectXValue - window.getX()));

            } catch (NumberFormatException e) {
                tell(e.getLocalizedMessage());
            }
        }
    }

    @FXML
    private void onKeyB(){
        Widget W = getCurrentWidget();
        if (W!=null){
            try {
                int newValue = Integer.parseInt(propBottom.getText());

                int newRectYValue = (int) Math.floor(window.getY() + window.getHeight() - W.getHeight() - newValue);
                Rectangle rect = getCurrentRectangle();
                rect.setY(newRectYValue);

                W.setY((int) (newRectYValue - window.getY()));
                propY.setText("" + (int) (newRectYValue - window.getY()));

            } catch (NumberFormatException e) {
                tell(e.getLocalizedMessage());
            }
        }
    }


    @FXML
    private void initialize() {

        UIupdate=true;
        clearProperties();
        listProjects();
        //comboLang.getItems().add("javafx");
        comboProjects.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                            if (UIupdate) return;
                            if (newValue == null) return;

                            //tell("project selection changed from " + oldValue + " to " + newValue);
                            if (wasModified) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Project modified");
                                alert.setHeaderText(null);
                                alert.setContentText("Do you want to save your project before closing it ?");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.get() == ButtonType.OK){
                                    saveCurrentProject();
                                }

                                wasModified=false;

                                Platform.runLater(new Runnable() {
                                    @Override public void run() {
                                        UIupdate = true;
                                        comboProjects.setValue("" + newValue);
                                        UIupdate = false;
                                    }});

                            } //else {

                                if (newValue.equals("import...")) {
                                    FileChooser fileChooser = new FileChooser();
                                    fileChooser.setTitle("Open File");
                                    fileChooser.getExtensionFilters().addAll(
                                            new FileChooser.ExtensionFilter("All Supported Files", "*.vb", "*.java", "*.fxml", "*.xml"),
                                            new FileChooser.ExtensionFilter("VB.NET form Files (.Designer.vb)", "*.vb"),
                                            new FileChooser.ExtensionFilter("Java Files (.java)", "*.java"),
                                            new FileChooser.ExtensionFilter("JAVAFX Files (.fxml)", "*.fxml"),
                                            new FileChooser.ExtensionFilter("Android Layout Files (.xml)", "*.xml")
                                    );
                                    File selectedFile = fileChooser.showOpenDialog(primaryStage);
                                    if (selectedFile != null) {
                                        //primaryStage.display(selectedFile);
                                        tell(selectedFile.getAbsolutePath());
                                        if (selectedFile.getAbsolutePath().endsWith(".Designer.vb")) {

                                        } else if (selectedFile.getAbsolutePath().toLowerCase().endsWith(".java")) {

                                        } else if (selectedFile.getAbsolutePath().toLowerCase().endsWith(".fxml")) {

                                        } else if (selectedFile.getAbsolutePath().toLowerCase().endsWith(".xml")) {

                                        }
                                    }
                                } else if (((String) newValue).length() > 0) {

                                    /**
                                     * load project
                                     */
                                        //UIupdate = true;
                                        String p = System.getProperty("user.dir");
                                        File file = new File(p + "\\ALFrame\\" + newValue + ".alf");

                                        Platform.runLater(new Runnable() {
                                        @Override public void run() {
                                            txtProj.setText((String) newValue);
                                        }});

                                            try {
                                                FileInputStream f = new FileInputStream(file);
                                                ObjectInputStream o = new ObjectInputStream(f);

                                                widgets = (ArrayList<Widget>) o.readObject();

                                                o.close();
                                                f.close();

                                                tell("loading");
                                                for (int i = 0; i < widgets.size() ; i++) {
                                                    String s=  ("id:" + widgets.get(i).getId()+
                                                            " t:"+widgets.get(i).getType()+
                                                            " x:"+widgets.get(i).getX()+
                                                            " y: "+ widgets.get(i).getY() +
                                                            " w: "+ widgets.get(i).getWidth() +
                                                            " h : " + widgets.get(i).getHeight());
                                                    tell(s);
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }

                                            /*
                                            create Rectangles for widgets
                                             */
                                            //clearProperties();
                                    Platform.runLater(new Runnable() {
                                        @Override public void run() {
                                            UIupdate = true;
                                            pane.getChildren().clear();
                                            maxid = 0;
                                            for (int i = 0; i < widgets.size(); i++) {
                                                if (widgets.get(i).getType().equals("Window")) {
                                                    window = createWidget(TYPE_WINDOW);
                                                    //window.setX(widgets.get(i).getX());
                                                    //window.setY(widgets.get(i).getY());
                                                    window.setWidth(widgets.get(i).getWidth());
                                                    window.setHeight(widgets.get(i).getHeight());
                                                } else {
                                                    Rectangle rect = createWidget(TYPE_WIDGET);
                                                    rect.setX(window.getX() + widgets.get(i).getX());
                                                    rect.setY(window.getY() + widgets.get(i).getY());
                                                    rect.setWidth(widgets.get(i).getWidth());
                                                    rect.setHeight(widgets.get(i).getHeight());

                                                    rect.setFill(getColor(widgets.get(i).getType()));
                                                }
                                            }
                                            UIupdate = false;
                                            wasModified = false;
                                        }});

                                        //}});
                                }
                        //}
                    }

                });

        propType.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                        if (UIupdate) return;

                        if (selectedId>1) {
                            Widget W = getCurrentWidget();
                            if(W!=null) {

                                tell("type changed from item "+selectedId + " to "+newValue);

                                W.setType((String) newValue);

                                Rectangle rectangleRef = getRectangleForId(selectedId);
                                if (rectangleRef!=null) {
                                    rectangleRef.setFill(getColor((String) newValue));
                                   /* if (newValue.equals("TEXT")){
                                        rectangleRef.setFill(Color.NAVY);
                                    }else*
                                    if (newValue.equals("Button")){
                                        rectangleRef.setFill(COLOR_BUTTON);
                                    }else
                                    if (newValue.equals("Check Box")){
                                        rectangleRef.setFill(COLOR_CHECK);
                                    }else
                                    if (newValue.equals("Drop Down Menu")){
                                        rectangleRef.setFill(COLOR_DROPDOWN);
                                    }else
                                    if (newValue.equals("Label")){
                                        rectangleRef.setFill(COLOR_LABEL);
                                    }else
                                    if (newValue.equals("Text Area")){
                                        rectangleRef.setFill(COLOR_TEXTAREA);
                                    }  else
                                    if (newValue.equals("Text Field")){
                                        rectangleRef.setFill(COLOR_TEXT);
                                    }
*/
                                }
                            }
                        }
                    }
                });

        window = createWidget(TYPE_WINDOW);
        Widget W = new Widget();
        W.setId(Integer.parseInt(window.getId()));
        W.setType("Window");
        //W.setX((int) Math.floor(window.getX()));
        //W.setY((int) Math.floor(window.getY()));
        W.setWidth((int) Math.floor(window.getWidth()));
        W.setHeight((int) Math.floor(window.getHeight()));
        widgets.add(W);

        wasModified=false;
        UIupdate=false;
    }

    private Paint getColor(String type) {
        if (type.equals("Text Field")){
            return(COLOR_TEXT);
        }else
        if (type.equals("Text Area")){
            return (COLOR_TEXTAREA);
        }else
        if (type.equals("Button")){
            return (COLOR_BUTTON);
        }else
        if (type.equals("Check Box")){
            return (COLOR_CHECK);
        }else
        if (type.equals("Drop Down Menu")){
            return (COLOR_DROPDOWN);
        }else
        if (type.equals("Label")){
            return (COLOR_LABEL);
        }
        return(COLOR_TEXT);
    }

    private Rectangle createWidget(int elementType) {

        if (UIupdate) wasModified = true;

        Rectangle rect = null;
        //Pane root = (Pane) loader.getNamespace().get("pane");

        switch (elementType){
            case TYPE_WIDGET:
                rect = createElement(200, 200, 100, 40);
                rect.setFill(COLOR_TEXT);
                break;

            case TYPE_WINDOW:
                /*double w = pane.getPrefWidth();
                double h = pane.getPrefHeight();
                int x = new Double  ((w-400)/2).intValue();
                int y = new Double ((h-300)/2).intValue();
                */
                rect = createElement( 10,10, 500, 350);
                rect.setFill(Color.DARKGRAY);
                break;
        }
        return rect;
    }

    private Rectangle createElement(double x, double y, double width, double height) {

        if (UIupdate) wasModified=true;

        maxid++;
        Rectangle rect = new Rectangle(x, y, width, height);
        rect.setId(""+maxid);

        // bottom right resize handle:
        //Circle resizeHandleSE = new Circle(handleRadius, Color.GOLD);
        Rectangle resizeHandleSE = new Rectangle(x,y,handleRadius, handleRadius);
        resizeHandleSE.setCursor(Cursor.SE_RESIZE);
        resizeHandleSE.setFill(Color.GREEN);

        // bind to bottom right corner of Rectangle:
        resizeHandleSE.xProperty().bind(rect.xProperty().add(rect.widthProperty()));
        resizeHandleSE.yProperty().bind(rect.yProperty().add(rect.heightProperty()));
        resizeHandleSE.setId(""+maxid+"handle");
        pane.getChildren().add(resizeHandleSE);

        Wrapper<Point2D> mouseLocation = new Wrapper<>();

        setUpDraggingHandler(resizeHandleSE, mouseLocation); ;
        setUpDraggingRectangle(rect, mouseLocation); ;

        pane.getChildren().add(rect);

        return rect ;
    }
    private void removeAllGlueRectangles() {

        glueSide=0;
        boolean found=true;
        while (found) {
            found=false;
            for (Iterator<Node> it = pane.getChildren().iterator(); it.hasNext(); ) {
                Rectangle rec = (Rectangle) it.next();
                if (rec.getId() == null) continue;

                if (rec.getId().contains("glue")) {
                    ((Pane) rec.getParent()).getChildren().remove(rec);
                    found=true;
                    break;
                }
            }
        }
    }
    private void createGlue(int theId) {

        Rectangle recSel = getRectangleForId(theId);

        Rectangle rectUp = new Rectangle(recSel.getX(), recSel.getY()-7, recSel.getWidth(), 5);
        rectUp.setId(""+theId+"glueUp");
        rectUp.setCursor(Cursor.CROSSHAIR);
        rectUp.setFill(Color.CYAN);
        rectUp.setOnMousePressed(event -> {
            tell("up");
            glueSide=1;
            pane.setCursor(Cursor.N_RESIZE);
        });

        Rectangle rectDown = new Rectangle(recSel.getX(), recSel.getY()+recSel.getHeight()+2, recSel.getWidth(), 5);
        rectDown.setId(""+theId+"glueDown");
        rectDown.setCursor(Cursor.CROSSHAIR);
        rectDown.setFill(Color.CYAN);
        rectDown.setOnMousePressed(event -> {
            tell("down");
            glueSide=3;
            pane.setCursor(Cursor.S_RESIZE);
        });

        Rectangle rectRight = new Rectangle(recSel.getX()+recSel.getWidth()+2, recSel.getY(), 5, recSel.getHeight());
        rectRight.setId(""+theId+"glueRight");
        rectRight.setCursor(Cursor.CROSSHAIR);
        rectRight.setFill(Color.CYAN);
        rectRight.setOnMousePressed(event -> {
            tell("right");
            glueSide=2;
            pane.setCursor(Cursor.E_RESIZE);
        });

        Rectangle rectLeft = new Rectangle(recSel.getX()-7, recSel.getY(), 5, recSel.getHeight());
        rectLeft.setId(""+theId+"glueLeft");
        rectLeft.setCursor(Cursor.CROSSHAIR);
        rectLeft.setFill(Color.CYAN);
        rectLeft.setOnMousePressed(event -> {
            tell("left");
            glueSide=4;
            pane.setCursor(Cursor.W_RESIZE);
        });

        pane.getChildren().add(rectUp);
        pane.getChildren().add(rectDown);
        pane.getChildren().add(rectLeft);
        pane.getChildren().add(rectRight);
    }

    private Widget getCurrentWidget() {
        for (int i = 0; i < widgets.size() ; i++) {
            if (widgets.get(i).getId()==selectedId){
                return widgets.get(i);
            }
        }
        return null;
    }

    private void setUpDraggingHandler(Shape shape, Wrapper<Point2D> mouseLocation) {

        shape.setOnDragDetected(event -> {
            wasModified=true;
            shape.getParent().setCursor(Cursor.HAND);
            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });

        shape.setOnMousePressed(event -> {

            UIupdate=true;
            removeAllGlueRectangles();
            selectedId = Integer.parseInt(shape.getId().replace("handle",""));
            for (Iterator<Node> it = pane.getChildren().iterator(); it.hasNext(); ) {
                Rectangle rec = (Rectangle)it.next();
                if (rec.getId().equals(String.valueOf(selectedId)))
                    rec.setStroke(Color.CYAN);
                else
                    rec.setStroke(Color.TRANSPARENT);
            }
            showProperties();
        });

        shape.setOnMouseReleased(event -> {
            shape.getParent().setCursor(Cursor.DEFAULT);
            mouseLocation.value = null ;

            Rectangle rect =null ;
            String recid = shape.getId().replace("handle","");
            for (Iterator<Node> it = ((Pane) shape.getParent()).getChildren().iterator(); it.hasNext(); ) {
                Rectangle rec = (Rectangle)it.next();
                if (rec.getId().equals(recid)) {
                    rect = rec;
                    break;
                }
            }
            if (rect!=null) {
                for (int i = 0; i < widgets.size() ; i++) {
                    if (widgets.get(i).getId()==selectedId){
                        widgets.get(i).setX((int) Math.floor(rect.getX() - window.getX()));
                        widgets.get(i).setY((int) Math.floor(rect.getY() - window.getY()));
                        widgets.get(i).setWidth((int) Math.floor(rect.getWidth()));
                        widgets.get(i).setHeight((int) Math.floor(rect.getHeight()));
                        break;
                    }
                }
                propX.setText("" + (int) Math.floor(rect.getX() - window.getX()));
                propY.setText("" + (int) Math.floor(rect.getY() - window.getY()));
                propWidth.setText("" + (int) Math.floor(rect.getWidth()));
                propHeight.setText("" + (int) Math.floor(rect.getHeight()));
                propRight.setText("" + (int) Math.floor(window.getX() + window.getWidth() - rect.getX() - rect.getWidth()));
                propBottom.setText("" + (int) Math.floor(window.getY() + window.getHeight() - rect.getY() - rect.getHeight()));
            }

            UIupdate=false;
        });

        shape.setOnMouseDragged(event -> {
            /**
             * widget resized from handler :
             */
            if (mouseLocation.value != null) {
                Rectangle rect =null ;
                String recid = shape.getId().replace("handle","");
                for (Iterator<Node> it = ((Pane) shape.getParent()).getChildren().iterator(); it.hasNext(); ) {
                    Rectangle rec = (Rectangle)it.next();
                    if (rec.getId().equals(recid)) {
                        rect = rec;
                        break;
                    }
                }
                if (rect!=null) {
                    double deltaX = event.getSceneX() - mouseLocation.value.getX();
                    double deltaY = event.getSceneY() - mouseLocation.value.getY();
                    double newMaxX = rect.getX() + rect.getWidth() + deltaX;
                    if (newMaxX >= rect.getX()
                            && newMaxX <= rect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                        rect.setWidth(rect.getWidth() + deltaX);
                        propWidth.setText("" + (int) Math.floor(rect.getWidth() + deltaX));
                        propRight.setText("" + (int) Math.floor(window.getX() + window.getWidth() - rect.getX() - rect.getWidth()));
                    }
                    double newMaxY = rect.getY() + rect.getHeight() + deltaY;
                    if (newMaxY >= rect.getY()
                            && newMaxY <= rect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                        rect.setHeight(rect.getHeight() + deltaY);
                        propHeight.setText("" + (int) Math.floor(rect.getHeight() + deltaY));
                        propBottom.setText("" + (int) Math.floor(window.getY() + window.getHeight() - rect.getY() - rect.getHeight()));
                    }
                    mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
                }
            }
        });
    }

    private void setUpDraggingRectangle(Shape shape, Wrapper<Point2D> mouseLocation) {

        shape.setOnMousePressed(event -> {
            //tell("pressed");
            UIupdate=true;

            if (glueSide==0) {
                /*
                 * clear selection
                 */
                //Rectangle shape = (Rectangle) event.getSource();
                for (Iterator<Node> it = ((Pane) shape.getParent()).getChildren().iterator(); it.hasNext(); ) {
                    Rectangle rec = (Rectangle) it.next();
                    rec.setStroke(Color.TRANSPARENT);
                }
                /*
                 * show selected rectangle
                 */
                shape.setStroke(Color.CYAN);
                selectedId = Integer.parseInt(shape.getId());

                showProperties();

                removeAllGlueRectangles();

            } else {
                tell("need to glue selectedId="+selectedId + " to this one with id="+Integer.parseInt(shape.getId()));

                try {
                    wasModified=true;
                    Rectangle rToChange = getRectangleForId(selectedId);
                    Rectangle rectRef = getRectangleForId(Integer.parseInt(shape.getId()));

                    //reshape selectedId
                    switch (glueSide){
                        case 1://top
                            rToChange.setY(rectRef.getY());
                            break;
                        case 2://right
                            double newX=rectRef.getX()+rectRef.getWidth()-rToChange.getX();
                            if (newX>0)
                                rToChange.setWidth(newX);
                            else
                                rToChange.setX(rectRef.getX()+rectRef.getWidth()-rToChange.getWidth());
                            break;
                        case 3://down
                            double newY=rectRef.getY()+rectRef.getHeight()-rToChange.getY();
                            if (newY>0)
                                rToChange.setHeight(newY);
                            else
                                rToChange.setY(rectRef.getY()+rectRef.getHeight()-rToChange.getHeight());
                            break;
                        case 4://left
                            rToChange.setX(rectRef.getX());
                            break;
                    }
                    for (int i = 0; i < widgets.size() ; i++) {
                        if (widgets.get(i).getId()==selectedId){
                            widgets.get(i).setX((int)(rToChange.getX()-window.getX()));
                            widgets.get(i).setY((int)(rToChange.getY()-window.getY()));
                            widgets.get(i).setWidth((int)rToChange.getWidth());
                            widgets.get(i).setHeight((int)rToChange.getHeight());
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    //if a resize handler or glue are selected, id is not a number
                    tell(e.getLocalizedMessage());
                }

                removeAllGlueRectangles();
                createGlue(selectedId);
            }
        });

        shape.setOnDragDetected(event -> {
            wasModified=true;
            shape.getParent().setCursor(Cursor.CLOSED_HAND);
            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });

        shape.setOnMouseReleased(event -> {
            shape.getParent().setCursor(Cursor.DEFAULT);
            mouseLocation.value = null;

            Rectangle rect = (Rectangle) shape;
            propX.setText("" + (int) Math.floor(rect.getX() - window.getX()));
            propY.setText("" + (int) Math.floor(rect.getY() - window.getY()));
            propWidth.setText("" + (int) Math.floor(rect.getWidth()));
            propHeight.setText("" + (int) Math.floor(rect.getHeight()));
            propRight.setText("" + (int) Math.floor(window.getX() + window.getWidth() - rect.getX() - rect.getWidth()));
            propBottom.setText("" + (int) Math.floor(window.getY() + window.getHeight() - rect.getY() - rect.getHeight()));

            for (int i = 0; i < widgets.size() ; i++) {
                if (widgets.get(i).getId()==selectedId){
                    widgets.get(i).setX(Integer.parseInt(propX.getText()));
                    widgets.get(i).setY(Integer.parseInt(propY.getText()));
                    widgets.get(i).setWidth(Integer.parseInt(propWidth.getText()));
                    widgets.get(i).setHeight(Integer.parseInt(propHeight.getText()));
                    break;
                }
            }

            if (rect.getId().equals("1")) {
                adjustWidgetsToWindow();
            }

            createGlue(selectedId);
            UIupdate=false;
        });

        shape.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {

                Rectangle rect = (Rectangle) shape;

                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = rect.getX() + deltaX;
                double newMaxX = newX + rect.getWidth();
                if (newX >= handleRadius
                        && newMaxX <= rect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    rect.setX(newX);
            /*if (rect.getId().equals("1")) {
                propX.setText("" + (int) Math.floor(newX));
            }else*/
                    propX.setText("" + (int) Math.floor(newX - window.getX()));
                }
                double newY = rect.getY() + deltaY;
                double newMaxY = newY + rect.getHeight();
                if (newY >= handleRadius
                        && newMaxY <= rect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    rect.setY(newY);
            /*if (rect.getId().equals("1")) {
                propY.setText("" + (int) Math.floor(newY));
            }else*/
                    propY.setText("" + (int) Math.floor(newY - window.getY()));
                }
                propWidth.setText("" + (int) Math.floor(rect.getWidth()));
                propHeight.setText("" + (int) Math.floor(rect.getHeight()));
                propRight.setText("" + (int) Math.floor(window.getX() + window.getWidth() - rect.getX() - rect.getWidth()));
                propBottom.setText("" + (int) Math.floor(window.getY() + window.getHeight() - rect.getY() - rect.getHeight()));

                //tell("drag : "+propX.getText()+" "+propY.getText());

                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });
    }


    private void showProperties() {
        UIupdate=true;
        /*
         * set properties for selected widget
         */
        btnDel.setDisable(selectedId<2);
        btnDup.setDisable(selectedId<2);
        txtId.setText(""+selectedId);
        Widget W = getCurrentWidget();
        if (W!=null) {
            propName.setText(W.getName());
            propText.setText(W.getText());
            propType.setValue(W.getType());
        }
        UIupdate=false;
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    static class Wrapper<T> { T value ; }

}
