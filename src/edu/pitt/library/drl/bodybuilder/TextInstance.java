package edu.pitt.library.drl.bodybuilder;

/*
 * TextInstance.java
 *
 * Created on August 26, 2005, 2:44 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author abrenner
 */
public class TextInstance {
    
    private String ID;
    private String[] accPages;
    private File xmlFile;
    private File textDir;
    
    /** Creates a new instance of TextInstance */
    public TextInstance(File file) throws Exception {
             
        textDir = file;
        ID = textDir.getName();
        xmlFile = new File(file + "\\" + ID + ".mets.xml");
        
        // Get the number of pages from the jpg files in the directory
        // (uses an anonymous inner class to implement a FilenameFilter)
        String [] fileNames = textDir.list(new FilenameFilter() {
            public boolean 
            accept(File dir, String n) {
              String f = new File(n).getName();
              return f.indexOf(".jpg") != -1;
            }
          });
        
          if (fileNames.length < 1) {
              
              throw new IOException("No jpegs found.  Check the directory and try again.");
              
          }  
          
          accPages = fileNames;
          java.util.Arrays.sort(accPages);  
        
        for (int i=0; i<fileNames.length; i++)
        {
            Matcher matcher = Pattern.compile(".jpg$").matcher(fileNames[i]);
            accPages[i] = matcher.replaceFirst("");
        }
  
        
    }
    
    public File getXmlFile()
    {
        return xmlFile;
    }
    
    
    public String getID()
    {
        return ID;
    }

	public String[] getPages() {
		return accPages;
	}


    
}
