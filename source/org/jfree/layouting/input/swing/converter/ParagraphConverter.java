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
 * $Id: ParagraphConverter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.swing.converter;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextAlign;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.swing.Converter;
import org.jfree.layouting.input.swing.ConverterAttributeSet;
import org.pentaho.reporting.libraries.base.util.DebugLog;

/**
 * This class handles convertions of paragraph style attributes to css style attributes.
 */
public class ParagraphConverter implements Converter {
  public ParagraphConverter()
  {
  }

  /**
   * Converts a style key and a style value to a CSS compatible style key and style value.
   * A conversion can result in more than one key and value.
   *
   * @param key The style key to convert.
   * @param value The style value to convert.
   * @param cssAttr The current converted CSS attributes for the current element.
   * @param context The current Element.
   * @return The conversion result or null if no converstion has been done.
   */
  public ConverterAttributeSet convertToCSS (final Object key, final Object value, final ConverterAttributeSet cssAttr,
                                    final Element context)
  {

    if(key instanceof StyleConstants.ParagraphConstants)
    {
      final StyleConstants.ParagraphConstants paragraphConstant = (StyleConstants.ParagraphConstants)key;

      return handleParagraphConstants(paragraphConstant, value);

    }

    return null;
  }

  /**
   * Handles the convertions of <code>StyleConstants.ParagraphConstants</code> key type.
   *
   * @param paragraphConstant The style key.
   * @param value The style value.
   * @return The conversion result or null if no converstion has been done.
   */
  private ConverterAttributeSet handleParagraphConstants (
          final StyleConstants.ParagraphConstants paragraphConstant, final Object value)
  {
    final ConverterAttributeSet attr = new ConverterAttributeSet();

    if(paragraphConstant == StyleConstants.FirstLineIndent)
    {
      final CSSNumericValue cssNumericValue = CSSNumericValue.createValue(CSSNumericType.PT, Double.parseDouble(value.toString()));
      attr.addAttribute(TextStyleKeys.TEXT_INDENT.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.RightIndent)
    {
      final CSSNumericValue cssNumericValue = CSSNumericValue.createValue(CSSNumericType.PT, Double.parseDouble(value.toString()));
      attr.addAttribute(BoxStyleKeys.MARGIN_RIGHT.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.LeftIndent)
    {
      final CSSNumericValue cssNumericValue = CSSNumericValue.createValue(CSSNumericType.PT, Double.parseDouble(value.toString()));
      attr.addAttribute(BoxStyleKeys.MARGIN_LEFT.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.LineSpacing)
    {
      final CSSNumericValue cssNumericValue = CSSNumericValue.createValue(CSSNumericType.EM, Double.parseDouble(value.toString()));
      attr.addAttribute(LineStyleKeys.LINE_HEIGHT, cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.SpaceAbove)
    {
      final CSSNumericValue cssNumericValue = CSSNumericValue.createValue(CSSNumericType.PT, Double.parseDouble(value.toString()));
      attr.addAttribute(BoxStyleKeys.MARGIN_TOP.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.SpaceBelow)
    {
      final CSSNumericValue cssNumericValue = CSSNumericValue.createValue(CSSNumericType.PT, Double.parseDouble(value.toString()));
      attr.addAttribute(BoxStyleKeys.MARGIN_BOTTOM.getName(), cssNumericValue);
    }
    else if(paragraphConstant == StyleConstants.Alignment)
    {
      Object val = null;
      final int interger = Integer.parseInt(value.toString());
      if(interger == StyleConstants.ALIGN_CENTER)
      {
        val = TextAlign.CENTER;
      }
      else if(interger == StyleConstants.ALIGN_JUSTIFIED)
      {
        val = TextAlign.JUSTIFY;
      }
      else if(interger == StyleConstants.ALIGN_LEFT)
      {
        val = TextAlign.LEFT;
      }
      else if(interger == StyleConstants.ALIGN_RIGHT)
      {
        val = TextAlign.RIGHT;
      }

      attr.addAttribute(TextStyleKeys.TEXT_ALIGN.getName(), val);
    }
    else
    {
      // StyleConstants.TabSet @see TabSet class
      DebugLog.log("Unkown type of paragraphe attribute: " + paragraphConstant);
      return null;
    }

    return attr;
  }
}
