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
 * $Id: WritingModeReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.text;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.text.BlockProgression;
import org.jfree.layouting.input.style.keys.text.Direction;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.values.CSSInheritValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 02.12.2005, 17:38:27
 *
 * @author Thomas Morgner
 */
public class WritingModeReadHandler implements CSSCompoundValueReadHandler
{
  public WritingModeReadHandler()
  {
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(final LexicalUnit unit)
  {
    if (unit.getLexicalUnitType() == LexicalUnit.SAC_INHERIT)
    {
      final Map map = new HashMap();
      map.put (TextStyleKeys.DIRECTION, CSSInheritValue.getInstance());
      map.put (TextStyleKeys.BLOCK_PROGRESSION, CSSInheritValue.getInstance());
      return map;
    }

    if (unit.getLexicalUnitType() != LexicalUnit.SAC_IDENT)
    {
      return null;
    }

    final CSSValue direction;
    final CSSValue blockProgression;
    final String strValue = unit.getStringValue();
    // lr-tb | rl-tb | tb-rl | tb-lr
    if ("lr-tb".equalsIgnoreCase(strValue))
    {
      direction = Direction.LTR;
      blockProgression = BlockProgression.TB;
    }
    else if ("rl-tb".equalsIgnoreCase(strValue))
    {
      direction = Direction.RTL;
      blockProgression = BlockProgression.TB;
    }
    else if ("tb-rl".equalsIgnoreCase(strValue))
    {
      direction = Direction.LTR;
      blockProgression = BlockProgression.RL;
    }
    else if ("tb-lr".equalsIgnoreCase(strValue))
    {
      direction = Direction.RTL;
      blockProgression = BlockProgression.LR;
    }
    else
    {
      return null;
    }

    final Map map = new HashMap();
    map.put (TextStyleKeys.DIRECTION, direction);
    map.put (TextStyleKeys.BLOCK_PROGRESSION, blockProgression);
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] {TextStyleKeys.DIRECTION, TextStyleKeys.BLOCK_PROGRESSION };
  }
}
