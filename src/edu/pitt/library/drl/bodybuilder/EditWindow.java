package edu.pitt.library.drl.bodybuilder;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author abrenner
 */

public class EditWindow extends JFrame
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Global GUI Elements
    private JFrame frame;
    private JButton addDivButton, editDivButton, deleteDivButton, autoNumberPagesButton;
    private JCheckBox pageNumberOverwrite;
    private JLabel idLabel, textId, pageImage;
    private JTextArea OCRText, XmlView;
    private DomTree domTree;
    private JScrollPane pageImageScroll, treeScrollPane;

    // Other Global Objects
    private boolean textChanged = false;
    private DomTreeModel model;
    private String itemPath; // from JFileChooser
    private Document document; 
    private TextInstance text; // an object representing current text selected
    private String pageID; // represents current page selection
    private Preferences prefs; // user preferences node object
    final String PREF_FILE_ROOT_PATH = "file_root_path";
    final String PREF_FILE_SELECT_METHOD = "file_selection_method";    
    final String PREF_OPEN_FILE_LOCATION = "open_file_location";

    final Font labelFont = new Font("Sans-serif", Font.PLAIN, 11);
    final Font treeFont = new Font("Monospaced", Font.PLAIN, 13);
        
    /**
     * Creates main GUI window.
     */
    public EditWindow() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            // OK to do nothing here; setLookAndFeel is just cosmetic.
        }

        ActionListener aListener = new MyActionListener();
        
        // The main frame
        frame = new JFrame("BodyBuilder");

        idLabel = new JLabel("Working with:");
        textId = new JLabel("");
        
        // "Add DIV" button
        addDivButton = new JButton("+ DIV");
        addDivButton.addActionListener(aListener);
        
        // "Edit DIV" button
        editDivButton = new JButton("Edit DIV");
        editDivButton.addActionListener(aListener);
        
        // "Delete DIV" button
        deleteDivButton = new JButton("- DIV");
        deleteDivButton.addActionListener(aListener);

        // "Auto Number Pages" button
        autoNumberPagesButton = new JButton("AUTO #");
        autoNumberPagesButton.addActionListener(aListener);
        
        // "Overwrite Page Numbers" check box
        pageNumberOverwrite = new JCheckBox("Overwrite Page Numbers");
        pageNumberOverwrite.addActionListener(aListener);
              
        // OCR text pane, put inside a JScrollPane
        OCRText = new JTextArea();
        OCRText.setLineWrap(true);
        OCRText.setEditable(false);
        JScrollPane OCRtextPanel = new JScrollPane(OCRText);
        
        // XML view text pane, put inside a JScrollPane
        XmlView = new JTextArea();
        XmlView.setLineWrap(true);
        XmlView.setEditable(false);
        @SuppressWarnings("unused")
		JScrollPane XmlViewPanel = new JScrollPane(XmlView);
        pageImage = new JLabel();
        pageImageScroll = new JScrollPane(pageImage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // TabbedPane to hold Page Image and OCR Text
        JTabbedPane tabby = new JTabbedPane();
        tabby.addTab("Page Image", pageImageScroll);
        tabby.addTab("OCR Text", OCRtextPanel);
        // tabby.addTab("XML View", XmlViewPanel);  -- not yet...

        // "File" Menu
        JMenu textMenu = new JMenu("File");
        textMenu.add(makeMenuItem("Open Text"));
        textMenu.add(makeMenuItem("Save Text"));
        textMenu.add(makeMenuItem("Quit"));
  
                
        // Add Menus to MenuBar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(textMenu);
        frame.setJMenuBar(menuBar);

        // Panel for the Item-level (i.e. Text-level) Metadata

        JPanel itemMetadata = new JPanel();
        itemMetadata.add(idLabel);
        itemMetadata.add(textId);
        JPanel namePanel = new JPanel(new GridLayout(1,1)); // used to be more here :-)
        namePanel.add(itemMetadata);

        treeScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Left Panel of the SplitPane holds Item-level Metadata
        // and the JTree
        JPanel leftPanel = new JPanel();    
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(namePanel, BorderLayout.NORTH);
        leftPanel.add(treeScrollPane);
        
        JPanel bottomLeftPanel = new JPanel();
        bottomLeftPanel.setLayout(new BorderLayout());
        
        JPanel divButtonPanel = new JPanel();
        JPanel pageNumberPanel = new JPanel();
        pageNumberPanel.add(autoNumberPagesButton);
        pageNumberPanel.add(pageNumberOverwrite);
        divButtonPanel.add(addDivButton);
        divButtonPanel.add(editDivButton);
        divButtonPanel.add(deleteDivButton);
        bottomLeftPanel.add(pageNumberPanel, BorderLayout.NORTH);
        bottomLeftPanel.add(divButtonPanel, BorderLayout.SOUTH);
        
        leftPanel.add(bottomLeftPanel, BorderLayout.SOUTH);
        
        // Right Panel of the SplitPane holds the Panel of Page-level metadata 
        // and the TabbedPane holding the Page Image, OCR Text, and XML view
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(tabby);
        
        // The SplitPane
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setOneTouchExpandable(true);
        split.setDividerLocation(375);
        frame.add(split);
        
        // Final overall Frame set-up
        frame.setSize(1000, 750);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        frame.setMaximizedBounds(env.getMaximumWindowBounds());
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        // Close the frame manually after checking if document has changed
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       
        frame.addWindowListener(new MyWindowListener());
        frame.setVisible(true);
        
        prefs = Preferences.userNodeForPackage(edu.pitt.library.drl.bodybuilder.EditWindow.class);
        

    }

    
 
    /**
     * Uses a JFileChooser to let user pick a text's directory from the filesystem.
     * After selection of directory, calls loadText
     * @see #loadText(TextInstance text)
     */
    private void pickTextFileSystem() {
    
        JFileChooser jfc;
        
        // Retrieve the user preference node for last working directory
        String storedPath = prefs.get(PREF_OPEN_FILE_LOCATION, null);
        
        if (storedPath != null) {
            jfc = new JFileChooser(storedPath);
        }
        else {
            jfc = new JFileChooser();
        }
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = jfc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File file = jfc.getSelectedFile();
            
            // store the parent location as the working directory
            prefs.put(PREF_OPEN_FILE_LOCATION, file.getParent());
            
            try {
                
              text = new TextInstance(file);
              itemPath = file.toString();
              loadText(text);    
              
            } catch (Exception e) {
                
               JOptionPane.showMessageDialog(frame, "Problem loading text: " + e.getMessage());   
               
            }
            
        }
          
    }
    
    
    
    
    private JMenuItem makeMenuItem(String name) {
        JMenuItem m = new JMenuItem(name);
        m.addActionListener(new MyActionListener());
        return m;
    }

    /**
     * Loads a text by reading/creating a DOM tree, assigning it to the TreeModel,
     * and rendering the model in the GUI.
     * @param text
     */
    private void loadText(TextInstance text) {
        
        // check to see if the text already has an XML file.  If so, load it and use it
        // to create the DOM/JTree.  If not, call the method to create a Skeleton XML file.
        
        File xmlFile = text.getXmlFile();
        
        if (xmlFile.exists()) {
            document = MyDomBuilder.makeDOMFromXML(xmlFile);
        }
        else {
            document = MyDomBuilder.makeSkeletonDOM(text.getPages());
        }
        
        model = new DomTreeModel(document);
        model.addTreeModelListener(new MyTreeModelListener());
        domTree = new DomTree(model);
        domTree.addKeyListener(new MyKeyListener());
        domTree.addTreeSelectionListener(new MyTreeSelectionListener()); 
        treeScrollPane.setViewportView(domTree);
        domTree.expandAll();
        domTree.selectFirstPage();
        domTree.requestFocus();
        textId.setText(text.getID());
        XmlView.setText(getXmlText());
    }
    

    /**
     * Loads the page image and OCR text when a page is selected.
     * @param pageID The id of the current selected page.
     */
    private void loadPageItems(String pageID) {

        // The page image
        File f = new File(itemPath + "/" + pageID + ".jpg");
        try {
            Page p = new Page(f);
            pageImage.setIcon(p.getImage());  
            OCRText.setText(p.getOcrText());
        }
        catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(frame, "Image for " + pageID + " not found.");
        } 

        
    }
   

    private void saveText()  {
        
        // Check to make sure we have a document open
        if (document == null) {
            JOptionPane.showMessageDialog(frame, "You must be working with a text to Save");
            return;
        }
        
        // Write out the DOM to XML
        try  {
            String xmlFilePath = text.getXmlFile().toString();
            WriteXML.writeToFile(document, xmlFilePath);
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Problem writing XML! This text has not been saved!\nError: " + e);
            return;
        }
              
        // re-set flag
        textChanged = false;
    }
    
    
    /**
     * Handles adding a structural division from the GUI.
     * @see ModifyDOM#addDivAsParent(Division division, AdapterNode[] nodeList, Document document)
     */
    private void addDiv() {
             
        // Check to make sure we have a document open
        if (document == null) {
            JOptionPane.showMessageDialog(frame, "You must be working with a text!");
            return;
        }
        
        // Get the selection paths as array to accomodate multi-page selections
        TreePath  [] selectedPaths = domTree.getSelectionPaths();      
        
        // get the Row number of the first selected item 
        // for re-setting the selection path after the transformation
        int [] rowList = new int [selectedPaths.length];
        

        
        // get each selected Node as an AdapterNode to give to
        // the DOM modifying method
        AdapterNode [] nodeList = new AdapterNode[selectedPaths.length];
        
        int lastRowIndex = 0;
        
        for (int i = 0; i < selectedPaths.length; i++) {
            rowList[i] = domTree.getRowForPath(selectedPaths[i]);
        
            if (i > 0 && (rowList[i] != lastRowIndex + 1)) {
                JOptionPane.showMessageDialog(frame, "Your page selection has a gap, or was selected out of order.  Please try again.\n\nHint: use \"SHIFT\" rather than \"CTRL\" to select multiple pages.\n");
                return;
            }
        
            nodeList[i] = (AdapterNode) selectedPaths[i].getLastPathComponent();
            lastRowIndex = rowList[i];
        }
        

        // Set image display to first in list (to view potential div title)
        loadPageItems(nodeList[0].getPageFileId());
        
        // Create the division editor dialog
        String divLabel = JOptionPane.showInputDialog(frame, "Division Label:");
        
        if (divLabel == null) {
        	return;
        }
        
        Division newDiv = new Division();
        newDiv.setTitle(divLabel);
        
        ModifyDOM.addDivAsParent(newDiv, nodeList, document);
        
        // Notifying the treemodel that the underlying structure
        // has changed. 
        model.nodeStructureChanged(nodeList[0]);
                
        // After changing from the root above, the whole JTree will
        // be collapsed, so this is necessary
        domTree.expandAll();
        
        // After expanding, reset selected node to first in list
        domTree.setSelectionRow(rowList[0]);
                
        textChanged = true;

  }

    private void autoNumberNativePagination() {
        
        // Select the first page
        domTree.selectFirstPage();
        
        // Get the selection path
        TreePath selectedPath = domTree.getSelectionPath();
        
        // get the selected Node as an AdapterNode to give to
        // the DOM modifying method
        AdapterNode pbNode = (AdapterNode) selectedPath.getLastPathComponent();
        String lastNativePage = "0001";
        ModifyDOM.setNativePagination(lastNativePage, pbNode, document);
        
        // loop over all the rest of the PB nodes, setting pagination.
        int rowInt = domTree.getRowForPath(selectedPath);
        while (rowInt < domTree.getRowCount() - 1) 
        {
            domTree.selectNextPage();
            selectedPath = domTree.getSelectionPath();
            rowInt = domTree.getRowForPath(selectedPath);
            pbNode = (AdapterNode) selectedPath.getLastPathComponent();
            String thisPage = derivePageFromPrevious(lastNativePage);
            ModifyDOM.setNativePagination(thisPage, pbNode, document);
            lastNativePage = thisPage;
        }
        
        // Re-select the first page
        domTree.selectFirstPage();
        domTree.requestFocus();
        textChanged = true;
        
    }
    
    /**
     * Called whenever tree selection changes.  Changes the DOM and then
     * notifies the model.
     */
    private void setNativePagination() {
        
        String natPag;

        // Get the selection path
        TreePath selectedPath = domTree.getSelectionPath();
        int rowInt = domTree.getRowForPath(selectedPath);
        
        // get the selected Node as an AdapterNode to give to
        // the DOM modifying method
        AdapterNode pageNode = (AdapterNode) selectedPath.getLastPathComponent();

        // get the PREVIOUS SIBLING pb node and its value by
        // iterating through previous siblings until we find a "mets:fptr"

        String prevNodeValue = ""; // initalize to empty string
        
        for (int r = (rowInt - 1); r > 0; r--) {
            
            TreePath tp = domTree.getPathForRow(r);
            AdapterNode prevNode = (AdapterNode) tp.getLastPathComponent();  
            Element prevElement = (Element) prevNode.domNode;
            if (prevElement.getAttribute("TYPE").equals("page")) {
                //prevNodeValue = prevNode.domNode.getAttributes().getNamedItem("LABEL").getNodeValue();
                prevNodeValue = prevElement.getAttribute("LABEL");
                break;
            }
        }

        // derive this page's number from the previous value
        if (prevNodeValue.equals("")) {
            natPag = "unum";
        }
        else {
            natPag = derivePageFromPrevious(prevNodeValue);
        }
        
        // Stop here if the page is already properly numbered, or else there is
        // an infinite loop!!!
        
        if (pageNode.domNode.getAttributes().getNamedItem("LABEL").getNodeValue().equals(natPag))
            return;
        
        //model.valueForPathChanged(selectedPath, natPag);
        ModifyDOM.setNativePagination(natPag, pageNode, document);
        
        // Notify the treemodel that the underlying structure has changed. 
        model.nodeStructureChanged(pageNode);
        
        domTree.expandAll();
        domTree.setSelectionRow(rowInt);
        
        textChanged = true;

  }
    
    
    /**
     * Handles removal of structural division from the GUI.
     */
    private void removeDiv() {
       
       // make sure that the current selection is a DIV
       AdapterNode thisNode = (AdapterNode) domTree.getLeadSelectionPath().getLastPathComponent();
       if ( ! Pattern.matches("mets:div", thisNode.domNode.getNodeName())){
           JOptionPane.showMessageDialog(frame, "You must have A DIV selected to edit it");
           return;
       }
       
       // pop-up confirmation
       int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this DIV?", "Please Confirm", JOptionPane.YES_NO_OPTION);
       if (result != JOptionPane.OK_OPTION)
            return;
       
       // get the Row number of the first selected item 
       // for re-setting the selection path after the transformation
       int leadRow = domTree.getRowForPath(domTree.getLeadSelectionPath());
       
       // if we've made it this far, send to the ModifyDOM class
       ModifyDOM.removeDIV(thisNode, document);
       
       // Notifying the treemodel that the underlying structure
       // has changed.  This is kind of a hack in that we always
       // tell the model that the treeScrollPane has changed at the root level.
       // This seems necessary for. proper repainting of the JTree...
       model.nodeStructureChanged(thisNode.getRoot());

       // After changing from the root above, the whole JTree will
       // be collapsed, so this is necessary
       domTree.expandAll();

       // Re-use the index value from earlier to. re-set selection
       domTree.setSelectionRow(leadRow);
       
       textChanged = true;
   }
   
   
    /**
     * Handles editing of structural division metadata from the GUI.
     */
    private void editDiv() {
       
       // make sure that the current selection is a DIV
       AdapterNode thisNode = (AdapterNode) domTree.getLeadSelectionPath().getLastPathComponent();
       if ( !thisNode.domNode.getNodeName().equals("mets:div")) {
           JOptionPane.showMessageDialog(frame, "You must have A DIV selected to edit it");
           return;
       }
       
       int leadRow = domTree.getRowForPath(domTree.getLeadSelectionPath());
       
       Division div = thisNode.getDivision();
      
        
       // Create the division editor dialog
       String divLabel = JOptionPane.showInputDialog(frame, "Division Label:", div.getTitle());
      
       if (divLabel == null) {
    	   return;
       }
        
       Division newDiv = new Division();
       newDiv.setTitle(divLabel);
       ModifyDOM.replaceDivMetadata(div, newDiv, thisNode, document);
        
       model.nodeStructureChanged(thisNode.getRoot());
       domTree.expandAll();
       domTree.setSelectionRow(leadRow);
       textChanged = true;
        

   }
   
    /**
     * Encapsulates the logic of determining a new page number from the previous page.  
     * If the previous page is "unum", the page will also be "unum".  If the previous
     * page ends with a letter (e.g. "001a"), the page will increment the letter (e.g.
     * "001b").  If the previous page starts with an "r" (e.g. "r001"), the page will
     * increment the last digit.  Finally, the page is assumed to be numeric, and is
     * incremented by one.  If there is any trouble parsing the integer of the previous
     * page, the page will become "unum".
     * 
     * @param prevPg The previous page number.
     * @return Returns the derived page value as a String.
     */
    public String derivePageFromPrevious(String prevPg) {
       
       // if previous page is "unum", this one should be too
       if (prevPg.equals("unum")) {
           return "unum";
       }

       // if previous page ends with a letter, increment last letter
       else if (Pattern.matches("\\d\\d\\d([a-z])", prevPg)) {
           
           Matcher matcher = Pattern.compile("[a-z]$").matcher(prevPg);
           char c = prevPg.charAt(3);
           c++;
           return matcher.replaceFirst(String.valueOf(c));

       }

       // if previous page starts with an 'r', increment last number
       else if (Pattern.matches("r\\d\\d\\d", prevPg)) {
           
           String c = prevPg.substring(1,4);

           try {
    
               int i = Integer.parseInt(c);
                // add one to the int
                i++;
                // convert int back into zero-padded string
                return "r" + String.format("%03d", i); 
            }
            catch (NumberFormatException nfe) {
                return "unum";
            }

       }       

       // otherwise, assume its a numeric page and try to increment it
       else {
            // convert the zero-padded String into an int,
           // but catch number format exceptions 
           
            try {
                int i = Integer.parseInt(prevPg);
                
                // add one to the int
                i++;

                // convert int back into zero-padded string
                return String.format("%04d", i); 
            }
            catch (NumberFormatException nfe) {
                return "unum";
            }

       }
   }
   
   private String getXmlText () {
       
       try {
           
            String rtn = " ";
            BufferedReader inputStream = new BufferedReader(new FileReader(text.getXmlFile()));
            String l;
            while ((l = inputStream.readLine()) != null) {
                rtn += l;
            }
            inputStream.close();
            return rtn;
       }
       catch (FileNotFoundException e) {
           
           return "XML File not found!  Something's pretty wrong!";
           
       }
       catch (IOException ioe) {
           
           return "I/O Exception: " + ioe.getMessage();
           
       }
       
       
   }
   
 
    
    private void promptForSave () {
        
           if ( textChanged ) {
               
               int result = JOptionPane.showConfirmDialog(frame, "Document has changed since it was last saved.  Save now?");

               // stop here if they've hit anything other than the OK button
               if (result == JOptionPane.YES_OPTION) 
               {
                   saveText();
               }
               else if ( result == JOptionPane.CANCEL_OPTION ) 
               {
                   return;
               }
           }
        
        
    }
    
   private class MyActionListener implements ActionListener, DocumentListener {
        
        // adapter to receive menu actions and route accordingly
        
        public void changedUpdate(DocumentEvent de) {
           System.out.println("changedUpdate event registered.");
        }
        public void insertUpdate(DocumentEvent de)  {
           //setNativePagination(pagination.getText());
        }
        public void removeUpdate(DocumentEvent de) {
           //setNativePagination(pagination.getText());
        }
        
        public void actionPerformed(ActionEvent e) {
            
            String command = e.getActionCommand();
            if (command.equals("Quit")) 
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

            else if (command.equals("Open Text")) 
            {
                promptForSave();
                pickTextFileSystem();
            }
            else if (command.equals("Save Text"))
                saveText();
            
            else if (e.getSource() == addDivButton)
                addDiv();
            
            else if ( e.getSource() == deleteDivButton ){
                removeDiv();
            }
            
            else if ( e.getSource() == editDivButton ){
                editDiv();
            }
            else if ( e.getSource() == autoNumberPagesButton ) {
               int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to auto-number all the pages?");
               // stop here if they've hit anyting other than the OK button
               if (result == JOptionPane.YES_OPTION) 
               {
                    autoNumberNativePagination();
               }
               else if ( result == JOptionPane.CANCEL_OPTION ) 
               {
                   return;
               }
            }
            else if ( e.getSource() == pageNumberOverwrite ){
                domTree.requestFocus();
            }
        }
        
    }
      
      
   private class MyWindowListener extends WindowAdapter {
       
        @Override
       public void windowClosing(WindowEvent e )
       {

           promptForSave();
           
           // now close
           System.exit(0);
       }
       
   }
   
   
   private class MyKeyListener extends KeyAdapter {
        
        @Override
        public void keyPressed (KeyEvent ke) {
            
            if ( ke.getKeyCode() == KeyEvent.VK_INSERT ) {
                if (ke.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK ) {
                    editDiv();
                }
                else {
                addDiv();
                }
            }
            else if ( ke.getKeyCode() == KeyEvent.VK_DELETE ) {
                removeDiv();
            }           
        }
    }

     private class MyTreeSelectionListener implements TreeSelectionListener {

         public void valueChanged(TreeSelectionEvent e) { 
             
            try {
                TreePath p = e.getNewLeadSelectionPath();
                if (p != null) {
                    AdapterNode adpNode = (AdapterNode) p.getLastPathComponent();
                    Element element = (Element) adpNode.domNode;
                    if (element.getAttribute("TYPE").equals("page")) {
                        pageID = adpNode.getPageFileId();
                        loadPageItems(pageID); 
                        if (element.getAttribute("LABEL").equals("") || pageNumberOverwrite.isSelected()) {
                            setNativePagination();     
                        }
                    }
                 }
            } catch (Exception ex) {
                System.out.print(ex.toString());
            }
            
        }
    }
    
}
