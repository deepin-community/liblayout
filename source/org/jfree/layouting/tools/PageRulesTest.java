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
 * $Id: PageRulesTest.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.tools;

import java.io.File;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.layouter.style.resolver.ResolverFactory;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.base.util.DebugLog;

/**
 * Creation-Date: 23.05.2006, 15:42:33
 *
 * @author Thomas Morgner
 */
public class PageRulesTest
{
  private PageRulesTest()
  {
  }

  public static void main(final String[] args) throws ResourceException,
      ResourceCreationException, ResourceLoadingException
  {
    LibLayoutBoot.getInstance().start();
    DebugLog.log("Start..." + StyleKeyRegistry.getRegistry().getKeyCount());

    ResolverFactory.getInstance().registerDefaults();

    DebugLog.log ("---------------------------------------------------------");
    final File defaultStyleURL = new File
            ("/home/src/jfreereport/head/liblayout/resource/pages.css");

    final ResourceManager manager = new ResourceManager();
    manager.registerDefaults();
    final Resource res = manager.createDirectly(defaultStyleURL, StyleSheet.class);
    final StyleSheet s = (StyleSheet) res.getResource();
    DebugLog.log("S");
  }
}
