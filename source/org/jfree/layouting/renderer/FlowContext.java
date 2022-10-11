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
 * $Id: FlowContext.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer;

import java.io.Serializable;

import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.renderer.model.NormalFlowRenderBox;
import org.jfree.layouting.renderer.text.RenderableTextFactory;

/**
 * Creation-Date: 11.11.2006, 15:47:36
 *
 * @author Thomas Morgner
 */
public final class FlowContext
{
  public static class FlowContextState implements Serializable
  {
    private State textFactoryState;
    private Object currentFlowId;

    public FlowContextState(final State textFactoryState,
                            final Object currentFlowId)
    {
      if (textFactoryState == null)
      {
        throw new NullPointerException();
      }
      if (currentFlowId == null)
      {
        throw new NullPointerException();
      }

      this.textFactoryState = textFactoryState;
      this.currentFlowId = currentFlowId;
    }

    public State getTextFactoryState()
    {
      return textFactoryState;
    }

    public Object getCurrentFlowId()
    {
      return currentFlowId;
    }
  }

  private RenderableTextFactory textFactory;
  private NormalFlowRenderBox currentFlow;

  public FlowContext(final RenderableTextFactory textFactory,
                     final NormalFlowRenderBox currentFlow)
  {
    if (textFactory == null)
    {
      throw new NullPointerException();
    }
    if (currentFlow == null)
    {
      throw new NullPointerException();
    }
    this.textFactory = textFactory;
    this.currentFlow = currentFlow;
  }

  public RenderableTextFactory getTextFactory()
  {
    return textFactory;
  }

  public NormalFlowRenderBox getCurrentFlow()
  {
    return currentFlow;
  }

  public FlowContextState saveState() throws StateException
  {
    final FlowContextState flowContextState = new FlowContextState
        (textFactory.saveState(), currentFlow.getInstanceId());
    //flowContextState.DEBUG_original = currentFlow;
    return flowContextState;
  }
}
