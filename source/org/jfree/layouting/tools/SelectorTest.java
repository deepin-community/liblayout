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
 * $Id: SelectorTest.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.tools;

import java.io.IOException;
import java.net.URL;

import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.parser.CSSParserFactory;
import org.jfree.layouting.input.style.parser.CSSParserInstantiationException;
import org.jfree.layouting.input.style.parser.StringInputSource;
import org.jfree.layouting.input.style.parser.StyleSheetHandler;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorList;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

public class SelectorTest
{
  private SelectorTest()
  {
  }

  public static void main (final String[] args)
          throws IOException, CSSParserInstantiationException
  {

    final ResourceManager resourceManager= new ResourceManager();
    resourceManager.registerDefaults();

    final Parser parser = CSSParserFactory.getInstance().createCSSParser();
    final StyleSheetHandler handler = new StyleSheetHandler();
    handler.init (resourceManager, null, 0, StyleKeyRegistry.getRegistry(), null);
    parser.setDocumentHandler(handler);
    final String selector = ".bordered";
    final SelectorList sl =
            parser.parseSelectors(new StringInputSource (selector, new URL("http://localhost")));
    System.exit(0);
  }

}
