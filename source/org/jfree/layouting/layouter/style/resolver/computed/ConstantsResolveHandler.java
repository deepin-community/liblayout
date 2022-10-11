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
 * $Id: ConstantsResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed;

import java.util.HashMap;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 11.12.2005, 23:15:57
 *
 * @author Thomas Morgner
 */
public abstract class ConstantsResolveHandler implements ResolveHandler
{
  private static final StyleKey[] EMPTY_STYLE_KEYS = new StyleKey[0];

  private HashMap constants;
  private CSSValue fallback;

  protected ConstantsResolveHandler()
  {
    constants = new HashMap();
  }

  public CSSValue getFallback()
  {
    return fallback;
  }

  protected void setFallback(final CSSValue fallback)
  {
    this.fallback = fallback;
  }

  protected CSSValue lookupValue(final CSSConstant value)
  {
    return (CSSValue) constants.get(value);
  }

  protected void addValue(final CSSConstant constant, final CSSValue value)
  {
    constants.put(constant, value);
  }

  protected void addNormalizeValue(final CSSConstant constant)
  {
    constants.put(constant, constant);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return  the array of required style keys.
   */
  public StyleKey[] getRequiredStyles()
  {
    return EMPTY_STYLE_KEYS;
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

    final CSSValue value = resolveValue(process, currentNode, key);
    if (value != null)
    {
      currentNode.getLayoutContext().setValue(key, value);
    }
  }

  protected CSSValue resolveValue (final LayoutProcess process,
                                   final LayoutElement currentNode,
                                   final StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final CSSValue value = layoutContext.getValue(key);
    if (value instanceof CSSConstant == false)
    {
      final CSSValue fallback = getFallback();
      if (fallback != null)
      {
        return fallback;
      }
      return null;
    }

    final CSSConstant constant = (CSSConstant) value;
    final CSSValue resolvedValue = lookupValue(constant);
    if (resolvedValue != null)
    {
//      layoutContext.setValue(key, resolvedValue);
      return resolvedValue;
    }

    final CSSValue fallback = getFallback();
    if (fallback != null)
    {
//      layoutContext.setValue(key, fallback);
      return fallback;
    }

    return null;
  }
}
