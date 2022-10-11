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
 * $Id: ImageUtils.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.util;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Creation-Date: 28.11.2006, 12:27:05
 *
 * @author Thomas Morgner
 */
public class ImageUtils
{
  private ImageUtils()
  {
  }

  /**
   * Creates a transparent image.  These can be used for aligning menu items.
   *
   * @param width  the width.
   * @param height the height.
   * @return the created transparent image.
   */
  public static BufferedImage createTransparentImage (final int width, final int height)
  {
    final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    final int[] data = img.getRGB(0, 0, width, height, null, 0, width);
    Arrays.fill(data, 0x00000000);
    img.setRGB(0, 0, width, height, data, 0, width);
    return img;
  }

}
