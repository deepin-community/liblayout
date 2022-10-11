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
 * $Id: FlowContext.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.normalizer.displaymodel;

import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.util.IntList;
import org.pentaho.reporting.libraries.base.util.FastStack;

/**
 * Creation-Date: Jan 5, 2007, 4:30:53 PM
 *
 * @author Thomas Morgner
 */
public class FlowContext implements Cloneable
{
  public static final int STATE_OPEN = 0;
  public static final int STATE_SUSPEND = 1;
  public static final int STATE_CLOSE = 2;

  private IntList displayModels;
  private IntList displayRoles;
  private IntList elementState;
  private FastStack layoutContexts;
  private int activeContext;

  public FlowContext()
  {
    displayModels = new IntList(30);
    displayRoles = new IntList(30);
    elementState = new IntList(30);
    layoutContexts = new FastStack();
  }

  public void addElement(final int model, final int role, final LayoutContext lc)
  {
    displayModels.push(model);
    displayRoles.push(role);
    elementState.push(STATE_OPEN);
    layoutContexts.push(lc);
    activeContext = displayModels.size() - 1;
  }

  public int getCurrentDisplayRole()
  {
    if (displayRoles.size() == 0)
    {
      return FastDisplayModelBuilder.TYPE_BLOCK;
    }
    return displayRoles.get(activeContext);
  }

  public int getCurrentDisplayModel()
  {
    if (displayModels.size() == 0)
    {
      return FastDisplayModelBuilder.MODEL_BLOCK_INSIDE;
    }
    return displayModels.get(activeContext);
  }

  public int getActiveDisplayModel()
  {
    if (displayModels.size() == 0)
    {
      return FastDisplayModelBuilder.MODEL_BLOCK_INSIDE;
    }
    int index = activeContext;
    while (elementState.get(index) == STATE_SUSPEND)
    {
      index -= 1;
    }
    return displayModels.get(index);
  }

  public void suspend()
  {
    final int state = elementState.get(activeContext);
    if (state != STATE_OPEN)
    {
      throw new IllegalStateException("Only open elements can be suspended.");
    }
    elementState.set(activeContext, STATE_SUSPEND);

    // find the next non-suspended element.
    while (elementState.get(activeContext) == STATE_SUSPEND)
    {
      activeContext -= 1;
    }
  }

  public int close()
  {
    final int state = elementState.pop();
    displayRoles.pop();
    displayModels.pop();
    layoutContexts.pop();
    activeContext = Math.min (activeContext - 1, elementState.size());
    return state;
  }

  public LayoutContext getCurrentLayoutContext()
  {
    return (LayoutContext) layoutContexts.peek();
  }

  public int getCurrentState()
  {
    return elementState.peek();
  }

  public Object clone () throws CloneNotSupportedException
  {
    final FlowContext c = (FlowContext) super.clone();
    c.displayModels = (IntList) displayModels.clone();
    c.displayRoles = (IntList) displayRoles.clone();
    c.elementState = (IntList) elementState.clone();
    c.layoutContexts = (FastStack) layoutContexts.clone();
    return c;
  }

  public boolean isEmpty()
  {
    return layoutContexts.isEmpty();
  }
}
