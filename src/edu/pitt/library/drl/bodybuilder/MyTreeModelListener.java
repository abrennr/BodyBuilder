package edu.pitt.library.drl.bodybuilder;
/*
 * MyTreeModelListener.java
 *
 * Created on October 31, 2005, 12:23 PM
 */

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 *
 * @author abrenner
 */
public class MyTreeModelListener implements TreeModelListener {
    
    /** Creates a new instance of MyTreeModelListener */
    public MyTreeModelListener() {
        
    }
    
  public void treeNodesChanged(TreeModelEvent evt) {
      
    //System.out.println("Tree Nodes Changed Event");
  
  }
  
  public void treeStructureChanged(TreeModelEvent evt) {
      
    //System.out.println("Tree Structure Changed Event");
  
  }
  
  public void treeNodesInserted(TreeModelEvent evt) {
      
    //System.out.println("Tree Nodes Inserted Event");
      
  } 
  
  public void treeNodesRemoved(TreeModelEvent evt) {
      
    //System.out.println("Tree Nodes Removed Event");
      
  }

    
}
