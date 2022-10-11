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
 * $Id: PositionResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.resolver.computed.position;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.box.Floating;
import org.jfree.layouting.input.style.keys.positioning.Position;
import org.jfree.layouting.input.style.keys.positioning.PositioningStyleKeys;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class PositionResolveHandler extends ConstantsResolveHandler
{
  public PositionResolveHandler ()
  {
    addNormalizeValue(Position.ABSOLUTE);
    addNormalizeValue(Position.FIXED);
    addNormalizeValue(Position.RELATIVE);
    addNormalizeValue(Position.STATIC);
    setFallback(Position.STATIC);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more dependent
   * is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles ()
  {
    return new StyleKey[]{
            BoxStyleKeys.DISPLAY_MODEL,
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
    final CSSValue displayModel = layoutContext.getValue(BoxStyleKeys.DISPLAY_MODEL);
    if (DisplayRole.NONE.equals(displayModel))
    {
      // skip ... the element will not be displayed ...
      layoutContext.setValue(PositioningStyleKeys.POSITION, Position.STATIC);
      return;
    }

    final CSSValue rawValue = layoutContext.getValue(key);
    if (rawValue instanceof CSSFunctionValue)
    {
      // OK; check for pending ..
      final CSSFunctionValue function = (CSSFunctionValue) rawValue;
      if ("running".equals(function.getFunctionName()))
      {
        // The element will be inside a block-context (same behaviour as
        // for floats)
        layoutContext.setValue(BoxStyleKeys.DISPLAY_MODEL, DisplayModel.BLOCK_INSIDE);
        layoutContext.setValue(BoxStyleKeys.DISPLAY_ROLE, DisplayRole.BLOCK);
        return;
      }
      layoutContext.setValue(PositioningStyleKeys.POSITION, Position.STATIC);
      return;
    }

    final CSSConstant value = (CSSConstant) resolveValue(process, currentNode, key);
    layoutContext.setValue(PositioningStyleKeys.POSITION, value);
    if (Position.ABSOLUTE.equals(value) ||
        Position.FIXED.equals(value))
    {
      // http://www.w3.org/TR/REC-CSS2/visuren.html#propdef-float
      // this is specified in 9.7: Relationships between 'display',
      // 'position', and 'float':

      // Quote: Otherwise, 'position' has the value 'absolute' or 'fixed',
      // 'display' is set to 'block' and 'float' is set to 'none'. The position
      // of the box will be determined by the 'top', 'right', 'bottom' and
      // 'left' properties and the box's containing block.
      layoutContext.setValue(BoxStyleKeys.DISPLAY_MODEL, DisplayModel.BLOCK_INSIDE);
      layoutContext.setValue(BoxStyleKeys.DISPLAY_ROLE, DisplayRole.BLOCK);
      layoutContext.setValue(BoxStyleKeys.FLOAT, Floating.NONE);
    }
  }
}
