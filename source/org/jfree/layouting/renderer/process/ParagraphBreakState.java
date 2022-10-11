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
 * $Id: ParagraphBreakState.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import java.util.ArrayList;

import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.process.layoutrules.InlineNodeSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.InlineSequenceElement;
import org.jfree.layouting.renderer.process.layoutrules.TextSequenceElement;

/**
 * Used in the Infinite*AxisLayoutSteps.
 *
 * @author Thomas Morgner
 */
public class ParagraphBreakState
{
  private Object suspendItem;
  private ArrayList primarySequence;
  private ParagraphRenderBox paragraph;
  private boolean containsContent;

  public ParagraphBreakState(final ParagraphRenderBox paragraph)
  {
    if (paragraph == null)
    {
      throw new NullPointerException();
    }
    this.paragraph = paragraph;
    this.primarySequence = new ArrayList();
  }

  public ParagraphRenderBox getParagraph()
  {
    return paragraph;
  }

  public Object getSuspendItem()
  {
    return suspendItem;
  }

  public void setSuspendItem(final Object suspendItem)
  {
    this.suspendItem = suspendItem;
  }

  public void add(final InlineSequenceElement element)
  {
    primarySequence.add(element);
    if (element instanceof TextSequenceElement ||
        element instanceof InlineNodeSequenceElement)
    {
      containsContent = true;
    }
  }

  public boolean isContainsContent()
  {
    return containsContent;
  }

  public boolean isSuspended()
  {
    return suspendItem != null;
  }

  public InlineSequenceElement[] getSequence()
  {
    return (InlineSequenceElement[]) primarySequence.toArray
            (new InlineSequenceElement[primarySequence.size()]);
  }

  public void clear()
  {
    primarySequence.clear();
    containsContent = false;
  }
}
