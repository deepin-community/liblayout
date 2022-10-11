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
 * $Id: EmptyImageData.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.pentaho.reporting.libraries.resourceloader.Resource;

/**
 * This is a placeholder for non-resolveable image values.
 *
 * @author Thomas Morgner
 */
public class EmptyImageData implements ImageData
{
  public EmptyImageData()
  {
  }

  public long getWidth()
  {
    return 0;
  }

  public long getHeight()
  {
    return 0;
  }

  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio
   * aware, these bounds should be used to compute the preferred aspect ratio
   * for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize()
  {
    return new Dimension(0,0);
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the
   * drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio()
  {
    return false;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(final Graphics2D g2, final Rectangle2D area)
  {

  }

  public Resource getSource()
  {
    return null;
  }
}
