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
 * $Id: PageSizeResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver.computed.page;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.keys.page.PageSizeFactory;
import org.jfree.layouting.input.style.keys.page.PageStyleKeys;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

/**
 * Creation-Date: 16.06.2006, 13:56:31
 *
 * @author Thomas Morgner
 */
public class PageSizeResolveHandler implements ResolveHandler
{
  public PageSizeResolveHandler()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[0];
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
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final CSSValue value = layoutContext.getValue(PageStyleKeys.SIZE);

    String name = null;
    if (value instanceof CSSStringValue)
    {
      final CSSStringValue sval = (CSSStringValue) value;
      name = sval.getValue();
    }
    else if (value instanceof CSSConstant)
    {
      name = value.toString();
    }

    PageSize ps = null;
    if (name != null)
    {
      ps = PageSizeFactory.getInstance().getPageSizeByName(name);
    }

    if (ps == null)
    {
      ps = process.getOutputMetaData().getDefaultPageSize();
    }
    // if it is stll null, then the output target is not valid.
    // We will crash in that case ..
    final CSSValue page =
        new CSSValuePair(CSSNumericValue.createPtValue(ps.getWidth()),
            CSSNumericValue.createPtValue(ps.getHeight()));
    layoutContext.setValue(PageStyleKeys.SIZE, page);
  }
}
