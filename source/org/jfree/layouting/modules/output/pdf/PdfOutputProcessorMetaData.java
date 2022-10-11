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
 * $Id: PdfOutputProcessorMetaData.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.pdf;

import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.output.AbstractOutputProcessorMetaData;
import org.pentaho.reporting.libraries.fonts.itext.ITextFontStorage;
import org.pentaho.reporting.libraries.fonts.registry.FontFamily;
import org.pentaho.reporting.libraries.fonts.FontMappingUtility;

/**
 * Creation-Date: 12.11.2006, 13:28:55
 *
 * @author Thomas Morgner
 */
public class PdfOutputProcessorMetaData extends AbstractOutputProcessorMetaData
{
  public PdfOutputProcessorMetaData(final ITextFontStorage fontStorage)
  {
    super(fontStorage);

    setFamilyMapping(FontFamilyValues.CURSIVE, "times-roman,italic");
    setFamilyMapping(FontFamilyValues.FANTASY, "helvetica");
    setFamilyMapping(FontFamilyValues.MONOSPACE, "courier");
    setFamilyMapping(FontFamilyValues.SERIF, "times-roman");
    setFamilyMapping(FontFamilyValues.SANS_SERIF, "helvetica");
  }

  public FontFamily getDefaultFontFamily()
  {
    return getFontRegistry().getFontFamily("helvetica");
  }

  public String getExportDescriptor()
  {
    return "pageable/pdf";
  }

  public String getNormalizedFontFamilyName(final String name)
  {
    final String mappedName = super.getNormalizedFontFamilyName(name);
    if (FontMappingUtility.isSerif(mappedName))
    {
      return "Times";
    }
    if (FontMappingUtility.isSansSerif(mappedName))
    {
      return "Helvetica";
    }
    if (FontMappingUtility.isCourier(mappedName))
    {
      return "Courier";
    }
    if (FontMappingUtility.isSymbol(mappedName))
    {
      return "Symbol";
    }
    return mappedName;
  }

  public ITextFontStorage getITextFontStorage()
  {
    return (ITextFontStorage) getFontStorage();
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
    return true;
  }
}
