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
 * $Id: FontFamilyResolveHandler.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.fonts;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.pentaho.reporting.libraries.base.util.DebugLog;
import org.pentaho.reporting.libraries.fonts.registry.FontFamily;


/**
 * Creation-Date: 18.12.2005, 16:35:28
 *
 * @author Thomas Morgner
 */
public class FontFamilyResolveHandler extends ConstantsResolveHandler
{
  public FontFamilyResolveHandler()
  {
    addNormalizeValue(FontFamilyValues.CURSIVE);
    addNormalizeValue(FontFamilyValues.FANTASY);
    addNormalizeValue(FontFamilyValues.MONOSPACE);
    addNormalizeValue(FontFamilyValues.SANS_SERIF);
    addNormalizeValue(FontFamilyValues.SERIF);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[] {
            FontStyleKeys.FONT_WEIGHT, FontStyleKeys.FONT_VARIANT,
            FontStyleKeys.FONT_SMOOTH, FontStyleKeys.FONT_STRETCH
    };
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve(final LayoutProcess process,
                         final LayoutElement currentNode,
                         final StyleKey key)
  {
    //Log.debug ("Processing: " + currentNode);
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final FontSpecification fs =
            layoutContext.getFontSpecification();
    final OutputProcessorMetaData outputMetaData = process.getOutputMetaData();
    final CSSValue cssValue = layoutContext.getValue(key);
    if (cssValue instanceof CSSValueList)
    {

      final CSSValueList list = (CSSValueList) cssValue;
      for (int i = 0; i < list.getLength(); i++)
      {
        final CSSValue item = list.getItem(i);
        if (item instanceof CSSConstant)
        {
          final CSSConstant c = (CSSConstant) lookupValue((CSSConstant) item);
          final FontFamily family = outputMetaData.getFontFamilyForGenericName(c);
          fs.setFontFamily(family.getFamilyName());
          if (process.getOutputMetaData().isValid(fs))
          {
            return;
          }
          // Ignore, although this is not ok.
          DebugLog.log ("Invalid state after setting predefined font family.");
        }
        else if (item instanceof CSSStringValue)
        {
          final CSSStringValue sval = (CSSStringValue) item;
          final String fontName = sval.getValue();
          fs.setFontFamily(fontName);
          if (process.getOutputMetaData().isValid(fs))
          {
            return;
          }
        }
      }
    }
    else if (cssValue instanceof CSSConstant)
    {
      if (FontFamilyValues.NONE.equals(cssValue))
      {
        fs.setFontFamily(null);
        return;
      }
    }

    final FontFamily family = outputMetaData.getDefaultFontFamily();
    fs.setFontFamily(family.getFamilyName());
    if (process.getOutputMetaData().isValid(fs) == false)
    {
      throw new IllegalStateException
              ("Invalid state after setting the default font family.");
    }
  }
}
