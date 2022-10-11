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
 * $Id: SpacerSequenceElement.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process.layoutrules;

import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.SpacerRenderNode;

/**
 * A spacer. This is some empty space (not padding, but for instance whitespaces)
 *
 * @author Thomas Morgner
 */
public class SpacerSequenceElement implements InlineSequenceElement
{
  private SpacerRenderNode spacerNode;

  public SpacerSequenceElement(final SpacerRenderNode spacerNode)
  {
    this.spacerNode = spacerNode;
  }

  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth()
  {
    return spacerNode.getMinimumChunkWidth();
  }

  /**
   * The extra-space width for an element. Some elements can expand to fill some
   * more space (justified text is a good example, adding some space between the
   * letters of each word to reduce the inner-word spacing).
   *
   * @return
   */
  public long getMaximumWidth()
  {
    return spacerNode.getMaximumBoxWidth();
  }

  public RenderNode getNode()
  {
    return spacerNode;
  }

  public boolean isPreserveWhitespace()
  {
    return false;
  }
}
