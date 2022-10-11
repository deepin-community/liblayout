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
 * $Id: EmptyBorderCorner.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.border;

/**
 * Creation-Date: 23.06.2006, 17:01:18
 *
 * @author Thomas Morgner
 */
public class EmptyBorderCorner implements BorderCorner
{
  private static EmptyBorderCorner instance;

  private EmptyBorderCorner()
  {
  }

  public RenderLength getWidth()
  {
    return RenderLength.EMPTY;
  }

  public RenderLength getHeight()
  {
    return RenderLength.EMPTY;
  }

  public static synchronized EmptyBorderCorner getInstance()
  {
    if (instance == null)
    {
      instance = new EmptyBorderCorner();
    }
    return instance;
  }
}
