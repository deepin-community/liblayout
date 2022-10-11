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
 * $Id: ContentResolveHandler.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.layouter.style.resolver.computed.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.content.ContentStyleKeys;
import org.jfree.layouting.input.style.keys.content.ContentValues;
import org.jfree.layouting.input.style.keys.list.ListStyleKeys;
import org.jfree.layouting.input.style.values.CSSAttrFunction;
import org.jfree.layouting.input.style.values.CSSConstant;
import org.jfree.layouting.input.style.values.CSSFunctionValue;
import org.jfree.layouting.input.style.values.CSSStringType;
import org.jfree.layouting.input.style.values.CSSStringValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.input.style.values.CSSValueList;
import org.jfree.layouting.layouter.content.ContentToken;
import org.jfree.layouting.layouter.content.computed.CloseQuoteToken;
import org.jfree.layouting.layouter.content.computed.ContentsToken;
import org.jfree.layouting.layouter.content.computed.CounterToken;
import org.jfree.layouting.layouter.content.computed.OpenQuoteToken;
import org.jfree.layouting.layouter.content.statics.StaticTextToken;
import org.jfree.layouting.layouter.context.ContentSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.counters.CounterStyle;
import org.jfree.layouting.layouter.counters.CounterStyleFactory;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.layouter.style.functions.FunctionEvaluationException;
import org.jfree.layouting.layouter.style.functions.FunctionFactory;
import org.jfree.layouting.layouter.style.functions.content.ContentFunction;
import org.jfree.layouting.layouter.style.resolver.ResolveHandler;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.base.util.DebugLog;

public class ContentResolveHandler implements ResolveHandler
{
  private static final ContentToken[] DEFAULT_CONTENT = new ContentToken[]{ContentsToken.CONTENTS};
  private static final ContentToken[] PSEUDO_CONTENT = new ContentToken[]{};
  private CSSValue listCounter;
  private HashMap tokenMapping;

  public ContentResolveHandler()
  {
    tokenMapping = new HashMap();
    tokenMapping.put(ContentValues.CONTENTS, ContentsToken.CONTENTS);
    tokenMapping.put(ContentValues.OPEN_QUOTE, new OpenQuoteToken(false));
    tokenMapping.put(ContentValues.NO_OPEN_QUOTE, new OpenQuoteToken(true));
    tokenMapping.put(ContentValues.CLOSE_QUOTE, new CloseQuoteToken(false));
    tokenMapping.put(ContentValues.NO_CLOSE_QUOTE, new CloseQuoteToken(true));

    final CSSStringValue param =
        new CSSStringValue(CSSStringType.STRING, "list-item");
    listCounter = new CSSFunctionValue("counter", new CSSValue[]{param});

  }

  /**
   * This indirectly defines the resolve order. The higher the order, the more
   * dependent is the resolver on other resolvers to be complete.
   *
   * @return the array of required style keys.
   */
  public StyleKey[] getRequiredStyles()
  {
    return new StyleKey[]{
        ContentStyleKeys.COUNTER_RESET,
        ContentStyleKeys.COUNTER_INCREMENT,
        ContentStyleKeys.QUOTES,
        ContentStyleKeys.STRING_SET
    };
  }

  /**
   * Resolves a single property.
   *
   * @param process the current layout process controlling everyting
   * @param element the current layout element that is processed
   * @param key     the style key that is computed.
   */
  public void resolve(final LayoutProcess process,
                      final LayoutElement element,
                      final StyleKey key)
  {
    final LayoutContext layoutContext = element.getLayoutContext();
    final ContentSpecification contentSpecification =
        layoutContext.getContentSpecification();

    final CSSValue value = layoutContext.getValue(key);
    if (value instanceof CSSConstant)
    {
      if (ContentValues.NONE.equals(value))
      {
        contentSpecification.setAllowContentProcessing(false);
        contentSpecification.setInhibitContent(false);
        contentSpecification.setContents(PSEUDO_CONTENT);
        return;
      }
      else if (ContentValues.INHIBIT.equals(value))
      {
        contentSpecification.setAllowContentProcessing(false);
        contentSpecification.setInhibitContent(true);
        contentSpecification.setContents(PSEUDO_CONTENT);
        return;
      }
      else if (ContentValues.NORMAL.equals(value))
      {
        if (layoutContext.isPseudoElement())
        {
          if (isListMarker(element))
          {
            processListItem(process, element, contentSpecification);
            return;
          }
          else
          {
            // a pseudo-element does not have content by default.
            contentSpecification.setAllowContentProcessing(false);
            contentSpecification.setInhibitContent(true);
            contentSpecification.setContents(PSEUDO_CONTENT);
            return;
          }
        }
      }
    }

    contentSpecification.setInhibitContent(false);
    contentSpecification.setAllowContentProcessing(true);
    contentSpecification.setContents(DEFAULT_CONTENT);

    if (value instanceof CSSAttrFunction)
    {
      final ContentToken token =
          evaluateFunction((CSSFunctionValue) value, process, element);
      if (token == null)
      {
        return;
      }
      contentSpecification.setContents(new ContentToken[]{token});
    }

    if (value instanceof CSSValueList == false)
    {
      return; // cant handle that one
    }

    final ArrayList tokens = new ArrayList();
    final CSSValueList list = (CSSValueList) value;
    final int size = list.getLength();
    for (int i = 0; i < size; i++)
    {
      final CSSValueList sequence = (CSSValueList) list.getItem(i);
      for (int j = 0; j < sequence.getLength(); j++)
      {
        final CSSValue content = sequence.getItem(j);
        final ContentToken token = createToken(process, element, content);
        if (token == null)
        {
          // ok, a failure. Skip to the next content spec ...
          tokens.clear();
          break;
        }
        tokens.add(token);
      }
      if (tokens.isEmpty() == false)
      {
        final ContentToken[] contents = (ContentToken[]) tokens.toArray
            (new ContentToken[tokens.size()]);
        contentSpecification.setContents(contents);
        return;
      }
    }

  }

