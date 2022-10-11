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
 * $Id: FinishedRenderNode.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

/**
 * A box replacement. It has a predefined width and height and does not change
 * those. It is a placeholder for all already printed content.
 * <p/>
 * If you see this node inside an inline box, you can be sure you've shot
 * yourself in the foot.
 *
 * @author Thomas Morgner
 */
public class FinishedRenderNode extends RenderNode
{
  private long layoutedWidth;
  private long layoutedHeight;
  private long effectiveMarginsTop;
  private long effectiveMarginsBottom;

  public FinishedRenderNode(final long layoutedWidth,
                            final long layoutedHeight,
                            final long effectiveMarginsTop,
                            final long effectiveMarginsBottom)
  {
    if (layoutedWidth <= 0)
    {
      throw new IllegalStateException();
    }
    if (layoutedHeight <= 0)
    {
      throw new IllegalStateException();
    }

    this.effectiveMarginsTop = effectiveMarginsTop;
    this.effectiveMarginsBottom = effectiveMarginsBottom;
    this.layoutedWidth = layoutedWidth;
    this.layoutedHeight = layoutedHeight;
  }

  public boolean isDiscardable()
  {
    return false;
  }

  public boolean isEmpty()
  {
    return false;
  }

  public long getLayoutedWidth()
  {
    return layoutedWidth;
  }

  public long getLayoutedHeight()
  {
    return layoutedHeight;
  }

  /**
   * If that method returns true, the element will not be used for rendering.
   * For the purpose of computing sizes or performing the layouting (in the
   * validate() step), this element will treated as if it is not there.
   * <p/>
   * If the element reports itself as non-empty, however, it will affect the
   * margin computation.
   *
   * @return
   */
  public boolean isIgnorableForRendering()
  {
    // Finished rows affect the margins ..
    return false;
  }

  public long getEffectiveMarginTop()
  {
    return effectiveMarginsTop;
  }

  public long getEffectiveMarginBottom()
  {
    return effectiveMarginsBottom;
  }
}
