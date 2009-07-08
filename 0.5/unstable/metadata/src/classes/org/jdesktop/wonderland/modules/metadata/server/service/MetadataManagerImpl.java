/**
 * Project Wonderland
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * Sun designates this particular file as subject to the "Classpath"
 * exception as provided by Sun in the License file that accompanied
 * this code.
 */
package org.jdesktop.wonderland.modules.metadata.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.modules.metadata.common.MetadataSPI;
import org.jdesktop.wonderland.modules.metadata.common.MetadataSearchFilters;



/**
 * Implementation of the metadata manager interface
 * @author mabonner
 */
public class MetadataManagerImpl implements MetadataManager {
    private MetadataService service;

    public MetadataManagerImpl(MetadataService service) throws Exception {
        this.service = service;
        // service.test();
    }

    public void test() {
        service.test();
    }

      /**
   * adds a new cell to the top level (e.g., has no parent besides the world)
   * @param cid id of cell to create
   */
  public void addCell(CellID cid){
    this.service.addCell(cid);
  }

  /**
   * adds a new cell beneath the passed in cell
   * @param cid id of cell to create
   * @param parent id of the parent cell to create under
   */
  public void addCell(CellID cid, CellID parent){
    this.service.addCell(cid, parent);
  }


  /**
   * adds the passed metadata object to the cell with cellID cid.
   * logs errors if the cell does not exist or the
   * metadata type has not been registered.
   * @param cid id of cell to add metadata to
   * @param metadata metadata object to add
   */
  public void addMetadata(CellID cid, MetadataSPI metadata){
    this.service.addMetadata(cid, metadata);
  }


  /**
   * Remove cell and all metadata. This should be called when a cell is deleted.
   *
   * @param cid cellID of the cell to delete
   */
  public void eraseCell(CellID cid){
    this.service.eraseCell(cid);
  }

  /**
   * Delete the specified metadata object
   * @param mid metadata id designating the metadata to remove
   */
  public void eraseMetadata(int mid){
    this.service.eraseMetadata(mid);
  }

  /**
   * Remove all metadata from a cell
   *
   * @param cid id of cell to remove metadata from
   */
  public void clearCellMetadata(CellID cid){
    this.service.clearCellMetadata(cid);
  }

  /**
   * Take any action necessary to register this metadatatype as an option.
   * Name collision on class name or attribute name is up to the implementation.
   *
   * This implementation uses the full package name to describe a Metadata obj
   * and its attributes, avoiding collisions.
   *
   * TODO will scan class loader take care of duplication checking anyway?
   * @param m example of the type to register
   */
  public void registerMetadataType(MetadataSPI m) throws Exception{
    this.service.registerMetadataType(m);
  }


  /**
   * Search all cells in the world, finding cells with metadata satisfying the
   * passed in MetadataSearchFilters
   *
   * @param filters search criteria
   * @param cid id of parent cell to scope the search
   * @return map, mapping cell id's (as Integers) whose metadata that matched the
   * search, to a set of metadata id's that matched the search for that cell.
   */
  public HashMap<Integer, Set<Integer> > searchMetadata(MetadataSearchFilters filters){
    return this.service.searchMetadata(filters);
  }

  /**
   * Search all cells beneath cid, finding cells with metadata satisfying the
   * passed in MetadataSearchFilters
   *
   * @param filters search criteria
   * @param cid id of parent cell to scope the search
   * @return map, mapping cell id's (as Integers) whose metadata that matched the
   * search, to a set of metadata id's that matched the search for that cell.
   */
  public HashMap<Integer, Set<Integer> > searchMetadata(MetadataSearchFilters filters, CellID cid){
    return this.service.searchMetadata(filters, cid);
  }

  /**
   * Convenience method (NOT in the Backend Interface)
   * Set a cell's metadata... clears away any prexisting metadata
   * @param cid ID of the cell to reset
   * @param metadata list of metadata to add to cell
   */
  public void setCellMetadata(CellID cid, ArrayList<MetadataSPI> metadata){
    this.service.setCellMetadata(cid, metadata);
  }
}
