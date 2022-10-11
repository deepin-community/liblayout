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
 * $Id: IntList.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util;

import java.io.Serializable;
import java.util.EmptyStackException;

/**
 * A Array-List for integer objects. Ints can be added to the list and will be
 * stored in an int-array.
 * <p/>
 * Using this list for storing ints is much faster than creating
 * java.lang.Integer objects and storing them in an ArrayList.
 * <p/>
 * This list is not synchronized and does not implement the full List interface.
 * In fact, this list can only be used to add new values or to clear the
 * complete list.
 *
 * @author Thomas Morgner
 */
public class IntList implements Serializable, Cloneable
{
  /**
   * An empty array used to avoid object creation.
   */
  private static final int[] EMPTY_ARRAY = new int[0];
  /**
   * The array holding the list data.
   */
  private int[] data;
  /**
   * The size of the list.
   */
  private int size;
  /**
   * The number of free slots added on every resize.
   */
  private int increment;

  /**
   * Creates a new IntList with the given initial capacity. The capacity will
   * also be used as increment value when extending the capacity of the list.
   *
   * @param capacity the initial capacity.
   */
  public IntList(final int capacity)
  {
    data = new int[capacity];
    increment = capacity;
  }

  /**
   * Ensures, that the list backend can store at least <code>c</code> elements.
   * This method does nothing, if the new capacity is less than the current
   * capacity.
   *
   * @param c the new capacity of the list.
   */
  private void ensureCapacity(final int c)
  {
    if (data.length <= c)
    {
      final int[] newData = new int[Math.max(data.length + increment, c + 1)];
      System.arraycopy(data, 0, newData, 0, size);
      data = newData;
    }
  }

  /**
   * Adds the given int value to the list.
   *
   * @param value the new value to be added.
   */
  public void add(final int value)
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
  public void set(final int index, final int value)
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
   * @throws IndexOutOfBoundsException if the index is greater or equal to the
   *                                   list size or if the index is negative.
   */
  public int get(final int index)
  {
    if (index >= size || index < 0)
    {
      throw new IndexOutOfBoundsException("Index " + index + " is invalid");
    }
    return data[index];
  }

  /**
   * Clears the list.
   */
  public void clear()
  {
    size = 0;
  }

  /**
   * Returns the number of elements in this list.
   *
   * @return the number of elements in the list
   */
  public int size()
  {
    return size;
  }

  public int pop()
  {
    if (size == 0)
    {
      throw new EmptyStackException();
    }
    size -= 1;
    return data[size];
  }

  public int peek()
  {
    if (size == 0)
    {
      throw new EmptyStackException();
    }
    return data[size - 1];
  }

  public final void push(final int value)
  {
    add(value);
  }

  /**
   * Copys the list contents into a new array.
   *
   * @return the list contents as array.
   */
  public int[] toArray()
  {
    if (size == 0)
    {
      return EMPTY_ARRAY;
    }

    return (int[]) data.clone();
  }

  public Object clone() throws CloneNotSupportedException
  {
    final IntList intList = (IntList) super.clone();
    intList.data = (int[]) data.clone();
    return intList;
  }
}
