package edu.pitt.library.drl.bodybuilder;

/*
 * DomToTreeModelAdaptor.java
 *
 * Created on August 11, 2005, 9:50 AM
 *
 */

import org.w3c.dom.Document;
import javax.swing.tree.*;
import java.util.regex.*;

public class DomTreeModel extends javax.swing.tree.DefaultTreeModel {

      /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Document document;
    
      public DomTreeModel(Document document) {
          
          /*
           * Constructor takes TreeNode of the document root to create 
           * the tree model.
           * In this case, we want the root to be an AdaperNode
           */
          super((TreeNode) new AdapterNode(document));
          this.document = document;
      
      }

     /*
      *
      */
    @Override
      public void valueForPathChanged(TreePath path, Object newValue)  {
          
	MutableTreeNode aNode = (MutableTreeNode)path.getLastPathComponent();
        String natPag = (String) newValue;
        
        // lower-case the string, and reject it unless is is 4 alpha-numeric chars
        natPag = natPag.toLowerCase();
        if (!Pattern.matches("\\w\\w\\w\\w", natPag))
            return;
        AdapterNode node = (AdapterNode) aNode;
        ModifyDOM.setNativePagination(natPag, node, document);
        //aNode.setUserObject(newValue);
        //nodeChanged(aNode);
    }

}