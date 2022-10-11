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
 * $Id: StyleSheetUtility.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.style;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;

/**
 * Creation-Date: Dec 3, 2006, 3:29:10 PM
 *
 * @author Thomas Morgner
 */
public class StyleSheetUtility
{
  private StyleSheetUtility()
  {
  }

  public static CSSPageRule createRuleForPage(final StyleSheet style,
                                              final PageFormat format)
  {
    final CSSPageRule rule = new CSSPageRule(style, null, null, null);
    updateRuleForPage(rule, format);
    return rule;
  }

  public static void updateRuleForPage(final CSSPageRule rule,
                                       final PageFormat format)
  {
    if (format == null)
    {
      rule.removeProperty(BoxStyleKeys.MARGIN_TOP);
      rule.removeProperty(BoxStyleKeys.MARGIN_LEFT);
      rule.removeProperty(BoxStyleKeys.MARGIN_BOTTOM);
      rule.removeProperty(BoxStyleKeys.MARGIN_RIGHT);
      rule.removeProperty(PageStyleKeys.SIZE);
      rule.removeProperty(PageStyleKeys.HORIZONTAL_PAGE_SPAN);
      rule.removeProperty(PageStyleKeys.VERTICAL_PAGE_SPAN);
      return;
    }


    final double width = format.getWidth();
    final double height = format.getHeight();
    rule.setPropertyValueAsString(PageStyleKeys.SIZE,
        width + "pt " + height + "pt");
    rule.setPropertyValueAsString(BoxStyleKeys.MARGIN_TOP, format.getImageableY() + "pt");
    rule.setPropertyValueAsString(BoxStyleKeys.MARGIN_LEFT, format.getImageableX() + "pt");

    final double marginRight = width - format.getImageableX() - format.getImageableWidth();
    final double marginBottom = height - format.getImageableY() - format.getImageableHeight();
    rule.setPropertyValueAsString(BoxStyleKeys.MARGIN_BOTTOM, marginBottom + "pt");
    rule.setPropertyValueAsString(BoxStyleKeys.MARGIN_RIGHT, marginRight + "pt");
    rule.setPropertyValueAsString(PageStyleKeys.HORIZONTAL_PAGE_SPAN, "1");
    rule.setPropertyValueAsString(PageStyleKeys.VERTICAL_PAGE_SPAN, "1");
  }

  public static PageFormat getPageFormat(final CSSPageRule rule)
  {
    // This does not take any inheritance into account.
    final CSSValue sizeValue = rule.getPropertyCSSValue(PageStyleKeys.SIZE);
    if (sizeValue instanceof CSSValuePair == false)
    {
      // not a valid thing ..
      return null;
    }
    final CSSValuePair sizePair = (CSSValuePair) sizeValue;
    final CSSValue firstValue = sizePair.getFirstValue();
    final CSSValue secondValue = sizePair.getSecondValue();
    final double width = CSSValueResolverUtility.convertLengthToDouble(firstValue);
    final double height = CSSValueResolverUtility.convertLengthToDouble(secondValue);
    if (width == 0 || height == 0)
    {
      return null;
    }

    // next the margins ..
    final double marginLeft = CSSValueResolverUtility.convertLengthToDouble
        (rule.getPropertyCSSValue(BoxStyleKeys.MARGIN_LEFT));
    final double marginTop = CSSValueResolverUtility.convertLengthToDouble
        (rule.getPropertyCSSValue(BoxStyleKeys.MARGIN_TOP));
    final double marginRight = CSSValueResolverUtility.convertLengthToDouble
        (rule.getPropertyCSSValue(BoxStyleKeys.MARGIN_RIGHT));
    final double marginBottom = CSSValueResolverUtility.convertLengthToDouble
        (rule.getPropertyCSSValue(BoxStyleKeys.MARGIN_BOTTOM));

    if (width < height)
    {
      final Paper p = new Paper();
      p.setSize(width, height);
      p.setImageableArea(marginLeft, marginRight,
          width - marginLeft - marginRight,
          height - marginTop - marginBottom);
      final PageFormat pageFormat = new PageFormat();
      pageFormat.setPaper(p);
      pageFormat.setOrientation(PageFormat.PORTRAIT);
      return pageFormat;
    }
    else
    {
      final Paper p = new Paper();
      p.setSize(height, width);
      p.setImageableArea(marginLeft, marginRight,
          width - marginLeft - marginRight,
          height - marginTop - marginBottom);
      final PageFormat pageFormat = new PageFormat();
      pageFormat.setPaper(p);
      pageFormat.setOrientation(PageFormat.LANDSCAPE);
      return pageFormat;
    }

  }
}
