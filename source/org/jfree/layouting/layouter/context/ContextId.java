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
 * $Id: ContextId.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.context;

/**
 * Creation-Date: 05.12.2005, 18:04:27
 *
 * @author Thomas Morgner
 */
public class ContextId
{
  public static final int SOURCE_NORMALIZER = 1;
  public static final int SOURCE_DISPLAY_MODEL = 2;
  public static final int SOURCE_RENDERER = 3;

  private long id;       // a document wide, unique id
  private int source;
  private long parentId; // for replaced content

  public ContextId(final int source, final long parentId, final long id)
  {
    this.source = source;
    this.parentId = parentId;
    this.id = id;
  }

  public long getId()
  {
    return id;
  }

  public long getParentId()
  {
    return parentId;
  }

  public int getSource()
  {
    return source;
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final ContextId contextId = (ContextId) o;

    if (id != contextId.id)
    {
      return false;
    }
    if (source != contextId.source)
    {
      return false;
    }
    return parentId == contextId.parentId;

  }

  public int hashCode()
  {
    int result = (int) (id ^ (id >>> 32));
    result = 29 * result + (int) (parentId ^ (parentId >>> 32));
    result = 29 * result + source;
    return result;
  }


  public String toString()
  {
    return "ContextId{" +
            "source=" + source +
            "id=" + id +
            ", parentId=" + parentId +
        '}';
  }

}
