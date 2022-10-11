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
 * $Id: LongList.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util;

import java.io.Serializable;

/**
 * A Array-List for integer objects. Ints can be added to the list and will be
 * stored in an int-array.
 * <p>
 * Using this list for storing ints is much faster than creating java.lang.Integer
 * objects and storing them in an ArrayList.
 * <p>
 * This list is not synchronized and does not implement the full List interface.
 * In fact, this list can only be used to add new values or to clear the complete
 * list.
 *
 * @author Thomas Morgner
 */
public class LongList implements Serializable, Cloneable
{
  /** An empty array used to avoid object creation. */
  private static final long[] EMPTY_ARRAY = new long[0];
  /** The array holding the list data. */
  private long[] data;
  /** The size of the list. */
  private int size;
  /** The number of free slots added on every resize. */
  private int increment;

  /**
   * Creates a new IntList with the given initial capacity.
   * The capacity will also be used as increment value when
   * extending the capacity of the list.
   *
   * @param capacity the initial capacity.
   */
  public LongList (final int capacity)
  {
    data = new long[capacity];
    increment = capacity;
  }

  /**
   * Ensures, that the list backend can store at least <code>c</code>
   * elements. This method does nothing, if the new capacity is less
   * than the current capacity.
   *
   * @param c the new capacity of the list.
   */
  private void ensureCapacity (final int c)
  {
    if (data.length <= c)
    {
      final long[] newData = new long[Math.max(data.length + increment, c + 1)];
      System.arraycopy(data, 0, newData, 0, size);
      data = newData;
    }
  }

  /**
   * Adds the given int value to the list.
   *
   * @param value the new value to be added.
   */
  public void add (final long value)
  {
    ensureCapacity(size);
    data[size] = value;
    size += 1;
  }

  /**
   * Adds the given int value to the list.
   *
   * @param value the new value to be added.
   */
  public void set (final int index, final long value)
  {
    ensureCapacity(index);
    data[index] = value;
    if (index >= size)
    {
      size = index + 1;
    }
  }


  /**
   * Returns the value at the given index.
   *
   * @param index the index
   * @return the value at the given index
   * @throws IndexOutOfBoundsException if the index is greater or
   * equal to the list size or if the index is negative.
   */
  public long get (final int index)
  {
    if (index >= size || index < 0)
    {
      throw new IndexOutOfBoundsException("Illegal Index: " + index + " Max:" + size);
    }
    return data[index];
  }

  /**
   * Clears the list.
   */
  public void clear ()
  {
    size = 0;
  }

  /**
   * Returns the number of elements in this list.
   *
   * @return the number of elements in the list
   */
  public int size ()
  {
    return size;
  }

  /**
   * Copys the list contents into a new array.
   *
   * @return the list contents as array.
   */
  public long[] toArray ()
  {
    if (size == 0)
    {
      return LongList.EMPTY_ARRAY;
    }

    return (long[]) data.clone();
  }

  public Object clone() throws CloneNotSupportedException
  {
    final LongList intList = (LongList) super.clone();
    intList.data = (long[]) data.clone();
    return data;
  }
}
