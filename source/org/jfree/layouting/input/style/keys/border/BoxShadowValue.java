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
 * $Id: BoxShadowValue.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style.keys.border;

import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 30.10.2005, 19:53:45
 *
 * @author Thomas Morgner
 */
public class BoxShadowValue implements CSSValue
{
  private CSSColorValue color;
  private CSSNumericValue horizontalOffset;
  private CSSNumericValue verticalOffset;
  private CSSNumericValue blurRadius;

  public BoxShadowValue(final CSSColorValue color,
                        final CSSNumericValue horizontalOffset,
                        final CSSNumericValue verticalOffset,
                        final CSSNumericValue blurRadius)
  {
    this.color = color;
    this.horizontalOffset = horizontalOffset;
    this.verticalOffset = verticalOffset;
    this.blurRadius = blurRadius;
  }

  public CSSColorValue getColor()
  {
    return color;
  }

  public CSSNumericValue getHorizontalOffset()
  {
    return horizontalOffset;
  }

  public CSSNumericValue getVerticalOffset()
  {
    return verticalOffset;
  }

  public CSSNumericValue getBlurRadius()
  {
    return blurRadius;
  }

  public String getCSSText()
  {
    return horizontalOffset + " " + verticalOffset + ' ' + blurRadius + ' ' + color;
  }
}
