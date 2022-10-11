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
 * $Id: FitPositionResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.resolver.autovalue.box;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.text.BlockProgression;
import org.jfree.layouting.input.style.keys.text.Direction;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;

public class FitPositionResolveHandler implements ResolveHandler
{
  private static final CSSNumericValue LEFT_TOP = CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, 0);
  private static final CSSNumericValue RIGHT_BOTTOM = CSSNumericValue.createValue(CSSNumericType.PERCENTAGE, 100);

  public FitPositionResolveHandler ()
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
            TextStyleKeys.BLOCK_PROGRESSION,
            TextStyleKeys.DIRECTION
    };
  }

  /**
   * Resolves a single property.
   *
   * @param currentNode
   * @param style
   */
  public void resolve (final LayoutProcess process,
                       final LayoutElement currentNode,
                       final StyleKey key)
  {
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final boolean rightToLeft = Direction.RTL.equals
        (layoutContext.getValue(TextStyleKeys.DIRECTION));
    final CSSValue blockProgression = layoutContext.getValue(TextStyleKeys.BLOCK_PROGRESSION);
    // this might be invalid ...
    if (BlockProgression.TB.equals(blockProgression))
    {
      if (rightToLeft)
      {
        layoutContext.setValue(key, new CSSValuePair(RIGHT_BOTTOM, LEFT_TOP));
      }
      else
      {
        layoutContext.setValue(key, new CSSValuePair(LEFT_TOP, LEFT_TOP));
      }
    }
    else if (BlockProgression.RL.equals(blockProgression))
    {
      if (rightToLeft)
      {
        layoutContext.setValue(key, new CSSValuePair(LEFT_TOP, LEFT_TOP));
      }
      else
      {
        layoutContext.setValue(key, new CSSValuePair(RIGHT_BOTTOM, LEFT_TOP));
      }
    }
    else if (BlockProgression.LR.equals(blockProgression))
    {
      if (rightToLeft)
      {
        layoutContext.setValue(key, new CSSValuePair(RIGHT_BOTTOM, RIGHT_BOTTOM));
      }
      else
      {
        layoutContext.setValue(key, new CSSValuePair(LEFT_TOP, LEFT_TOP));
      }
    }
  }
}
