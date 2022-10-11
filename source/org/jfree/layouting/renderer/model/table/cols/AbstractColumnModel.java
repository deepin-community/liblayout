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
 * $Id: AbstractColumnModel.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.cols;

import java.util.ArrayList;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Creation-Date: 21.07.2006, 19:21:43
 *
 * @author Thomas Morgner
 */
public abstract class AbstractColumnModel implements TableColumnModel
{
  private boolean validated;
  private boolean incrementalModeSupported;

  private ArrayList columnGroups;
  private TableColumn[] columns;

  public AbstractColumnModel()
  {
    this.incrementalModeSupported = true;
    this.columns = null;
    this.columnGroups = new ArrayList();
  }

  public void addColumnGroup(final TableColumnGroup column)
  {
    columnGroups.add(column);
    column.freeze();
    validated = false;
  }

  public void addAutoColumn()
  {
    final TableColumnGroup autoGroup = new TableColumnGroup();
    final TableColumn column = new TableColumn
            (Border.createEmptyBorder(), RenderLength.AUTO, true);
    autoGroup.addColumn(column);
    autoGroup.freeze();
    columnGroups.add(autoGroup);
    validated = false;
  }

  public boolean isIncrementalModeSupported()
  {
    return incrementalModeSupported;
  }

  /**
   * The column count may change over time, when new columnGroups get added.
   *
   * @return
   */
  public int getColumnGroupCount()
  {
    return columnGroups.size();
  }

  public int getColumnCount()
  {
    buildColumns();
    return columns.length;
  }

  private void buildColumns()
  {
    if (validated)
    {
      return;
    }

    final ArrayList cols = new ArrayList();
    for (int i = 0; i < columnGroups.size(); i++)
    {
      final TableColumnGroup node = (TableColumnGroup) columnGroups.get(i);

      final int count = node.getColumnCount();
      for (int x = 0; x < count; x++)
      {
        final TableColumn column = node.getColumn(x);
        cols.add(column);
      }
    }

    columns = (TableColumn[]) cols.toArray(new TableColumn[cols.size()]);
    validated = true;
  }

  public TableColumnGroup getColumnGroup(final int i)
  {
    return (TableColumnGroup) columnGroups.get(i);
  }

  public TableColumn getColumn(final int i)
  {
    buildColumns();
    return columns[i];
  }

  public TableColumn[] getColumns ()
  {
    buildColumns();
    return columns;
  }

  public boolean isValidated()
  {
    return validated;
  }

  public long getBorderSpacing()
  {
    return 0;
  }

  public TableColumnGroup getGroupForIndex(final int idx)
  {
    int offset = 0;
    for (int j = 0; j < columnGroups.size(); j++)
    {
      final TableColumnGroup group = (TableColumnGroup) columnGroups.get(j);
      if (offset + group.getColumnCount() <= idx)
      {
        offset += group.getColumnCount();
      }
      else
      {
        return group;
      }
    }
    throw new IndexOutOfBoundsException("No such group");
  }

  public Object clone () throws CloneNotSupportedException
  {
    final AbstractColumnModel cm = (AbstractColumnModel) super.clone();
    cm.columns = null;
    cm.validated = false;
    cm.columnGroups = (ArrayList) columnGroups.clone();
    return cm;
  }
}
