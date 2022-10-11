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
 * $Id: HtmlOutputProcessorMetaData.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.html;

import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.output.AbstractOutputProcessorMetaData;
import org.pentaho.reporting.libraries.fonts.registry.FontStorage;
import org.pentaho.reporting.libraries.fonts.registry.DefaultFontStorage;
import org.pentaho.reporting.libraries.fonts.registry.FontFamily;
import org.pentaho.reporting.libraries.fonts.awt.AWTFontRegistry;

/**
 * Creation-Date: 02.01.2006, 18:38:20
 *
 * @author Thomas Morgner
 */
public class HtmlOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  public static final int PAGINATION_NONE = 0;
  public static final int PAGINATION_MANUAL = 1;
  public static final int PAGINATION_FULL = 2;

  private int paginationMode;

  public HtmlOutputProcessorMetaData(final int paginationMode)
  {
    this(createFontStorage(), paginationMode);
  }

  private static FontStorage createFontStorage()
  {
    return new DefaultFontStorage(new AWTFontRegistry());
  }

  public HtmlOutputProcessorMetaData(final FontStorage fontStorage,
                                     final int paginationMode)
  {
    super(fontStorage);
    this.paginationMode = paginationMode;

    setFamilyMapping(FontFamilyValues.CURSIVE, "sans-serif");
    setFamilyMapping(FontFamilyValues.FANTASY, "fantasy");
    setFamilyMapping(FontFamilyValues.MONOSPACE, "monospace");
    setFamilyMapping(FontFamilyValues.SERIF, "serif");
    setFamilyMapping(FontFamilyValues.SANS_SERIF, "sans-serif");
  }

  public FontFamily getDefaultFontFamily()
  {
    return getFontRegistry().getFontFamily("sans-serif");
  }

  public String getExportDescriptor()
  {
    if (paginationMode == PAGINATION_FULL)
    {
      return "pageable/html";
    }

    return "streaming/html";
  }

  /**
   * An iterative output processor accepts and processes small content chunks.
   * If this method returns false, the output processor will not receive the
   * content until the whole document is processed.
   *
   * @return
   */
  public boolean isIterative()
  {
    return paginationMode != PAGINATION_NONE;
  }
}
