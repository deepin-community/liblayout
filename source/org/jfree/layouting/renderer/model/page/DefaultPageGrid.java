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
 * $Id: DefaultPageGrid.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.page;

import org.jfree.layouting.input.style.PageAreaType;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * Creation-Date: 15.06.2006, 16:16:00
 *
 * @author Thomas Morgner
 */
public class DefaultPageGrid implements PageGrid
{
  private int rows;
  private int columns;
  private PhysicalPageBox[] pages;

  public DefaultPageGrid(final PageContext pageContext,
                         final OutputProcessorMetaData metaData)
  {
    final LayoutStyle areaDefinition =
            pageContext.getAreaDefinition(PageAreaType.CONTENT);
    final CSSValue hspanValue = areaDefinition.getValue(PageStyleKeys.HORIZONTAL_PAGE_SPAN);
    final CSSValue vspanValue = areaDefinition.getValue(PageStyleKeys.VERTICAL_PAGE_SPAN);

    this.columns = Math.max (1, (int) CSSValueResolverUtility.getNumericValue
            (hspanValue, metaData.getHorizontalPageSpan()));
    this.rows = Math.max (1, (int) CSSValueResolverUtility.getNumericValue
            (vspanValue, metaData.getVerticalPageSpan()));

    this.pages = new PhysicalPageBox[rows * columns];

    long globalY = 0;
    for (int row = 0; row < rows; row++)
    {
      long globalX = 0;
      long lastHeight = 0;
      for (int col = 0; col < columns; col++)
      {
        final PhysicalPageBox ppb =
            new PhysicalPageBox(pageContext, metaData, globalX, globalY);
        pages[row * columns + col] = ppb;
        globalX += ppb.getImageableWidth();
        lastHeight = ppb.getImageableHeight();
      }
      globalY += lastHeight;
    }
  }


  public PhysicalPageBox getPage(final int row, final int col)
  {
    return pages[row * rows + col];
  }

  public int getRowCount()
  {
    return rows;
  }

  public int getColumnCount()
  {
    return columns;
  }

  public Object clone ()
  {
    try
    {
      final DefaultPageGrid o = (DefaultPageGrid) super.clone();
      o.pages = (PhysicalPageBox[]) pages.clone();
      for (int i = 0; i < pages.length; i++)
      {
        final PhysicalPageBox page = pages[i];
        o.pages[i] = (PhysicalPageBox) page.clone();
      }
      return o;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException();
    }
  }
}
