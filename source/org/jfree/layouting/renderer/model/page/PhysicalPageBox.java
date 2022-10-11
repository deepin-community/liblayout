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
 * $Id: PhysicalPageBox.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.page;

import org.jfree.layouting.input.style.PageAreaType;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.layouter.context.PageContext;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Defines the properties of a single physical page. In a later version, this
 * box may receive physical page header and footer or may even support the full
 * CSS-pagebox modell.
 */
public class PhysicalPageBox implements Cloneable
{
  private PageContext pageContext;
  private long width;
  private long height;
  private long imageableX;
  private long imageableY;
  private long imageableWidth;
  private long imageableHeight;
  private long globalX;
  private long globalY;

  public PhysicalPageBox(final PageContext pageContext,
                         final OutputProcessorMetaData metaData,
                         final long globalX,
                         final long globalY)
  {
    this.globalX = globalX;
    this.globalY = globalY;
    this.pageContext = pageContext;

    final LayoutStyle areaDefinition =
        pageContext.getAreaDefinition(PageAreaType.CONTENT);
    final CSSValue pageValue = areaDefinition.getValue(PageStyleKeys.SIZE);
    final PageSize pageSize = PageGridUtility.lookupPageSize(pageValue, metaData);

    this.width = StrictGeomUtility.toInternalValue(pageSize.getWidth());
    this.height = StrictGeomUtility.toInternalValue(pageSize.getHeight());

    final CSSValue marginTopValue =
        areaDefinition.getValue(BoxStyleKeys.MARGIN_TOP);
    final CSSValue marginLeftValue =
        areaDefinition.getValue(BoxStyleKeys.MARGIN_LEFT);
    final CSSValue marginBottomValue =
        areaDefinition.getValue(BoxStyleKeys.MARGIN_BOTTOM);
    final CSSValue marginRightValue =
        areaDefinition.getValue(BoxStyleKeys.MARGIN_RIGHT);

    final long marginTop = computeWidth(marginTopValue, metaData).resolve(width);
    final long marginLeft  = computeWidth(marginLeftValue, metaData).resolve(width);
    final long marginBottom = computeWidth(marginBottomValue, metaData).resolve(width);
    final long marginRight = computeWidth(marginRightValue, metaData).resolve(width);

    imageableX = marginLeft;
    imageableY = marginTop;
    imageableWidth = Math.max(0, width - marginLeft - marginRight);
    imageableHeight = Math.max(0, height - marginTop - marginBottom);

    if (imageableWidth == 0)
    {
      imageableWidth = Math.max(0, width - marginLeft);
      if (imageableWidth == 0)
      {
        imageableWidth = Math.max(0, width);
        if (imageableHeight == 0)
        {
          imageableWidth = StrictGeomUtility.toInternalValue
              (metaData.getDefaultPageSize().getWidth());
          if (imageableWidth <= 0)
          {
            throw new IllegalStateException("The margin-definition is invalid and would not yield a layoutable page.");
          }
        }
      }
    }

    if (imageableHeight == 0)
    {
      imageableHeight = Math.max(0, height - marginTop);
      if (imageableHeight == 0)
      {
        imageableHeight = Math.max(0, height);
        if (imageableHeight == 0)
        {
          imageableHeight = StrictGeomUtility.toInternalValue
              (metaData.getDefaultPageSize().getHeight());
          if (imageableHeight <= 0)
          {
            throw new IllegalStateException
                ("I tried everything to save you, but you ignored me.");
          }
        }
      }
    }
  }

  private static RenderLength computeWidth(final CSSValue widthValue,
                                           final OutputProcessorMetaData metaData)
  {
    if (widthValue instanceof CSSNumericValue == false)
    {
      return RenderLength.EMPTY;
    }
    else
    {
      final CSSNumericValue nval = (CSSNumericValue) widthValue;
      if (nval.getValue() > 0)
      {
        final RenderLength renderLength =
            RenderLength.convertToInternal(widthValue, null, metaData);
        if (renderLength.getValue() > 0)
        {
          return renderLength;
        }
      }
      return RenderLength.EMPTY;
    }
  }

  public long getImageableX()
  {
    return imageableX;
  }

  public long getImageableY()
  {
    return imageableY;
  }

  public long getImageableWidth()
  {
    return imageableWidth;
  }

  public long getImageableHeight()
  {
    return imageableHeight;
  }

  public long getGlobalX()
  {
    return globalX;
  }

  public void setGlobalX(final long globalX)
  {
    this.globalX = globalX;
  }

  public long getGlobalY()
  {
    return globalY;
  }

  public void setGlobalY(final long globalY)
  {
    this.globalY = globalY;
  }

  public long getWidth()
  {
    return width;
  }

  public long getHeight()
  {
    return height;
  }

  public PageContext getPageContext()
  {
    return pageContext;
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
