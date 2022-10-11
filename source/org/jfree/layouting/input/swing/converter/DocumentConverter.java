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
 * $Id: DocumentConverter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.swing.converter;

import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.swing.Converter;
import org.jfree.layouting.input.swing.ConverterAttributeSet;
import org.pentaho.reporting.libraries.base.util.DebugLog;

import javax.swing.text.Element;

/**
 * This class handles convertions of document properties to css style attributes.
 */
public class DocumentConverter implements Converter
{
  public static final String PAGE_RULE_TYPE = "@page";

  public static final String RTF_PAGEWIDTH = "paperw";
  public static final String RTF_PAGEHEIGHT = "paperh";
  public static final String RTF_MARGINLEFT = "margl";
  public static final String RTF_MARGINRIGHT = "margr";
  public static final String RTF_MARGINTOP = "margt";
  public static final String RTF_MARGINBOTTOM = "margb";
  // Binding Edge treatment @ CSS 3
  // The binding edge is the edge of the page box that is towards the binding if the material is bound. The binding edge
  // often has a larger margin than the opposite edge to provide for the space used by the binding. The binding edge
  // can be any of the four edges. However, page sheets are customarily bound so that the binding edge of page boxes
  // with portrait orientation is vertical. This module provides no method to specify the binding edge. In duplex
  // printing, the binding edge is on opposite sides of the page box for the left and right pages.
  /**
   * The inner margin of a page (near the spine) of a book.
   */
  public static final String RTF_GUTTERWIDTH = "gutter";
  /**
   * Page orientation in which the page width exceeds the page length. The opposit is portrait.
   */
  public static final String RTF_LANDSCAPE = "landscape";

  public DocumentConverter()
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
  public ConverterAttributeSet convertToCSS(final Object key, final Object value, final ConverterAttributeSet cssAttr, final Element context)
  {
    if (key instanceof String)
    {
      return handleStringAttributes(key, value, cssAttr);
    }

    return null;
  }

  /**
   * Handles the convertions of <code>String</code> key type.
   *
   * @param key The style key.
   * @param value The style value.
   * @param cssAttr The current converted CSS attributes.
   * @return The conversion result or null if no converstion has been done.
   */
  private ConverterAttributeSet handleStringAttributes(final Object key, final Object value, final ConverterAttributeSet cssAttr)
  {
    final ConverterAttributeSet attr = new ConverterAttributeSet();

    final String styleKey = (String) key;

    if (styleKey.equals(RTF_PAGEWIDTH) || styleKey.equals(RTF_PAGEHEIGHT))
    {
      final float floatValue = ((Float) value).floatValue();
      final Object size = cssAttr.getAttribute(PageStyleKeys.SIZE.getName());
      double width = 0;
      double height = 0;

      if(size instanceof PageSize)
      {
        final PageSize pageSize = (PageSize)size;
        width = pageSize.getWidth();
        height = pageSize.getHeight();
      }
      else if(RTF_LANDSCAPE.equals(size))
      {
        if (styleKey.equals(RTF_PAGEWIDTH))
        {
          height = twipToInt(floatValue);
        }
        else
        {
          width = twipToInt(floatValue);
        }
      }
      else
      {
        if (styleKey.equals(RTF_PAGEWIDTH))
        {
          width = twipToInt(floatValue);
        }
        else
        {
          height = twipToInt(floatValue);
        }
      }

      attr.addAttribute(PAGE_RULE_TYPE, PageStyleKeys.SIZE.getName(), new PageSize(width, height));
    }
    else if(styleKey.equals(RTF_MARGINLEFT))
    {
      final float floatValue = ((Float) value).floatValue();
      attr.addAttribute(PAGE_RULE_TYPE, BoxStyleKeys.MARGIN_LEFT.getName(), CSSNumericValue.createValue(CSSNumericType.PT, floatValue));
    }
    else if(styleKey.equals(RTF_MARGINRIGHT))
    {
      final float floatValue = ((Float) value).floatValue();
      attr.addAttribute(PAGE_RULE_TYPE, BoxStyleKeys.MARGIN_RIGHT.getName(), CSSNumericValue.createValue(CSSNumericType.PT, floatValue));
    }
    else if(styleKey.equals(RTF_MARGINTOP))
    {
      final float floatValue = ((Float) value).floatValue();
      attr.addAttribute(PAGE_RULE_TYPE, BoxStyleKeys.MARGIN_TOP.getName(), CSSNumericValue.createValue(CSSNumericType.PT, floatValue));
    }
    else if(styleKey.equals(RTF_MARGINBOTTOM))
    {
      final float floatValue = ((Float) value).floatValue();
      attr.addAttribute(PAGE_RULE_TYPE, BoxStyleKeys.MARGIN_BOTTOM.getName(), CSSNumericValue.createValue(CSSNumericType.PT, floatValue));
    }
    else if(styleKey.equals(RTF_LANDSCAPE))
    {
      final Object size = cssAttr.getAttribute(PageStyleKeys.SIZE.getName());
      if(size instanceof PageSize)
      {
        final PageSize pageSize = (PageSize)size;
        attr.addAttribute(PAGE_RULE_TYPE, PageStyleKeys.SIZE.getName(), new PageSize(pageSize.getHeight(), pageSize.getWidth()));
      }
      else
      {
        attr.addAttribute(PAGE_RULE_TYPE, PageStyleKeys.SIZE.getName(), RTF_LANDSCAPE);
      }
    }
    else
    {
      DebugLog.log("Unkown type of document attribute: " + styleKey);
      return null;
    }

    return attr;
  }


  /**
   * Measurements in RTF are in twips. A twip is 1/20 point.
   *
   * @param twips The measurement in twips.
   * @return The measurement in points.
   */
  private float twipToInt(final float twips)
  {
    return  twips * 20;
  }
}
