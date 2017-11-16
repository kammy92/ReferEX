package com.referex.model;

/**
 * Created by l on 23/10/2017.
 */

public class MyAccount {
    String text, value, candidates;
    
    public MyAccount (String text, String value, String candidates) {
        this.text = text;
        this.value = value;
        this.candidates = candidates;
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
    
    public String getCandidates () {
        return candidates;
    }
    
    public void setCandidates (String candidates) {
        this.candidates = candidates;
    }
}
