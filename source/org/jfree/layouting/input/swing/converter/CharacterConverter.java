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
 * $Id: CharacterConverter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.swing.converter;

import java.util.StringTokenizer;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.line.VerticalAlign;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.UnicodeBidi;
import org.jfree.layouting.input.style.keys.text.TextTransform;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.font.FontVariant;
import org.jfree.layouting.input.style.keys.font.FontEffects;
import org.jfree.layouting.input.swing.Converter;
import org.jfree.layouting.input.swing.ConverterAttributeSet;
import org.pentaho.reporting.libraries.base.util.DebugLog;

/**
 * This class handles convertions of character style attributes to css style attributes.
 */
public class CharacterConverter implements Converter {
  /**
   * CSS text decoration key.
   */
  public static final String TEXT_DECORATION_KEY = "text-decoration";
  //none | [ underline || overline || line-through || blink]
  /**
   * CSS text decoration value.
   */
  public static final String NONE_TEXT_DECORATION = "none";
  /**
   * CSS text decoration value.
   */
  public static final String UNDERLINE_TEXT_DECORATION = "underline";
  /**
   * CSS text decoration value.
   */
  public static final String LINETHROUGH_TEXT_DECORATION = "line-through";

  // todo : move these keys to FontConverter?
  public static final String RTF_CAPS = "caps";
  public static final String RTF_SMALLCAPS = "scaps";
  public static final String RTF_OUTLINE = "outl";

  public static final String RTF_SHADOW = "shad";
  public static final String RTF_Hidden = "v";
  public static final String RTF_STRIKETRHOUGH = "strike";
  public static final String RTF_DELETED = "deleted";

  public CharacterConverter()
  {
  }

  // todo : remove the NONE_TEXT_DECORATION
  private Object mergeTextDecorationValues(final Object current, final Object newone) {

    if(newone == null)
    {
      return current;
    }

    String str = (String) current;
    str = str+ ' ' +newone;
    final StringTokenizer tokenizer = new StringTokenizer(str);
    boolean none = false;
    boolean strikethrough = false;
    boolean underline = false;
    while(tokenizer.hasMoreTokens())
    {
      final String s = tokenizer.nextToken().trim();
      if(NONE_TEXT_DECORATION.equals(s))
      {
        none = true;
      }
      else if(UNDERLINE_TEXT_DECORATION.equals(s))
      {
        underline = true;
      }
      else if(LINETHROUGH_TEXT_DECORATION.equals(s))
      {
        strikethrough = true;
      }
    }

    if(underline && strikethrough)
    {
      return UNDERLINE_TEXT_DECORATION + ' ' + LINETHROUGH_TEXT_DECORATION;
    }
    if(underline)
    {
      return UNDERLINE_TEXT_DECORATION;
    }
    if(strikethrough)
    {
      return LINETHROUGH_TEXT_DECORATION;
    }

    return NONE_TEXT_DECORATION;
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
  public ConverterAttributeSet convertToCSS (final Object key, final Object value, final ConverterAttributeSet cssAttr, final Element context)
  {
    if(key instanceof StyleConstants.CharacterConstants)
    {
      final StyleConstants.CharacterConstants characterConstant = (StyleConstants.CharacterConstants) key;

      return handleCharacterConstants(characterConstant, value, cssAttr);
    }
    else if(key instanceof String)
    {
      final String constant = (String)key;
      final ConverterAttributeSet attr = new ConverterAttributeSet();
      if(constant.equals(RTF_SMALLCAPS))
      {
        final Boolean b = (Boolean)value;
        if(Boolean.TRUE.equals(b))
        {
          attr.addAttribute(FontStyleKeys.FONT_VARIANT.getName(), FontVariant.SMALL_CAPS);
        }
      }
      else if(constant.equals(RTF_CAPS))
      {
        final Boolean b = (Boolean)value;
        if(Boolean.TRUE.equals(b))
        {
          attr.addAttribute(TextStyleKeys.TEXT_TRANSFORM.getName(), TextTransform.CAPITALIZE);
        }
      }
      else if(constant.equals(RTF_OUTLINE))
      {
        final Boolean b = (Boolean)value;
        if(Boolean.TRUE.equals(b))
        {
          attr.addAttribute(FontStyleKeys.FONT_EFFECT, FontEffects.OUTLINE);
        }
      }
      else if(constant.equals(RTF_STRIKETRHOUGH))
      {
        final Object current = cssAttr.getAttribute(TEXT_DECORATION_KEY);
        attr.addAttribute(TEXT_DECORATION_KEY, mergeTextDecorationValues(current, LINETHROUGH_TEXT_DECORATION));
      }
      else
      {
        DebugLog.log("Unkown type of character attribute" + constant);
        return null;
      }
      return attr;
    }

    return null;
  }

  /**
   * Handles the convertions of <code>StyleConstants.CharacterConstants</code> key type.
   *
   * @param characterConstant The style key.
   * @param value The style value.
   * @param cssAttr The current converted CSS attributes.
   * @return The conversion result or null if no converstion has been done.
   */
  private ConverterAttributeSet handleCharacterConstants (
          final StyleConstants.CharacterConstants characterConstant, final Object value,
          final ConverterAttributeSet cssAttr)
  {
    final ConverterAttributeSet attr = new ConverterAttributeSet();

    if(characterConstant == StyleConstants.Underline)
    {
      final Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        final Object current = cssAttr.getAttribute(TEXT_DECORATION_KEY);
        attr.addAttribute(TEXT_DECORATION_KEY, mergeTextDecorationValues(current, UNDERLINE_TEXT_DECORATION));
      }
      else
      {
        attr.addAttribute(TEXT_DECORATION_KEY, NONE_TEXT_DECORATION);
      }
    }
    else if(characterConstant == StyleConstants.StrikeThrough)
    {
      final Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        final Object current = cssAttr.getAttribute(TEXT_DECORATION_KEY);
        attr.addAttribute(TEXT_DECORATION_KEY, mergeTextDecorationValues(current, LINETHROUGH_TEXT_DECORATION));
      }
      else
      {
        attr.addAttribute(TEXT_DECORATION_KEY, NONE_TEXT_DECORATION);
      }
    }
    else if(characterConstant == StyleConstants.Superscript)
    {
      final Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        attr.addAttribute(LineStyleKeys.VERTICAL_ALIGN.getName(), VerticalAlign.SUPER);
      }
    }
    else if(characterConstant == StyleConstants.Subscript)
    {
      final Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        attr.addAttribute(LineStyleKeys.VERTICAL_ALIGN.getName(), VerticalAlign.SUB);
      }
    }
    else if(characterConstant == StyleConstants.BidiLevel)
    {
      final Boolean b = (Boolean)value;
      if(Boolean.TRUE.equals(b))
      {
        attr.addAttribute(TextStyleKeys.UNICODE_BIDI.getName(), UnicodeBidi.EMBED);
      }
      else
      {
        attr.addAttribute(TextStyleKeys.UNICODE_BIDI.getName(), UnicodeBidi.NORMAL);
      }
    }
    else
    {
      DebugLog.log("Unkown type of character attribute" + characterConstant);
      return null;
    }

    return cssAttr;
  }
}
