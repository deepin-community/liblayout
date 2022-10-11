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
 * $Id: SpacingLimitReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.text;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.CSSValueFactory;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 24.05.2006, 15:13:13
 *
 * @author Thomas Morgner
 */
public abstract class SpacingLimitReadHandler implements CSSCompoundValueReadHandler
{
  public static final CSSConstant NORMAL = new CSSConstant("normal");

  protected SpacingLimitReadHandler()
  {
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    final CSSValue optimum = parseSingleSpacingValue(unit);
    if (optimum == null)
    {
      return null;
    }
    unit = unit.getNextLexicalUnit();

    final CSSValue minimum = parseSingleSpacingValue(unit);
    if (minimum != null)
    {
      unit = unit.getNextLexicalUnit();
    }

    final CSSValue maximum = parseSingleSpacingValue(unit);
    final Map map = new HashMap();
    map.put(getMinimumKey(), minimum);
    map.put(TextStyleKeys.X_MAX_LETTER_SPACING, maximum);
    map.put(TextStyleKeys.X_OPTIMUM_LETTER_SPACING, optimum);
    return map;
  }

  protected abstract StyleKey getMinimumKey();

  protected abstract StyleKey getMaximumKey();

  protected abstract StyleKey getOptimumKey();

  private CSSValue parseSingleSpacingValue(final LexicalUnit value)
  {
    if (value == null)
    {
      return null;
    }

    if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
    {
      if ("normal".equalsIgnoreCase(value.getStringValue()))
      {
        return SpacingLimitReadHandler.NORMAL;
      }
      return null;
    }
    if (value.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)
    {
      return CSSNumericValue.createValue(CSSNumericType.PERCENTAGE,
              value.getFloatValue());
    }

    return CSSValueFactory.createLengthValue(value);
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[] { getMinimumKey(), getMaximumKey(), getOptimumKey()};
  }
}
