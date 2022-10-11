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
 * $Id: ChainingComponent.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A chaining component accepts calls from outside, forwards them to its
 * wrapped object, and records all calls to the next chain element. Only
 * after the initial call has been fully completed (and thus the wrapped
 * object is back in a consistent state) all generated sub-calls will be
 * forwarded to the next chain element.
 *
 * Of course, the whole architecture assumes, that the execution flow is a
 * one-way street and that the execution and computation of the n-th step does
 * not rely on results and/or the current state of the n+1-th step.
 *
 * It is guaranteed, that all calls are executed in the same order they have
 * been recorded.
 *
 * @author Thomas Morgner
 */
public abstract class ChainingComponent
{
  public static final int STATE_FRESH = 0;
  public static final int STATE_ERROR = 1;
  public static final int STATE_DONE = 2;

  public static class RecordedCall
  {
    private int methodId;
    private Object parameters;
    private int state;

    public RecordedCall(final int method, final Object parameters)
    {
      this.methodId = method;
      this.parameters = parameters;
    }

    public int getState()
    {
      return state;
    }

    public void setState(final int state)
    {
      this.state = state;
    }

    public int getMethod()
    {
      return methodId;
    }

    public Object getParameters()
    {
      return parameters;
    }
  }

  private ArrayList calls;

  public ChainingComponent()
  {
    calls = new ArrayList();
  }

  public void addCall (final RecordedCall c)
  {
    calls.add(c);
  }

  public void clear ()
  {
    calls.clear();
  }

  protected RecordedCall[] retrieveRecordedCalls()
  {
    final RecordedCall[] recordedCalls = (RecordedCall[]) calls.toArray(new RecordedCall[calls.size()]);
    calls.clear();
    return recordedCalls;
  }

  public void setRecordedCalls (final RecordedCall[] recordedCalls)
  {
    calls.addAll(Arrays.asList(recordedCalls));
  }

  public synchronized void replay (final Object target) throws ChainingCallException
  {
    // Copy all recorded calls and be done with it.
    final RecordedCall[] recordedCalls = retrieveRecordedCalls();

    for (int i = 0; i < recordedCalls.length; i++)
    {
      final RecordedCall call = recordedCalls[i];
      if (call.getState() == STATE_FRESH)
      {
        try
        {
          invoke(target, call.getMethod(), call.getParameters());
          call.setState(STATE_DONE);
        }
        catch(Exception e)
        {
          call.setState(STATE_ERROR);
          throw new ChainingCallException("Chained Call failed:", e);
        }
      }
    }
  }

  protected abstract void invoke (Object target, int methodId, Object parameters)
          throws Exception;
}
