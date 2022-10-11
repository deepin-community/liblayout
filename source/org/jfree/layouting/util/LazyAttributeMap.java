package org.jfree.layouting.util;

import java.util.Map;

/**
 * Todo: Document Me
 *
 * @author Thomas Morgner
 */
public class LazyAttributeMap extends AttributeMap
{
  private AttributeMap lazyMap;

  public LazyAttributeMap(final AttributeMap copy)
  {
    if (copy == null)
    {
      return;
    }
    if (copy.isReadOnly() == false)
    {
      copyInto(copy);
      return;
    }

    lazyMap = copy;
  }

  public Object setAttribute(final String namespace, final String attribute, final Object value)
  {
    if (isReadOnly())
    {
      throw new IllegalStateException();
    }
    if (lazyMap != null)
    {
      copyInto(lazyMap);
      lazyMap = null;
    }
    return super.setAttribute(namespace, attribute, value);
  }

  public AttributeMap createUnmodifiableMap()
  {
    if (lazyMap != null)
    {
      return lazyMap;
    }
    return super.createUnmodifiableMap();
  }

  public boolean isEmpty()
  {
    if (lazyMap != null)
    {
      return lazyMap.isEmpty();
    }
    return super.isEmpty();
  }

  public Object getAttribute(final String namespace, final String attribute)
  {
    if (lazyMap != null)
    {
      return lazyMap.getAttribute(namespace, attribute);
    }
    return super.getAttribute(namespace, attribute);
  }

  public Object getFirstAttribute(final String attribute)
  {
    if (lazyMap != null)
    {
      return lazyMap.getFirstAttribute(attribute);
    }
    return super.getFirstAttribute(attribute);
  }

  public Map getAttributes(final String namespace)
  {
    if (lazyMap != null)
    {
      return lazyMap.getAttributes(namespace);
    }
    return super.getAttributes(namespace);
  }

  public String[] getNameSpaces()
  {
    if (lazyMap != null)
    {
      return lazyMap.getNameSpaces();
    }
    return super.getNameSpaces();
  }

  public long getChangeTracker()
  {
    if (lazyMap != null)
    {
      return lazyMap.getChangeTracker();
    }
    return super.getChangeTracker();
  }
}
