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
 * $Id: CSSParserFactory.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.selectors.CSSSelectorFactory;
import org.jfree.layouting.input.style.selectors.conditions.CSSConditionFactory;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.helpers.ParserFactory;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;
import org.pentaho.reporting.libraries.base.config.Configuration;

/**
 * Creates a new CSS parser by first looking for a specified parser in the
 * libLayout configuration and if that fails, by using the W3C parser factory.
 *
 * @author Thomas Morgner
 */
public class CSSParserFactory
{
  private static CSSParserFactory parserFactory;

  public static synchronized CSSParserFactory getInstance()
  {
    if (parserFactory == null)
    {
      parserFactory = new CSSParserFactory();
    }
    return parserFactory;
  }

  private CSSParserFactory()
  {
  }

  public Parser createCSSParser ()
          throws CSSParserInstantiationException
  {
    final Configuration config = LibLayoutBoot.getInstance().getGlobalConfig();
    final String parserClass =
            config.getConfigProperty("org.jfree.layouting.css.Parser");
    if (parserClass != null)
    {
      final Parser p = (Parser) ObjectUtilities.loadAndInstantiate
            (parserClass, CSSParserFactory.class, Parser.class);
      if (p != null)
      {
        p.setConditionFactory(new FixNamespaceConditionFactory(new CSSConditionFactory()));
        p.setSelectorFactory(new FixNamespaceSelectorFactory(new CSSSelectorFactory()));
        return p;
      }
    }
    try
    {
      final Parser p = new ParserFactory().makeParser();
      if (p == null)
      {
        return null;
      }
      p.setConditionFactory(new FixNamespaceConditionFactory(new CSSConditionFactory()));
      p.setSelectorFactory(new FixNamespaceSelectorFactory(new CSSSelectorFactory()));
      return p;
    }
    catch (Exception e)
    {
      throw new CSSParserInstantiationException();
    }
  }
}
