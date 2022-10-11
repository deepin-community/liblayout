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
 * $Id: CleanStreamedBoxesStep.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;

/**
 * Creation-Date: 27.10.2006, 18:19:24
 *
 * @author Thomas Morgner
 */
public class CleanStreamedBoxesStep extends IterateVisualProcessStep
{
  public CleanStreamedBoxesStep()
  {
  }

  public void compute(final LogicalPageBox pageBox)
  {
    if (startBlockLevelBox(pageBox))
    {
      // not processing the header and footer area: they are 'out-of-context' bands
      processBoxChilds(pageBox);
    }
    finishBlockLevelBox(pageBox);
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    // we do not process the paragraph lines. This should have been done
    // in the startblock thing and they get re-added anyway as long as the
    // paragraph is active.
  }

  protected boolean startBlockLevelBox(final RenderBox box)
  {
    return false;
  }
}
