package edu.pitt.library.drl.bodybuilder;

/*
 * MyDomBuilder.java
 *
 * Created on August 24, 2005, 9:54 AM
 */

/**
 *
 * @author abrenner
 */
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MyDomBuilder {
    
    // This constructor is for a brand new tree where an XML file
    // doesn't already exist.  It takes a list of
    // pages as an argument, and makes a DOM from scratch.
    public static Document makeSkeletonDOM (String[] pages) {
        
        // this should take a list of pages and make a skeleton XML document
        // after this, the XML should be saved, loaded, and DOM/JTree-ified
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
            
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();  
            
            // Create from whole cloth
            Element root = (Element) document.createElement("mets"); 
            root.setAttribute("xmlns", "http://www.loc.gov/METS/");
            //root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            root.setAttribute("xmlns:mets", "http://www.loc.gov/METS/");
            //root.setAttribute("xmlns:mods", "http://www.loc.gov/MODS");
            root.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
            //root.setAttribute("xsi:schemaLocation", "http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/mets.xsd");
            document.appendChild(root);
            
            Element fileSec = document.createElement("mets:fileSec");
            Element fileGrp = document.createElement("mets:fileGrp");
            fileSec.appendChild(fileGrp);
            root.appendChild(fileSec);
            
            Element structMap = document.createElement("mets:structMap");
            structMap.setAttribute("TYPE", "mixed");
            Element div_vol = document.createElement("mets:div");
            div_vol.setAttribute("TYPE", "volume");
            structMap.appendChild(div_vol);
            root.appendChild(structMap);
            
            for (String s : pages) {
                // File group files
                Element file = document.createElement("mets:file");
                String fid = "fid" + s;
                file.setAttribute("ID", fid);
                file.setAttribute("MIMETYPE", "image/tiff");
                file.setAttribute("SEQ", s);
                Element FLocat = document.createElement("mets:FLocat");
                String href = s + ".tif";
                FLocat.setAttribute("xlink:href", href);
                FLocat.setAttribute("LOCTYPE", "URL");
                file.appendChild(FLocat);
                fileGrp.appendChild(file);
                // Struct Map files
                Element page_div = document.createElement("mets:div");
                page_div.setAttribute("TYPE", "page");
                page_div.setAttribute("LABEL", "");
                Element fptr = document.createElement("mets:fptr");
                fptr.setAttribute("FILEID", fid);
                page_div.appendChild(fptr);
                div_vol.appendChild(page_div);
            }
            
            return document;
                        
        }  catch (Exception e) {
            
            // Parser with specified options can't be built
            System.out.println("Error creating skeleton DOM...");
            System.out.println(e.getMessage());
            return(null);
        }
    }
    
    public static Document makeDOMFromXML (File xmlFile) 
    {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            try  {
            
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(xmlFile);
                return document;
            
            }  catch (Exception e) {
                
                return null;
            
            }
            
    }
    
   
}
