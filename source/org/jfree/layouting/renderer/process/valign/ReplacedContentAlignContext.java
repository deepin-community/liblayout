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
 * $Id: ReplacedContentAlignContext.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.renderer.process.valign;

import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.text.ExtendedBaselineInfo;
import org.jfree.layouting.renderer.border.RenderLength;
import org.jfree.layouting.util.geom.StrictDimension;

/**
 * A generic align context for images and other nodes. (Renderable-Content
 * should have been aligned by the parent.
 *
 * @author Thomas Morgner
 */
public class ReplacedContentAlignContext extends AlignContext
{
  private long shift;
  private long height;

  public ReplacedContentAlignContext(final RenderableReplacedContent node)
  {
    super(node);
    final StrictDimension contentSize = node.getContentSize();
    // we better compute the height of the content or we will run into
    // trouble ..

    if (RenderLength.AUTO.equals(node.getRequestedWidth()))
    {
      // if width is auto, and height is auto,
      if (RenderLength.AUTO.equals(node.getRequestedHeight()))
      {
        // use the intrinsic height ..
        height = contentSize.getHeight();
      }
      // if height is not auto, but the width is, then use the declared height.
      else
      {
        // A percentage is now relative to the intrinsinc size.
        // And yes, I'm aware that this is not what the standard says ..
        final RenderLength blockContextWidth =
            node.getComputedLayoutProperties().getBlockContextWidth();
        height = node.getRequestedHeight().resolve(blockContextWidth.resolve(0));
      }
    }
    else
    {
      // width is not auto.
      // If the height is auto, we have to preserve the aspect ratio ..
      if (RenderLength.AUTO.equals(node.getRequestedHeight()))
      {
        if (contentSize.getWidth() > 0)
        {
          final RenderLength blockContextWidth =
              node.getComputedLayoutProperties().getBlockContextWidth();
          final long computedWidth =
              node.getRequestedWidth().resolve(blockContextWidth.resolve(0));
          // Requested height must be computed to preserve the aspect ratio.
          height = computedWidth * contentSize.getHeight()/contentSize.getWidth();
        }
        else
        {
          height = 0;
        }
      }
      else
      {
        // height is something fixed ..
        final RenderLength blockContextWidth =
            node.getComputedLayoutProperties().getBlockContextWidth();
        height = node.getRequestedHeight().resolve(blockContextWidth.resolve(0));
      }
    }
  }

  public long getBaselineDistance(final int baseline)
  {
    if (baseline == ExtendedBaselineInfo.BEFORE_EDGE)
    {
      return 0;
    }
    if (baseline == ExtendedBaselineInfo.TEXT_BEFORE_EDGE)
    {
      return 0;
    }
    // oh that's soooo primitive ..
    return height;
  }

  public void shift(final long delta)
  {
    this.shift += delta;
  }

  public long getAfterEdge()
  {
    return shift + height;
  }

  public long getBeforeEdge()
  {
    return shift;
  }
}
