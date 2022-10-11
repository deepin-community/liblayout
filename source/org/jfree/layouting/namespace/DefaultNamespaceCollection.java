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
 * $Id: DefaultNamespaceCollection.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.namespace;

import java.util.HashMap;

/**
 * Creation-Date: 13.04.2006, 12:38:42
 *
 * @author Thomas Morgner
 */
public class DefaultNamespaceCollection implements NamespaceCollection
{
  private HashMap namespaces;

  public DefaultNamespaceCollection()
  {
    namespaces = new HashMap();
  }

  public void addDefinitions (final NamespaceDefinition[] definitions)
  {
    for (int i = 0; i < definitions.length; i++)
    {
      final NamespaceDefinition definition = definitions[i];
      namespaces.put(definition.getURI(), definition);
    }
  }

  public void addDefinition (final NamespaceDefinition definition)
  {
    namespaces.put(definition.getURI(), definition);
  }

  public synchronized String[] getNamespaces()
  {
    return (String[]) namespaces.keySet().toArray
            (new String[namespaces.size()]);
  }

  public NamespaceDefinition getDefinition(final String namespace)
  {
    return (NamespaceDefinition) namespaces.get(namespace);
  }
}
