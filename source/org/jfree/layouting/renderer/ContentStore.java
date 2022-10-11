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
 * $Id: ContentStore.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer;

import org.jfree.layouting.input.style.keys.page.PagePolicy;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.renderer.model.RenderNode;

/**
 * For the first throw, the content ramins very simple. We support the 4 modes:
 * start - the initial content is used. first - the first value set in this page
 * is used (else the initial content) last - the last value is used. last-except
 * - the last value is used on the next page. (Contrary to the specification, we
 * fall back to the start-value instead of using an empty value).
 *
 * @author Thomas Morgner
 */
public class ContentStore extends AbstractStore
{
  public ContentStore()
  {
  }

  public void add(final String name, final RenderNode[] value)
  {
    super.addInternal(name, value);
  }

  public RenderNode[] get(final String name)
  {
    return get(name, PagePolicy.LAST);
  }

  public RenderNode[] get(final String name, final CSSValue pagePolicy)
  {
    if (PagePolicy.START.equals(pagePolicy))
    {
      final RenderNode[] initial = (RenderNode[]) getInitialInternal(name);
      if (initial == null)
      {
        return null;
      }
      return deriveRetval(initial);
    }
    else if (PagePolicy.FIRST.equals(pagePolicy))
    {
      final RenderNode[] first = (RenderNode[]) getFirstInternal(name);
      if (first == null)
      {
        return null;
      }
      return deriveRetval(first);
    }
    else
    {
      final RenderNode[] last = (RenderNode[]) getLastInternal(name);
      if (last == null)
      {
        return null;
      }
      return deriveRetval(last);
    }
  }

  private RenderNode[] deriveRetval(final RenderNode[] val)
  {
    if (val == null)
    {
      return new RenderNode[0];
    }
    final RenderNode[] reval = new RenderNode[val.length];
    for (int i = 0; i < val.length; i++)
    {
      final RenderNode node = val[i];
      reval[i] = node.derive(true);
    }
    return val;
  }

}
