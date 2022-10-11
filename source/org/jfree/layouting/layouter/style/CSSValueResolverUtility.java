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
 * $Id: CSSValueResolverUtility.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style;

import org.jfree.layouting.input.style.values.CSSNumericType;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.output.OutputProcessorFeature;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;

/**
 * Creation-Date: 15.12.2005, 11:29:22
 *
 * @author Thomas Morgner
 */
public class CSSValueResolverUtility
{
  public static final double DEFAULT_X_HEIGHT_FACTOR = 0.58;

  public static boolean isAbsoluteValue(final CSSNumericValue value)
  {
    if (CSSNumericType.PT.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.PC.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.INCH.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.CM.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.MM.equals(value.getType()))
    {
      return true;
    }
    // PX is relative to the device, so during a layouting process, it can
    // be considered absolute
    return CSSNumericType.PX.equals(value.getType());
  }

  public static boolean isLengthValue(final CSSNumericValue value)
  {
    if (CSSNumericType.PT.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.PC.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.INCH.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.CM.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.MM.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.EM.equals(value.getType()))
    {
      return true;
    }
    if (CSSNumericType.EX.equals(value.getType()))
    {
      return true;
    }
    // PX is relative to the device, so during a layouting process, it can
    // be considered absolute
    return CSSNumericType.PX.equals(value.getType());
  }

  public static double convertLengthToDouble(final CSSValue rawValue)
  {
    return convertLengthToDouble(rawValue, null, null);
  }

  /**
   * Returns the length in point as a double primitive value.
   * Be aware that using double-values is not very accurate.
   *
   * @param rawValue
   * @param context
   * @param metaData
   * @return
   */
  public static strictfp double convertLengthToDouble(final CSSValue rawValue,
                                                      final LayoutContext context,
                                                      final OutputProcessorMetaData metaData)
  {
    if (rawValue instanceof CSSNumericValue == false)
    {
      return 0;
    }

    final CSSNumericValue value = (CSSNumericValue) rawValue;
    if (CSSNumericType.PT.equals(value.getType()))
    {
      return value.getValue();
    }
    if (CSSNumericType.PC.equals(value.getType()))
    {
      return (value.getValue() / 12.0d);
    }
    if (CSSNumericType.INCH.equals(value.getType()))
    {
      return (value.getValue() / 72.0d);
    }
    if (CSSNumericType.CM.equals(value.getType()))
    {
      return ((value.getValue() * 100 * 72.0d) / 254.0d);
    }
    if (CSSNumericType.MM.equals(value.getType()))
    {
      return ((value.getValue() * 10 * 72.0d) / 254.0d);
    }

    if (CSSNumericType.PX.equals(value.getType()))
    {
      final int pixelPerInch;
      if (metaData != null)
      {
        pixelPerInch = (int) metaData.getNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION);
      }
      else
      {
        pixelPerInch = 96;
      }
      if (pixelPerInch <= 0)
      {
        // we assume 72 pixel per inch ...
        return value.getValue();
      }
      return value.getValue() * 72d / pixelPerInch;
    }

    if (metaData == null)
    {
      return 0;
    }