  private void processListItem(final LayoutProcess process,
                               final LayoutElement element,
                               final ContentSpecification contentSpecification)
  {
    contentSpecification.setAllowContentProcessing(false);
    contentSpecification.setInhibitContent(false);

    final LayoutContext layoutContext = element.getLayoutContext();
    final CSSValue value =
        layoutContext.getValue(ListStyleKeys.LIST_STYLE_IMAGE);
    if (value != null)
    {
      final ContentToken token = createToken(process, element, value);
      if (token != null)
      {
        contentSpecification.setContents(new ContentToken[]{ token });
        return;
      }
    }

    final ContentToken token = createToken
        (process, element, listCounter);
    if (token instanceof CounterToken)
    {
      final CounterToken counterToken = (CounterToken) token;
      final CounterStyle style = counterToken.getStyle();
      final String suffix = style.getSuffix();
      if (suffix == null || suffix.length() == 0)
      {
        contentSpecification.setContents(new ContentToken[]{ token });
      }
      else
      {
        contentSpecification.setContents
            (new ContentToken[]{ counterToken, new StaticTextToken(suffix)});
      }
    }
    else
    {
      contentSpecification.setContents(new ContentToken[]{ token });
    }
  }

  private boolean isListMarker (final LayoutElement element)
  {
    final LayoutContext layoutContext = element.getLayoutContext();
    if ("marker".equals(layoutContext.getPseudoElement()) == false)
    {
       return false;
    }
    final LayoutElement parent = element.getParent();
    if (parent == null)
    {
      return false;
    }
    final CSSValue parentDisplayRole =
        parent.getLayoutContext().getValue(BoxStyleKeys.DISPLAY_ROLE);
    if (DisplayRole.LIST_ITEM.equals(parentDisplayRole))
    {
      return true;
    }

    return false;
  }

  private ContentToken createToken(final LayoutProcess process,
                                   final LayoutElement element,
                                   final CSSValue content)
  {
    try
    {
      if (content instanceof CSSStringValue)
      {
        final CSSStringValue sval = (CSSStringValue) content;
        if (CSSStringType.STRING.equals(sval.getType()))
        {
          return new StaticTextToken(sval.getValue());
        }
        else
        {
          // this is an external URL, so try to load it.
          final CSSFunctionValue function = new CSSFunctionValue
              ("url", new CSSValue[]{sval});
          return evaluateFunction(function, process, element);
        }
      }

      if (content instanceof CSSConstant)
      {
        if (ContentValues.DOCUMENT_URL.equals(content))
        {
          final Object docUrl = process.getDocumentContext().getMetaAttribute
              ("document-url");
          if (docUrl != null)
          {
            return new StaticTextToken(String.valueOf(docUrl));
          }

          final ResourceKey baseKey =
              DocumentContextUtility.getBaseResource(process.getDocumentContext());
          final ResourceManager resourceManager =
              DocumentContextUtility.getResourceManager(process.getDocumentContext());
          final URL url = resourceManager.toURL(baseKey);
          if (url != null)
          {
            return new StaticTextToken(url.toExternalForm());
          }
          return null;
        }

        final ContentToken token = (ContentToken) tokenMapping.get(content);
        if (token != null)
        {
          return token;
        }

        return resolveContentAlias(content);
      }

      if (content instanceof CSSFunctionValue)
      {
        return evaluateFunction((CSSFunctionValue) content, process, element);
      }
    }
    catch (Exception e)
    {
      DebugLog.log("Content-Resolver: Failed to evaluate " + content);
    }

    return null;
  }

  private ContentToken resolveContentAlias(final CSSValue content)
  {

    if (ContentValues.FOOTNOTE.equals(content))
    {
      final CounterStyle style =
          CounterStyleFactory.getInstance().getCounterStyle("normal");
      return new CounterToken("footnote", style);
    }
    if (ContentValues.ENDNOTE.equals(content))
    {
      final CounterStyle style =
          CounterStyleFactory.getInstance().getCounterStyle("normal");
      return new CounterToken("endnote", style);
    }
    if (ContentValues.SECTIONNOTE.equals(content))
    {
      final CounterStyle style =
          CounterStyleFactory.getInstance().getCounterStyle("normal");
      return new CounterToken("section-note", style);
    }
    if (ContentValues.LISTITEM.equals(content))
    {
      final CounterStyle style =
          CounterStyleFactory.getInstance().getCounterStyle("normal");
      return new CounterToken("list-item", style);
    }
    return null;
  }

  private ContentToken evaluateFunction(final CSSFunctionValue function,
                                        final LayoutProcess process,
                                        final LayoutElement element)
  {
    final ContentFunction styleFunction =
        FunctionFactory.getInstance().getContentFunction(function.getFunctionName());
    if (styleFunction == null)
    {
      return null;
    }
    try
    {
      return styleFunction.evaluate(process, element, function);
    }
    catch (FunctionEvaluationException e)
    {
      DebugLog.log("Evaluation failed " + e);
      return null;
    }
  }
}
