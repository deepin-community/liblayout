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
 * $Id: DefaultLayoutProcess.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting;

import org.jfree.layouting.layouter.feed.DefaultInputFeed;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.output.OutputProcessor;

/**
 * Creation-Date: 10.11.2006, 15:16:40
 *
 * @author Thomas Morgner
 */
public class DefaultLayoutProcess extends AbstractLayoutProcess
{
  protected static class DefaultLayoutProcessState
      extends AbstractLayoutProcess.AbstractLayoutProcessState
  {
    protected DefaultLayoutProcessState(final AbstractLayoutProcess lp)
        throws StateException
    {
      super(lp);
    }

    public LayoutProcess restore(final OutputProcessor outputProcessor)
        throws StateException
    {
      return restore(outputProcessor, new DefaultLayoutProcess(outputProcessor));
    }
  }

  public DefaultLayoutProcess(final OutputProcessor outputProcessor)
  {
    super(outputProcessor);
  }

  protected InputFeed createInputFeed()
  {
    return new DefaultInputFeed(this);
  }

  public LayoutProcessState saveState() throws StateException
  {
    return new DefaultLayoutProcessState(this);
  }
}
