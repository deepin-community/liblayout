/**
 * ===========================================
 * LibLayout : a free Java layouting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/liblayout/
 *
 * (C) Copyright 2006-2007, by Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * $Id: ConflictingCell.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.cells;

import java.util.ArrayList;

/**
 * A storage item for conflicting cells. Conflicts can only happen between
 * two placeholder cells. The first cell is represented by the conflictingCell
 * instance itself, all additional cells are stored in a list of placeholder
 * cells.
 *
 * This information can be used to resolve the conflict by inserting extra
 * rows. For now, we simply log the whole stuff and blame the user if things
 * go wrong.
 *
 * @author Thomas Morgner
 */
public class ConflictingCell extends PlaceHolderCell
{
  private ArrayList additionalCells;

  public ConflictingCell(final DataCell sourceCell,
                         final int rowSpan,
                         final int colSpan)
  {
    super(sourceCell, rowSpan, colSpan);
    additionalCells = new ArrayList();
  }

  public void addConflictingCell (final PlaceHolderCell cell)
  {
    additionalCells.add(cell);
  }

  public int getConflictingCellCount()
  {
    return additionalCells.size();
  }

  public PlaceHolderCell getConflictingCell (final int pos)
  {
    return (PlaceHolderCell) additionalCells.get(pos);
  }


  public String toString()
  {
    return "ConflictingCell{" +
            "rowSpan=" + getRowSpan() +
            ", colSpan=" + getColSpan() +
            ", sourceCell=" + getSourceCell() +
            ", additionalCells=" + additionalCells +
            '}';
  }
}
