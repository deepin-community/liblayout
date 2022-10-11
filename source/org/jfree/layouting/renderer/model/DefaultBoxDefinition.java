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
 * $Id: DefaultBoxDefinition.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Describes the margins, paddings, borders and sizes of a box. (This does not
 * define or describe the *actual* value used for the rendering, it describes
 * the stylesheet's computed values.)
 *
 * @author Thomas Morgner
 */
public class DefaultBoxDefinition implements BoxDefinition
{
  private RenderLength marginTop;
  private RenderLength marginBottom;
  private RenderLength marginLeft;
  private RenderLength marginRight;

  private RenderLength paddingTop;
  private RenderLength paddingLeft;
  private RenderLength paddingBottom;
  private RenderLength paddingRight;

  private Border border;

  private RenderLength minimumWidth;
  private RenderLength minimumHeight;
  private RenderLength maximumWidth;
  private RenderLength maximumHeight;
  private RenderLength preferredWidth;
  private RenderLength preferredHeight;

  private CSSColorValue backgroundColor;

  private Boolean empty;

  public DefaultBoxDefinition()
  {
    marginTop = RenderLength.EMPTY;
    marginLeft = RenderLength.EMPTY;
    marginBottom = RenderLength.EMPTY;
    marginRight = RenderLength.EMPTY;

    paddingTop = RenderLength.EMPTY;
    paddingLeft = RenderLength.EMPTY;
    paddingBottom = RenderLength.EMPTY;
    paddingRight = RenderLength.EMPTY;

    maximumWidth = RenderLength.EMPTY;
    maximumHeight = RenderLength.EMPTY;
    minimumWidth = RenderLength.EMPTY;
    minimumHeight = RenderLength.EMPTY;
    preferredWidth = RenderLength.EMPTY;
    preferredHeight = RenderLength.EMPTY;

    border = Border.createEmptyBorder();
  }

  public Border getBorder()
  {
    return border;
  }

  public void setBorder(final Border border)
  {
    if (border == null)
    {
      throw new NullPointerException();
    }
    this.border = border;
  }

  public RenderLength getMarginTop()
  {
    return marginTop;
  }

  public void setMarginTop(final RenderLength marginTop)
  {
    if (marginTop == null)
    {
      throw new NullPointerException();
    }
    this.marginTop = marginTop;
  }

  public RenderLength getMarginBottom()
  {
    return marginBottom;
  }

  public void setMarginBottom(final RenderLength marginBottom)
  {
    if (marginBottom == null)
    {
      throw new NullPointerException();
    }
    this.marginBottom = marginBottom;
  }

  public RenderLength getMarginLeft()
  {
    return marginLeft;
  }

  public void setMarginLeft(final RenderLength marginLeft)
  {
    if (marginLeft == null)
    {
      throw new NullPointerException();
    }
    this.marginLeft = marginLeft;
  }

  public RenderLength getMarginRight()
  {
    return marginRight;
  }

  public void setMarginRight(final RenderLength marginRight)
  {
    if (marginRight == null)
    {
      throw new NullPointerException();
    }
    this.marginRight = marginRight;
  }

  public RenderLength getPaddingTop()
  {
    return paddingTop;
  }

  public void setPaddingTop(final RenderLength paddingTop)
  {
    if (paddingTop == null)
    {
      throw new NullPointerException();
    }
    this.paddingTop = paddingTop;
  }

  public RenderLength getPaddingLeft()
  {
    return paddingLeft;
  }

  public void setPaddingLeft(final RenderLength paddingLeft)
  {
    if (paddingLeft == null)
    {
      throw new NullPointerException();
    }
    this.paddingLeft = paddingLeft;
  }

  public RenderLength getPaddingBottom()
  {
    return paddingBottom;
  }

  public void setPaddingBottom(final RenderLength paddingBottom)
  {
    if (paddingBottom == null)
    {
      throw new NullPointerException();
    }
    this.paddingBottom = paddingBottom;
  }

  public RenderLength getPaddingRight()
  {
    return paddingRight;
  }

  public void setPaddingRight(final RenderLength paddingRight)
  {
    if (paddingRight == null)
    {
      throw new NullPointerException();
    }
    this.paddingRight = paddingRight;
  }

  public RenderLength getMinimumWidth()
  {
    return minimumWidth;
  }

  public void setMinimumWidth(final RenderLength minimumWidth)
  {
    if (minimumWidth == null)
    {
      throw new NullPointerException();
    }
    this.minimumWidth = minimumWidth;
  }

  public RenderLength getMinimumHeight()
  {
    return minimumHeight;
  }

  public void setMinimumHeight(final RenderLength minimumHeight)
  {
    if (minimumHeight == null)
    {
      throw new NullPointerException();
    }
    this.minimumHeight = minimumHeight;
  }

  public RenderLength getMaximumWidth()
  {
    return maximumWidth;
  }

  public void setMaximumWidth(final RenderLength maximumWidth)
  {
    if (maximumWidth == null)
    {
      throw new NullPointerException();
    }
    this.maximumWidth = maximumWidth;
  }

  public RenderLength getMaximumHeight()
  {
    return maximumHeight;
  }

  public void setMaximumHeight(final RenderLength maximumHeight)
  {
    if (maximumHeight == null)
    {
      throw new NullPointerException();
    }
    this.maximumHeight = maximumHeight;
  }

  public RenderLength getPreferredWidth()
  {
    return preferredWidth;
  }

  public void setPreferredWidth(final RenderLength preferredWidth)
  {
    if (preferredWidth == null)
    {
      throw new NullPointerException();
    }
    this.preferredWidth = preferredWidth;
  }

  public RenderLength getPreferredHeight()
  {
    return preferredHeight;
  }

  public void setPreferredHeight(final RenderLength preferredHeight)
  {
    if (preferredHeight == null)
    {
      throw new NullPointerException();
    }
    this.preferredHeight = preferredHeight;
  }

  /**
   * Split the box definition for the given major axis. A horizontal axis will
   * perform vertical splits (resulting in a left and right box definition) and
   * a given vertical axis will split the box into a top and bottom box.
   *
   * @param axis
   * @return
   */
  public BoxDefinition[] split(final int axis)
  {
    if (axis == RenderNode.HORIZONTAL_AXIS)
    {
      return splitVertically();
    }
    return splitHorizontally();
  }

  public BoxDefinition[] splitVertically()
  {
    final Border[] borders = border.splitVertically(null);
    final DefaultBoxDefinition first = new DefaultBoxDefinition();
    first.marginTop = marginTop;
    first.marginLeft = marginLeft;
    first.marginBottom = marginBottom;
    first.marginRight = RenderLength.EMPTY;
    first.paddingBottom = paddingBottom;
    first.paddingTop = paddingTop;
    first.paddingLeft = paddingLeft;
    first.paddingRight = RenderLength.EMPTY;
    first.border = borders[0];
    first.preferredHeight = preferredHeight;
    first.preferredWidth = preferredWidth;
    first.minimumHeight = minimumHeight;
    first.minimumWidth = minimumWidth;
    first.maximumHeight = maximumHeight;
    first.maximumWidth = maximumWidth;

    final DefaultBoxDefinition second = new DefaultBoxDefinition();
    second.marginTop = marginTop;
    second.marginLeft = RenderLength.EMPTY;
    second.marginBottom = marginBottom;
    second.marginRight = marginRight;
    second.paddingBottom = paddingBottom;
    second.paddingTop = paddingTop;
    second.paddingLeft = RenderLength.EMPTY;
    second.paddingRight = paddingRight;
    second.border = borders[1];
    second.preferredHeight = preferredHeight;
    second.preferredWidth = preferredWidth;
    second.minimumHeight = minimumHeight;
    second.minimumWidth = minimumWidth;
    second.maximumHeight = maximumHeight;
    second.maximumWidth = maximumWidth;

    final BoxDefinition[] boxes = new BoxDefinition[2];
    boxes[0] = first;
    boxes[1] = second;
    return boxes;
  }

  public BoxDefinition[] splitHorizontally()
  {
    final Border[] borders = border.splitHorizontally(null);

    final DefaultBoxDefinition first = new DefaultBoxDefinition();
    first.marginTop = marginTop;
    first.marginLeft = marginLeft;
    first.marginBottom = RenderLength.EMPTY;
    first.marginRight = marginRight;
    first.paddingBottom = RenderLength.EMPTY;
    first.paddingTop = paddingTop;
    first.paddingLeft = paddingLeft;
    first.paddingRight = paddingRight;
    first.border = borders[0];
    first.preferredHeight = preferredHeight;
    first.preferredWidth = preferredWidth;
    first.minimumHeight = minimumHeight;
    first.minimumWidth = minimumWidth;
    first.maximumHeight = maximumHeight;
    first.maximumWidth = maximumWidth;

    final DefaultBoxDefinition second = new DefaultBoxDefinition();
    second.marginTop = RenderLength.EMPTY;
    second.marginLeft = marginLeft;
    second.marginBottom = marginBottom;
    second.marginRight = marginRight;
    second.paddingBottom = paddingBottom;
    second.paddingTop = RenderLength.EMPTY;
    second.paddingLeft = paddingLeft;
    second.paddingRight = paddingRight;
    second.border = borders[1];
    second.preferredHeight = preferredHeight;
    second.preferredWidth = preferredWidth;
    second.minimumHeight = minimumHeight;
    second.minimumWidth = minimumWidth;
    second.maximumHeight = maximumHeight;
    second.maximumWidth = maximumWidth;

    final BoxDefinition[] boxes = new BoxDefinition[2];
    boxes[0] = first;
    boxes[1] = second;
    return boxes;
  }

  public CSSColorValue getBackgroundColor()
  {
    return backgroundColor;
  }

  public void setBackgroundColor(final CSSColorValue backgroundColor)
  {
    this.backgroundColor = backgroundColor;
  }

  public boolean isEmpty()
  {
    if (empty != null)
    {
      return empty.booleanValue();
    }

    if (paddingTop.getValue() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (paddingLeft.getValue() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (paddingBottom.getValue() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (paddingRight.getValue() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (border.isEmpty())
    {
      empty = Boolean.FALSE;
      return false;
    }

    empty = Boolean.TRUE;
    return true;
  }
}
