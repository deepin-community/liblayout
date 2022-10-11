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
 * $Id: DefaultLocalizationContext.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.i18n;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DefaultLocalizationContext implements LocalizationContext
{
  public DefaultLocalizationContext()
  {
  }

  public DateFormat getDateFormat (final Locale language)
  {
    return DateFormat.getDateInstance(DateFormat.MEDIUM, language);
  }

  public NumberFormat getIntegerFormat (final Locale language)
  {
    return NumberFormat.getInstance(language);
  }

  public NumberFormat getNumberFormat (final Locale language)
  {
    return NumberFormat.getNumberInstance(language);
  }

  public DateFormat getTimeFormat (final Locale language)
  {
    return DateFormat.getTimeInstance(DateFormat.MEDIUM, language);
  }

  public DateFormat getDateFormat (final String format, final Locale language)
  {
    return new SimpleDateFormat(format, language);
  }

  public NumberFormat getNumberFormat(final String format, final Locale language)
  {
    return new DecimalFormat (format, new DecimalFormatSymbols(language));
  }
}
