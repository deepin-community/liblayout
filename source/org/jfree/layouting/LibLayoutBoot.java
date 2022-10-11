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
 * $Id: LibLayoutBoot.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting;

import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.pentaho.reporting.libraries.base.boot.AbstractBoot;
import org.pentaho.reporting.libraries.base.boot.PackageManager;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.base.config.HierarchicalConfiguration;
import org.pentaho.reporting.libraries.base.config.PropertyFileConfiguration;
import org.pentaho.reporting.libraries.base.config.SystemPropertyConfiguration;
import org.pentaho.reporting.libraries.base.versioning.ProjectInformation;

public class LibLayoutBoot extends AbstractBoot
{
  private static LibLayoutBoot singleton;

  public static synchronized LibLayoutBoot getInstance()
  {
    if (singleton == null)
    {
      singleton = new LibLayoutBoot();
    }
    return singleton;
  }

  private LibLayoutBoot ()
  {
  }

  /**
   * Returns the project info.
   *
   * @return The project info.
   */
  protected ProjectInformation getProjectInfo ()
  {
    return LibLayoutInfo.getInstance();
  }

  /**
   * Loads the configuration.
   *
   * @return The configuration.
   */
  protected Configuration loadConfiguration ()
  {
    final HierarchicalConfiguration globalConfig = new HierarchicalConfiguration();

    final PropertyFileConfiguration rootProperty = new PropertyFileConfiguration();
    rootProperty.load("/org/jfree/layouting/layout.properties");
    globalConfig.insertConfiguration(rootProperty);
    globalConfig.insertConfiguration(getPackageManager().getPackageConfiguration());

    final PropertyFileConfiguration baseProperty = new PropertyFileConfiguration();
    baseProperty.load("/layout.properties");
    globalConfig.insertConfiguration(baseProperty);

    final SystemPropertyConfiguration systemConfig = new SystemPropertyConfiguration();
    globalConfig.insertConfiguration(systemConfig);
    return globalConfig;
  }

  /**
   * Performs the boot.
   */
  protected void performBoot ()
  {
    StyleKeyRegistry.getRegistry().registerDefaults();

    final PackageManager mgr = getPackageManager();
    mgr.addModule(LibLayoutCoreModule.class.getName());
    mgr.load("org.jfree.layouting.modules.");
    mgr.load("org.jfree.layouting.userdefined.modules.");
    mgr.initializeModules();
  }

  public static void main(final String[] args)
  {
    LibLayoutBoot.getInstance().start();
  }

  public static boolean isAsserationEnabled()
  {
    return getInstance().getExtendedConfig().getBoolProperty
        ("org.jfree.layouting.EnableAssertations");
  }
}
