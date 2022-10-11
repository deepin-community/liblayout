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
 * $Id: FloatResolveHandler.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.resolver.computed.box;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayModel;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.box.Floating;
import org.jfree.layouting.input.style.keys.positioning.PositioningStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.resolver.computed.ConstantsResolveHandler;

public class FloatResolveHandler extends ConstantsResolveHandler
{
  public FloatResolveHandler()
  {
    addNormalizeValue(Floating.BOTTOM);
    addNormalizeValue(Floating.LEFT);
    addNormalizeValue(Floating.END);
    addNormalizeValue(Floating.INSIDE);
    addNormalizeValue(Floating.IN_COLUMN);
    addNormalizeValue(Floating.MID_COLUMN);
    addNormalizeValue(Floating.NONE);
    addNormalizeValue(Floating.OUTSIDE);
    addNormalizeValue(Floating.RIGHT);
    addNormalizeValue(Floating.START);
    addNormalizeValue(Floating.TOP);
    setFallback(Floating.NONE);
  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[]{
        BoxStyleKeys.DISPLAY_ROLE,
        PositioningStyleKeys.POSITION
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
    final LayoutContext layoutContext = currentNode.getLayoutContext();
    final CSSValue displayRole = layoutContext.getValue(BoxStyleKeys.DISPLAY_ROLE);
    final CSSValue floating;
    if (DisplayRole.NONE.equals(displayRole))
    {
      floating = Floating.NONE;
    }
    else
    {
      floating = resolveValue(process, currentNode, key);
    }

    if (Floating.NONE.equals(floating) == false)
    {
      //  Otherwise, if 'float' has a value other than 'none', 'display'
      // is set to 'block' and the box is floated.
      layoutContext.setValue(BoxStyleKeys.DISPLAY_MODEL, DisplayModel.BLOCK_INSIDE);
      layoutContext.setValue(BoxStyleKeys.DISPLAY_ROLE, DisplayRole.BLOCK);
    }

    layoutContext.setValue(key, Floating.NONE);
  }
}
