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
 * $Id: TableColumnGroup.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.cols;

import java.util.ArrayList;

import org.jfree.layouting.renderer.border.Border;

/**
 * A table column group contains one or more table columns. The table column
 * group is a normalized element.
 * <p/>
 * A column group may defined a shared background for all columns. The column
 * group may define a minimum width. If the contained cells do not use all of
 * that granted width, they get some extra padding.
 * <p/>
 * As Mozilla does not take the width of a colgroup into account, we will
 * neither.
 *
 * @author Thomas Morgner
 */
public class TableColumnGroup
{
  private ArrayList tableColumns;
  private Border border;
  private boolean freeze;

  public TableColumnGroup(final Border border)
  {
    this.border = border;
    this.tableColumns = new ArrayList();
  }

  public TableColumnGroup()
  {
    this(Border.createEmptyBorder());
  }

  public void freeze()
  {
    freeze = true;
  }

  public void addColumn(final TableColumn column)
  {
    if (freeze)
    {
      throw new IllegalStateException();
    }
    this.tableColumns.add(column);
  }

  public Border getBorder()
  {
    return border;
  }

  public int getColumnCount()
  {
    return tableColumns.size();
  }

  public TableColumn getColumn(final int pos)
  {
    return (TableColumn) tableColumns.get(pos);
  }
}
