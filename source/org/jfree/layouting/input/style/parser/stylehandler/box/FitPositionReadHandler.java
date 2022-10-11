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
 * $Id: FitPositionReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.box;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSAutoValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 21:48:44
 *
 * @author Thomas Morgner
 */
public class FitPositionReadHandler extends OneOfConstantsReadHandler
{
  public static final CSSNumericValue CENTER =
          CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, 50);
  public static final CSSNumericValue TOP =
          CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, 0);
  public static final CSSNumericValue LEFT =
          CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, 0);
  public static final CSSNumericValue BOTTOM =
          CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, 100);
  public static final CSSNumericValue RIGHT =
          CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, 100);

  public FitPositionReadHandler()
  {
    super(false);
  }

  public CSSValue createValue(final StyleKey name, LexicalUnit value)
  {
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      final String stringValue = value.getStringValue();
      if ("auto".equalsIgnoreCase(stringValue))
      {
        return CSSAutoValue.getInstance();
      }
    }

    final CSSValue firstPosition = parseFirstPosition (value);
    if (firstPosition == null)
    {
      return null;
    }

    value = value.getNextLexicalUnit();
    final CSSValue secondPosition = parseSecondPosition(value, firstPosition);
    if (secondPosition == null)
    {
      return null;
    }

    return createResultList(firstPosition, secondPosition);
  }


  protected CSSValue parseFirstPosition(final LexicalUnit value)
  {
    if (value == null)
    {
      return null;
    }

    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if ("left".equalsIgnoreCase(value.getStringValue()))
      {
        return LEFT;
      }
      else if ("center".equalsIgnoreCase(value.getStringValue()))
      {
        return CENTER;
      }
      else if ("right".equalsIgnoreCase(value.getStringValue()))
      {
        return RIGHT;
      }
      else if ("top".equalsIgnoreCase(value.getStringValue()))
      {
        return TOP;
      }
      else if ("bottom".equalsIgnoreCase(value.getStringValue()))
      {
        return BOTTOM;
      }

      // ignore this rule.
      return null;
    }

    if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return CSSNumericValue.createValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }
    if (CSSValueFactory.isLengthValue(value))
    {
      return CSSValueFactory.createLengthValue(value);
    }
    // contains errors, we ignore this rule.
    return null;
  }

  protected CSSValuePair createResultList(final CSSValue firstPosition,
                                          final CSSValue secondPosition)
  {
    if (firstPosition == TOP || firstPosition == BOTTOM)
    {
      return new CSSValuePair(secondPosition, firstPosition);
    }
    else if (secondPosition == LEFT || secondPosition == RIGHT)
    {
      return new CSSValuePair(secondPosition, firstPosition);
    }
    else
    {
      return new CSSValuePair(firstPosition, secondPosition);
    }
  }

  protected CSSValue parseSecondPosition(final LexicalUnit value,
                                         final CSSValue firstValue)
  {
    if (value == null)
    {
      return CENTER;
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if ("left".equalsIgnoreCase(value.getStringValue()))
      {
        return LEFT;
      }
      else if ("center".equalsIgnoreCase(value.getStringValue()))
      {
        return CENTER;
      }
      else if ("right".equalsIgnoreCase(value.getStringValue()))
      {
        return RIGHT;
      }
      else if ("top".equalsIgnoreCase(value.getStringValue()))
      {
        return TOP;
      }
      else if ("bottom".equalsIgnoreCase(value.getStringValue()))
      {
        return BOTTOM;
      }
      return null; // ignore this rule, it contains errors.
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return CSSNumericValue.createValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }
    else if (CSSValueFactory.isLengthValue(value))
    {
      return CSSValueFactory.createLengthValue(value);
    }
    return CENTER;
  }
}
