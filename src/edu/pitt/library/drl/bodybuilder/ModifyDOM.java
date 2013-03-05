package edu.pitt.library.drl.bodybuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *
 * @author abrenner
 */
public class ModifyDOM {
    
    /** Creates a new instance of ModifyDOM */
    public ModifyDOM() {
    }
    
  
    /*
     * addDivAsParent
     *
    */
    public static void addDivAsParent(Division division, AdapterNode[] nodeList, Document document)
    {

        // get the Title
        String divTitle = division.getTitle();
        
        // make the DIV element.
        Element div = document.createElement("mets:div");

        div.setAttribute("TYPE", "section");
        div.setAttribute("LABEL", divTitle);

        /**
         * Now we can work over the selected nodes (pages) to re-work
         * the DOM tree.
            
        */
        Node parentNode = nodeList[0].domNode.getParentNode();
        for (int i = 0; i < nodeList.length; i++) {

            // get the Node out of the AdapterNode object
            Node theNode = nodeList[i].domNode;


            // we only want to process first-order children.
            if (!theNode.getParentNode().equals(parentNode))
                continue;
            
            // if it's the first node of the selection, replace it with the new div
            if (i == 0) {
                
                theNode.getParentNode().replaceChild(div, theNode);
            
            } 

            div.appendChild(theNode); 
        }
        
    }
    
    public static void removeDIV (AdapterNode divAdapterNode, Document document) {
        
        // Get the DIV as a Node
        Node theDiv = divAdapterNode.domNode;
        
        // get all the children of the DIV.
        NodeList childrenOfDiv = theDiv.getChildNodes();

        // get the next sibling of the DIV, this will be the node that
        // we insert all the removed children in front of.
        Node nextSibling = theDiv.getNextSibling();
        Node parentOfDiv = theDiv.getParentNode();
               
        // for each child node, remove it from it's parent (the DIV) and
        // insert it before nextSibling 
        while ( childrenOfDiv.getLength() > 0){
            
            // I guess the NodeList works like a stack, because each
            // time we do the following operation, i.e. moving a node,
            // the size of the NodeList decreases by one, and the next
            // node in line is always at index zero.  That's why the
            // operation below always works on index zero...
            Node thisNode = childrenOfDiv.item(0);
            parentOfDiv.insertBefore(thisNode, nextSibling);        
            
        }
  
        // finally, remove the div itself
        parentOfDiv.removeChild(theDiv);
    
    }
    
    
    public static void replaceDivMetadata (Division oldDiv, Division newDiv, AdapterNode divNode, Document document) {
        
        Element divElement = (Element) divNode.domNode;
        divElement.setAttribute("LABEL", newDiv.getTitle());     
       
    }
    
  
    
     
    public static void setDivPageRange (String pageRange, AdapterNode node, Document document) {
        Element divElement = (Element) node.domNode;
        Element biblElement = (Element) divElement.getElementsByTagName("BIBL").item(0);
        Node biblscopeElement = biblElement.getElementsByTagName("BIBLSCOPE").item(0);
        biblscopeElement.setTextContent(pageRange);
        
    }
    
    
    public static void setNativePagination (String natPag, AdapterNode node, Document document) {
        Element page_div = (Element) node.domNode;
        page_div.setAttribute("LABEL", natPag);
        
        // if there are any child (text) nodes on the pb, 
        // remove them before writing appending the new native
        // pagination as a text node.
//        if (pb.hasChildNodes())
//        {
//            NodeList childrenOfPb = pb.getChildNodes(); 
//            while ( childrenOfPb.getLength() > 0)
//            {
//                pb.removeChild(childrenOfPb.item(0));
//            }
//        }
//        
         //pb.appendChild(document.createTextNode("foo"));
    }
    
  
    
    
}
