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
 * $Id: ComputeTableICMMetricsStep.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowInfoStructure;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.cells.DataCell;
import org.jfree.layouting.renderer.model.table.cells.TableCell;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.model.table.rows.TableRow;
import org.jfree.layouting.renderer.model.table.rows.TableRowModel;
import org.pentaho.reporting.libraries.base.util.FastStack;

/**
 * Computes the table-column base-size ratios. These ratios cannot be computed
 * unless the preferred size of all cell-contents are known.
 * <p/>
 * As soon as the cell-sizes have been computed, the column model is filled and
 * cell-ratios get assigned. The prelimentary cell-widths are kept in the column
 * model object itself.
 * <p/>
 * For now, that computation creates a valid 'separate' border model. The
 * compound-border-model smells like voodo right now, I have no clue how to do
 * that.
 * <p/>
 * This step cannot be computed, if the table is not valid (is either closed or
 * contains at least one row in the body-section).
 *
 * @author Thomas Morgner
 */
public class ComputeTableICMMetricsStep extends IterateStructuralProcessStep
{
  public static class TableInfoStructure
  {
    private TableRenderBox table;
    private TableColumnModel columnModel;
    private TableRowModel rowModel;
    private int rowNumber;

    public TableInfoStructure(final TableRenderBox table)
    {
      this.table = table;
      this.columnModel = table.getColumnModel();
    }

    public TableRenderBox getTable()
    {
      return table;
    }

    public TableColumnModel getColumnModel()
    {
      return columnModel;
    }

    public TableRowModel getRowModel()
    {
      return rowModel;
    }

    public void setRowModel(final TableRowModel rowModel)
    {
      this.rowModel = rowModel;
      this.rowNumber = 0;
    }

    public int getRowNumber()
    {
      return rowNumber;
    }

    public void increaseRowNumber()
    {
      this.rowNumber += 1;
    }
  }

  private FastStack tableStack;
  private TableInfoStructure currentTable;

  public ComputeTableICMMetricsStep()
  {
    tableStack = new FastStack();
  }

  public void compute(final LogicalPageBox root)
  {
    startProcessing(root);
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    if (box instanceof TableRenderBox)
    {
      final TableRenderBox table = (TableRenderBox) box;
      currentTable = new TableInfoStructure(table);
      tableStack.push(currentTable);
    }
    else if (box instanceof TableSectionRenderBox)
    {
      final TableSectionRenderBox sectionBox = (TableSectionRenderBox) box;
      currentTable.setRowModel(sectionBox.getRowModel());
    }

    // caching would be cool?
    return true;
  }

  protected void finishBlockBox(final BlockRenderBox box)
  {
    if (box instanceof TableRowRenderBox)
    {
      finishTableRow((TableRowRenderBox) box);
    }
    else if (box instanceof TableRenderBox)
    {
      finishTable((TableRenderBox) box);
    }
    else if (box instanceof TableSectionRenderBox)
    {
      currentTable.setRowModel(null);
    }

  }

  private void finishTable(final TableRenderBox box)
  {
    // grab the column model - recompute the columns.
    // this does nothing, if the table has not been changed.
    // This causes terror and pain, if the columns have been fixed already.
    box.getColumnModel().validateSizes(box);

    tableStack.pop();
    if (tableStack.isEmpty() == false)
    {
      currentTable = (TableInfoStructure) tableStack.peek();
    }
    else
    {
      currentTable = null;
    }

  }

  private void finishTableRow(final TableRowRenderBox box)
  {
    final TableRenderBox table = currentTable.getTable();
    final TableColumnModel columnModel = table.getColumnModel();

    final TableSectionRenderBox section = (TableSectionRenderBox) box.getParent();
    final TableRowModel rowModel = section.getRowModel();

    final TableRowInfoStructure rowInfoStructure = box.getRowInfoStructure();
    final int rowNumber = currentTable.getRowNumber();
    rowInfoStructure.setRowNumber(rowNumber);
    final TableRow row = rowModel.getRow(rowNumber);

    final BoxDefinition boxDefinition = box.getBoxDefinition();
    final RenderLength preferredHeight = boxDefinition.getPreferredHeight();
    final ComputedLayoutProperties rowNlp = box.getComputedLayoutProperties();
    final RenderLength rowComputedWidth = rowNlp.getComputedWidth();
    row.clearSizes();
    row.setPreferredSize(preferredHeight.resolve(rowComputedWidth.resolve(0)));

    final int cellCount = rowInfoStructure.getCellCount();
    for (int i = 0; i < cellCount; i++)
    {
      final TableCell cellAt = rowInfoStructure.getCellAt(i);
      if (cellAt instanceof DataCell == false)
      {
        continue;
      }

      // We dont handle spanned cells here; thats done indirectly by the
      // column model itself.
      final DataCell dataCell = (DataCell) cellAt;

      final RenderNode cell = findCellInRow(box, dataCell.getCellRenderBox());
      if (cell == null)
      {
        throw new IllegalStateException
            ("No such cell: " + dataCell.getCellRenderBox());
      }
      final TableColumn column = columnModel.getColumn(i);
      final int colSpan = dataCell.getColSpan();

      column.updateMinimumChunkSize(colSpan, cell.getMinimumChunkWidth());
      column.updateMaxBoxSize(colSpan, cell.getMaximumBoxWidth());

      final RenderLength computedWidth =
          cell.getComputedLayoutProperties().getComputedWidth();
      if (computedWidth == RenderLength.AUTO == false)
      {
        // if we have a computed width, set it. If the user explicitly specified
        // a width, then that one is returned as computed width.
        column.updatePreferredSize(colSpan, computedWidth.getValue());
      }

      final RenderLength definedHeight =
          box.getBoxDefinition().getPreferredHeight();
      row.updateDefinedSize(dataCell.getRowSpan(),
          definedHeight.resolve(computedWidth.resolve(0)));
    }

    currentTable.increaseRowNumber();
  }

  private RenderNode findCellInRow(final TableRowRenderBox box, final Object instanceId)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      if (node.getInstanceId() == instanceId)
      {
        return node;
      }
      node = node.getNext();
    }
    return null;
  }
}
