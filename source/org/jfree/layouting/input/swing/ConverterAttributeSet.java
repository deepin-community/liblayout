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
 * $Id: ConverterAttributeSet.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.swing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;

public class ConverterAttributeSet extends SimpleAttributeSet
{
  // todo : use a classe to have a better typing instead of a simple String
  public static final String NOT_TYPED = "not_typed";

  private Map typeMap;

  public ConverterAttributeSet()
  {
    super();
    typeMap = new HashMap();
  }

  public void addAttribute(final Object name, final Object value)
  {
    addAttribute(null, name, value);
  }

  public synchronized void addAttribute(Object type, final Object name, final Object value)
  {
    if (type == null)
    {
      type = NOT_TYPED;
    }

    typeMap.put(name, type);
    super.addAttribute(name, value);
  }

  public void addAttributes(final ConverterAttributeSet attributes)
  {
    super.addAttributes(attributes);
    typeMap.putAll(attributes.getTypeMap());
  }


  public void removeAttribute(final Object name)
  {
    super.removeAttribute(name);
    typeMap.remove(name);
  }

  public Map getTypeMap()
  {
    return typeMap;
  }

  public void setTypeMap(final Map typeMap)
  {
    this.typeMap = typeMap;
  }

  public AttributeSet getAttributesByType(final Object type)
  {
    final SimpleAttributeSet attr = new SimpleAttributeSet();

    final Iterator it = typeMap.keySet().iterator();
    while (it.hasNext())
    {
      final Object name = it.next();
      if (name != null && typeMap.get(name).equals(type))
      {
        attr.addAttribute(name, getAttribute(name));
      }
    }

    return attr;
  }
}
