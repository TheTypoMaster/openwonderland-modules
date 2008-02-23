/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jdesktop.wonderland.worldbuilder.wrapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.jdesktop.wonderland.worldbuilder.Cell;

/**
 *
 * @author jkaplan
 */
@XmlRootElement(name="cell")
public class TreeCellWrapper extends CellWrapper {
    private static final Logger logger =
            Logger.getLogger(TreeCellWrapper.class.getName());
   
    private int maxDepth;
    private boolean topLevel;
    
    public TreeCellWrapper() {
        super ();
    }
    
    public TreeCellWrapper(Cell cell, URI uri, int maxDepth, boolean topLevel) {
        super (cell, uri);
        
        this.maxDepth = maxDepth;
        this.topLevel = topLevel;
    }
    
    @XmlTransient
    public int getMaxDepth() {
        return maxDepth;
    }

    @Override
    public CellRefWrapper getParentRef() {
        if (!topLevel) {
            return null;
        }
        
        return super.getParentRef();
    }
    
    @Override
    protected CellsWrapper getChildWrapper() {
        if (getMaxDepth() == 0) {
            return super.getChildWrapper();
        }
        
        Collection<CellWrapper> childList = new ArrayList(getCell().getChildren().size());
        for (Cell child : getCell().getChildren()) {
            URI childURI = getURI().resolve(child.getCellID());
            childList.add(new TreeCellWrapper(child, childURI, 
                                              getMaxDepth() - 1, false)); 
        }
        
        return new CellsWrapper(childList, true);
    }
    
    
}
