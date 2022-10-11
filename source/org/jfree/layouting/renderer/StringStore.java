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
 * $Id: StringStore.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer;

import org.jfree.layouting.input.style.keys.page.PagePolicy;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * For the first throw, the content remains very simple. We support the 4 modes:
 * start - the initial content is used. first - the first value set in this page
 * is used (else the initial content) last - the last value is used. last-except
 * - the last value is used on the next page. (Contrary to the specification, we
 * fall back to the start-value instead of using an empty value).
 *
 * The string store is used for all counter, counters and string properties.
 *
 * @author Thomas Morgner
 */
public class StringStore extends AbstractStore
{
  public StringStore()
  {
  }

  public void add(final String name, final String contents)
  {
    addInternal(name, contents);
  }

  public String get(final String name)
  {
    return get(name, PagePolicy.LAST);
  }

  public String get(final String name, final CSSValue pagePolicy)
  {
    if (PagePolicy.START.equals(pagePolicy))
    {
      return (String) getInitialInternal(name);
    }
    else if (PagePolicy.FIRST.equals(pagePolicy))
    {
      return (String) getFirstInternal(name);
    }
    else
    {
      return (String) getLastInternal(name);
    }
  }
}
