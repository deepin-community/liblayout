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
 * $Id: SpearateColumnModel.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.cols;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.table.TableRenderBox;

/**
 * Creation-Date: 18.07.2006, 16:46:11
 *
 * @author Thomas Morgner
 */
public class SpearateColumnModel extends AbstractColumnModel
{
  private long validationTrack;

  private long preferredSize;
  private long maxBoxSize;
  private long minimumChunkSize;

  private long borderSpacing;

  public SpearateColumnModel()
  {
  }


  public void validateSizes(final TableRenderBox table)
  {
    if (isValidated() && (validationTrack == table.getChangeTracker()))
    {
      return;
    }


    int maxColSpan = 0;
    final TableColumn[] columns = getColumns();
    final int colCount = columns.length;
    for (int i = 0; i < colCount; i++)
    {
      final TableColumn column = columns[i];
      final int cs = column.getMaxColspan();
      if (cs > maxColSpan)
      {
        maxColSpan = cs;
      }
    }

    if (colCount == 0)
    {
      validationTrack = table.getChangeTracker();
      return;
    }

    final ComputedLayoutProperties nlp = table.getComputedLayoutProperties();
    final RenderLength blockContextWidth = nlp.getBlockContextWidth();
    final long bcw = blockContextWidth.resolve(0);

    final RenderLength borderSpacingLength = table.getBorderSpacing();
    borderSpacing = borderSpacingLength.resolve(bcw);

    final long totalBorderSpacing = (colCount - 1) * borderSpacing;
    minimumChunkSize = totalBorderSpacing;
    maxBoxSize = totalBorderSpacing;
    preferredSize = totalBorderSpacing;

    // first, find out how much space is already used.
    final long[] minChunkSizes = new long[colCount];
    final long[] maxBoxSizes = new long[colCount];
    final long[] preferredSizes = new long[colCount];

    // For each colspan distribute the content.
    // The 1-column size also gets the preferred size ...
    for (int colIdx = 0; colIdx < minChunkSizes.length; colIdx++)
    {
      final TableColumn column = columns[colIdx];
      final long minimumChunkSize = column.getMinimumChunkSize(1);
      final long maxBoxSize = column.getMaximumBoxWidth(1);
      final long resolvedColWidth = column.getDefinedWidth().resolve(bcw);
      final long preferredSize = Math.max
          (resolvedColWidth, column.getPreferredWidth(1));

      minChunkSizes[colIdx] = minimumChunkSize;
      maxBoxSizes[colIdx] = maxBoxSize;
      preferredSizes[colIdx] = preferredSize;
    }

    for (int colspan = 2; colspan <= maxColSpan; colspan += 1)
    {
      for (int colIdx = 0; colIdx < minChunkSizes.length; colIdx++)
      {
        final TableColumn column = columns[colIdx];
        final long minimumChunkSize = column.getMinimumChunkSize(colspan);
        final long maxBoxSize = column.getMaximumBoxWidth(colspan);
        final long preferredSize = column.getPreferredWidth(colspan);

        distribute(minimumChunkSize, minChunkSizes, colIdx, colspan);
        distribute(preferredSize, preferredSizes, colIdx, colspan);
        distribute(maxBoxSize, maxBoxSizes, colIdx, colspan);
      }
    }

    for (int i = 0; i < minChunkSizes.length; i++)
    {
      final TableColumn column = columns[i];

      final long cmin = minChunkSizes[i];
      final long cpref = preferredSizes[i];
      final long cmax = maxBoxSizes[i];
      final long width = Math.max(cmin, cpref);

      minimumChunkSize += cmin;
      preferredSize += width;
      maxBoxSize += cmax;

      if (column.isValidated())
      {
        continue;
      }

      column.setComputedPreferredSize(cpref);
      column.setComputedMinChunkSize(cmin);
      column.setComputedMaximumWidth(cmax);
      column.setEffectiveSize(width);
      column.setValidated(true);
    }

    // Table-AutoWidth means, the tables width is based on the content of the
    // cells. The minimum chunk size and the preferred sizes are the only
    // metrics used in that computation.
    //
    // If the table's width is fixed, then we have to shrink or expand the
    // columns to fit that additional requirement.
    final RenderLength tableCWidth = table.getComputedLayoutProperties().getComputedWidth();
    if (tableCWidth != RenderLength.AUTO)
    {
      final long tableSize = Math.max(tableCWidth.resolve(0), minimumChunkSize);
      // the space we are able to distribute .. (This can be negative, if we
      // have to remove space to get to the defined table size!)
      // The space that is available for the content from the cells. The
      // border-spacing eats some space as well (already in the pref-size-value)
      final long extraSpace = tableSize - preferredSize;
      final long extraSpacePerCol = extraSpace / minChunkSizes.length;
      for (int i = 0; i < minChunkSizes.length; i++)
      {
        final TableColumn column = columns[i];
        final long colSize = column.getEffectiveSize() + extraSpacePerCol;
        column.setEffectiveSize(colSize);
      }

      preferredSize = tableSize;
    }

    validationTrack = table.getChangeTracker();
  }

  public long getPreferredSize()
  {
    return preferredSize;
  }

  public long getMaximumBoxSize()
  {
    return maxBoxSize;
  }

  public long getMinimumChunkSize()
  {
    return minimumChunkSize;
  }

  private void distribute(final long usedSpace,
                          final long[] allSpaces,
                          final int colIdx,
                          final int colspan)
  {
    final int maxColspan = Math.min(colIdx + colspan, allSpaces.length) - colIdx;
    final int maxSize = Math.min(allSpaces.length, colIdx + maxColspan);

    // compute the space occupied by all columns of the range.
    // That has been computed earlier (for 'colspan - 1') and is zero if
    // there is no colspan at all
    long usedPrev = 0;
    for (int i = colIdx; i < maxSize; i++)
    {
      usedPrev += allSpaces[i];
    }

    if (usedSpace <= usedPrev)
    {
      // no need to expand the cells, as the requested size for the whole
      // span will be less than the size occupied by all the columns ..
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

  public long getBorderSpacing()
  {
    return borderSpacing;
  }
}
