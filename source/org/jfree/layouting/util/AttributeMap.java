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
 * $Id: AttributeMap.java 6513 2008-11-28 18:42:46Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

/**
 * Creation-Date: 09.04.2006, 16:12:13
 *
 * @author Thomas Morgner
 */
public class AttributeMap implements Serializable, Cloneable
{
  private static final Log logger = LogFactory.getLog(AttributeMap.class);

  private static final long serialVersionUID = -7442871030874215436L;
  private static final String[] EMPTY_NAMESPACES = new String[0];

  private String[] namespacesArray;
  private HashMap namespaces;
  private boolean readOnly;
  private long changeTracker;

  public AttributeMap()
  {
  }

  public AttributeMap(final AttributeMap copy)
  {
    if (copy == null)
    {
      return;
    }

    if (copy.namespaces == null)
    {
      return;
    }

    copyInto(copy);
  }

  protected void copyInto(final AttributeMap copy)
  {
    changeTracker = copy.changeTracker;
    namespaces = (HashMap) copy.namespaces.clone();
    final Iterator entries = namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final HashMap value = (HashMap) entry.getValue();
      entry.setValue(value.clone());
    }
  }

  public AttributeMap createUnmodifiableMap()
  {
    if (readOnly)
    {
      return this;
    }
    try
    {
      final AttributeMap o = (AttributeMap) super.clone();
      o.readOnly = true;
      return o;
    }
    catch (Exception e)
    {
      logger.error("Clone failed for ReportAttributeMap.createUnmodifiableMap", e);
      throw new IllegalStateException("Clone failed for ReportAttributeMap.createUnmodifiableMap");
    }
  }


  public Object clone()
      throws CloneNotSupportedException
  {
    final AttributeMap map = (AttributeMap) super.clone();
    map.namespaces = (HashMap) namespaces.clone();
    final Iterator entries = map.namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final HashMap value = (HashMap) entry.getValue();
      entry.setValue(value.clone());
    }
    return map;
  }


  public Object setAttribute(final String namespace, final String attribute, final Object value)
  {
    if (readOnly)
    {
      throw new UnsupportedOperationException("This collection is marked as read-only");
    }
    final Object oldValue = setAttributeInternal(namespace, attribute, value);
    if (ObjectUtilities.equal(oldValue, value) == false)
    {
      changeTracker += 1;
    }
    return oldValue;
  }

  protected Object setAttributeInternal(final String namespace,
                                     final String attribute,
                                     final Object value)
  {
    if (namespaces == null)
    {
      namespaces = new HashMap();
    }

    final HashMap attrs = (HashMap) namespaces.get(namespace);
    if (attrs == null)
    {
      if (value == null)
      {
        return null;
      }

      final HashMap newAtts = new HashMap();
      newAtts.put(attribute, value);
      namespacesArray = null;
      return namespaces.put(namespace, newAtts);
    }
    else
    {
      if (value == null)
      {
        attrs.remove(attribute);
        if (attrs.isEmpty())
        {
          namespacesArray = null;
          namespaces.remove(namespace);
        }
        return null;
      }
      else
      {
        return attrs.put(attribute, value);
      }
    }
  }

  public boolean isEmpty()
  {
    if (namespaces == null)
    {
      return true;
    }
    return namespaces.isEmpty();
  }

  public Object getAttribute(final String namespace,
                             final String attribute)
  {
    if (namespaces == null)
    {
      return null;
    }

    final HashMap attrs = (HashMap) namespaces.get(namespace);
    if (attrs == null)
    {
      return null;
    }
    else
    {
      return attrs.get(attribute);
    }
  }

  public Object getFirstAttribute(final String attribute)
  {
    if (namespaces == null)
    {
      return null;
    }

    final Iterator entries = namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final HashMap map = (HashMap) entry.getValue();
      final Object val = map.get(attribute);
      if (val != null)
      {
        return val;
      }
    }
    return null;
  }

  public Map getAttributes(final String namespace)
  {
    if (namespaces == null)
    {
      return null;
    }

    final HashMap attrs = (HashMap) namespaces.get(namespace);
    if (attrs == null)
    {
      return null;
    }
    else
    {
      return Collections.unmodifiableMap(attrs);
    }
  }

  public String[] getNameSpaces()
  {
    if (namespaces == null)
    {
      return AttributeMap.EMPTY_NAMESPACES;
    }
    if (namespacesArray != null)
    {
      return namespacesArray.clone();
    }
    namespacesArray = (String[]) namespaces.keySet().toArray(new String[namespaces.size()]);
    return namespacesArray.clone();
  }

  public void makeReadOnly()
  {
    this.readOnly = true;
  }

  public boolean isReadOnly()
  {
    return readOnly;
  }

  public long getChangeTracker()
  {
    return changeTracker;
  }

}