    if (context != null)
    {
      if (CSSNumericType.EM.equals(value.getType()))
      {
        final FontSpecification fspec =
            context.getFontSpecification();
        final double fontSize = fspec.getFontSize();
        return (fontSize * value.getValue());
      }
      if (CSSNumericType.EX.equals(value.getType()))
      {
        final FontSpecification fspec =
            context.getFontSpecification();
        final FontMetrics fontMetrics = metaData.getFontMetrics(fspec);
        if (fontMetrics == null)
        {
          final long fontSize = (long) (fspec.getFontSize() * DEFAULT_X_HEIGHT_FACTOR);
          return StrictGeomUtility.toExternalValue((long) (value.getValue() * fontSize));
        }
        else
        {
          return StrictGeomUtility.toExternalValue((long) (value.getValue() * fontMetrics.getXHeight()));
        }
      }
    }
    return 0;
  }

  /**
   * Returns the length in point as a double primitive value.
   *
   * @param rawValue
   * @param context
   * @param metaData
   * @return
   */
  public static strictfp long convertLengthToLong(final CSSValue rawValue,
                                                  final LayoutContext context,
                                                  final OutputProcessorMetaData metaData)
  {
    if (rawValue instanceof CSSNumericValue == false)
    {
      return 0;
    }

    final CSSNumericValue value = (CSSNumericValue) rawValue;
    final long internal = StrictGeomUtility.toInternalValue(value.getValue());
    if (CSSNumericType.PT.equals(value.getType()))
    {
      return internal;
    }
    if (CSSNumericType.PC.equals(value.getType()))
    {
      return (internal / 12);
    }
    if (CSSNumericType.INCH.equals(value.getType()))
    {
      return (internal / 72);
    }
    if (CSSNumericType.CM.equals(value.getType()))
    {
      return (internal * 100 * 72 / 254);
    }
    if (CSSNumericType.MM.equals(value.getType()))
    {
      return (internal * 10 * 72 / 254);
    }

    if (CSSNumericType.PX.equals(value.getType()))
    {
      final int pixelPerInch;
      if (metaData != null)
      {
        pixelPerInch = (int) metaData.getNumericFeatureValue(OutputProcessorFeature.DEVICE_RESOLUTION);
      }
      else
      {
        pixelPerInch = 96;
      }
      if (pixelPerInch <= 0)
      {
        // we assume 72 pixel per inch ...
        return internal;
      }
      return internal * 72 / pixelPerInch;
    }

    if (metaData == null)
    {
      return 0;
    }

    if (context != null)
    {
      if (CSSNumericType.EM.equals(value.getType()))
      {
        final FontSpecification fspec =
            context.getFontSpecification();
        final double fontSize = fspec.getFontSize();
        return (long) (fontSize * internal);
      }
      if (CSSNumericType.EX.equals(value.getType()))
      {
        final FontSpecification fspec =
            context.getFontSpecification();
        final FontMetrics fontMetrics = metaData.getFontMetrics(fspec);
        if (fontMetrics == null)
        {
          final long fontSize = (long) (fspec.getFontSize() * DEFAULT_X_HEIGHT_FACTOR);
          return StrictGeomUtility.multiply (internal, fontSize);
        }
        else
        {
          return StrictGeomUtility.multiply (internal, fontMetrics.getXHeight());
        }
      }
    }
    return 0;
  }

  public static CSSNumericValue convertLength(final CSSValue rawValue,
                                              final LayoutContext context,
                                              final OutputProcessorMetaData metaData)
  {
    return CSSNumericValue.createValue(CSSNumericType.PT,
        convertLengthToDouble(rawValue, context, metaData));
  }

  public static CSSNumericValue getLength(final CSSValue value)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return null;
    }

    final CSSNumericValue nval = (CSSNumericValue) value;
    if (isNumericType(CSSNumericType.PERCENTAGE, nval))
    {
      return null;
    }

    return nval;
  }

  private static boolean isNumericType(final CSSNumericType type,
                                       final CSSValue value)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return false;
    }
    final CSSNumericValue nval = (CSSNumericValue) value;
    return nval.getType().equals(type);
  }


  public static CSSNumericValue getLength(final CSSValue value,
                                          final CSSNumericValue percentageBase)
  {
    if (value instanceof CSSNumericValue == false)
    {
      return null;
    }

    final CSSNumericValue nval = (CSSNumericValue) value;
    if (isNumericType(CSSNumericType.PERCENTAGE, nval))
    {
      if (percentageBase == null)
      {
        return null;
      }

      final double percentage = nval.getValue();
      return CSSNumericValue.createValue(percentageBase.getType(),
          percentageBase.getValue() * percentage / 100.0d);
    }

    return nval;
  }

//
//  public static CSSNumericValue getParentFontSize(final LayoutElement node)
//  {
//    final LayoutElement parent = node.getParent();
//    if (parent == null)
//    {
//      return null;
//    }
//    final long fs =
//        parent.getLayoutContext().getFontSpecification().getFontSize();
//    return CSSNumericValue.createInternalValue(CSSNumericType.PT, fs);
//  }
//

  public static boolean isURI(final CSSValue value)
  {
    if (value instanceof CSSStringValue == false)
    {
      return false;
    }
    final CSSStringValue sval = (CSSStringValue) value;
    return sval.getType().equals(CSSStringType.URI);
  }

  public static double getNumericValue(final CSSValue value,
                                       final double defaultValue)
  {
    if (value instanceof CSSNumericValue)
    {
      final CSSNumericValue nval = (CSSNumericValue) value;
      if (CSSNumericType.NUMBER.equals(nval.getType()))
      {
        return nval.getValue();
      }
    }
    return defaultValue;
  }


  public static CSSNumericValue convertLength(final CSSNumericValue value, final CSSNumericType type)
  {
    if (type == CSSNumericType.NUMBER || type == CSSNumericType.PERCENTAGE || type == CSSNumericType.DEG)
    {
      throw new IllegalArgumentException();
    }
    final CSSNumericType valueType = value.getType();
    if (valueType == CSSNumericType.NUMBER || valueType == CSSNumericType.PERCENTAGE || valueType == CSSNumericType.DEG)
    {
      throw new IllegalArgumentException();
    }
    if (valueType == type)
    {
      return value;
    }

    final double targetFactor = getFactor(type);
    final double sourceFactor = getFactor(valueType);
    final double unitvalue = value.getValue() * targetFactor / sourceFactor;
    return CSSNumericValue.createValue(type, unitvalue);
  }

  private static double getFactor (final CSSNumericType type)
  {
    for (int i = 0; i < types.length; i++)
    {
      final CSSNumericType numericType = types[i];
      if (type == numericType)
      {
        return vals[i];
      }
    }
    throw new IllegalArgumentException();
  }

  private static CSSNumericType[] types = new CSSNumericType[]{
      CSSNumericType.CM,
      CSSNumericType.MM,
      CSSNumericType.PX,
      CSSNumericType.PT,
      CSSNumericType.PC,
      CSSNumericType.INCH
  };

  private static double[] vals = new double[]{
      254, // CM
      2540, //MM
      9600, // PX
      7200, // PT
      600, // PC
      100 // Inch
  };
  
  private CSSValueResolverUtility()
  {
  }
}
