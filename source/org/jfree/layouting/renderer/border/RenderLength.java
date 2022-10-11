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
 * $Id: RenderLength.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.border;

import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 09.07.2006, 21:03:12
 *
 * @author Thomas Morgner
 */
public class RenderLength
{
  public static final RenderLength AUTO = new RenderLength(Long.MIN_VALUE, false);
  public static final RenderLength EMPTY = new RenderLength(0, false);

  private long value;
  private boolean percentage;

  public RenderLength(final long value,
                      final boolean percentage)
  {
    this.value = value;
    this.percentage = percentage;
  }

  public long getValue()
  {
    return value;
  }

  public boolean isPercentage()
  {
    return percentage;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final RenderLength that = (RenderLength) o;

    if (percentage != that.percentage)
    {
      return false;
    }
    if (value != that.value)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = (int) (value ^ (value >>> 32));
    result = 29 * result + (percentage ? 1 : 0);
    return result;
  }

  public long resolve (final long parent)
  {
    if (isPercentage())
    {
      return StrictGeomUtility.multiply(value, parent) / 100;
    }
    else if (value == Long.MIN_VALUE)
    {
      return 0;
    }
    else
    {
      return value;
    }
  }

  public RenderLength resolveToRenderLength (final long parent)
  {
    if (isPercentage())
    {
      if (parent <= 0)
      {
        // An unresolvable parent ...
        return RenderLength.AUTO;
      }
      // This may resolve to zero - which is valid
      return new RenderLength(StrictGeomUtility.multiply(value, parent) / 100, false);
    }
    else if (value <= 0)
    {
      return RenderLength.AUTO;
    }
    else
    {
      return new RenderLength(value, false);
    }
  }


  public static RenderLength convertToInternal(final CSSValue value,
                                               final LayoutContext layoutContext,
                                               final OutputProcessorMetaData metaData)
  {
    if (value instanceof CSSNumericValue)
    {
      final CSSNumericValue nval = (CSSNumericValue) value;
      if (nval.getType() == CSSNumericType.PERCENTAGE)
      {
        // Range is between 100.000 and 0
        return new RenderLength(StrictGeomUtility.toInternalValue
                (nval.getValue()), true);
      }
      if (nval.getType() == CSSNumericType.NUMBER)
      {
        // Range is between 100.000 and 0
        return new RenderLength(StrictGeomUtility.toInternalValue
                (nval.getValue()) * 100, true);
      }

      final CSSNumericValue cssNumericValue =
          CSSValueResolverUtility.convertLength(value, layoutContext, metaData);
      // the resulting nvalue is guaranteed to have the unit PT

      return new RenderLength(StrictGeomUtility.toInternalValue
                      (cssNumericValue.getValue()), false);
    }
    return RenderLength.EMPTY;
  }


  public String toString()
  {
    if (value == Long.MIN_VALUE)
    {
      return "RenderLength{value=AUTO}";
    }
    if (isPercentage())
    {
      return "RenderLength{" +
            "value=" + value +
            "% }";
    }
    else
    {
      return "RenderLength{" +
            "value=" + value +
            "pt }";
    }
  }
}
