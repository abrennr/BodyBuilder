package edu.pitt.library.drl.bodybuilder;

import java.awt.Font;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.w3c.dom.Element;

/**
 *
 * @author abrenner
 */
public class DomTree extends JTree {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final Font treeFont = new Font("Monospaced", Font.PLAIN, 13);
    
    /** Creates a new instance of DomTree */
    public DomTree(TreeModel model) {
        
        super(model);
        setFont(treeFont);
        setEditable(true);
        
    }
    
    public void expandAll() {
        
        int row = 0;
        while (row < getRowCount()) 
        {
            expandRow(row++);
        }
        
    } 
    
    
     public void selectFirstPage () {
         
        try {
            int r = 0;
            AdapterNode adpNode;
            Element element; 
            do
            {
                r++;
                adpNode = (AdapterNode) getPathForRow(r).getLastPathComponent();
                element = (Element) adpNode.domNode;
            }
            while (!element.getAttribute("TYPE").equals("page"));
            setSelectionRow(r);
            
        } 
        catch (Exception ex) {
            System.out.print("error selecting last path component: " + ex.toString());
        }
        
    }
    
    public void selectPreviousPage () {
        if (getLeadSelectionRow() > 0)
            setSelectionRow(getLeadSelectionRow() - 1);
    }
    
    public void selectNextPage () {
       // if (domTree.getLeadSelectionRow() < (domTree.getRowCount() -1))
            setSelectionRow(getLeadSelectionRow() + 1);
    }   
}
