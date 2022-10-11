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
 * $Id: PdfOutputModule.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.pdf;

import org.pentaho.reporting.libraries.base.boot.AbstractModule;
import org.pentaho.reporting.libraries.base.boot.ModuleInitializeException;
import org.pentaho.reporting.libraries.base.boot.SubSystem;
import org.pentaho.reporting.libraries.fonts.itext.ITextFontRegistry;

/**
 * Creation-Date: 02.12.2006, 17:02:40
 *
 * @author Thomas Morgner
 */
public class PdfOutputModule extends AbstractModule
{
  /**
   * A constant for the encryption type (40 bit).
   */
  public static final String SECURITY_ENCRYPTION_NONE = "none";

  /**
   * A constant for the encryption type (40 bit).
   */
  public static final String SECURITY_ENCRYPTION_40BIT = "40bit";

  /**
   * A constant for the encryption type (128 bit).
   */
  public static final String SECURITY_ENCRYPTION_128BIT = "128bit";
  private static ITextFontRegistry fontRegistry;


  public PdfOutputModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  public static synchronized ITextFontRegistry getFontRegistry()
  {
    if (fontRegistry == null)
    {
      fontRegistry = new ITextFontRegistry();
      fontRegistry.initialize();
    }
    return fontRegistry;
  }

  /**
   * Initialialize the font factory when this class is loaded and the system property of
   * <code>"org.jfree.report.modules.output.pageable.itext.PDFOutputTarget.AutoInit"</code>
   * is set to <code>true</code>.
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize (final SubSystem subSystem)
          throws ModuleInitializeException
  {
    if (isClassLoadable("com.lowagie.text.Document", PdfOutputModule.class) == false)
    {
      throw new ModuleInitializeException("Unable to load iText classes. " +
              "Check your classpath configuration.");
    }

    if ("onInit".equals(subSystem.getGlobalConfig().getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.AutoInit")))
    {
      getFontRegistry();
    }
  }
}
