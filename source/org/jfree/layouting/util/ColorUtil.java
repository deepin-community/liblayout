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
 * $Id: ColorUtil.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.jfree.layouting.input.style.keys.color.CSSSystemColors;
import org.jfree.layouting.input.style.keys.color.HtmlColors;
import org.jfree.layouting.input.style.keys.color.SVGColors;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSValue;

/**
 * Creation-Date: 16.04.2006, 15:23:58
 *
 * @author Thomas Morgner
 */
public class ColorUtil
{
  private ColorUtil()
  {
  }

  private static final float ONE_THIRD = 1f / 3f;

  /*
   * HOW TO RETURN hsl.to.rgb(h, s, l):
       SELECT:
	  l<=0.5: PUT l*(s+1) IN m2
	  ELSE: PUT l+s-l*s IN m2
       PUT l*2-m2 IN m1
       PUT hue.to.rgb(m1, m2, h+1/3) IN r
       PUT hue.to.rgb(m1, m2, h    ) IN g
       PUT hue.to.rgb(m1, m2, h-1/3) IN b
       RETURN (r, g, b)

    HOW TO RETURN hue.to.rgb(m1, m2, h):
       IF h<0: PUT h+1 IN h
       IF h>1: PUT h-1 IN h
       IF h*6<1: RETURN m1+(m2-m1)*h*6
       IF h*2<1: RETURN m2
       IF h*3<2: RETURN m1+(m2-m1)*(2/3-h)*6
       RETURN m1
   */
  public static float[] hslToRGB (final int h, final float s, final float l)
  {
    final int hue = normalizeHue(h);

    float saturation = s;
    if (saturation > 100)
    {
      saturation = 100;
    }
    if (saturation < 0)
    {
      saturation = 0;
    }
    float lightness = l;
    if (lightness > 100)
    {
      lightness = 100;
    }
    if (lightness < 0)
    {
      lightness = 0;
    }
    final float m2;
    if (lightness <= 0.5)
    {
      m2 = lightness * (saturation + 1);
    }
    else
    {
      m2 = lightness + saturation - lightness * saturation;
    }
    final float m1 = lightness * 2 - m2;

    final float r = hueToRGB(m1, m2, hue + ONE_THIRD);
    final float g = hueToRGB(m1, m2, hue);
    final float b = hueToRGB(m1, m2, hue - ONE_THIRD);
    return new float[]{r, g, b};

  }

  private static float hueToRGB(final float m1, final float m2, float h)
  {
    if (h < 0)
    {
      h = h + 1;
    }
    if (h > 1)
    {
      h = h - 1;
    }
    if ((h * 6f) < 1)
    {
      return m1 + (m2 - m1) * h * 6;
    }
    if ((h * 2f) < 1)
    {
      return m2;
    }
    if ((h * 3f) < 2)
    {
      return m1 + (m2 - m1) * (2 * ONE_THIRD - h) * 6;
    }
    return m1;
  }

  private static int normalizeHue(final int integerValue)
  {
    return ((integerValue % 360) + 360) % 360;
  }

  public static CSSValue parseColor(String colorSpec)
  {
    final CSSValue color = parseIdentColor(colorSpec);
    if (color != null)
    {
      return color;
    }
    try
    {
      if (colorSpec.length() == 4) // #rgb
      {

        colorSpec = "#" + colorSpec.charAt(1) + colorSpec.charAt(1) +
                colorSpec.charAt(2) + colorSpec.charAt(2) +
                colorSpec.charAt(3) + colorSpec.charAt(3);

      }
      final Integer decoded = Integer.decode(colorSpec);
      return new CSSColorValue(decoded.intValue(), false);
    }
    catch(Exception e)
    {
      return null;
    }
  }

  public static CSSValue parseIdentColor(final String name)
  {
    if (CSSSystemColors.CURRENT_COLOR.getCSSText().equalsIgnoreCase(name))
    {
      return CSSSystemColors.CURRENT_COLOR;
    }

    final CSSColorValue htmlColors = parseColorFromClass(name, HtmlColors.class);
    if (htmlColors != null)
    {
      return htmlColors;
    }
    final CSSColorValue svgColors = parseColorFromClass(name, SVGColors.class);
    if (svgColors != null)
    {
      return svgColors;
    }
    final CSSColorValue systemColors = parseColorFromClass(name,
            CSSSystemColors.class);
    if (systemColors != null)
    {
      return systemColors;
    }
    //Log.debug("No such constant: " + name);
    return null;
  }

  private static CSSColorValue parseColorFromClass(final String name, final Class c)
  {
    // try to get a color by name using reflection
    final Field[] f = c.getFields();
    for (int i = 0; i < f.length; i++)
    {
      try
      {
        final Field field = f[i];
        if (field.getName().equalsIgnoreCase(name) == false)
        {
          continue;
        }
        if (CSSColorValue.class.isAssignableFrom(field.getType()) == false)
        {
          continue;
        }
        if (Modifier.isPublic(field.getModifiers()) &&
                Modifier.isStatic((field.getModifiers())))
        {
          return (CSSColorValue) field.get(null);
        }
      }
      catch (Exception ce)
      {
        // we ignore exceptions here.
      }
    }
    return null;
  }
}
