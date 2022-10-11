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
 * $Id: PlaintextFontRegistry.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.plaintext;

import org.pentaho.reporting.libraries.fonts.registry.FontRegistry;
import org.pentaho.reporting.libraries.fonts.registry.FontFamily;
import org.pentaho.reporting.libraries.fonts.registry.FontMetricsFactory;
import org.pentaho.reporting.libraries.fonts.cache.FontCache;
import org.pentaho.reporting.libraries.fonts.cache.NullFontCache;


/**
 * Creation-Date: 13.11.2006, 12:50:35
 *
 * @author Thomas Morgner
 */
public class PlaintextFontRegistry implements FontRegistry
{
  private double charWidth;
  private double charHeight;

  public PlaintextFontRegistry(final double charWidth, final double charHeight)
  {
    this.charWidth = charWidth;
    this.charHeight = charHeight;
  }

  public void initialize()
  {

  }

  /**
   * Tries to find a font family with the given name, looking through all
   * alternative font names if neccessary.
   *
   * @param name
   * @return the font family or null, if there is no such family.
   */
  public FontFamily getFontFamily(final String name)
  {
    return null;
  }

  public String[] getRegisteredFamilies()
  {
    return new String[0];
  }

  public String[] getAllRegisteredFamilies()
  {
    return new String[0];
  }

  /**
   * Creates a new font metrics factory. That factory is specific to a certain
   * font registry and is not required to handle font records from foreign font
   * registries.
   * <p/>
   * A font metrics factory should never be used on its own. It should be
   * embedded into and used by a FontStorage implementation.
   *
   * @return
   */
  public FontMetricsFactory createMetricsFactory()
  {
    return new PlaintextFontMetricsFactory(charWidth, charHeight);
  }

  public FontCache getSecondLevelCache()
  {
    return new NullFontCache();
  }
}
