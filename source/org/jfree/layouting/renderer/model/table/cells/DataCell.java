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
 * $Id: DataCell.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.cells;

/**
 * A data-cell holds a reference to a TableCellRenderBox. It contains data
 * and is usually found in the upper left corner of an cell.
 *
 *
 * @author Thomas Morgner
 */
public class DataCell extends TableCell
{
  // The instance-id of the cell ..
  private Object cellRenderBox;

  public DataCell(final int rowSpan,
                  final int colSpan,
                  final Object cellRenderBox)
  {
    super(rowSpan, colSpan);

    if (cellRenderBox == null)
    {
      throw new NullPointerException();
    }

    this.cellRenderBox = cellRenderBox;
  }

  public Object getCellRenderBox()
  {
    return cellRenderBox;
  }


  public String toString()
  {
    return "DataCell{" +
            "rowSpan=" + getRowSpan() +
            ", colSpan=" + getColSpan() +
            ", cellRenderBox=" + cellRenderBox +
            '}';
  }
}
