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
 * $Id: BorderColorReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueReadHandler;
import org.jfree.layouting.input.style.parser.stylehandler.color.ColorReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 19:07:11
 *
 * @author Thomas Morgner
 */
public class BorderColorReadHandler implements CSSValueReadHandler, CSSCompoundValueReadHandler
{
  public BorderColorReadHandler()
  {
  }

  public CSSValue createValue(final StyleKey name, final LexicalUnit value)
  {
    return ColorReadHandler.createColorValue(value);
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSValue topColor = ColorReadHandler.createColorValue(unit);
    if (topColor == null)
    {
      return null;
    }

    unit = unit.getNextLexicalUnit();

    final CSSValue rightColor;
    if (unit == null)
    {
      rightColor = topColor;
    }
    else
    {
      rightColor = ColorReadHandler.createColorValue(unit);
      if (rightColor == null)
      {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final CSSValue bottomColor;
    if (unit == null)
    {
      bottomColor = topColor;
    }
    else
    {
      bottomColor = ColorReadHandler.createColorValue(unit);
      if (bottomColor == null)
      {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final CSSValue leftColor;
    if (unit == null)
    {
      leftColor = rightColor;
    }
    else
    {
      leftColor = ColorReadHandler.createColorValue(unit);
      if (leftColor == null)
      {
        return null;
      }
    }

    final Map map = new HashMap();
    map.put(BorderStyleKeys.BORDER_TOP_COLOR, topColor);
    map.put(BorderStyleKeys.BORDER_RIGHT_COLOR, rightColor);
    map.put(BorderStyleKeys.BORDER_BOTTOM_COLOR, bottomColor);
    map.put(BorderStyleKeys.BORDER_LEFT_COLOR, leftColor);
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[]{
            BorderStyleKeys.BORDER_TOP_COLOR,
            BorderStyleKeys.BORDER_RIGHT_COLOR,
            BorderStyleKeys.BORDER_BOTTOM_COLOR,
            BorderStyleKeys.BORDER_LEFT_COLOR
    };
  }
}
