
package edu.pitt.library.drl.bodybuilder;

/*
 * Author.java
 *
 * Created on May 22, 2006, 10:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author abrenner
 */
public class Author {
    
    private String lastName;
    private String firstName;
    
    /** Creates a new instance of Author */
    public Author(String fn, String ln) {
        setFirstName(fn);
        setLastName(ln);
    }
    
    public void setFirstName (String fn)
    {
           this.firstName = fn;
    }
    
    public void setLastName (String ln)
    {
        this.lastName = ln;
    }
    
    public String getFirstName ()
    {
        return firstName;
    }
    
    public String getLastName ()
    {
        return lastName;
    }
    
    @Override
    public String toString()
    {
        return (firstName + " " + lastName);
    }
}
