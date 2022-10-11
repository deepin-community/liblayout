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
 * $Id: CSSParseTest.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.parser.StyleSheetParserUtil;
import org.w3c.css.sac.InputSource;
import org.w3c.flute.parser.Parser;

/**
 * Creation-Date: 23.11.2005, 13:00:17
 *
 * @author Thomas Morgner
 */
public class CSSParseTest
{
  private CSSParseTest()
  {
  }

  public static void main(final String[] args)
  {
    LibLayoutBoot.getInstance().start();
    final HashMap namespaces = new HashMap();
    namespaces.put("xml", "balh");

    final StyleSheetParserUtil styleSheetParserUtil = StyleSheetParserUtil.getInstance();
    final Object value = styleSheetParserUtil.parseStyleValue
            (namespaces, LineStyleKeys.VERTICAL_ALIGN,
                    "baseline", null, null);
    System.out.println ("Value: " + value);

    final InputSource source = new InputSource();
    source.setCharacterStream(new StringReader("A|A"));

    final Parser p = new Parser();
    try
    {
      System.out.println (p.parseNamespaceToken(source));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }


    final String selector = "html|tag[Test|Attr]";
    source.setCharacterStream(new StringReader(selector));
    try
    {
      final Object o = p.parseSelectors(source);
      System.out.println(o);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
