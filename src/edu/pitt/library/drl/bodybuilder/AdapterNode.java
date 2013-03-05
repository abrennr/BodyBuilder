
package edu.pitt.library.drl.bodybuilder;

/*
 * AdaptorNode.java
 *
 * Created on August 10, 2005, 4:16 PM
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class AdapterNode extends javax.swing.tree.DefaultMutableTreeNode {
     
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	org.w3c.dom.Node domNode;

    // used as a lookup list by the boolean treeElement method below
    static String[] treeElementNames = {
        "mets", "mets:fileGrp", "mets:file", "mets:FLocat", "mets:structMap", "mets:div",     
    };

    static final int ELEMENT_TYPE = Node.ELEMENT_NODE;
    static final int ATTR_TYPE = Node.ATTRIBUTE_NODE;
    static final int TEXT_TYPE = Node.TEXT_NODE;
    static final int CDATA_TYPE = Node.CDATA_SECTION_NODE;
    static final int ENTITYREF_TYPE = Node.ENTITY_REFERENCE_NODE;
    static final int ENTITY_TYPE = Node.ENTITY_NODE;
    static final int PROCINSTR_TYPE = Node.PROCESSING_INSTRUCTION_NODE;
    static final int COMMENT_TYPE = Node.COMMENT_NODE;
    static final int DOCUMENT_TYPE = Node.DOCUMENT_NODE;
    static final int DOCTYPE_TYPE = Node.DOCUMENT_TYPE_NODE;
    static final int DOCFRAG_TYPE = Node.DOCUMENT_FRAGMENT_NODE;
    static final int NOTATION_TYPE = Node.NOTATION_NODE;

     
    // Construct an Adapter node from a DOM node
    public AdapterNode(org.w3c.dom.Node node) {
        domNode = node;
    }

    // Return a string that identifies this node in the tree
    @Override
    public String toString() {
        String s = ""; 
        String nodeName = domNode.getNodeName();

        if ( nodeName.equals("mets:div") ) {
            Element div = (Element) domNode;
            s = div.getAttribute("TYPE") + ": " + div.getAttribute("LABEL");
        }
        else if ( nodeName.equals("mets:file")) 
        {
            s = "file";
        }
        else if ( nodeName.equals("mets:fptr"))
        {
            s = "fptr";
        }
        else if ( nodeName.equals("mets:fileSec"))
        {
            s = "fileSec";
        }
        else if ( nodeName.equals("mets:fileGrp"))
        {
            s = "fileGrp";
        }
        else if ( nodeName.equals("mets:FLocat"))
        {
            s = "FLocat";
        }
        else if ( nodeName.equals("mets:structMap"))
        {
            s = "structMap";
        }
        else if ( nodeName.equals("mets"))
        {
            s = "mets";
        }

//        String t = content();
//        int x = t.indexOf("\n");
//
//        if (x >= 0) 
//        {
//            t = t.substring(0, x);
//        }
//
//        s += (" " + t);

        return s;

    }

    public String content() {
        
        String s = "";
        org.w3c.dom.NodeList nodeList = domNode.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            
            org.w3c.dom.Node node = nodeList.item(i);
            int type = node.getNodeType();
            AdapterNode adpNode = new AdapterNode(node); //inefficient, but works

            if (type == ELEMENT_TYPE) {
                
                //Skip subelements that are displayed in the tree.   
                if (treeElement(node.getNodeName())) {
                    continue;
                }
                s += (node.getNodeName());
                //s += adpNode.content();
                //s += ("</" + node.getNodeName() + ">");
            } else if (type == TEXT_TYPE) {
                
                s += node.getNodeValue();
                
            } else if (type == ENTITYREF_TYPE) {
                
                // The content is in the TEXT node under it
                s += adpNode.content();
                
            } else if (type == CDATA_TYPE) {
                
                // The "value" has the text, same as a text node.
                //   while EntityRef has it in a text node underneath.
                //   (because EntityRef can contain multiple sub-elements)
                // Convert angle brackets and ampersands for display
                StringBuffer sb = new StringBuffer(node.getNodeValue());

                for (int j = 0; j < sb.length(); j++) {
                    if (sb.charAt(j) == '<') {
                        sb.setCharAt(j, '&');
                        sb.insert(j + 1, "lt;");
                        j += 3;
                    } else if (sb.charAt(j) == '&') {
                        sb.setCharAt(j, '&');
                        sb.insert(j + 1, "amp;");
                        j += 4;
                    }
                }

                s += ("<pre>" + sb + "\n</pre>");
            }

            // Ignoring these:
            //   ATTR_TYPE      -- not in the DOM tree
            //   ENTITY_TYPE    -- does not appear in the DOM
            //   PROCINSTR_TYPE -- not "data"
            //   COMMENT_TYPE   -- not "data"
            //   DOCUMENT_TYPE  -- Root node only. No data to display.
            //   DOCTYPE_TYPE   -- Appears under the root only
            //   DOCFRAG_TYPE   -- equiv. to "document" for fragments
            //   NOTATION_TYPE  -- nothing but binary data in here
        }

        return s;
    }

    /*
     * Return children, index, and count values
     */
    public int getIndex(AdapterNode child) {
        //System.err.println("Looking for index of " + child);
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            AdapterNode n = this.getChildAt(i);

            if (child.domNode == n.domNode) {
                return i;
            }
        }

        return -1; // Should never get here.
    }

    @Override
    public AdapterNode getChildAt(int searchIndex) {
        //Note: JTree index is zero-based. 
        org.w3c.dom.Node node = domNode.getChildNodes().item(searchIndex);

      // Return Nth displayable node
      int elementNodeIndex = 0;
      for (int i=0; i<domNode.getChildNodes().getLength(); i++) 
      {
        node = domNode.getChildNodes().item(i);
        if (node.getNodeType() == ELEMENT_TYPE 
        && treeElement( node.getNodeName() )
        && elementNodeIndex++ == searchIndex) 
        {
           break; 
        }
      }

        return new AdapterNode(node); 
      }



    @Override
    public int getChildCount() {

        int count = 0;

        for (int i = 0; i < domNode.getChildNodes().getLength(); i++) {
            org.w3c.dom.Node node = domNode.getChildNodes().item(i);
            if (node.getNodeType() == ELEMENT_TYPE && treeElement(node.getNodeName())) 
            {
                ++count;
            }
        }
        return count;
    }
   
    /* 
     * treeElement is used by DomToTreeModelAdapter to determine
     * if a node (in the org.w3c.dom.Document) should show up 
     * as a node in the JTree.  To test, the method just checks the
     * list of treeElementNames defined in this class.
    */ 
    boolean treeElement(String elementName) 
    {
        for (int i = 0; i < treeElementNames.length; i++) 
        {
            if (elementName.equals(treeElementNames[i])) 
            {
                return true;
                }
        }
        return false;
    }

    public String getDIVTitle() {
        
        // when passed a div node, get the value of the LABEL attribute
        Element theDiv = (Element) this.domNode;
        return theDiv.getAttribute("LABEL");
   }

    public  String getPageFileId() {
        Element fptr = (Element) this.domNode.getFirstChild();
        String fid = fptr.getAttribute("FILEID");  
        Matcher matcher = Pattern.compile("^fid").matcher(fid);
        return matcher.replaceFirst("");
    }
    
    public  Division getDivision() {
        Division div = new Division();
        Element divElement = (Element) this.domNode;
        div.setTitle(divElement.getAttribute("LABEL"));
        return div;
    }
    
    
}
