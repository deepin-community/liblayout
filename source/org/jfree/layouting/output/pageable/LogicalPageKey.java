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
 * $Id: LogicalPageKey.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.output.pageable;

import java.io.Serializable;

/**
 * Creation-Date: 10.11.2006, 13:04:36
 *
 * @author Thomas Morgner
 */
public final class LogicalPageKey implements Serializable
{
  private int position;
  private int width;
  private int height;
  private PhysicalPageKey[] physicalPageKeys;

  public LogicalPageKey(final int position,
                        final int width,
                        final int height)
  {
    this.position = position;
    this.width = width;
    this.height = height;
    this.physicalPageKeys = new PhysicalPageKey[width * height];

    for (int i = 0; i < physicalPageKeys.length; i++)
    {
      physicalPageKeys[i] = new PhysicalPageKey(this, i % width, i / width);
    }
  }

  public int getWidth()
  {
    return width;
  }

  public int getHeight()
  {
    return height;
  }

  public int getPosition()
  {
    return position;
  }

  public PhysicalPageKey getPage(final int x, final int y)
  {
    return physicalPageKeys[x + y * width];
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final LogicalPageKey that = (LogicalPageKey) o;

    if (height != that.height)
    {
      return false;
    }
    if (position != that.position)
    {
      return false;
    }
    if (width != that.width)
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = position;
    result = 29 * result + width;
    result = 29 * result + height;
    return result;
  }
}
