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
 * $Id: SingleRepositoryURLRewriter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.html;

import java.util.ArrayList;

import org.pentaho.reporting.libraries.repository.ContentEntity;
import org.pentaho.reporting.libraries.repository.ContentLocation;

/**
 * This URL-Rewriter assumes that both the content and data entity have been
 * created from the same repository. This one builds a relative URL connecting
 * the data entity with the content.
 *
 * @author Thomas Morgner
 */
public class SingleRepositoryURLRewriter implements URLRewriter
{
  public SingleRepositoryURLRewriter()
  {
  }

  public String rewrite(final ContentEntity sourceDocument,
                        final ContentEntity dataEntity)
      throws URLRewriteException
  {
    if (sourceDocument.getRepository().equals(dataEntity.getRepository()) == false)
    {
      // cannot proceed ..
      throw new URLRewriteException("Content and data repository must be the same.");
    }

    final ArrayList entityNames = new ArrayList();
    entityNames.add(dataEntity.getName());

    ContentLocation location = dataEntity.getParent();
    while (location != null)
    {
      entityNames.add(location.getName());
      location = location.getParent();
    }

    final ArrayList contentNames = new ArrayList();
    location = sourceDocument.getParent();
    while (location != null)
    {
      contentNames.add(location.getName());
      location = location.getParent();
    }

    // now remove all path elements that are equal ..
    while (contentNames.isEmpty() == false && entityNames.isEmpty() == false)
    {
      final String lastEntity = (String) entityNames.get(entityNames.size() - 1);
      final String lastContent = (String) contentNames.get(contentNames.size() - 1);
      if (lastContent.equals(lastEntity) == false)
      {
        break;
      }
      entityNames.remove(entityNames.size() - 1);
      contentNames.remove(contentNames.size() - 1);
    }

    final StringBuffer b = new StringBuffer();
    for (int i = entityNames.size() - 1; i >= 0; i--)
    {
      final String name = (String) entityNames.get(i);
      b.append(name);
      if (i != 0)
      {
        b.append('/');
      }
    }
    return b.toString();
  }
}
