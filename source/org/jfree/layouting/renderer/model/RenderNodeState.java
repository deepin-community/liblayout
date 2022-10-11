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
 * $Id: RenderNodeState.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model;

/**
 * Creation-Date: 01.07.2006, 14:38:13
 *
 * @author Thomas Morgner
 */
public class RenderNodeState
{
  /**
   * The node is new or has undergone some major structural changes. Dont assume
   * anything.
   */
  public static final RenderNodeState UNCLEAN = new RenderNodeState("UNCLEAN", 1000);

  /**
   * The node or one of its childs or predecessors has got new childs. The
   * position is still valid, but all pending or unclean childs have to be
   * revalidated.
   *
   * This may affect the margins of this box, so recompute them.
   */
  public static final RenderNodeState PENDING = new RenderNodeState("PENDING", 3000);

  /**
   * The box has done at least some layouting and has cached some information.
   * Layouting is not finished. (Maybe the preferred size has been computed and
   * stored for later use.)
   */
  public static final RenderNodeState LAYOUTING = new RenderNodeState("LAYOUTING", 4000);

  /**
   * The box is fully layouted and ready to be painted. An open box should never
   * reach the finished state.
   */
  public static final RenderNodeState FINISHED = new RenderNodeState("FINISHED", 5000);

  private final String myName; // for debug only
  private int weight;

  private RenderNodeState(final String name, final int weight)
  {
    this.weight = weight;
    myName = name;
  }

  public int getWeight()
  {
    return weight;
  }

  public String toString()
  {
    return myName;
  }
}
