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
 * $Id: LayoutStylePool.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style;

import org.jfree.layouting.layouter.context.LayoutStyle;

/**
 * This class does not work as expected as long as we have no clear life-cycle
 * for the layout-style classes.
 */
public class LayoutStylePool
{
  private static class LayoutStyleReference
  {
    private boolean inUse;
    private LayoutStyle referent;

    /**
     * Creates a new soft reference that refers to the given object.  The new reference is
     * not registered with any queue.
     *
     * @param referent object the new soft reference will refer to
     */
    private LayoutStyleReference (final LayoutStyle referent)
    {
      this.referent = referent;
      inUse = true;
    }

    /**
     * Returns this reference object's referent.  If this reference object has been cleared,
     * either by the program or by the garbage collector, then this method returns
     * <code>null</code>.
     *
     * @return The object to which this reference refers, or <code>null</code> if this
     *         reference object has been cleared
     */
    public Object get ()
    {
      return referent;
    }

    public boolean isInUse ()
    {
      return inUse;
    }

    public void setInUse (final boolean inUse)
    {
      this.inUse = inUse;
    }
  }

  private static LayoutStylePool pool;


  public static synchronized LayoutStylePool getPool ()
  {
    if (pool == null)
    {
      pool = new LayoutStylePool();
    }
    return pool;
  }

//  private LayoutStyleReference[] storage;
  private int cursor;

  private LayoutStylePool ()
  {
//    storage = new LayoutStyleReference[32];
  }

  public synchronized LayoutStyleImpl getStyle ()
  {
    return new LayoutStyleImpl();
//
//    final int oldCursor = cursor;
//    if (oldCursor == 0)
//    {
//      final LayoutStyleImpl retval = seekUpTo(storage.length);
//      if (retval == null)
//      {
//       // return new LayoutStyle();
//        // todo: This is a deadly stupid construct.
//        throw new OutOfMemoryError("Pool was invalid.");
//      }
//      return retval;
//    }
//
//    final LayoutStyleImpl retval = seekUpTo(storage.length);
//    if (retval != null)
//    {
//      return retval;
//    }
//    cursor = 0;
//    final LayoutStyleImpl secondRun = seekUpTo(oldCursor - 1);
//    if (secondRun == null)
//    {
//      throw new OutOfMemoryError("Pool was invalid.");
//    }
//    return secondRun;
  }
//
//  private LayoutStyleImpl seekUpTo (final int limit)
//  {
//    for (; cursor < limit; cursor++)
//    {
//      LayoutStyleReference reference = storage[cursor];
//      if (reference == null)
//      {
//        final LayoutStyleImpl referent = new LayoutStyleImpl();
//        storage[cursor] = new LayoutStyleReference(referent);
//        referent.setReference(storage[cursor]);
//        cursor += 1;
//        return referent;
//      }
//      if (reference.isInUse())
//      {
//        continue;
//      }
//
//      final LayoutStyleImpl style = (LayoutStyleImpl) reference.get();
//      if (style != null)
//      {
//        reference.setInUse(true);
//        return style;
//      }
//      else
//      {
//        final LayoutStyleImpl referent = new LayoutStyleImpl();
//        storage[cursor] = new LayoutStyleReference(referent);
//        referent.setReference(storage[cursor]);
//        cursor += 1;
//        return referent;
//      }
//    }
//    return null;
//  }

  public synchronized void reclaim (final LayoutStyle layoutStyle,
                                    final Object reference)
  {
    final LayoutStyleReference ref = (LayoutStyleReference) reference;
    ref.setInUse(false);
  }
}
