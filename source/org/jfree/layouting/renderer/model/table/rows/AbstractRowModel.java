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
 * $Id: AbstractRowModel.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.rows;

import java.util.ArrayList;

import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;

/**
 * Creation-Date: 21.07.2006, 19:21:43
 *
 * @author Thomas Morgner
 */
public abstract class AbstractRowModel implements TableRowModel
{
  private ArrayList rows;
  private TableSectionRenderBox tableSection;

  public AbstractRowModel(final TableSectionRenderBox tableSection)
  {
    this.tableSection = tableSection;
    this.rows = new ArrayList();
  }

  public void addRow(final TableRow row)
  {
    rows.add(row);
  }

  public int getRowCount()
  {
    return rows.size();
  }

  public TableSectionRenderBox getTableSection()
  {
    return tableSection;
  }

  public TableRow getRow(final int i)
  {
    return (TableRow) rows.get(i);
  }

  public TableRow[] getRows ()
  {
    return (TableRow[]) rows.toArray(new TableRow[rows.size()]);
  }

  public long getRowSpacing()
  {
    return 0;
  }
}
