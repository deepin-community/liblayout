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
 * $Id: EmptyBoxDefinition.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.keys.color.CSSSystemColors;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * Creation-Date: 15.06.2006, 17:10:27
 *
 * @author Thomas Morgner
 */
public final class EmptyBoxDefinition implements BoxDefinition
{
  private static EmptyBoxDefinition instance;

  public static synchronized EmptyBoxDefinition getInstance()
  {
    if (instance == null)
    {
      instance = new EmptyBoxDefinition();
    }
    return instance;
  }

  private Border border;

  private EmptyBoxDefinition()
  {
    border = Border.createEmptyBorder();
  }

  public RenderLength getMarginTop()
  {
    return RenderLength.EMPTY;
  }

  public RenderLength getMarginBottom()
  {
    return RenderLength.EMPTY;
  }

  public RenderLength getMarginLeft()
  {
    return RenderLength.EMPTY;
  }

  public RenderLength getMarginRight()
  {
    return RenderLength.EMPTY;
  }

  public RenderLength getPaddingTop()
  {
    return RenderLength.EMPTY;
  }

  public RenderLength getPaddingLeft()
  {
    return RenderLength.EMPTY;
  }

  public RenderLength getPaddingBottom()
  {
    return RenderLength.EMPTY;
  }

  public RenderLength getPaddingRight()
  {
    return RenderLength.EMPTY;
  }

  public Border getBorder()
  {
    return border;
  }

  public RenderLength getMinimumWidth()
  {
    return RenderLength.AUTO;
  }

  public RenderLength getMinimumHeight()
  {
    return RenderLength.AUTO;
  }

  public RenderLength getMaximumWidth()
  {
    return RenderLength.AUTO;
  }

  public RenderLength getMaximumHeight()
  {
    return RenderLength.AUTO;
  }

  public RenderLength getPreferredWidth()
  {
    return RenderLength.AUTO;
  }

  public RenderLength getPreferredHeight()
  {
    return RenderLength.AUTO;
  }

  public BoxDefinition[] split(final int axis)
  {
    return new BoxDefinition[] { this, this };
  }

  public CSSColorValue getBackgroundColor()
  {
    return CSSSystemColors.TRANSPARENT;
  }

  public boolean isEmpty()
  {
    return true;
  }
}
