package com.referex.model;

/**
 * Created by l on 23/10/2017.
 */

public class BottomSheet {
    String text, value;
    
    public BottomSheet (String text, String value) {
        this.text = text;
        this.value = value;
    }
    
    public String getText () {
        return text;
    }
    
    public void setText (String text) {
        this.text = text;
    }
    
    public String getValue () {
        return value;
    }
    
    public void setValue (String value) {
        this.value = value;
    }
}
