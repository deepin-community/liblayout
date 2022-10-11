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
 * $Id: BoxDefinition.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.renderer.border.Border;
import org.jfree.layouting.renderer.border.RenderLength;

/**
 * A box definition.
 *
 * Todo How to deal with auto-size margins?
 *
 * @author Thomas Morgner
 */
public interface BoxDefinition
{
  public RenderLength getMarginTop();

  public RenderLength getMarginBottom();

  public RenderLength getMarginLeft();

  public RenderLength getMarginRight();

  public RenderLength getPaddingTop();

  public RenderLength getPaddingLeft();

  public RenderLength getPaddingBottom();

  public RenderLength getPaddingRight();

  public Border getBorder();

  public RenderLength getMinimumWidth();

  public RenderLength getMinimumHeight();

  public RenderLength getMaximumWidth();

  public RenderLength getMaximumHeight();

  /**
   * The preferred size is only set, if a width has been explicitly defined.
   *
   * @return
   */
  public RenderLength getPreferredWidth();

  /**
   * The preferred size is only set, if a height has been explicitly defined.
   *
   * @return
   */
  public RenderLength getPreferredHeight();

  public BoxDefinition[] split (int axis);

//  public BoxDefinition[] splitVertically();
//
//  public BoxDefinition[] splitHorizontally();
//
  public CSSColorValue getBackgroundColor();

  public boolean isEmpty();
}
