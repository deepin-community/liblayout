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
 * $Id: PageGridUtility.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.model.page;

import org.jfree.layouting.input.style.keys.page.PageSize;
import org.jfree.layouting.input.style.values.CSSNumericValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValuePair;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.output.OutputProcessorMetaData;

/**
 * Creation-Date: 20.10.2006, 20:46:30
 *
 * @author Thomas Morgner
 */
public class PageGridUtility
{
  private PageGridUtility()
  {
  }

  public static PageSize lookupPageSize (final CSSValue sizeVal,
                                         final OutputProcessorMetaData metaData)
  {

    if (sizeVal instanceof CSSValuePair == false)
    {
      final PageSize defaultVal = metaData.getDefaultPageSize();
      return defaultVal;
    }
    final CSSValuePair valuePair = (CSSValuePair) sizeVal;
    final CSSValue firstValue = valuePair.getFirstValue();
    if (firstValue instanceof CSSNumericValue == false)
    {
      final PageSize defaultVal = metaData.getDefaultPageSize();
      return defaultVal;
    }
    final CSSValue secondValue = valuePair.getSecondValue();
    if (secondValue instanceof CSSNumericValue == false)
    {
      final PageSize defaultVal = metaData.getDefaultPageSize();
      return defaultVal;
    }

    final double width = CSSValueResolverUtility.convertLengthToDouble
            (firstValue, null, metaData);
    final double height = CSSValueResolverUtility.convertLengthToDouble
            (secondValue, null, metaData);
    if (width < 1 || height < 1)
    {
      final PageSize defaultVal = metaData.getDefaultPageSize();
      return defaultVal;
    }
    return new PageSize(width, height);
  }

}
