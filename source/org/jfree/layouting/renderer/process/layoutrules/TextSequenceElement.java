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
 * $Id: TextSequenceElement.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process.layoutrules;

import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;

/**
 * Text in a line. A text item is special as it may shrink or stretch to
 * better fit the line. If hyphenation is implemented, it may even split or
 * change into a different text (this happens a lot in the german language).
 *
 * @author Thomas Morgner
 */
public class TextSequenceElement implements InlineSequenceElement
{
  private RenderableText text;

  public TextSequenceElement(final RenderableText text)
  {
    this.text = text;
  }

  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth()
  {
    return text.getMinimumWidth();
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
    return text.getPreferredWidth();
  }

  public RenderNode getNode()
  {
    return text;
  }

  public boolean isPreserveWhitespace()
  {
    return false;
  }

}
