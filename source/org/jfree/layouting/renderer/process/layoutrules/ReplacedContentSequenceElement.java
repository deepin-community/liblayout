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
 * $Id: ReplacedContentSequenceElement.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.process.layoutrules;

import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.util.geom.StrictDimension;

/**
 * Anthing that is not text. This could be an image or an inline-block element.
 * For now, we assume that these beasts are not breakable at the end of the
 * line (outer linebreaks).
 *
 * @author Thomas Morgner
 */
public class ReplacedContentSequenceElement  implements InlineSequenceElement
{
  private RenderableReplacedContent node;
  private long width;

  public ReplacedContentSequenceElement(final RenderableReplacedContent node)
  {
    this.node = node;
    final StrictDimension contentSize = node.getContentSize();

    if (RenderLength.AUTO.equals(node.getRequestedWidth()))
    {
      // if width is auto, and height is auto,
      if (RenderLength.AUTO.equals(node.getRequestedHeight()))
      {
        // use the intrinsic width ..
        width = contentSize.getWidth();
      }
      // if height is not auto, but the width is, then compute a width that
      // preserves the aspect ratio.
      else if (contentSize.getHeight() > 0)
      {
        final RenderLength blockContextWidth =
            node.getComputedLayoutProperties().getBlockContextWidth();
        final long height =
            node.getRequestedHeight().resolve(blockContextWidth.resolve(0));
        width = height * contentSize.getWidth() / contentSize.getHeight();
      }
      else
      {
        width = 0;
      }
    }
    else
    {
      final RenderLength blockContextWidth =
          node.getComputedLayoutProperties().getBlockContextWidth();
      // width is not auto.
      width = node.getRequestedWidth().resolve(blockContextWidth.resolve(0));
    }
  }

  /**
   * The width of the element. This is the minimum width of the element.
   *
   * @return
   */
  public long getMinimumWidth()
  {
    return width;
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
    return Math.max (width, node.getMaximumBoxWidth());
  }

  public RenderNode getNode()
  {
    return node;
  }

  public boolean isPreserveWhitespace()
  {
    return false;
  }
}
