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
 * $Id: LayoutStyleImpl.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.LayoutStyle;

/**
 * Unlike the old JFreeReport stylesheet, this implementation has no inheritance
 * at all. It needs to be resolved manually, which is no bad thing, as we have
 * to do this anyway during the computation.
 *
 * @author Thomas Morgner
 */
public final class LayoutStyleImpl implements LayoutStyle
{
  private CSSValue[] values;
  private Object reference;

  public LayoutStyleImpl()
  {
  }

  public Object getReference()
  {
    return reference;
  }

  public void setReference(final Object reference)
  {
    this.reference = reference;
  }

  public synchronized CSSValue getValue(final StyleKey key)
  {
    if (values == null)
    {
      return null;
    }
    return values[key.getIndex()];
  }

  public synchronized void setValue(final StyleKey key, final CSSValue value)
  {
    if (values == null)
    {
      values = new CSSValue[StyleKeyRegistry.getRegistry().getKeyCount()];
    }
    values[key.getIndex()] = value;
  }

  // todo: Make sure we call dispose once the layout style goes out of context
  public synchronized void dispose()
  {
  }

  public synchronized LayoutStyleImpl createCopy()
  {
    final LayoutStyleImpl style = new LayoutStyleImpl();
    if (values == null)
    {
      style.values = null;
      return style;
    }

    style.values = (CSSValue[]) values.clone();
    return style;
  }

  public boolean isClean()
  {
    if (values == null)
    {
      return true;
    }
    for (int i = 0; i < values.length; i++)
    {
      if (values[i] != null)
      {
        return false;
      }
    }
    return true;
  }

  public boolean copyFrom(final LayoutStyle style)
  {
    if (style instanceof LayoutStyleImpl == false)
    {
      return false;
    }

    final LayoutStyleImpl rawstyle = (LayoutStyleImpl) style;
    if (rawstyle.values == null)
    {
      return true;
    }

    if (values == null)
    {
      values = (CSSValue[]) rawstyle.values.clone();
      return true;
    }

    final int length = rawstyle.values.length;
    for (int i = 0; i < length; i++)
    {
      final CSSValue o = rawstyle.values[i];
      if (o != null)
      {
        values[i] = o;
      }
    }
    return true;
  }

}
