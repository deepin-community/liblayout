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
 * $Id: BorderStyleReadHandler.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.border;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.stylehandler.OneOfConstantsReadHandler;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 27.11.2005, 19:17:22
 *
 * @author Thomas Morgner
 */
public class BorderStyleReadHandler extends OneOfConstantsReadHandler
  implements CSSCompoundValueReadHandler
{
  public BorderStyleReadHandler()
  {
    super( false);
    addValue(BorderStyle.DASHED);
    addValue(BorderStyle.DOT_DASH);
    addValue(BorderStyle.DOT_DOT_DASH);
    addValue(BorderStyle.DOTTED);
    addValue(BorderStyle.DOUBLE);
    addValue(BorderStyle.GROOVE);
    addValue(BorderStyle.HIDDEN);
    addValue(BorderStyle.INSET);
    addValue(BorderStyle.NONE);
    addValue(BorderStyle.OUTSET);
    addValue(BorderStyle.RIDGE);
    addValue(BorderStyle.SOLID);
    addValue(BorderStyle.WAVE);
  }


  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSConstant topStyle = (CSSConstant) lookupValue(unit);
    if (topStyle == null)
    {
      return null;
    }

    unit = unit.getNextLexicalUnit();

    final CSSConstant rightStyle;
    if (unit == null)
    {
      rightStyle = topStyle;
    }
    else
    {
      rightStyle = (CSSConstant) lookupValue(unit);
      if (rightStyle == null)
      {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final CSSConstant bottomStyle;
    if (unit == null)
    {
      bottomStyle = topStyle;
    }
    else
    {
      bottomStyle = (CSSConstant) lookupValue(unit);
      if (bottomStyle == null)
      {
        return null;
      }
      unit = unit.getNextLexicalUnit();
    }

    final CSSConstant leftStyle;
    if (unit == null)
    {
      leftStyle = rightStyle;
    }
    else
    {
      leftStyle = (CSSConstant) lookupValue(unit);
      if (leftStyle == null)
      {
        return null;
      }
    }

    final Map map = new HashMap();
    map.put(BorderStyleKeys.BORDER_TOP_STYLE, topStyle);
    map.put(BorderStyleKeys.BORDER_RIGHT_STYLE, rightStyle);
    map.put(BorderStyleKeys.BORDER_BOTTOM_STYLE, bottomStyle);
    map.put(BorderStyleKeys.BORDER_LEFT_STYLE, leftStyle);
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] {
            BorderStyleKeys.BORDER_TOP_STYLE,
            BorderStyleKeys.BORDER_RIGHT_STYLE,
            BorderStyleKeys.BORDER_BOTTOM_STYLE,
            BorderStyleKeys.BORDER_LEFT_STYLE
    };
  }
}
