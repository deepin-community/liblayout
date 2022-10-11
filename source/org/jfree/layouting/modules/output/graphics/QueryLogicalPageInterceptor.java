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
 * $Id: QueryLogicalPageInterceptor.java 2755 2007-04-10 19:27:09Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.graphics;

import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PhysicalPageKey;

/**
 * Creation-Date: 10.11.2006, 20:41:29
 *
 * @author Thomas Morgner
 */
public class QueryLogicalPageInterceptor implements GraphicsContentInterceptor
{
  private PageDrawable drawable;
  private LogicalPageKey pageKey;

  public QueryLogicalPageInterceptor(final LogicalPageKey pageKey)
  {
    this.pageKey = pageKey;
  }

  public boolean isLogicalPageAccepted(final LogicalPageKey key)
  {
    return pageKey.equals(key);
  }

  public void processLogicalPage(final LogicalPageKey key, final PageDrawable page)
  {
    this.drawable = page;
  }

  public boolean isPhysicalPageAccepted(final PhysicalPageKey key)
  {
    return false;
  }

  public void processPhysicalPage(final PhysicalPageKey key, final PageDrawable page)
  {
  }

  public boolean isMoreContentNeeded()
  {
    return drawable == null;
  }

  public PageDrawable getDrawable()
  {
    return drawable;
  }
}
