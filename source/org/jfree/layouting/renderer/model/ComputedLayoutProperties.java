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
 * $Id: ComputedLayoutProperties.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.model;

import java.io.Serializable;

import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Immutable version of the node-layout properties.
 *
 * @author Thomas Morgner
 */
public class ComputedLayoutProperties implements Serializable
{
  private long marginLeft;
  private long marginRight;
  private long marginTop;
  private long marginBottom;
  private long paddingLeft;
  private long paddingTop;
  private long paddingRight;
  private long paddingBottom;
  private long borderLeft;
  private long borderRight;
  private long borderTop;
  private long borderBottom;

  // This represents the computed with.
  private RenderLength blockContextWidth;
  // Either AUTO or a valid width.
  private RenderLength computedWidth;

  public ComputedLayoutProperties()
  {
  }
  
//  public ComputedLayoutProperties(final RenderLength blockContextWidth,
//                                  final RenderLength computedWidth)
//  {
//    setBlockContextWidth(blockContextWidth);
//    setComputedWidth(computedWidth);
//  }

  public void setBlockContextWidth(final RenderLength blockContextWidth)
  {
    if (blockContextWidth.isPercentage())
    {
      throw new IllegalArgumentException
              ("Percentages are not allowed at this stage.");
    }
    if (blockContextWidth == RenderLength.AUTO)
    {
      this.blockContextWidth = RenderLength.AUTO;
    }
    else if (blockContextWidth.getValue() <= 0)
    {
      this.blockContextWidth = RenderLength.EMPTY;
    }
    else
    {
      this.blockContextWidth = blockContextWidth;
    }
  }

  public void setComputedWidth(final RenderLength computedWidth)
  {
    if (computedWidth.isPercentage())
    {
      throw new IllegalArgumentException
              ("Percentages are not allowed at this stage.");
    }
    if (computedWidth == RenderLength.AUTO)
    {
      this.computedWidth = RenderLength.AUTO;
    }
    else if (computedWidth.getValue() <= 0)
    {
      this.computedWidth = RenderLength.EMPTY;
    }
    else
    {
      this.computedWidth = computedWidth;
    }
  }

  public RenderLength getBlockContextWidth()
  {
    return blockContextWidth;
  }

  public RenderLength getComputedWidth()
  {
    return computedWidth;
  }

  public long getMarginLeft()
  {
    return marginLeft;
  }

  public void setMarginLeft(final long marginLeft)
  {
    this.marginLeft = marginLeft;
  }

  public long getMarginRight()
  {
    return marginRight;
  }

  public void setMarginRight(final long marginRight)
  {
    this.marginRight = marginRight;
  }

  public long getMarginTop()
  {
    return marginTop;
  }

  public void setMarginTop(final long marginTop)
  {
    this.marginTop = marginTop;
  }

  public long getMarginBottom()
  {
    return marginBottom;
  }

  public void setMarginBottom(final long marginBottom)
  {
    this.marginBottom = marginBottom;
  }

  public long getPaddingLeft()
  {
    return paddingLeft;
  }

  public void setPaddingLeft(final long paddingLeft)
  {
    this.paddingLeft = paddingLeft;
  }

  public long getPaddingTop()
  {
    return paddingTop;
  }

  public void setPaddingTop(final long paddingTop)
  {
    this.paddingTop = paddingTop;
  }

  public long getPaddingRight()
  {
    return paddingRight;
  }

  public void setPaddingRight(final long paddingRight)
  {
    this.paddingRight = paddingRight;
  }

  public long getPaddingBottom()
  {
    return paddingBottom;
  }

  public void setPaddingBottom(final long paddingBottom)
  {
    this.paddingBottom = paddingBottom;
  }

  public long getBorderLeft()
  {
    return borderLeft;
  }

  public void setBorderLeft(final long borderLeft)
  {
    this.borderLeft = borderLeft;
  }

  public long getBorderRight()
  {
    return borderRight;
  }

  public void setBorderRight(final long borderRight)
  {
    this.borderRight = borderRight;
  }

  public long getBorderTop()
  {
    return borderTop;
  }

  public void setBorderTop(final long borderTop)
  {
    this.borderTop = borderTop;
  }

  public long getBorderBottom()
  {
    return borderBottom;
  }

  public void setBorderBottom(final long borderBottom)
  {
    this.borderBottom = borderBottom;
  }


  public String toString()
  {
    return "ComputedLayoutProperties{" +
        "marginLeft=" + marginLeft +
        ", marginRight=" + marginRight +
        ", marginTop=" + marginTop +
        ", marginBottom=" + marginBottom +
        ", paddingLeft=" + paddingLeft +
        ", paddingTop=" + paddingTop +
        ", paddingRight=" + paddingRight +
        ", paddingBottom=" + paddingBottom +
        ", borderLeft=" + borderLeft +
        ", borderRight=" + borderRight +
        ", borderTop=" + borderTop +
        ", borderBottom=" + borderBottom +
        ", blockContextWidth=" + blockContextWidth +
        ", computedWidth=" + computedWidth +
        '}';
  }
}
