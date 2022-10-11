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
 * $Id: Border.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.border;

/**
 * A border is a split-supporting component that contains five border edges
 * (top,left, bottom, right, split). The split-border is not used for rendering,
 * it is meant to replace the other borders on inner split-edges.
 * <p/>
 * The border object itself is immutable. During a split operation, new borders
 * have to be created.
 * <p/>
 * The border-radius defines the corner-rounding that might take place. This
 * defines the applicable background area of the content box. (Round rects never
 * cause the background to overlap the border, the corner space that lies
 * outside the rounded corners will not receive the background.)
 * <p/>
 * The radius *must* be normalized; the sum of the radius sizes for a single
 * edge must not exceed the edge's total size. (Ex: height &gt;=
 * (topLeftRadius.getHeight() + bottomLeftRadius.getHeight()). If the height is
 * smaller as the radius, reduce the radius until both sizes fit.
 *
 * @author Thomas Morgner
 */
public class Border implements Cloneable
{
  private static Border emptyBorder;

  private BorderEdge top;
  private BorderEdge left;
  private BorderEdge bottom;
  private BorderEdge right;
  private BorderEdge splittingEdge;

  private BorderCorner topLeft;
  private BorderCorner topRight;
  private BorderCorner bottomLeft;
  private BorderCorner bottomRight;
  private Boolean empty;

  public Border(final BorderEdge top,
                final BorderEdge left,
                final BorderEdge bottom,
                final BorderEdge right,
                final BorderEdge splittingEdge,
                final BorderCorner topLeft,
                final BorderCorner topRight,
                final BorderCorner bottomLeft,
                final BorderCorner bottomRight)
  {
    this.top = top;
    this.left = left;
    this.bottom = bottom;
    this.right = right;
    this.splittingEdge = splittingEdge;
    this.topLeft = topLeft;
    this.topRight = topRight;
    this.bottomLeft = bottomLeft;
    this.bottomRight = bottomRight;
  }

  public BorderEdge getTop()
  {
    return top;
  }

  public BorderEdge getLeft()
  {
    return left;
  }

  public BorderEdge getBottom()
  {
    return bottom;
  }

  public BorderEdge getRight()
  {
    return right;
  }

  public BorderEdge getSplittingEdge()
  {
    return splittingEdge;
  }

  public BorderCorner getTopLeft()
  {
    return topLeft;
  }

  public BorderCorner getTopRight()
  {
    return topRight;
  }

  public BorderCorner getBottomLeft()
  {
    return bottomLeft;
  }

  public BorderCorner getBottomRight()
  {
    return bottomRight;
  }

  public Border[] splitVertically(Border[] borders)
  {
    if (borders == null || borders.length < 2)
    {
      borders = new Border[2];
    }

    borders[0] = (Border) clone();
    borders[0].empty = null;
    borders[0].right = borders[0].splittingEdge;

    borders[1] = (Border) clone();
    borders[1].empty = null;
    borders[1].left = borders[1].splittingEdge;
    return borders;
  }

  public Border[] splitHorizontally(Border[] borders)
  {
    if (borders == null || borders.length < 2)
    {
      borders = new Border[2];
    }

    borders[0] = (Border) clone();
    borders[0].empty = null;
    borders[0].bottom = borders[0].splittingEdge;

    borders[1] = (Border) clone();
    borders[1].empty = null;
    borders[1].top = borders[1].splittingEdge;
    return borders;
  }

  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Borders not supporting clone is evil!");
    }
  }

  public static synchronized Border createEmptyBorder()
  {
    if (emptyBorder == null)
    {
      final BorderEdge edge = EmptyBorderEdge.getInstance();
      final BorderCorner corner = EmptyBorderCorner.getInstance();

      emptyBorder = new Border(edge, edge, edge, edge, edge,
          corner, corner, corner, corner);
    }
    return emptyBorder;
  }

  public boolean isEmpty()
  {
    if (empty != null)
    {
      return empty.booleanValue();
    }

    if (top.getWidth().getValue() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (left.getWidth().getValue() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (bottom.getWidth().getValue() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }
    if (right.getWidth().getValue() != 0)
    {
      empty = Boolean.FALSE;
      return false;
    }

    empty = Boolean.TRUE;
    return true;
  }

}
