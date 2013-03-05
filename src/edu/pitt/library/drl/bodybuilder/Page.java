package edu.pitt.library.drl.bodybuilder;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;


public class Page {
    

    String fileRoot, imageFileName, ocrText;
    ImageIcon theImage;

    
    /**
     * 
     * @param file
     * @throws java.io.FileNotFoundException
     */
    public Page(File file) throws FileNotFoundException {
    
        if (!file.isFile()) {
            throw new FileNotFoundException();
        }
        
        this.fileRoot = file.getParent();
        this.imageFileName = file.getName();
        theImage = new ImageIcon(file.getPath());
        
        // get OCR text
        Matcher matcher = Pattern.compile(".jpg").matcher(file.toString());
        String ocrFilePath = matcher.replaceAll(".txt");
        File ocrFile = new File(ocrFilePath);
        OcrPage ocrPage = new OcrPage(ocrFile);
        this.ocrText = ocrPage.getText();
     
    }

     
    public ImageIcon getImage () {
        
        return theImage;
        
    }
    
    public String getOcrText () {
        
        return ocrText;
        
    }  
}
