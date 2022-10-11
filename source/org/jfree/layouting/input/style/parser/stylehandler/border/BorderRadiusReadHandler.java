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
 * $Id: BorderRadiusReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.w3c.css.sac.LexicalUnit;

/**
 * This looks a bit funny, as if the standard has not been completed.
 * THe compound property may change ...
 *
 * @author Thomas Morgner
 */
public class BorderRadiusReadHandler implements CSSValueReadHandler, CSSCompoundValueReadHandler
{
  public BorderRadiusReadHandler()
  {
  }

  public CSSValue createValue(final StyleKey name, LexicalUnit value)
  {
    final CSSNumericValue firstValue = CSSValueFactory.createLengthValue(value);
    if (firstValue == null)
    {
      return null;
    }
    value = value.getNextLexicalUnit();
    final CSSNumericValue secondValue;
    if (value == null)
    {
      secondValue = firstValue;
    }
    else
    {
      secondValue = CSSValueFactory.createLengthValue(value);
      if (secondValue == null)
      {
        return null;
      }
    }

    return new CSSValuePair(firstValue, secondValue);
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(final LexicalUnit unit)
  {
    final CSSValue value = createValue(null, unit);
    if (value == null)
    {
      return null;
    }

    final Map map = new HashMap();
    map.put(BorderStyleKeys.BORDER_TOP_RIGHT_RADIUS, value);
    map.put(BorderStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS, value);
    map.put(BorderStyleKeys.BORDER_BOTTOM_LEFT_RADIUS, value);
    map.put(BorderStyleKeys.BORDER_TOP_LEFT_RADIUS, value);
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] {
        BorderStyleKeys.BORDER_TOP_RIGHT_RADIUS,
        BorderStyleKeys.BORDER_BOTTOM_RIGHT_RADIUS,
        BorderStyleKeys.BORDER_BOTTOM_LEFT_RADIUS,
        BorderStyleKeys.BORDER_TOP_LEFT_RADIUS
    };
  }
}
