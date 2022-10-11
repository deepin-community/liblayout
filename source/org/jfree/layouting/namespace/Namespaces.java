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
 * $Id: Namespaces.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.namespace;

import java.util.ArrayList;
import java.util.Iterator;

import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.base.util.DebugLog;


/**
 * Known and supported namespaces.
 *
 * @author Thomas Morgner
 */
public final class Namespaces
{
  public static final String LIBLAYOUT_NAMESPACE =
      "http://jfreereport.sourceforge.net/namespaces/layout";

  /**
   * The XML-Namespace is used for the 'id' attribute.
   */
  public static final String XML_NAMESPACE =
      "http://www.w3.org/XML/1998/namespace";

  /**
   * The XML-Namespace is used for the 'id' attribute.
   */
// The old HTML namespace is not supported, use XHTML instead.
//  public static final String HTML_NAMESPACE =
//          "http://www.w3.org/TR/REC-html40";
  public static final String XHTML_NAMESPACE =
      "http://www.w3.org/1999/xhtml";


  private Namespaces()
  {
  }

  public static NamespaceDefinition[] createFromConfig
      (final Configuration config,
       final String prefix,
       final ResourceManager resourceManager)
  {
    final ArrayList retvals = new ArrayList();
    final Iterator keys = config.findPropertyKeys(prefix);
    while (keys.hasNext())
    {
      final String key = (String) keys.next();
      if (key.endsWith(".Uri") == false)
      {
        continue;
      }
      final String nsPrefix = key.substring(0, key.length() - 3);
      final String uri = config.getConfigProperty(key);
      if (uri == null)
      {
        continue;
      }
      final String trimmedUri = uri.trim();
      if (trimmedUri.length() == 0)
      {
        continue;
      }
      final String classAttr = config.getConfigProperty(nsPrefix + "ClassAttr");
      final String styleAttr = config.getConfigProperty(nsPrefix + "StyleAttr");
      final String prefixAttr = config.getConfigProperty(nsPrefix + "Prefix");
      final String defaultStyle = config.getConfigProperty(
          nsPrefix + "Default-Style");

      ResourceKey styleResourceKey = null;
      if (resourceManager != null)
      {
        try
        {
          if (defaultStyle != null)
          {
            styleResourceKey = resourceManager.createKey(defaultStyle);
          }
        }
        catch (ResourceException e)
        {
          // ignored ..
          DebugLog.log("Unable to create resourcekey for style " + trimmedUri);
        }
      }
      retvals.add(new DefaultNamespaceDefinition
          (trimmedUri, styleResourceKey, classAttr, styleAttr, prefixAttr));
    }

    return (NamespaceDefinition[])
        retvals.toArray(new NamespaceDefinition[retvals.size()]);
  }
}
