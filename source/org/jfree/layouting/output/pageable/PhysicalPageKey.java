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
 * $Id: PhysicalPageKey.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.output.pageable;

import java.io.Serializable;

/**
 * A physical page-key identifies a generated page.
 *
 * @author Thomas Morgner
 */
public final class PhysicalPageKey implements Serializable
{
  private LogicalPageKey logicalPage;
  private int x;
  private int y;

  public PhysicalPageKey(final LogicalPageKey logicalPage,
                         final int x, final int y)
  {
    this.x = x;
    this.y = y;
    this.logicalPage = logicalPage;
  }

  public LogicalPageKey getLogicalPage()
  {
    return logicalPage;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
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

    final PhysicalPageKey that = (PhysicalPageKey) o;

    if (x != that.x)
    {
      return false;
    }
    if (y != that.y)
    {
      return false;
    }
    if (!logicalPage.equals(that.logicalPage))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = logicalPage.hashCode();
    result = 29 * result + x;
    result = 29 * result + y;
    return result;
  }
}
