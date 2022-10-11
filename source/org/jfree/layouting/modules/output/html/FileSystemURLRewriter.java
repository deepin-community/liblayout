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
 * $Id: FileSystemURLRewriter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.pentaho.reporting.libraries.repository.ContentEntity;
import org.pentaho.reporting.libraries.repository.Repository;
import org.pentaho.reporting.libraries.repository.UrlRepository;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.base.util.IOUtils;


/**
 * This URL rewriter assumes that the content repository is an URL based
 * repository and that each content entity can be resolved to an URL.
 *
 * @author Thomas Morgner
 */
public class FileSystemURLRewriter implements URLRewriter
{
  public FileSystemURLRewriter()
  {
  }

  public String rewrite(final ContentEntity sourceDocument, final ContentEntity dataEntity)
      throws URLRewriteException
  {
    final Repository dataRepository = dataEntity.getRepository();
    if (dataRepository instanceof UrlRepository == false)
    {
      // cannot proceed ..
      throw new URLRewriteException("DataRepository is no URL-Repository.");
    }

    final UrlRepository dataUrlRepo = (UrlRepository) dataRepository;
    final String dataPath = buildPath(dataEntity);
    final URL dataItemUrl;
    try
    {
      dataItemUrl = new URL(dataUrlRepo.getURL(), dataPath);
    }
    catch (MalformedURLException e)
    {
      // cannot proceed ..
      throw new URLRewriteException("DataEntity has no valid URL.");
    }

    final Repository documentRepository = sourceDocument.getRepository();
    if (documentRepository instanceof UrlRepository == false)
    {
      // If at least the data entity has an URL, we can always fall back
      // to an global URL..
      return dataItemUrl.toExternalForm();
    }

    try
    {
      final UrlRepository documentUrlRepo = (UrlRepository) documentRepository;
      final String documentPath = buildPath(sourceDocument);
      final URL documentUrl = new URL(documentUrlRepo.getURL(), documentPath);
      return IOUtils.getInstance().createRelativeURL(dataItemUrl, documentUrl);
    }
    catch (MalformedURLException e)
    {
      // If at least the data entity has an URL, we can always fall back
      // to an global URL..
      return dataItemUrl.toExternalForm();
    }
  }

  private String buildPath(final ContentEntity entity)
  {
    final ArrayList entityNames = new ArrayList();
    entityNames.add(entity.getName());

    ContentLocation location = entity.getParent();
    while (location != null)
    {
      entityNames.add(location.getName());
      location = location.getParent();
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
