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
 * $Id: AbstractOutputProcessorMetaData.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.output;

import java.awt.Image;
import java.util.HashMap;
import java.util.HashSet;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.font.FontFamilyValues;
import org.jfree.layouting.input.style.keys.font.FontSizeConstant;
import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.pentaho.reporting.libraries.fonts.registry.FontStorage;
import org.pentaho.reporting.libraries.fonts.registry.FontRegistry;
import org.pentaho.reporting.libraries.fonts.registry.FontFamily;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;
import org.pentaho.reporting.libraries.fonts.registry.FontRecord;
import org.pentaho.reporting.libraries.base.config.ExtendedConfiguration;
import org.pentaho.reporting.libraries.base.util.DebugLog;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

/**
 * Creation-Date: 02.01.2006, 18:39:46
 *
 * @author Thomas Morgner
 */
public abstract class AbstractOutputProcessorMetaData
        implements OutputProcessorMetaData
{
  private HashSet features;
  private HashMap fontSizes;
  private HashMap numericFeatures;
  private HashMap fontFamilies;
  private FontStorage fontStorage;
  private static final Class[] SUPPORTED_TYPES = new Class[] {DrawableWrapper.class, Image.class};

  protected AbstractOutputProcessorMetaData(final FontStorage fontStorage)
  {
    if (fontStorage == null)
    {
      throw new NullPointerException();
    }

    this.fontStorage = fontStorage;
    this.features = new HashSet();
    this.numericFeatures = new HashMap();

    final ExtendedConfiguration extendedConfig =
            LibLayoutBoot.getInstance().getExtendedConfig();
    final double defaultFontSize = extendedConfig.getIntProperty
        ("org.jfree.layouting.defaults.FontSize", 12);

    final int xxSmall = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.xx-small", 60);
    final int xSmall = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.x-small", 75);
    final int small = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.small", 89);
    final int medium = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.medium", 100);
    final int large = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.large", 120);
    final int xLarge = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.x-large", 150);
    final int xxLarge = extendedConfig.getIntProperty
            ("org.jfree.layouting.defaults.FontSizeFactor.xx-large", 200);

    fontSizes = new HashMap();
    fontSizes.put(FontSizeConstant.XX_SMALL,
            new Double(defaultFontSize * xxSmall / 100d));
    fontSizes.put(FontSizeConstant.X_SMALL,
            new Double(defaultFontSize * xSmall / 100d));
    fontSizes.put(FontSizeConstant.SMALL,
            new Double(defaultFontSize * small / 100d));
    fontSizes.put(FontSizeConstant.MEDIUM,
            new Double(defaultFontSize * medium / 100d));
    fontSizes.put(FontSizeConstant.LARGE,
            new Double(defaultFontSize * large / 100d));
    fontSizes.put(FontSizeConstant.X_LARGE,
            new Double(defaultFontSize * xLarge / 100d));
    fontSizes.put(FontSizeConstant.XX_LARGE,
            new Double(defaultFontSize * xxLarge / 100d));


    fontFamilies = new HashMap();

    setNumericFeatureValue(OutputProcessorFeature.DEFAULT_FONT_SIZE,
            defaultFontSize);

    final double fontSmoothThreshold =
        extendedConfig.getIntProperty("org.jfree.layouting.defaults.FontSmoothThreshold", 8);
    setNumericFeatureValue(OutputProcessorFeature.FONT_SMOOTH_THRESHOLD, fontSmoothThreshold);

  }

  protected void setFamilyMapping(final CSSConstant family, final String name)
  {
    if (family == null)
    {
      throw new NullPointerException();
    }
    if (name == null)
    {
      throw new NullPointerException();
    }
    fontFamilies.put(family, name);
  }

  public double getFontSize(final CSSConstant constant)
  {
    final Double d = (Double) fontSizes.get(constant);
    if (d == null)
    {
      return getNumericFeatureValue(OutputProcessorFeature.DEFAULT_FONT_SIZE);
    }
    return d.doubleValue();
  }

  protected void addFeature
          (final OutputProcessorFeature.BooleanOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    this.features.add(feature);
  }

  public boolean isFeatureSupported
          (final OutputProcessorFeature.BooleanOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    return this.features.contains(feature);
  }

  protected void setNumericFeatureValue
          (final OutputProcessorFeature.NumericOutputProcessorFeature feature,
           final double value)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    numericFeatures.put(feature, new Double(value));
  }

  public double getNumericFeatureValue
          (final OutputProcessorFeature.NumericOutputProcessorFeature feature)
  {
    if (feature == null)
    {
      throw new NullPointerException();
    }
    final Double d = (Double) numericFeatures.get(feature);
    if (d == null)
    {
      return 0;
    }
    return d.doubleValue();
  }

  /**
   * Although most font systems are global, some may have some issues with
   * caching. OutputTargets may have to tweak the font storage system to their
   * needs.
   *
   * @return
   */
  public FontStorage getFontStorage()
  {
    return fontStorage;
  }

  public String getNormalizedFontFamilyName (final String name)
  {
    final String normalizedFontFamily = (String) fontFamilies.get(name);
    if (normalizedFontFamily == null)
    {
      return name;
    }
    return normalizedFontFamily;
  }

  public FontFamily getFontFamilyForGenericName(final CSSConstant genericName)
  {
    if (FontFamilyValues.NONE.equals(genericName))
    {
      return null;
    }

    final String name = (String) fontFamilies.get(genericName);
    if (name == null)
    {
      return getDefaultFontFamily();
    }

    final FontFamily ff = fontStorage.getFontRegistry().getFontFamily(name);
    if (ff != null)
    {
      return ff;
    }
    return getDefaultFontFamily();
  }

  protected FontRegistry getFontRegistry()
  {
    return fontStorage.getFontRegistry();
  }

  public PageSize getDefaultPageSize()
  {
//    throw new UnsupportedOperationException();
    return PageSize.A4;
    //return new PageSize(420, 200);
  }

  /**
   * Returns the vertical page span. If the value is zero or negative, no
   *
   * @return
   */
  public int getVerticalPageSpan()
  {
    return 1;
  }

  public int getHorizontalPageSpan()
  {
    return 1;
  }

  public String getMediaType()
  {
    return "print";
  }

  public boolean isValid(final FontSpecification spec)
  {
    final FontRegistry registry = getFontRegistry();
    final String fontFamily = spec.getFontFamily();
    if (fontFamily == null)
    {
      return false;
    }
    final FontFamily family = registry.getFontFamily(fontFamily);
    return family != null;

  }

  public FontMetrics getFontMetrics(final FontSpecification spec)
  {
    final String fontFamily = spec.getFontFamily();
    if (fontFamily == null)
    {
      DebugLog.log("No font family specified.");
      return null;
    }
    final FontRegistry registry = getFontRegistry();
    final FontFamily family = registry.getFontFamily(fontFamily);
    if (family == null)
    {
      DebugLog.log("Unable to lookup the font family.");
      return null;
    }
    // todo
    final DefaultFontContext fontContext =
        new DefaultFontContext (this, spec.isAntiAliasing(), spec.getFontSize(), "UTF-8", false);

    final FontRecord record = family.getFontRecord
            (spec.getFontWeight() > 600, spec.isItalic() || spec.isOblique());
    final FontMetrics fm = getFontStorage().getFontMetrics
            (record.getIdentifier(), fontContext);
    if (fm == null)
    {
      throw new NullPointerException("FontMetrics returned from factory is null.");
    }
    return fm;
  }

  public Class[] getSupportedResourceTypes()
  {
    return (Class[]) SUPPORTED_TYPES.clone();
  }
}
