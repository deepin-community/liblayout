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
 * $Id: CounterStyleFactory.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.counters;

import java.util.HashMap;
import java.util.Iterator;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.layouter.counters.numeric.DecimalCounterStyle;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

public class CounterStyleFactory
{
  private static final CounterStyle DEFAULTCOUNTER = new DecimalCounterStyle();

  private static CounterStyleFactory factory;
  public static final String PREFIX = "org.jfree.layouting.numbering.";

  public static synchronized CounterStyleFactory getInstance ()
  {
    if (factory == null)
    {
      factory = new CounterStyleFactory();
      factory.registerDefaults();
    }
    return factory;
  }

  private HashMap knownCounters;

  private CounterStyleFactory ()
  {
    knownCounters = new HashMap();
  }

  public void registerDefaults ()
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    final Iterator it = config.findPropertyKeys(PREFIX);
    while (it.hasNext())
    {
      final String key = (String) it.next();
      final String counterClass = config.getConfigProperty(key);
      if (counterClass == null)
      {
        continue;
      }
      final Object o = ObjectUtilities.loadAndInstantiate
          (counterClass, CounterStyleFactory.class, CounterStyle.class);
      if (o instanceof CounterStyle)
      {
        final String name = key.substring(PREFIX.length());
        knownCounters.put (name, o);
      }
    }
  }

  public CounterStyle getCounterStyle (final String name)
  {
    final CounterStyle cs = (CounterStyle) knownCounters.get(name);
    if (cs != null)
    {
      return cs;
    }
    return DEFAULTCOUNTER;
  }
}
