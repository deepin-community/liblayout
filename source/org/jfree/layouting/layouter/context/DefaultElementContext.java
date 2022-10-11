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
 * $Id: DefaultElementContext.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.context;

import java.util.HashMap;

public class DefaultElementContext implements ElementContext
{
  private HashMap strings;
  private HashMap counters;
  private ElementContext parentContext;

  public DefaultElementContext(final ElementContext parentContext)
  {
    this.parentContext = parentContext;
  }

  /**
   * Returns the value for the given counter. If no counter exists under that
   * name, this method returns 0.
   *
   * @param counterName
   * @return the value for the given counter.
   */
  public int getCounterValue(final String counterName)
  {
    if (counters != null)
    {
      final Integer counterValue = (Integer) counters.get(counterName);
      if (counterValue != null)
      {
        return counterValue.intValue();
      }
    }
    if (parentContext != null)
    {
      return parentContext.getCounterValue(counterName);
    }
    return 0;
  }

  /**
   * Increments the counter with the given name. If no counter is known under
   * that name, the root node will create one.
   *
   * @param name
   * @param value
   */
  public void incrementCounter(final String name, final int value)
  {
    // Step 1: Check if the counter is locally defined.
    if (counters != null)
    {
      if (counters.containsKey(name))
      {
        final int oldValue = getCounterValue(name);
        counters.put(name, new Integer(oldValue + value));
        return;
      }
    }

    // check if we have a parent, which may know that counter ...
    if (parentContext != null)
    {
      parentContext.incrementCounter(name, value);
      return;
    }

    // ok, being desperate: Create a new one ..
    final int oldValue = getCounterValue(name);
    if (counters == null)
    {
      counters = new HashMap();
    }
    counters.put(name, new Integer(oldValue + value));
  }

  /**
   * Reseting an counter creates a new Counter-Instance. Counters from parent
   * elements are not affected and remain unchanged. All further operations
   * issued by all sub-elements will now work with this counter.
   *
   * @param name
   * @param value
   */
  public void resetCounter(final String name, final int value)
  {
    if (counters == null)
    {
      counters = new HashMap();
    }
    counters.put(name, new Integer(value));
  }

  public boolean isCounterDefined(final String counterName)
  {
    if (counters == null)
    {
      return false;
    }
    return counters.containsKey(counterName);
  }

  /**
   * Sets a named string.
   *
   * @param name   the name
   * @param value  the value
   * @param define if set to true, this defines a new nesting context.
   */
  public void setString(final String name, final String value, final boolean define)
  {
    if (!define)
    {
      // not define ...
      if (parentContext != null)
      {
        parentContext.setString(name, value, define);
        return;
      }
    }

    if (value == null)
    {
      if (strings != null)
      {
        strings.remove(name);
      }
      // else ignore the request
    }
    else
    {
      if (strings == null)
      {
        strings = new HashMap();
      }
      strings.put(name, value);
    }
  }

  /**
   * Retrieves the value for a given string. The value returned always
   * represents the *actual* value, ignoring any possibly defined
   * page-policies.
   *
   * @param name
   * @return
   */
  public String getString(final String name)
  {
    if (strings != null)
    {
      final String value = (String) strings.get(name);
      if (value != null)
      {
        return value;
      }
    }

    if (parentContext != null)
    {
      return parentContext.getString(name);
    }
    else
    {
      return null;
    }
  }
}
