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
 * $Id: CenterAlignmentProcessor.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.process.layoutrules.InlineSequenceElement;

/**
 * Right alignment strategy. Not working yet, as this is unimplemented right now.
 *
 * @author Thomas Morgner
 */
public class CenterAlignmentProcessor extends AbstractAlignmentProcessor
{
  public CenterAlignmentProcessor()
  {
  }

  protected int handleLayout(final int start,
                             final int count,
                             final int contentIndex,
                             final long usedWidth)
  {
    final InlineSequenceElement[] sequenceElements = getSequenceElements();
    final long[] elementDimensions = getElementDimensions();
    final long[] elementPositions = getElementPositions();

    // if we reached that method, then this means, that the elements may fit
    // into the available space. (Assuming that there is no inner pagebreak;
    // a thing we do not handle yet)

    final long totalWidth = getEndOfLine() - getStartOfLine();
    final long emptySpace = (totalWidth - usedWidth);
    long position = getStartOfLine() + emptySpace / 2;
    for (int i = 0; i < sequenceElements.length; i++)
    {
      final long elementWidth = sequenceElements[i].getMaximumWidth();
      elementDimensions[i] = elementWidth;
      elementPositions[i] = position;

      position += elementWidth;
    }
    return start + count;
  }

}
