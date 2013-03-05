package edu.pitt.library.drl.bodybuilder;

import java.io.*;
import org.w3c.dom.Document;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class WriteXML {
    
    public static void writeToFile(Document document, String xmlFilePath) throws Exception {

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source source = new DOMSource((Document) document);
            FileOutputStream fileOut = new FileOutputStream(xmlFilePath);
            Result output = new StreamResult(fileOut);
            transformer.transform(source, output);

        } catch (Exception e) {

            throw new Exception("Problem writing XML: " + e);
        
        }

    }
    
}
