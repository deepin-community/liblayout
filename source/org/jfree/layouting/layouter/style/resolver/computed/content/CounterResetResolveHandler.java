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
 * $Id: CounterResetResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.resolver.computed.content;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class CounterResetResolveHandler implements ResolveHandler
{
  public CounterResetResolveHandler ()
  {
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[] {
            BoxStyleKeys.DISPLAY_ROLE
    };
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve (final LayoutProcess process,
                       final LayoutElement element,
                       final StyleKey key)
  {
    final LayoutContext layoutContext = element.getLayoutContext();
    final CSSValue displayRole = layoutContext.getValue(BoxStyleKeys.DISPLAY_ROLE);
    if (DisplayRole.NONE.equals(displayRole))
    {
      // [GENERATED] 8.3. Counters in elements with 'display: none'
      //
      // An element that is not displayed ('display' set to 'none') cannot
      // increment or reset a counter.
      return;
    }

    final CSSValue value = layoutContext.getValue(key);
    if (value instanceof CSSValueList == false)
    {
      return; // do nothing.
    }

    final CSSValueList valueList = (CSSValueList) value;
    for (int i = 0; i < valueList.getLength(); i++)
    {
      final CSSValue item = valueList.getItem(i);
      if (item instanceof CSSValuePair == false)
      {
        continue;
      }
      final CSSValuePair counter = (CSSValuePair) item;
      final CSSValue counterName = counter.getFirstValue();
      if (counterName instanceof CSSConstant == false)
      {
        continue;
      }

      final CSSValue counterValue = counter.getSecondValue();
      final int counterIntValue = parseCounterValue(counterValue, element);
      element.resetCounter(counterName.getCSSText(), counterIntValue);
    }
  }

  private int parseCounterValue (final CSSValue rawValue,
                                 final LayoutElement element)
  {

    if (rawValue instanceof CSSNumericValue)
    {
      final CSSNumericValue nval = (CSSNumericValue) rawValue;
      return (int) nval.getValue();
    }
    if (rawValue instanceof CSSAttrFunction)
    {
      final CSSAttrFunction attrFunction = (CSSAttrFunction) rawValue;
      final String attrName = attrFunction.getName();
      final String attrNamespace = attrFunction.getNamespace();
      final Object rawAttribute =
              element.getLayoutContext().getAttributes().getAttribute
                      (attrNamespace, attrName);
      if (rawAttribute instanceof Number)
      {
        final Number nAttr = (Number) rawAttribute;
        return nAttr.intValue();
      }
    }
    return 0;
  }
}
