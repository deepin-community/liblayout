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
 * $Id: StyleSheetParserUtil.java 6495 2008-11-28 15:53:18Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.jfree.layouting.input.style.CSSDeclarationRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.values.CSSValue;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorList;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.base.util.LFUMap;

/**
 * A helper class that simplifies the parsing of stylesheets.
 *
 * @author Thomas Morgner
 */
public final class StyleSheetParserUtil
{
  private static StyleSheetParserUtil singleton;
  private Parser parser;
  private StyleKeyRegistry registry;
  private LFUMap lexicalValueCache;

  public StyleSheetParserUtil()
  {
    registry = StyleKeyRegistry.getRegistry();
    lexicalValueCache = new LFUMap(100);
  }


  public static synchronized StyleSheetParserUtil getInstance()
  {
    if (singleton == null)
    {
      singleton = new StyleSheetParserUtil();
    }
    return singleton;
  }

  /**
   * Parses a single style value for the given key. Returns <code>null</code>,
   * if the key denotes a compound definition, which has no internal
   * representation.
   *
   * @param namespaces an optional map of known namespaces (prefix -> uri)
   * @param selector the selector text that should be parsed.
   * @param resourceManager an optional resource manager
   * @param baseURL an optional base url
   * @return the parsed selector or null
   */
  public SelectorList parseSelector(final Map namespaces,
                                    final String selector,
                                    final ResourceManager resourceManager,
                                    final ResourceKey baseURL)
  {
    if (selector == null)
    {
      throw new NullPointerException();
    }

    try
    {
      final Parser parser = getParser();
      synchronized(parser)
      {
        final StyleSheetHandler handler = new StyleSheetHandler();
        handler.init
            (resourceManager, baseURL, -1, StyleKeyRegistry.getRegistry(), null);

        setupNamespaces(namespaces, handler);

        final InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(selector));

        handler.initParseContext(source);
        handler.setStyleRule(new CSSStyleRule(null, null));
        parser.setDocumentHandler(handler);

        final SelectorList selectorList = parser.parseSelectors(source);
        CSSParserContext.getContext().destroy();
        return selectorList;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  private void setupNamespaces(final Map namespaces,
                               final StyleSheetHandler handler)
  {
    if (namespaces == null)
    {
      return;
    }

    final Iterator entries = namespaces.entrySet().iterator();
    while (entries.hasNext())
    {
      final Map.Entry entry = (Map.Entry) entries.next();
      final String prefix = (String) entry.getKey();
      final String uri = (String) entry.getValue();
      handler.registerNamespace(prefix, uri);
    }
  }

  /**
   * Parses a single style value for the given key. Returns <code>null</code>,
   * if the key denotes a compound definition, which has no internal
   * representation.
   *
   * @param namespaces an optional map of known namespaces (prefix -> uri)
   * @param key the stylekey to which the value should be assigned.
   * @param value the value text
   * @param resourceManager an optional resource manager
   * @param baseURL an optional base url
   * @return the parsed value or null, if the value was not valid.
   */
  public CSSValue parseStyleValue(final Map namespaces,
                                  final StyleKey key,
                                  final String value,
                                  final ResourceManager resourceManager,
                                  final ResourceKey baseURL)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    if (value == null)
    {
      throw new NullPointerException();
    }

    try
    {
      final Parser parser = getParser();
      synchronized(parser)
      {
        final StyleSheetHandler handler = new StyleSheetHandler();
        setupNamespaces(namespaces, handler);

        handler.init
            (resourceManager, baseURL, -1, registry, null);

        final InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(value));

        handler.initParseContext(source);
        handler.setStyleRule(new CSSStyleRule(null, null));
        parser.setDocumentHandler(handler);
        final LexicalUnit lu = parser.parsePropertyValue(source);
        handler.property(key.getName(), lu, false);
        final CSSStyleRule rule = (CSSStyleRule) handler.getStyleRule();

        CSSParserContext.getContext().destroy();

        return rule.getPropertyCSSValue(key);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parses an single lexical unit. This returns the un-interpreted tokenized
   * value. The only use this method has is to parse performance critical
   * tokens.
   *
   * @param value the value as string.
   * @return the parsed value or null, if the string was unparseable.
   */
  public LexicalUnit parseLexicalStyleValue (final String value)
  {
    if (value == null)
    {
      throw new NullPointerException();
    }
    try
    {
      final Parser parser = getParser();
      synchronized(parser)
      {
        final Object cached = lexicalValueCache.get(value);
        if (cached != null)
        {
          return (LexicalUnit) cached;
        }
        final StyleSheetHandler handler = new StyleSheetHandler();
        handler.init (null, null, -1, registry, null);
        final InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(value));
        handler.initParseContext(source);
        parser.setDocumentHandler(handler);
        final LexicalUnit result = parser.parsePropertyValue(source);
        lexicalValueCache.put (value, result);
        return result;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Parses a style value. If the style value is a compound key, the
   * corresonding style entries will be added to the style rule.
   *
   * @param namespaces an optional map of known namespaces (prefix -> uri)
   * @param key the stylekey to which the value should be assigned.
   * @param value the value text
   * @param resourceManager an optional resource manager
   * @param baseURL an optional base url
   * @return the CSS-Style-Rule that contains all values for the given text.
   */
  public CSSStyleRule parseStyles(final Map namespaces,
                                  final StyleKey key,
                                  final String value,
                                  final ResourceManager resourceManager,
                                  final ResourceKey baseURL)
  {
    if (key == null)
    {
      throw new NullPointerException();
    }
    return parseStyles
        (namespaces, key.getName(), value, resourceManager, baseURL);
  }

  /**
   * Parses a style rule.
   *
   * @param namespaces an optional map of known namespaces (prefix -> uri)
   * @param styleText the css text that should be parsed
   * @param resourceManager an optional resource manager
   * @param baseURL an optional base url
   * @param baseRule an optional base-rule to which the result gets added.
   * @return the CSS-Style-Rule that contains all values for the given text.
   */
  public CSSDeclarationRule parseStyleRule(final Map namespaces,
                                           final String styleText,
                                           final ResourceManager resourceManager,
                                           final ResourceKey baseURL,
                                           final CSSDeclarationRule baseRule)
  {
    if (styleText == null)
    {
      throw new NullPointerException("Name is null");
    }

    try
    {
      final Parser parser = getParser();
      synchronized(parser)
      {
        final StyleSheetHandler handler = new StyleSheetHandler();
        setupNamespaces(namespaces, handler);
        handler.init (resourceManager, baseURL, -1, registry, null);

        final InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(styleText));

        handler.initParseContext(source);
        handler.setStyleRule(baseRule);
        parser.setDocumentHandler(handler);
        parser.parseStyleDeclaration(source);
        final CSSDeclarationRule rule = handler.getStyleRule();
        CSSParserContext.getContext().destroy();
        return rule;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }


  /**
   * Parses a style value. If the style value is a compound key, the
   * corresonding style entries will be added to the style rule.
   *
   * @param namespaces an optional map of known namespaces (prefix -> uri)
   * @param name the stylekey-name to which the value should be assigned.
   * @param value the value text
   * @param resourceManager an optional resource manager
   * @param baseURL an optional base url
   * @return the CSS-Style-Rule that contains all values for the given text.
   */
  public CSSStyleRule parseStyles(final Map namespaces,
                                  final String name,
                                  final String value,
                                  final ResourceManager resourceManager,
                                  final ResourceKey baseURL)
  {
    return parseStyles(namespaces, name, value, resourceManager,
        baseURL, new CSSStyleRule(null, null));
  }


  /**
   * Parses a style value. If the style value is a compound key, the
   * corresonding style entries will be added to the style rule.
   *
   * @param namespaces an optional map of known namespaces (prefix -> uri)
   * @param name the stylekey-name to which the value should be assigned.
   * @param value the value text
   * @param resourceManager an optional resource manager
   * @param baseURL an optional base url
   * @param baseRule an optional base-rule to which the result gets added.
   * @return the CSS-Style-Rule that contains all values for the given text.
   */
  public CSSStyleRule parseStyles(final Map namespaces,
                                  final String name,
                                  final String value,
                                  final ResourceManager resourceManager,
                                  final ResourceKey baseURL,
                                  final CSSDeclarationRule baseRule)
  {
    if (name == null)
    {
      throw new NullPointerException("Name is null");
    }
    if (value == null)
    {
      throw new NullPointerException("Value is null");
    }

    try
    {
      final Parser parser = getParser();
      synchronized(parser)
      {
        final StyleSheetHandler handler = new StyleSheetHandler();
        handler.init
            (resourceManager, baseURL, -1, StyleKeyRegistry.getRegistry(), null);

        setupNamespaces(namespaces, handler);
        final InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(value));

        handler.initParseContext(source);
        handler.setStyleRule(baseRule);
        parser.setDocumentHandler(handler);
        final LexicalUnit lu = parser.parsePropertyValue(source);
        handler.property(name, lu, false);
        final CSSStyleRule rule = (CSSStyleRule) handler.getStyleRule();

        CSSParserContext.getContext().destroy();
        return rule;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns the initialized parser.
   *
   * @return the parser's local instance.
   * @throws CSSParserInstantiationException if the parser cannot be instantiated.
   */
  private synchronized Parser getParser()
      throws CSSParserInstantiationException
  {
    if (parser == null)
    {
      parser = CSSParserFactory.getInstance().createCSSParser();
    }
    return parser;
  }

  /**
   * Parses a single namespace identifier. This simply splits the given
   * attribute name when a namespace separator is encountered ('|').
   *
   * @param attrName the attribute name
   * @return the parsed attribute.
   */
  public static String[] parseNamespaceIdent(final String attrName)
  {
    final String name;
    final String namespace;
    final StringTokenizer strtok = new StringTokenizer(attrName, "|");
    final CSSParserContext context = CSSParserContext.getContext();
    // explicitly undefined is different from default namespace..
    // With that construct I definitly violate the standard, but
    // most stylesheets are not yet written with namespaces in mind
    // (and most tools dont support namespaces in CSS).
    //
    // by acknowledging the explicit rule but redefining the rule where
    // no namespace syntax is used at all, I create compatiblity. Still,
    // if the stylesheet does not carry a @namespace rule, this is the same
    // as if the namespace was omited.
    if (strtok.countTokens() == 2)
    {
      final String tkNamespace = strtok.nextToken();
      if (tkNamespace.length() == 0)
      {
        namespace = null;
      }
      else if ("*".equals(tkNamespace))
      {
        namespace = "*";
      }
      else
      {
        namespace = (String)
            context.getNamespaces().get(tkNamespace);
      }
      name = strtok.nextToken();
    }
    else
    {
      name = strtok.nextToken();
      namespace = context.getDefaultNamespace();
    }
    return new String[]{namespace, name};
  }
}
