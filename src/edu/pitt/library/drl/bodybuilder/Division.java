package edu.pitt.library.drl.bodybuilder;

/*
 * Division.java
 *
 * Created on May 23, 2006, 10:24 AM
 *
 */

/**
 *
 * @author abrenner
 * 
 * Divisions used to be much more complex; this remains as a stub in case it goes back that way.
 * 
 */
public class Division {
    
    private String type;
    private String title;
    
    
    /** Creates a new instance of Division */
    public Division() {
        
    }
    
    
    public boolean hasType() {
        if (type != null)
            return true;
        else
            return false;
    }
        
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

     
}
