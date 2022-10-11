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
 * $Id: ColorConverter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.swing.converter;

import java.awt.Color;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.swing.Converter;
import org.jfree.layouting.input.swing.ConverterAttributeSet;
import org.pentaho.reporting.libraries.base.util.DebugLog;

/**
 * This class handles convertions of color style attributes to css style attributes.
 */
public class ColorConverter implements Converter
{
  public ColorConverter()
  {
  }

  /**
   * Converts a style key and a style value to a CSS compatible style key and style value. A conversion can result in
   * more than one key and value.
   *
   * @param key     The style key to convert.
   * @param value   The style value to convert.
   * @param cssAttr The current converted CSS attributes for the current element.
   * @param context The current Element.
   * @return The conversion result or null if no converstion has been done.
   */
  public ConverterAttributeSet convertToCSS(final Object key, final Object value, final ConverterAttributeSet cssAttr,
                                            final Element context)
  {
    if (key instanceof StyleConstants.ColorConstants)
    {
      final StyleConstants.ColorConstants colorConstant = (StyleConstants.ColorConstants) key;

      return handleColorConstants(colorConstant, value);
    }

    return null;
  }

  /**
   * Handles the convertions of <code>StyleConstants.ColorConstants</code> key type.
   *
   * @param colorConstant The style key.
   * @param value         The style value.
   * @return The conversion result or null if no converstion has been done.
   */
  private ConverterAttributeSet handleColorConstants(final StyleConstants.ColorConstants colorConstant,
                                                     final Object value)
  {
    final ConverterAttributeSet attr = new ConverterAttributeSet();

    if (colorConstant == StyleConstants.Foreground)
    {
      final CSSColorValue cssColorValue = new CSSColorValue((Color) value);
      attr.addAttribute(ColorStyleKeys.COLOR.getName(), cssColorValue);
    }
    else if (colorConstant == StyleConstants.Background)
    {
      final CSSColorValue cssColorValue = new CSSColorValue((Color) value);
      attr.addAttribute(BorderStyleKeys.BACKGROUND_COLOR.getName(), cssColorValue);
    }
    else
    {
      DebugLog.log("Unkown type of color attribute " + colorConstant);
      return null;
    }

    return attr;
  }
}
