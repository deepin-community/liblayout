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
 * $Id: SeparateRowModel.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.rows;

import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;

/**
 * Creation-Date: 22.07.2006, 15:23:00
 *
 * @author Thomas Morgner
 */
public class SeparateRowModel extends AbstractRowModel
{
  private long preferredSize;
  private boolean validatedSize;
  private long rowSpacing;

  public SeparateRowModel(final TableSectionRenderBox tableSection)
  {
    super(tableSection);
  }

  public long getPreferredSize()
  {
    validateSizes();
    return preferredSize;
  }

  public long getRowSpacing()
  {
    return rowSpacing;
  }

  public void validateSizes()
  {
    if (validatedSize)
    {
      return;
    }

    int maxRowSpan = 0;
    final TableRow[] rows = getRows();
    final int rowCount = rows.length;
    for (int i = 0; i < rowCount; i++)
    {
      final TableRow row = rows[i];
      final int cs = row.getMaximumRowSpan();
      if (cs > maxRowSpan)
      {
        maxRowSpan = cs;
      }
    }

    final TableRenderBox table = getTableSection().getTable();
    rowSpacing = table.getRowSpacing().resolve(0);

    preferredSize = (rowCount - 1) * rowSpacing;

    // first, find out how much space is already used.
    final long[] preferredSizes = new long[rowCount];
    // For each rowspan ...
    for (int rowspan = 1; rowspan <= maxRowSpan; rowspan += 1)
    {
      for (int rowIdx = 0; rowIdx < rowCount; rowIdx++)
      {
        final TableRow row = rows[rowIdx];
        final long preferredSize = row.getPreferredSize(rowspan);

        distribute(preferredSize, preferredSizes, rowIdx, rowspan);
      }
    }

    for (int i = 0; i < rowCount; i++)
    {
      preferredSize += preferredSizes[i];

      final TableRow row = rows[i];
      row.setPreferredSize(preferredSizes[i]);
    }

    validatedSize = true;
  }


  public void validateActualSizes()
  {
    validateSizes();

    int maxRowSpan = 0;
    final TableRow[] rows = getRows();
    final int rowCount = rows.length;
    for (int i = 0; i < rowCount; i++)
    {
      final TableRow row = rows[i];
      final int cs = row.getMaxValidatedRowSpan();
      if (cs > maxRowSpan)
      {
        maxRowSpan = cs;
      }
    }

    // first, find out how much space is already used.
    // This follows the classical model.

    final long[] trailingSizes = new long[rowCount];
    // For each rowspan ...
    for (int rowspan = 1; rowspan <= maxRowSpan; rowspan += 1)
    {
      for (int rowIdx = 0; rowIdx < trailingSizes.length; rowIdx++)
      {
        final TableRow row = rows[rowIdx];
        final long size = row.getValidatedTrailingSize(rowspan);

        distribute(size, trailingSizes, rowIdx, rowspan);
      }
    }

    for (int i = 0; i < trailingSizes.length; i++)
    {
      final TableRow row = rows[i];
      final long validateSize = trailingSizes[i] + row.getValidatedLeadingSize();
      row.setValidateSize(Math.max (row.getPreferredSize(), validateSize));
    }
  }

  private void distribute (final long usedSpace, final long[] allSpaces,
                           final int colIdx, final int colspanX)
  {
    final int maxColspan = Math.min (colIdx + colspanX, allSpaces.length) - colIdx;
    long usedPrev = 0;
    final int maxSize = Math.min(allSpaces.length, colIdx + maxColspan);
    for (int i = colIdx; i < maxSize; i++)
    {
      usedPrev += allSpaces[i];
    }

    if (usedSpace <= usedPrev)
    {
      // no need to expand the cells.
      return;
    }

    final long distSpace = (usedSpace - usedPrev);
    final long delta = distSpace / maxColspan;
    for (int i = 0; i < maxColspan - 1; i++)
    {
      allSpaces[colIdx + i] = delta;
    }
    // any uneven remainder gets added to the last column
    allSpaces[colIdx + maxColspan - 1] = distSpace - ((maxColspan - 1) * delta);
  }

  public void clear()
  {
    final TableRow[] rows = getRows();
    final int rowCount = rows.length;
    for (int i = 0; i < rowCount; i++)
    {
      final TableRow row = rows[i];
      row.clear();
    }
  }
}
