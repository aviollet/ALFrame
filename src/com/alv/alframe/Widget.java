package com.alv.alframe;

import java.io.Serializable;

public class Widget  implements Serializable {

    private int X;
    private int Y;
    private int width;
    private int height;
    private int id;
    private String name="";
    private String text="";
    private String type="";

    public Widget() {
    }

    public Widget clone() {
        Widget W = new Widget();
        W.width=this.width;
        W.height=this.height;
        W.X=this.X;
        W.Y=this.Y;
        W.type=this.type;
        return W;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String name) {
        this.type = name;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String name) {
        this.text = name;
    }

    public int getX() {
        return this.X;
    }
    public void setX(int x) {
        this.X = x;
    }
    public int getY() {
        return this.Y;
    }
    public void setY(int y) {
        this.Y = y;
    }
    public int getWidth() {
        return this.width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

}
