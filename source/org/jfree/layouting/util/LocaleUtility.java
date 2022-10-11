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
 * $Id: LocaleUtility.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.util;

import java.util.Locale;

/**
 * Creation-Date: 17.04.2006, 12:49:46
 *
 * @author Thomas Morgner
 */
public class LocaleUtility
{
  private LocaleUtility () {}

  public static Locale createLocale (final String locale)
  {
    int parseState = 0;
    StringBuffer langBuffer = null;
    StringBuffer countryBuffer = null;
    StringBuffer variantBuffer = null;

    final char[] localeText = locale.toCharArray();
    for (int i = 0; i < localeText.length; i++)
    {
      final char ch = localeText[i];
      if (Character.isWhitespace(ch))
      {
        continue;
      }
      if (parseState == 0)
      {
        langBuffer = new StringBuffer();
        parseState = 1;
      }
      else if (parseState == 1)
      {
        if (ch == '-' || ch == '_')
        {
          parseState = 2;
          countryBuffer = new StringBuffer();
        }
        else
        {
          langBuffer.append(ch);
        }
      }
      else if (parseState == 2)
      {
        if (ch == '-' || ch == '_')
        {
          parseState = 3;
          variantBuffer = new StringBuffer();
        }
        else
        {
          countryBuffer.append(ch);
        }
      }
      else
      {
        variantBuffer.append(ch);
      }
    }

    if (variantBuffer != null)
    {
      return new Locale(langBuffer.toString(), countryBuffer.toString(), variantBuffer.toString());
    }
    if (countryBuffer != null)
    {
      return new Locale(langBuffer.toString(), countryBuffer.toString());
    }
    if (langBuffer != null)
    {
      return new Locale(langBuffer.toString(), "");
    }
    return Locale.getDefault();
  }
}
