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
 * $Id: TableRowInfoStructure.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table;

import java.util.ArrayList;

import org.jfree.layouting.renderer.model.table.cells.RemovedCell;
import org.jfree.layouting.renderer.model.table.cells.TableCell;

/**
 * Creation-Date: 10.09.2006, 20:01:18
 *
 * @author Thomas Morgner
 */
public class TableRowInfoStructure implements Cloneable
{
  private ArrayList cells;
  private boolean validationDone;
  private int rowNumber;

  public TableRowInfoStructure()
  {
    cells = new ArrayList();
  }

  public void addCell(final TableCell cell)
  {
    if (cell == null)
    {
      throw new NullPointerException();
    }
    cells.add(cell);
  }

  public int getCellCount()
  {
    return cells.size();
  }

  public TableCell getCellAt(final int col)
  {
    return (TableCell) cells.get(col);
  }

  public boolean isValidationDone()
  {
    return validationDone;
  }

  public void setValidationDone(final boolean validationDone)
  {
    this.validationDone = validationDone;
  }

  public int getRowNumber()
  {
    return rowNumber;
  }

  public void setRowNumber(final int rowNumber)
  {
    this.rowNumber = rowNumber;
  }

  public void replaceCell(final int pos, final RemovedCell cell)
  {
    if (cell == null)
    {
      throw new NullPointerException();
    }
    this.cells.set(pos, cell);
  }

  public Object clone () throws CloneNotSupportedException
  {
    final TableRowInfoStructure o = (TableRowInfoStructure) super.clone();
    o.cells = (ArrayList) cells.clone();
    return o;
  }
}
