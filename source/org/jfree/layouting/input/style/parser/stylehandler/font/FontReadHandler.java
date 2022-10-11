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
 * $Id: FontReadHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser.stylehandler.font;

import java.util.HashMap;
import java.util.Map;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.parser.CSSCompoundValueReadHandler;
import org.jfree.layouting.input.style.parser.stylehandler.line.LineHeightReadHandler;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.LexicalUnit;

/**
 * Creation-Date: 28.11.2005, 17:05:01
 *
 * @author Thomas Morgner
 */
public class FontReadHandler implements CSSCompoundValueReadHandler
{
  private FontStyleReadHandler styleReadHandler;
  private FontSizeReadHandler sizeReadHandler;
  private FontWeightReadHandler weightReadHandler;
  private FontVariantReadHandler variantReadHandler;
  private LineHeightReadHandler lineHeightReadHandler;
  private FontFamilyReadHandler fontFamilyReadHandler;

  public FontReadHandler()
  {
    this.styleReadHandler = new FontStyleReadHandler();
    this.sizeReadHandler = new FontSizeReadHandler();
    this.weightReadHandler = new FontWeightReadHandler();
    this.variantReadHandler = new FontVariantReadHandler();
    this.lineHeightReadHandler = new LineHeightReadHandler();
    this.fontFamilyReadHandler = new FontFamilyReadHandler();
  }

  /**
   * Parses the LexicalUnit and returns a map of (StyleKey, CSSValue) pairs.
   *
   * @param unit
   * @return
   */
  public Map createValues(LexicalUnit unit)
  {
    // todo we ignore the font-family system font styles for now.

    final CSSValue fontStyle = styleReadHandler.createValue(null, unit);
    if (fontStyle != null)
    {
      unit = unit.getNextLexicalUnit();
      if (unit == null)
      {
        return null;
      }
    }
    final CSSValue fontVariant = variantReadHandler.createValue(null, unit);
    if (fontVariant != null)
    {
      unit = unit.getNextLexicalUnit();
      if (unit == null)
      {
        return null;
      }
    }
    final CSSValue fontWeight = weightReadHandler.createValue(null, unit);
    if (fontWeight != null)
    {
      unit = unit.getNextLexicalUnit();
      if (unit == null)
      {
        return null;
      }
    }

    final CSSValue fontSize = sizeReadHandler.createValue(null, unit);
    if (fontSize == null)
    {
      return null; // required value is missing
    }

    unit = unit.getNextLexicalUnit();
    if (unit == null)
    {
      return null; // font family missing
    }

    CSSValue lineHeight = null;
    if (unit.getLexicalUnitType() == LexicalUnit.SAC_OPERATOR_SLASH)
    {
      unit = unit.getNextLexicalUnit();
      if (unit == null)
      {
        return null;
      }

      lineHeight = lineHeightReadHandler.createValue(null, unit);
      if (lineHeight == null)
      {
        return null; // required sequence missing
      }
      unit = unit.getNextLexicalUnit();
      if (unit == null)
      {
        return null;
      }
    }

    final CSSValue fontFamily = fontFamilyReadHandler.createValue(null, unit);
    if (fontFamily == null)
    {
      return null; // font family is required!
    }

    final Map map = new HashMap();
    map.put(FontStyleKeys.FONT_FAMILY, fontFamily);
    map.put(FontStyleKeys.FONT_SIZE, fontSize);
    if (lineHeight != null)
    {
      map.put(LineStyleKeys.LINE_HEIGHT, lineHeight);
    }
    if (fontWeight != null)
    {
      map.put(FontStyleKeys.FONT_WEIGHT, fontWeight);
    }
    if (fontVariant != null)
    {
      map.put(FontStyleKeys.FONT_VARIANT, fontVariant);
    }
    if (fontStyle != null)
    {
      map.put(FontStyleKeys.FONT_STYLE, fontStyle);
    }
    return map;
  }

  public StyleKey[] getAffectedKeys()
  {
    return new StyleKey[]{
            FontStyleKeys.FONT_FAMILY,
            FontStyleKeys.FONT_SIZE,
            FontStyleKeys.FONT_WEIGHT,
            FontStyleKeys.FONT_VARIANT,
            FontStyleKeys.FONT_STYLE,
            LineStyleKeys.LINE_HEIGHT

    };
  }
}
