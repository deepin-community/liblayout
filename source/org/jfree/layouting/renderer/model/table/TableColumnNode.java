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
 * $Id: TableColumnNode.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.table;

import org.jfree.layouting.input.style.keys.table.TableStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.renderer.model.BoxDefinition;
import org.jfree.layouting.renderer.model.SpacerRenderNode;

/**
 * A table column defines a limited set of style properties, which may be
 * applied to the cells.
 * <p/>
 * Border, if the border-model is the collapsing border model. Background, if
 * both cell and row have a transparent background Width, is a minimum width. If
 * the cell exceeds that size, the table cannot be rendered in incremental mode
 * anymore. We may have to use the validation run to check for that rule.
 * Visiblity, if set to collapse, the column will not be rendered. Not yet.
 *
 * @author Thomas Morgner
 */
public class TableColumnNode extends SpacerRenderNode
{
  private BoxDefinition definition;
  private int colspan;

  public TableColumnNode(final BoxDefinition definition)
  {
    super(0, 0, true);
    this.definition = definition;
    this.colspan = 1;
  }

  public TableColumnNode(final BoxDefinition definition,
                         final LayoutContext context)
  {
    super(0, 0, true);
    this.definition = definition;

    final CSSValue value = context.getValue(TableStyleKeys.COL_SPAN);
    this.colspan = (int) CSSValueResolverUtility.getNumericValue(value, 1);
  }

  public int getColspan()
  {
    return colspan;
  }

  public BoxDefinition getBoxDefinition()
  {
    return definition;
  }
}
