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
 * $Id: OutputProcessorMetaData.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.output;

import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.pentaho.reporting.libraries.fonts.registry.FontFamily;
import org.pentaho.reporting.libraries.fonts.registry.FontStorage;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;

/**
 * Creation-Date: 14.12.2005, 13:47:00
 *
 * @author Thomas Morgner
 */
public interface OutputProcessorMetaData
{
  public boolean isFeatureSupported (OutputProcessorFeature.BooleanOutputProcessorFeature feature);
  public double getNumericFeatureValue (OutputProcessorFeature.NumericOutputProcessorFeature feature);

  public String getNormalizedFontFamilyName (final String name);
  public FontFamily getDefaultFontFamily();
  public FontFamily getFontFamilyForGenericName(CSSConstant genericName);

  /**
   * Although most font systems are global, some may have some issues with
   * caching. OutputTargets may have to tweak the font storage system to their
   * needs.
   *
   * @return
   */
  public FontStorage getFontStorage();

  public double getFontSize (CSSConstant constant);

  /**
   * Returns the media type of the output target. This corresponds directly to
   * the CSS defined media types and is used as a selector.
   *
   * @return the media type of the output target.
   */
  public String getMediaType();

  /**
   * The export descriptor is a string that describes the output characteristics.
   * For libLayout outputs, it should start with the output class (one of
   * 'pageable', 'flow' or 'stream'), followed by '/liblayout/' and finally
   * followed by the output type (ie. PDF, Print, etc).
   *
   * @return the export descriptor.
   */
  public String getExportDescriptor();

  /**
   * Returns the default physical page size. If not defined otherwise, this
   * will also be the logical size.
   *
   * @return
   */
  public PageSize getDefaultPageSize();

  /**
   * Returns the vertical page span. If the value is less than one, it will
   * be corrected to one.
   *
   * @return
   */
  public int getVerticalPageSpan ();

  /**
   * Returns the horizontal page span. If the value is less than one, it will
   * be corrected to one.
   *
   * @return
   */
  public int getHorizontalPageSpan ();

  public boolean isValid (FontSpecification spec);

  public FontMetrics getFontMetrics(FontSpecification spec);

  public Class[] getSupportedResourceTypes();

  /**
   * An iterative output processor accepts and processes small content chunks.
   * If this method returns false, the output processor will not receive the
   * content until the whole document is processed.
   *
   * @return
   */
  public boolean isIterative();
}
