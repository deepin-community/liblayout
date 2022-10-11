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
 * $Id: CSSStyleRule.java 6501 2008-11-28 17:55:53Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style;

import java.util.Iterator;

import org.jfree.layouting.input.style.selectors.CSSSelector;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 23.11.2005, 10:59:26
 *
 * @author Thomas Morgner
 */
public class CSSStyleRule extends CSSDeclarationRule
{
  private CSSSelector selector;

  public CSSStyleRule(final StyleSheet parentStyle,
                      final StyleRule parentRule)
  {
    super(parentStyle, parentRule);
  }

  public CSSSelector getSelector()
  {
    return selector;
  }

  public void setSelector(final CSSSelector selector)
  {
    if (isReadOnly())
    {
      throw new UnmodifiableStyleSheetException();
    }
    this.selector = selector;
  }

  public void merge(final CSSStyleRule elementRule)
  {
    if (elementRule.isEmpty())
    {
      return;
    }

    final boolean[] importantFlags = elementRule.getImportantValues();
    final CSSValue[] values = elementRule.getStyleValues();
    final StyleKey[] keys = elementRule.getPropertyKeysAsArray();
    for (int i = 0; i < values.length; i++)
    {
      final CSSValue cssValue = values[i];
      if (cssValue != null)
      {
        final StyleKey propertyName = keys[i];
        setPropertyValue(propertyName, cssValue, importantFlags[i] && isImportant(propertyName));
      }
    }
  }
}
