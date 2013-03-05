package edu.pitt.library.drl.bodybuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 *
 * @author abrenner
 */
public class OcrPage {
    
    String text;
    
    /** Creates a new instance of OcrPage
     * 
     * @param ocrFile The File that contains the OCR text
     */
    public OcrPage(File ocrFile) {
        
        // Try reading contents of text file
        try  {       
            FileReader fr = new FileReader(ocrFile);
            BufferedReader in = new BufferedReader(fr);
            String pageText = "", line;
            while ((line = in.readLine()) != null) {
                pageText += (line + "\n"); // re-add stripped newline char
            }
            in.close();
            this.text = pageText;
        }
        catch (FileNotFoundException fnf) {
            this.text = "No OCR File found for this image";
        }
        catch (Exception e)  {
            this.text = "Problem! :" + e;
        }

    }
        
    /**
     * Gets the contents of the OCR text file.
     * @return String 
     */
    public String getText() {
 
        return this.text;
    
    }
    
}
