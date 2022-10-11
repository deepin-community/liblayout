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
 * $Id: StrictPoint.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util.geom;

import java.io.Serializable;

/**
 * A StrictPoint class represents a coordinate in the report layout. It
 * is similiar to the {@link java.awt.geom.Point2D} class, but uses
 * micro-points instead of doubles.
 *
 * @author Thomas Morgner
 */
public class StrictPoint implements Serializable, Cloneable
{
  /** The x-Coordinate. */
  private long x;
  /** The y-coordinate. */
  private long y;
  /** A flag indicating whether this object is mutable. */
  private boolean locked;

  /**
   * DefaultConstructor.
   */
  public StrictPoint ()
  {
  }

  /**
   * Creates a StrictBounds object with the given coordinates, width
   * and height.
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   */
  public StrictPoint (final long x, final long y)
  {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the X coordinate of this <code>StrictPoint</code>  in micro points.
   *
   * @return the X coordinate of this <code>StrictPoint</code>.
   */
  public long getX ()
  {
    return x;
  }

  /**
   * Returns the Y coordinate of this <code>StrictPoint</code> in micro points.
   *
   * @return the Y coordinate of this <code>StrictPoint</code>.
   */
  public long getY ()
  {
    return y;
  }

  /**
   * Sets the location of this <code>StrictPoint</code> to the specified coordinates.
   *
   * @param x the coordinates of this <code>StrictPoint</code>
   * @param y the coordinates of this <code>StrictPoint</code>
   */
  public void setLocation (final long x, final long y)
  {
    if (locked)
    {
      throw new IllegalStateException();
    }

    this.x = x;
    this.y = y;
  }

  /**
   * Checks, whether this point object is locked.
   *
   * @return true, if the point is locked and therefore immutable, false otherwise.
   */
  public boolean isLocked ()
  {
    return locked;
  }


  /**
   * Returns a copy of this bounds object which cannot be modified anymore.
   *
   * @return a locked copy.
   */
  public StrictPoint getLockedInstance ()
  {
    if (locked)
    {
      return this;
    }

    final StrictPoint retval = (StrictPoint) clone();
    retval.locked = true;
    return retval;
  }

  /**
   * Returns a copy of this bounds object which can be modified later.
   *
   * @return an unlocked copy.
   */
  public StrictPoint getUnlockedInstance ()
  {
    final StrictPoint retval = (StrictPoint) clone();
    retval.locked = false;
    return retval;
  }

  /**
   * Returns a copy of this Point object. This method will never throw a
   * 'CloneNotSupportedException'.
   *
   * @return the cloned instance.
   */
  public Object clone ()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new InternalError("Clone must always be supported.");
    }
  }


  /**
   * Returns a string representation of these bounds.
   *
   * @return the string representing this object.
   */
  public String toString ()
  {
    return "org.jfree.report.util.geom.StrictPoint{" +
            "x=" + x +
            ", y=" + y +
        '}';
  }

  /**
   * Checks, whether the given object is a StrictPoint instance sharing the same
   * coordinates as this point.
   *
   * @param o the other object.
   * @return true, if the other object is equal to this object, false otherwise.
   */
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

    final StrictPoint that = (StrictPoint) o;

    if (x != that.x)
    {
      return false;
    }
    if (y != that.y)
    {
      return false;
    }

    return true;
  }

  /**
   * Computes the hashcode for this point.
   *
   * @return the computed hashcode.
   */
  public int hashCode()
  {
    int result = (int) (x ^ (x >>> 32));
    result = 29 * result + (int) (y ^ (y >>> 32));
    return result;
  }
}
