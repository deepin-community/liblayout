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
 * $Id: TableRow.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table.rows;

import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.util.LongList;

/**
 * Creation-Date: 22.07.2006, 13:20:47
 *
 * @author Thomas Morgner
 */
public class TableRow
{
  // Borders will be needed for the combined column model ..
  private Border border;

  private long preferredSize;
  private long validateSize;

  private LongList preferredSizes;
  private long validatedLeadingSize;
  private LongList validatedTrailingSize;

  public TableRow()
  {
    this(Border.createEmptyBorder());
  }

  public TableRow(final Border border)
  {
    this.border = border;
    this.preferredSizes = new LongList(10);
    this.validatedLeadingSize = 0;
    this.validatedTrailingSize = new LongList(10);
  }

  public long getPreferredSize()
  {
    return preferredSize;
  }

  public void setPreferredSize(final long preferredSize)
  {
    this.preferredSize = preferredSize;
  }

  public long getPreferredSize(final int colspan)
  {
    final int index = colspan - 1;
    if (index < 0)
    {
      throw new IllegalArgumentException();
    }

    if (preferredSizes.size() <= index)
    {
      return 0;
    }
    return preferredSizes.get(index);
  }

  public int getMaximumRowSpan()
  {
    return preferredSizes.size();
  }

  public void updateDefinedSize(final int rowSpan,
                                final long preferredWidth)
  {
    if (rowSpan < 1)
    {
      throw new IllegalArgumentException();
    }
    final int idx = rowSpan - 1;

    if ((idx >= preferredSizes.size()) ||
            (preferredSizes.get(idx) < preferredWidth))
    {
      preferredSizes.set(idx, preferredWidth);
    }
  }

  public long getValidatedLeadingSize()
  {
    return validatedLeadingSize;
  }

  public long getValidatedTrailingSize(final int rowSpan)
  {
    if (rowSpan > validatedTrailingSize.size())
    {
      return 0;
    }
    return validatedTrailingSize.get(rowSpan - 1);
  }

  public void setValidatedTralingSize(final int rowSpan,
                                      final long validatedSize)
  {
    this.validatedTrailingSize.set(rowSpan - 1, validatedSize);
  }

  public int getMaxValidatedRowSpan()
  {
    return this.validatedTrailingSize.size();
  }

  public void updateValidatedSize(final int rowSpan,
                                  final long leading,
                                  final long trailing)
  {
    final int idx = rowSpan - 1;
    if (validatedLeadingSize < leading)
    {
      validatedLeadingSize = leading;
    }

    if ((idx >= validatedTrailingSize.size()) ||
            (validatedTrailingSize.get(idx) < trailing))
    {
      validatedTrailingSize.set(idx, trailing);
    }
  }

  public long getValidateSize()
  {
    return validateSize;
  }

  public void setValidateSize(final long validateSize)
  {
    this.validateSize = validateSize;
  }

  public void clear()
  {
    //this.validatedLeadingSize = 0;
    this.validatedTrailingSize.clear();
    this.validateSize = 0;
  }

  public void clearSizes()
  {
    this.preferredSizes.clear();
    this.preferredSize = 0;
  }
}
