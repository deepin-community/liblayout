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
 * $Id: StyleBuilder.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.html;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.values.CSSValue;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.base.util.StringUtils;

/**
 * Creation-Date: 26.11.2006, 12:46:22
 *
 * @author Thomas Morgner
 */
public class StyleBuilder
{
  private static final String INDENT = "    ";

  private boolean compact;
  private Map backend;
  private Map parentBackend;

  public StyleBuilder(final boolean compact, final StyleBuilder parent)
  {
    this(compact);
    if (parent != null)
    {
      this.parentBackend = new TreeMap();
      if (parent.parentBackend != null)
      {
        this.parentBackend.putAll(parent.parentBackend);
      }
      this.parentBackend.putAll(parent.backend);
    }
  }

  public StyleBuilder(final boolean compact)
  {
    this.compact = compact;
    this.backend = new TreeMap();
    this.parentBackend = null;
  }

  public void append(final StyleKey key, final CSSValue value)
  {
    if (value == null)
    {
      return;
    }

    final String cssText = value.getCSSText();
    final String name = key.getName();
    if (parentBackend == null || key.isInherited() == false)
    {
      backend.put(name, cssText);
      return;
    }

    if (parentContains(name, cssText) == false)
    {
      backend.put(name, cssText);
    }
  }

  public void append(final String key, final boolean inheritable, final String value)
  {
    if (parentBackend == null || inheritable == false)
    {
      backend.put(key, value);
      return;
    }

    if (parentContains(key, value) == false)
    {
      backend.put(key, value);
    }
  }

  public void append(final StyleKey key, final String value, final String unit)
  {
    if (value == null)
    {
      return;
    }
    final String textvalue = value + unit;
    final String name = key.getName();
    if (parentBackend == null || key.isInherited() == false)
    {
      backend.put(name, textvalue);
      return;
    }

    if (parentContains(name, textvalue) == false)
    {
      backend.put(name, textvalue);
    }
  }

  public void append(final StyleKey key, final String text)
  {
    final String name = key.getName();
    if (parentBackend == null || key.isInherited() == false)
    {
      backend.put(name, text);
      return;
    }

    if (parentContains(name, text) == false)
    {
      backend.put(name, text);
    }
  }
//
//  public void append(final String key, final String value, final String unit)
//  {
//    final String textvalue = value + unit;
//    if (parentBackend == null)
//    {
//      backend.put(key, textvalue);
//      return;
//    }
//
//    if (parentContains(key, textvalue) == false)
//    {
//      backend.put(key, textvalue);
//    }
//  }

  public boolean parentContains(final String key, final String value)
  {
    if (parentBackend == null)
    {
      return false;
    }

    final Object o1 = parentBackend.get(key);
    return ObjectUtilities.equal(o1, value);
  }

  public String toString()
  {
    final StringBuffer style = new StringBuffer();
    if (compact == false)
    {
      final Iterator it = backend.entrySet().iterator();
      while (it.hasNext())
      {
        if (style.length() != 0)
        {
          style.append(StringUtils.getLineSeparator());
        }
        style.append(INDENT);

        final Map.Entry entry = (Map.Entry) it.next();
        style.append(entry.getKey());
        style.append(": ");
        style.append(entry.getValue());
        style.append(';');
      }
    }
    else
    {
      final Iterator it = backend.entrySet().iterator();
      while (it.hasNext())
      {
        final Map.Entry entry = (Map.Entry) it.next();
        style.append(entry.getKey());
        style.append(": ");
        style.append(entry.getValue());
        style.append("; ");
      }
    }
    return style.toString();
  }

  public static void main(final String[] args)
  {
    final StyleBuilder level1 = new StyleBuilder(false);
    level1.append(BoxStyleKeys.PADDING_TOP, "red");

    final StyleBuilder level2 = new StyleBuilder(false, level1);
    level2.append(BoxStyleKeys.PADDING_TOP, "red");

    final StyleBuilder level3 = new StyleBuilder(false, level2);
    level3.append(BoxStyleKeys.PADDING_TOP, "red");
  }

  public boolean isEmpty()
  {
    return backend.isEmpty();
  }
}
