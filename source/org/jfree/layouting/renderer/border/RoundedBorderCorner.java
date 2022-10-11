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
 * $Id: RoundedBorderCorner.java 2745 2007-04-04 10:51:07Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.border;

/**
 * Creation-Date: 23.06.2006, 17:01:18
 *
 * @author Thomas Morgner
 */
public class RoundedBorderCorner implements BorderCorner
{
  private RenderLength width;
  private RenderLength height;

  public RoundedBorderCorner(final RenderLength width, final RenderLength height)
  {
    this.width = width;
    this.height = height;
  }

  public RenderLength getWidth()
  {
    return width;
  }

  public RenderLength getHeight()
  {
    return height;
  }
}

