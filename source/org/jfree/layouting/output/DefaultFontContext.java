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
 * $Id: DefaultFontContext.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.output;

import org.pentaho.reporting.libraries.fonts.registry.FontContext;


/**
 * Creation-Date: 23.06.2006, 17:37:56
 *
 * @author Thomas Morgner
 */
public class DefaultFontContext implements FontContext
{
  private double size;
  private boolean fractionalMetrics;
  private boolean antiAliasing;
  private String encoding;
  private boolean embedded;

  public DefaultFontContext(final OutputProcessorMetaData metaData,
                            final boolean antiAliased,
                            final double size,
                            final String encoding,
                            final boolean embedded)
  {
    this.size = size;
    this.fractionalMetrics =
            metaData.isFeatureSupported
                    (OutputProcessorFeature.FONT_FRACTIONAL_METRICS);

    this.antiAliasing =
            antiAliased && metaData.isFeatureSupported
                    (OutputProcessorFeature.FONT_SUPPORTS_ANTI_ALIASING);

    this.encoding = encoding;
    this.embedded = embedded;
  }

  public String getEncoding()
  {
    return encoding;
  }

  public boolean isEmbedded()
  {
    return embedded;
  }

  /**
   * This is controlled by the output target and the stylesheet. If the output
   * target does not support aliasing, it makes no sense to enable it and all
   * such requests are ignored.
   *
   * @return
   */
  public boolean isAntiAliased()
  {
    return antiAliasing;
  }

  /**
   * This is defined by the output target. This is not controlled by the
   * stylesheet.
   *
   * @return
   */
  public boolean isFractionalMetrics()
  {
    return fractionalMetrics;
  }

  /**
   * The requested font size. A font may have a fractional font size (ie. 8.5
   * point). The font size may be influenced by the output target.
   *
   * @return the font size.
   */
  public double getFontSize()
  {
    return size;
  }
}
