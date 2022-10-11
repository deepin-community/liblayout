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
 * $Id: AWTImageData.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.pentaho.reporting.libraries.resourceloader.Resource;

/**
 * Creation-Date: 14.12.2005, 14:03:08
 *
 * @author Thomas Morgner
 */
public class AWTImageData implements ImageData
{
  private Resource source;
  private Image image;

  public AWTImageData(final Resource source,
                      final Image image)
  {
    if (image == null)
    {
      throw new NullPointerException();
    }
    this.source = source;
    this.image = image;
  }

  /**
   * Returns the resource definition that was used to load the image. Return
   * null, if there was no resource loader involved. (This covers both
   * invalid/empty content and generated content.)
   *
   * @return
   */
  public Resource getSource()
  {
    return source;
  }

  public long getWidth()
  {
    return image.getWidth(null);
  }

  public long getHeight()
  {
    return image.getWidth(null);
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
    return new Dimension
            ((int) StrictGeomUtility.toExternalValue(getWidth()),
                    (int) StrictGeomUtility.toExternalValue(getHeight()));
  }

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the
   * drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio()
  {
    return true;
  }

  /**
   * Draws the object.
   *
   * @param g2   the graphics device.
   * @param area the area inside which the object should be drawn.
   */
  public void draw(final Graphics2D g2, final Rectangle2D area)
  {
    g2.drawImage(image, 0, 0, null);
  }
}
