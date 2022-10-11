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
 * $Id: CropReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSRectangleType;
import org.jfree.layouting.input.style.values.CSSRectangleValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 15:36:05
 *
 * @author Thomas Morgner
 */
public class CropReadHandler implements CSSValueReadHandler
{
  public CropReadHandler()
  {
  }

  public CSSValue createValue(final StyleKey name, final LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      final String stringValue = value.getStringValue();
      if ("auto".equalsIgnoreCase(stringValue) ||
          "none".equalsIgnoreCase(stringValue))
      {
        return CSSAutoValue.getInstance();
      }
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_FUNCTION)
    {
      if ("inset-rect".equalsIgnoreCase(value.getFunctionName()))
      {
        return getRectangle(CSSRectangleType.INSET_RECT, value.getParameters());
      }
      return null;
    }
    else if (value.getLexicalUnitType() == LexicalUnit.SAC_RECT_FUNCTION)
    {
      return getRectangle(CSSRectangleType.RECT, value.getParameters());
    }
    return null;
  }


  private static CSSRectangleValue getRectangle
          (final CSSRectangleType type, LexicalUnit value)
  {
    final CSSNumericValue[] list = new CSSNumericValue[4];
    for (int index = 0; index < 4; index++)
    {
      if (value == null)
      {
        return null;
      }
      final CSSNumericValue nval = CSSValueFactory.createLengthValue(value);
      if (nval != null)
      {
        list[index] = nval;
      }
      else if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
      {
        list[index] = CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, value.getFloatValue());
      }
      else
      {
        return null;
      }
      value = CSSValueFactory.parseComma(value);
    }

    return new CSSRectangleValue (type, list[0], list[1], list[2], list[3]);
  }

}
