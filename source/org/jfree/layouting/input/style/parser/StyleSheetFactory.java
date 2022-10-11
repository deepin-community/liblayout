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
 * $Id: StyleSheetFactory.java 10759 2009-12-02 19:26:16Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser;

import java.io.IOException;
import java.io.InputStream;

import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.StyleSheet;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.pentaho.reporting.libraries.resourceloader.ResourceFactory;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.DependencyCollector;
import org.pentaho.reporting.libraries.resourceloader.CompoundResource;

/**
 * Creation-Date: 12.04.2006, 15:25:39
 *
 * @author Thomas Morgner
 */
public class StyleSheetFactory implements ResourceFactory
{
  public StyleSheetFactory()
  {
  }

  public Resource create(final ResourceManager manager,
                         final ResourceData data,
                         final ResourceKey context)
          throws ResourceCreationException, ResourceLoadingException
  {
    try
    {
      final Parser parser = CSSParserFactory.getInstance().createCSSParser();

      final ResourceKey key;
      final long version;
      if (context == null)
      {
        key = data.getKey();
        version = data.getVersion(manager);
      }
      else
      {
        key = context;
        version = -1;
      }

      final StyleSheetHandler handler = new StyleSheetHandler();
      handler.init
              (manager, key, version, StyleKeyRegistry.getRegistry(), null);
      parser.setDocumentHandler(handler);
      parser.setErrorHandler(handler);

      final InputStream stream = data.getResourceAsStream(manager);
      final InputSource inputSource = new InputSource();
      inputSource.setByteStream(stream);
      parser.parseStyleSheet(inputSource);

      final DependencyCollector dependencies = handler.getDependencies();
      if (context != null)
      {
        dependencies.add(data.getKey(), data.getVersion(manager));
      }

      CSSParserContext.getContext().destroy();

      final CompoundResource cr = new CompoundResource
              (data.getKey(), dependencies, handler.getStyleSheet(), getFactoryType());
      return cr;
    }
    catch (CSSParserInstantiationException e)
    {
      throw new ResourceCreationException("Failed to parse the stylesheet.");
    }
    catch (IOException e)
    {
      throw new ResourceLoadingException("Failed to load the stylesheet.");
    }
  }

  public Class getFactoryType()
  {
    return StyleSheet.class;
  }

  public void initializeDefaults()
  {
    // nothing needed ...
  }
}
