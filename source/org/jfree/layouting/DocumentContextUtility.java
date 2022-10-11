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
 * $Id: DocumentContextUtility.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting;

import java.util.Date;

import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.layouter.i18n.DefaultLocalizationContext;
import org.jfree.layouting.layouter.i18n.LocalizationContext;
import org.jfree.layouting.layouter.style.resolver.SimpleStyleRuleMatcher;
import org.jfree.layouting.layouter.style.resolver.StyleRuleMatcher;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

/**
 * Creation-Date: 13.04.2006, 12:11:20
 *
 * @author Thomas Morgner
 */
public class DocumentContextUtility
{
  private DocumentContextUtility()
  {
  }


  public static ResourceKey getBaseResource(final DocumentContext context)
  {
    final Object o = context.getMetaAttribute
            (DocumentContext.BASE_RESOURCE_ATTR);
    if (o instanceof ResourceKey == false)
    {
      return null;
    }
    return (ResourceKey) o;
  }

  public static LocalizationContext getLocalizationContext(final DocumentContext context)
  {
    final Object o = context.getMetaAttribute
            (DocumentContext.LOCALIZATION_ATTR);
    if (o instanceof LocalizationContext == false)
    {
      final DefaultLocalizationContext value = new DefaultLocalizationContext();
      context.setMetaAttribute(DocumentContext.LOCALIZATION_ATTR, value);
      return value;
    }
    return (LocalizationContext) o;
  }

  public static ResourceManager getResourceManager(final DocumentContext context)
  {
    final Object o = context.getMetaAttribute(DocumentContext.RESOURCE_MANAGER_ATTR);
    if (o instanceof ResourceManager == false)
    {
      final ResourceManager value = new ResourceManager();
      value.registerDefaults();
      context.setMetaAttribute(DocumentContext.RESOURCE_MANAGER_ATTR, value);
      return value;
    }
    return (ResourceManager) o;
  }

  public static Date getDate(final DocumentContext context)
  {
    final Object o = context.getMetaAttribute(DocumentContext.DATE_ATTR);
    if (o instanceof Date == false)
    {
      final Date value = new Date();
      context.setMetaAttribute(DocumentContext.DATE_ATTR, value);
      return value;
    }
    return (Date) o;
  }

  public static StyleRuleMatcher getStyleRuleMatcher(final DocumentContext context)
  {
    final Object o = context.getMetaAttribute(DocumentContext.STYLE_MATCHER_ATTR);
    if (o instanceof StyleRuleMatcher == false)
    {
      final StyleRuleMatcher value = new SimpleStyleRuleMatcher();
      context.setMetaAttribute(DocumentContext.STYLE_MATCHER_ATTR, value);
      return value;
    }
    return (StyleRuleMatcher) o;
  }

  public static LayoutStyle getInitialStyle(final DocumentContext context)
  {
    final Object o = context.getMetaAttribute(DocumentContext.INITIAL_STYLE);
    if (o instanceof LayoutStyle == false)
    {
      throw new IllegalStateException();
    }
    return (LayoutStyle) o;
  }

}
