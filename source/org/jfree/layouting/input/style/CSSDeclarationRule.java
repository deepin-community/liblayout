/**
 * =============================================
 * LibCSS: A free document archive library
 * =============================================
 *
 * Project Info:  http://reporting.pentaho.org/libcss/
 *
 * (C) Copyright 2007,2008, by Pentaho Corporation and Contributors.
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
 * CSSDeclarationRule.java
 * ------------
 */

package org.jfree.layouting.input.style;

import java.util.Arrays;

import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.jfree.layouting.input.style.parser.StyleSheetParserUtil;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * This class is a merger between the CSSStyleDeclaration and the other
 * stylerule classes holding property name pairs. Actually, this is what once
 * was called a stylesheet in JFreeReport.
 * <p/>
 * StyleProperties are key as Strings and have CSSValues as mapped values..
 *
 * @author Thomas Morgner
 */
public abstract class CSSDeclarationRule extends StyleRule
{
  private CSSValue[] styleValues;
  private boolean[] importantValues;
  private StyleSheetParserUtil styleSheetParserUtil;

  protected CSSDeclarationRule(final StyleSheet parentStyle,
                               final StyleRule parentRule)
  {
    super(parentStyle, parentRule);
  }

  public boolean isImportant(StyleKey propertyName)
  {
    if (propertyName == null)
    {
      throw new NullPointerException();
    }

    if (importantValues == null)
    {
      return false;
    }
    return importantValues[propertyName.index];
  }

  public void setImportant(final StyleKey propertyName, final boolean important)
  {
    if (propertyName == null)
    {
      throw new NullPointerException();
    }

    if (importantValues == null)
    {
      final StyleKeyRegistry styleKeyRegistry = getStyleKeyRegistry();
      importantValues = new boolean[styleKeyRegistry.getKeyCount()];
    }

    importantValues[propertyName.index] = important;
  }

  public CSSValue getPropertyCSSValue(StyleKey propertyName)
  {
    if (propertyName == null)
    {
      throw new NullPointerException();
    }
    if (styleValues == null)
    {
      return null;
    }
    return styleValues[propertyName.index];
  }

  public void setPropertyValueAsString(final String styleKey,
                                       final String value)
  {
    final StyleSheet parentStyle = getParentStyle();
    final ResourceKey source;
    if (parentStyle == null)
    {
      source = null;
    }
    else
    {
      source = parentStyle.getSource();
    }

    if (styleSheetParserUtil == null)
    {
      styleSheetParserUtil = new StyleSheetParserUtil();
    }

    final StyleSheet parent = getParentStyle();
    final CSSStyleRule cssValues;
    if (parent != null)
    {
      cssValues = styleSheetParserUtil.parseStyles
          (parent.getNamespaces(), styleKey, value, parent.getResourceManager(), source, this);
    }
    else
    {
      final ResourceManager resourceManager = new ResourceManager();
      resourceManager.registerDefaults();

      cssValues = styleSheetParserUtil.parseStyles
          (null, styleKey, value, resourceManager, source, this);
    }

    if (cssValues != null)
    {
      if (cssValues.isEmpty())
      {
        return;
      }
      final boolean[] importantFlags = cssValues.getImportantValues();
      final CSSValue[] values = cssValues.getStyleValues();
      final StyleKey[] keys = cssValues.getPropertyKeysAsArray();
      for (int i = 0; i < values.length; i++)
      {
        final CSSValue cssValue = values[i];
        if (cssValue != null)
        {
          setPropertyValue(keys[i], cssValue, importantFlags[i]);
        }
      }
    }
  }

  /**
   * Parses the given value for the stylekey. As stylekeys are only defined for
   * atomic style declarations, this method will only affect a single name-value
   * pair.
   *
   * @param styleKey
   * @param value
   */
  public void setPropertyValueAsString(final StyleKey styleKey,
                                       final String value)
  {
    setPropertyValueAsString(styleKey.getName(), value);
  }

  public void setPropertyValue(StyleKey propertyName, CSSValue value)
  {
    setPropertyValue(propertyName, value, false);
  }

  public void setPropertyValue(StyleKey propertyName, CSSValue value, boolean important)
  {
    if (styleValues == null)
    {
      final StyleKeyRegistry styleKeyRegistry = getStyleKeyRegistry();
      styleValues = new CSSValue[styleKeyRegistry.getKeyCount()];
    }

    styleValues[propertyName.index] = value;
    setImportant(propertyName, important);
  }

  public void removeProperty(StyleKey name)
  {
    if (styleValues == null)
    {
      return;
    }

    setPropertyValue(name, null);
  }

  public void clear()
  {
    if (styleValues != null)
    {
      Arrays.fill(styleValues, null);
    }
    if (importantValues != null)
    {
      Arrays.fill(importantValues, false);
    }
  }

  public StyleKey[] getPropertyKeysAsArray()
  {
    return getStyleKeyRegistry().getKeys();
  }

  public CSSValue[] getStyleValues()
  {
    if (styleValues == null)
    {
      final StyleKeyRegistry styleKeyRegistry = getStyleKeyRegistry();
      styleValues = new CSSValue[styleKeyRegistry.getKeyCount()];
    }
    return (CSSValue[]) styleValues.clone();
  }

  public boolean[] getImportantValues()
  {
    if (importantValues == null)
    {
      importantValues = new boolean[getStyleKeyRegistry().getKeyCount()];
    }
    return (boolean[]) importantValues.clone();
  }

  public Object clone() throws CloneNotSupportedException
  {
    final CSSDeclarationRule rule = (CSSDeclarationRule) super.clone();
    if (importantValues != null)
    {
      rule.importantValues = (boolean[]) importantValues.clone();
    }
    if (styleValues != null)
    {
      rule.styleValues = (CSSValue[]) styleValues.clone();
    }
    return rule;
  }

  public boolean isEmpty()
  {
    return importantValues == null && styleValues == null;
  }
}
