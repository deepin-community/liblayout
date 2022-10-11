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
 * $Id: BoxShifter.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.process;

import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;

/**
 * By keeping the shifting in a separate class, we can optimize it later without
 * having to touch the other code. Remember: Recursive calls can be evil in
 * complex documents..
 *
 * @author Thomas Morgner
 */
public class BoxShifter
{
  public BoxShifter()
  {
  }

  public void shiftBox(final RenderBox box, final long amount)
  {
    if (amount == 0)
    {
      return;
    }
    if (amount < 0)
    {
      throw new IllegalArgumentException("Cannot shift upwards: " + amount);
    }

    box.setY(box.getY() + amount);
    shiftBoxInternal(box, amount);
  }

  public void shiftBoxUnchecked(final RenderBox box, final long amount)
  {
    if (amount == 0)
    {
      return;
    }

    box.setY(box.getY() + amount);
    shiftBoxInternal(box, amount);
  }

  private void shiftBoxInternal(final RenderBox box, final long amount)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      node.setY(node.getY() + amount);
      if (node instanceof RenderBox)
      {
        shiftBoxInternal((RenderBox) node, amount);
      }
      node = node.getNext();
    }
  }


  public void extendHeight(final RenderNode node, final long amount)
  {
    if (amount < 0)
    {
      throw new IllegalArgumentException("Cannot shrink elements.");
    }
    if (node == null || amount == 0)
    {
      return;
    }

    node.setHeight(node.getHeight() + amount);

    RenderBox parent = node.getParent();
    while (parent != null)
    {
      parent.setHeight(parent.getHeight() + amount);
      parent = parent.getParent();
    }
  }
}
