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
 * $Id: SimpleStyleRuleMatcher.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.layouter.style.resolver;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.input.style.CSSCounterRule;
import org.jfree.layouting.input.style.CSSPageRule;
import org.jfree.layouting.input.style.CSSStyleRule;
import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.StyleRule;
import org.jfree.layouting.input.style.StyleSheet;
import org.jfree.layouting.input.style.selectors.CSSSelector;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.DocumentMetaNode;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.model.LayoutElement;
import org.jfree.layouting.namespace.NamespaceCollection;
import org.jfree.layouting.namespace.NamespaceDefinition;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.util.AttributeMap;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.ResourceKeyCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.base.util.DebugLog;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

/**
 * A stateless implementation of the style rule matching. This implementation is
 * stateless within the current layout process.
 *
 * @author Thomas Morgner
 */
public class SimpleStyleRuleMatcher implements StyleRuleMatcher
{
  private LayoutProcess layoutProcess;
  private ResourceManager resourceManager;
  private CSSStyleRule[] activeStyleRules;
  private CSSStyleRule[] activePseudoStyleRules;
  private CSSPageRule[] pageRules;
  private CSSCounterRule[] counterRules;
  private NamespaceCollection namespaces;

  public SimpleStyleRuleMatcher()
  {
  }

  public void initialize(final LayoutProcess layoutProcess)
  {
    if (layoutProcess == null)
    {
      throw new NullPointerException();
    }
    this.layoutProcess = layoutProcess;
    this.resourceManager = layoutProcess.getResourceManager();

    final ArrayList pageRules = new ArrayList();
    final ArrayList counterRules = new ArrayList();
    final ArrayList styleRules = new ArrayList();
    final DocumentContext dc = this.layoutProcess.getDocumentContext();

    namespaces = dc.getNamespaces();
    final String[] nsUri = namespaces.getNamespaces();
    for (int i = 0; i < nsUri.length; i++)
    {
      final String uri = nsUri[i];
      final NamespaceDefinition nsDef = namespaces.getDefinition(uri);
      final ResourceKey rawKey = nsDef.getDefaultStyleSheetLocation();
      if (rawKey == null)
      {
        // there is no default stylesheet for that namespace.
        continue;
      }

      final ResourceKey baseKey =
          DocumentContextUtility.getBaseResource
              (layoutProcess.getDocumentContext());
      final StyleSheet styleSheet = parseStyleSheet(rawKey, baseKey);
      if (styleSheet == null)
      {
        continue;
      }
      addStyleRules(styleSheet, styleRules);
      addPageRules(styleSheet, pageRules);
      addCounterRules(styleSheet, counterRules);
    }

    final int metaNodeCount = dc.getMetaNodeCount();
    for (int i = 0; i < metaNodeCount; i++)
    {
      final DocumentMetaNode dmn = dc.getMetaNode(i);
      final String type = (String) dmn.getMetaAttribute("type");
      if ("link".equals(type))
      {
        handleLinkNode(dmn, styleRules, pageRules, counterRules);
      }
      else if ("style".equals(type))
      {
        handleStyleNode(dmn, styleRules, pageRules, counterRules);
      }
    }
    activeStyleRules = (CSSStyleRule[])
        styleRules.toArray(new CSSStyleRule[styleRules.size()]);
    this.pageRules = (CSSPageRule[])
        pageRules.toArray(new CSSPageRule[pageRules.size()]);
    this.counterRules = (CSSCounterRule[])
        counterRules.toArray(new CSSCounterRule[counterRules.size()]);

    styleRules.clear();
    for (int i = 0; i < activeStyleRules.length; i++)
    {
      final CSSStyleRule activeStyleRule = activeStyleRules[i];
      if (isPseudoElementRule(activeStyleRule) == false)
      {
        continue;
      }
      styleRules.add(activeStyleRule);
    }
    activePseudoStyleRules = (CSSStyleRule[])
        styleRules.toArray(new CSSStyleRule[styleRules.size()]);

  }

  private void handleLinkNode(final DocumentMetaNode node,
                              final ArrayList styleRules,
                              final ArrayList pageRules,
                              final ArrayList counterRules)
  {
    // do some external parsing
    // (Same as the <link> element of HTML)
    try
    {
      final Object href = node.getMetaAttribute("href");
      final ResourceKey baseKey =
          DocumentContextUtility.getBaseResource
              (layoutProcess.getDocumentContext());

      final ResourceKey derivedKey;
      if (baseKey == null)
      {
        derivedKey = resourceManager.createKey(href);
      }
      else
      {
        derivedKey = resourceManager.deriveKey(baseKey, String.valueOf(href));
      }

      final StyleSheet styleSheet = parseStyleSheet(derivedKey, null);
      if (styleSheet == null)
      {
        return;
      }
      addStyleRules(styleSheet, styleRules);
      addPageRules(styleSheet, pageRules);
      addCounterRules(styleSheet, counterRules);
    }
    catch (ResourceKeyCreationException e)
    {
      e.printStackTrace();
    }
  }


  private void handleStyleNode(final DocumentMetaNode node,
                               final ArrayList styleRules,
                               final ArrayList pageRules,
                               final ArrayList counterRules)
  {
    // do some inline parsing
    // (Same as the <style> element of HTML)
    // we also accept preparsed content ...

    final Object style = node.getMetaAttribute("#pcdata");
    if (style == null)
    {
      final Object content = node.getMetaAttribute("#content");
      if (content instanceof StyleSheet)
      {
        final StyleSheet styleSheet = (StyleSheet) content;
        addStyleRules(styleSheet, styleRules);
        addPageRules(styleSheet, pageRules);
        addCounterRules(styleSheet, counterRules);
        return;
      }
    }

    final String styleText = String.valueOf(style);
    try
    {
      final byte[] bytes = styleText.getBytes("UTF-8");
      final ResourceKey rawKey = resourceManager.createKey(bytes);

      final ResourceKey baseKey =
          DocumentContextUtility.getBaseResource
              (layoutProcess.getDocumentContext());
      final StyleSheet styleSheet = parseStyleSheet(rawKey, baseKey);
      if (styleSheet == null)
      {
        return;
      }
      addStyleRules(styleSheet, styleRules);
      addPageRules(styleSheet, pageRules);
      addCounterRules(styleSheet, counterRules);
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    catch (ResourceKeyCreationException e)
    {
      e.printStackTrace();
    }
  }


  private void addCounterRules(final StyleSheet styleSheet,
                               final ArrayList rules)
  {
    final int sc = styleSheet.getStyleSheetCount();
    for (int i = 0; i < sc; i++)
    {
      addCounterRules(styleSheet.getStyleSheet(i), rules);
    }

    final int rc = styleSheet.getRuleCount();
    for (int i = 0; i < rc; i++)
    {
      final StyleRule rule = styleSheet.getRule(i);
      if (rule instanceof CSSCounterRule)
      {
        final CSSCounterRule drule = (CSSCounterRule) rule;
        rules.add(drule);
      }
    }
  }


  private void addPageRules(final StyleSheet styleSheet,
                            final ArrayList rules)
  {
    final int sc = styleSheet.getStyleSheetCount();
    for (int i = 0; i < sc; i++)
    {
      addPageRules(styleSheet.getStyleSheet(i), rules);
    }

    final int rc = styleSheet.getRuleCount();
    for (int i = 0; i < rc; i++)
    {
      final StyleRule rule = styleSheet.getRule(i);
      if (rule instanceof CSSPageRule)
      {
        final CSSPageRule drule = (CSSPageRule) rule;
        rules.add(drule);
      }
    }
  }


  private void addStyleRules(final StyleSheet styleSheet,
                             final ArrayList activeStyleRules)
  {
    final int sc = styleSheet.getStyleSheetCount();
    for (int i = 0; i < sc; i++)
    {
      addStyleRules(styleSheet.getStyleSheet(i), activeStyleRules);
    }

    final int rc = styleSheet.getRuleCount();
    for (int i = 0; i < rc; i++)
    {
      final StyleRule rule = styleSheet.getRule(i);
      if (rule instanceof CSSStyleRule)
      {
        final CSSStyleRule drule = (CSSStyleRule) rule;
        activeStyleRules.add(drule);
      }
    }
  }

  private StyleSheet parseStyleSheet(final ResourceKey key,
                                     final ResourceKey context)
  {
    try
    {
      final Resource resource = resourceManager.create
          (key, context, StyleSheet.class);
      return (StyleSheet) resource.getResource();
    }
    catch (ResourceException e)
    {
      DebugLog.log("Unable to parse StyleSheet: " + e.getLocalizedMessage());
    }
    return null;
  }

  private boolean isPseudoElementRule(final CSSStyleRule rule)
  {
    final CSSSelector selector = rule.getSelector();
    if (selector == null)
    {
      return false;
    }

    if (selector.getSelectorType() != Selector.SAC_CONDITIONAL_SELECTOR)
    {
      return false;
    }

    final ConditionalSelector cs = (ConditionalSelector) selector;
    final Condition condition = cs.getCondition();
    if (condition.getConditionType() != Condition.SAC_PSEUDO_CLASS_CONDITION)
    {
      return false;
    }
    return true;
  }

  public boolean isMatchingPseudoElement(final LayoutElement element, final String pseudo)
  {
    for (int i = 0; i < activePseudoStyleRules.length; i++)
    {
      final CSSStyleRule activeStyleRule = activePseudoStyleRules[i];

      final CSSSelector selector = activeStyleRule.getSelector();
      final ConditionalSelector cs = (ConditionalSelector) selector;
      final Condition condition = cs.getCondition();

      final AttributeCondition ac = (AttributeCondition) condition;
      if (ObjectUtilities.equal(ac.getValue(), pseudo) == false)
      {
        continue;
      }

      final SimpleSelector simpleSelector = cs.getSimpleSelector();
      if (isMatch(element, simpleSelector))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates an independent copy of this style rule matcher.
   *
   * @return this instance, as this implementation is stateless
   */
  public StyleRuleMatcher deriveInstance()
  {
    return this;
  }

  public CSSStyleRule[] getMatchingRules(final LayoutElement element)
  {
    final ArrayList retvals = new ArrayList();
    for (int i = 0; i < activeStyleRules.length; i++)
    {
      final CSSStyleRule activeStyleRule = activeStyleRules[i];
      final CSSSelector selector = activeStyleRule.getSelector();
      if (selector == null)
      {
        continue;
      }

      if (isMatch(element, selector))
      {
        retvals.add(activeStyleRule);
      }
    }

//    Log.debug ("Got " + retvals.size() + " matching rules for " +
//            layoutContext.getTagName() + ":" +
//            layoutContext.getPseudoElement());

    return (CSSStyleRule[]) retvals.toArray
        (new CSSStyleRule[retvals.size()]);
  }

  private boolean isMatch(final LayoutElement node, final Selector selector)
  {
    final short selectorType = selector.getSelectorType();
    switch (selectorType)
    {
      case Selector.SAC_ANY_NODE_SELECTOR:
        return true;
      case Selector.SAC_ROOT_NODE_SELECTOR:
        return node.getParent() == null;
      case Selector.SAC_NEGATIVE_SELECTOR:
      {
        final NegativeSelector negativeSelector = (NegativeSelector) selector;
        return isMatch(node, negativeSelector) == false;
      }
      case Selector.SAC_DIRECT_ADJACENT_SELECTOR:
      {
        final SiblingSelector silbSelect = (SiblingSelector) selector;
        return isSilblingMatch(node, silbSelect);
      }
      case Selector.SAC_PSEUDO_ELEMENT_SELECTOR:
      {
        final LayoutContext layoutContext = node.getLayoutContext();
        return layoutContext.isPseudoElement();
      }
      case Selector.SAC_ELEMENT_NODE_SELECTOR:
      {
        final ElementSelector es = (ElementSelector) selector;
        final LayoutContext layoutContext = node.getLayoutContext();
        final String localName = es.getLocalName();
        if (localName != null)
        {
          if (localName.equals(layoutContext.getTagName()) == false)
          {
            return false;
          }
        }
        final String namespaceURI = es.getNamespaceURI();
        if (namespaceURI != null)
        {
          if (namespaceURI.equals(layoutContext.getNamespace()) == false)
          {
            return false;
          }
        }
        return true;
      }
      case Selector.SAC_CHILD_SELECTOR:
      {
        final DescendantSelector ds = (DescendantSelector) selector;
        if (isMatch(node, ds.getSimpleSelector()) == false)
        {
          return false;
        }
        final LayoutElement parent = node.getParent();
        return (isMatch(parent, ds.getAncestorSelector()));
      }
      case Selector.SAC_DESCENDANT_SELECTOR:
      {
        final DescendantSelector ds = (DescendantSelector) selector;
        if (isMatch(node, ds.getSimpleSelector()) == false)
        {
          return false;
        }
        return (isDescendantMatch(node, ds.getAncestorSelector()));
      }
      case Selector.SAC_CONDITIONAL_SELECTOR:
      {
        final ConditionalSelector cs = (ConditionalSelector) selector;
        if (evaluateCondition(node, cs.getCondition()) == false)
        {
          return false;
        }
        if (isMatch(node, cs.getSimpleSelector()) == false)
        {
          return false;
        }
        return true;
      }
      default:
        return false;
    }
  }

  private boolean evaluateCondition(final LayoutElement node,
                                    final Condition condition)
  {
    switch (condition.getConditionType())
    {
      case Condition.SAC_AND_CONDITION:
      {
        final CombinatorCondition cc = (CombinatorCondition) condition;
        return (evaluateCondition(node, cc.getFirstCondition()) &&
            evaluateCondition(node, cc.getSecondCondition()));
      }
      case Condition.SAC_OR_CONDITION:
      {
        final CombinatorCondition cc = (CombinatorCondition) condition;
        return (evaluateCondition(node, cc.getFirstCondition()) ||
            evaluateCondition(node, cc.getSecondCondition()));
      }
      case Condition.SAC_ATTRIBUTE_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final LayoutContext layoutContext = node.getLayoutContext();
        String namespaceURI = ac.getNamespaceURI();
        if (namespaceURI == null)
        {
          namespaceURI = layoutContext.getNamespace();
        }

        final AttributeMap attributes = layoutContext.getAttributes();
        final Object attr = attributes.getAttribute
            (namespaceURI, ac.getLocalName());
        if (ac.getValue() == null)
        {
          // dont care what's inside, as long as there is a value ..
          return attr != null;
        }
        else
        {
          return ObjectUtilities.equal(attr, ac.getValue());
        }
      }
      case Condition.SAC_CLASS_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final LayoutContext layoutContext = node.getLayoutContext();
        final String namespace = layoutContext.getNamespace();
        if (namespace == null)
        {
          return false;
        }
        final NamespaceDefinition ndef = namespaces.getDefinition(namespace);
        if (ndef == null)
        {
          return false;
        }
        final String[] classAttribute = ndef.getClassAttribute(
            layoutContext.getTagName());
        for (int i = 0; i < classAttribute.length; i++)
        {
          final String attr = classAttribute[i];
          final String htmlAttr = (String)
              layoutContext.getAttributes().getAttribute(
                  namespace, attr);
          if (isOneOfAttributes(htmlAttr, ac.getValue()))
          {
            return true;
          }
        }
        return false;
      }
      case Condition.SAC_ID_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final LayoutContext layoutContext = node.getLayoutContext();
        final AttributeMap attributes = layoutContext.getAttributes();
        final Object id = attributes.getAttribute(Namespaces.XML_NAMESPACE,
            "id");
        return ObjectUtilities.equal(ac.getValue(), id);
      }
      case Condition.SAC_LANG_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final LayoutContext layoutContext = node.getLayoutContext();
        final Locale locale = layoutContext.getLanguage();
        final String lang = locale.getLanguage();
        return isBeginHyphenAttribute(lang, ac.getValue());
      }
      case Condition.SAC_NEGATIVE_CONDITION:
      {
        final NegativeCondition nc = (NegativeCondition) condition;
        return evaluateCondition(node, nc.getCondition()) == false;
      }
      case Condition.SAC_ONE_OF_ATTRIBUTE_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final LayoutContext layoutContext = node.getLayoutContext();
        final String attr = (String)
            layoutContext.getAttributes().getAttribute
                (ac.getNamespaceURI(), ac.getLocalName());
        return isOneOfAttributes(attr, ac.getValue());
      }
      case Condition.SAC_PSEUDO_CLASS_CONDITION:
      {
        final AttributeCondition ac = (AttributeCondition) condition;
        final LayoutContext layoutContext = node.getLayoutContext();
        final String pseudoClass = layoutContext.getPseudoElement();
        if (pseudoClass == null)
        {
          return false;
        }
        if (pseudoClass.equals(ac.getValue()))
        {
          return true;
        }
        return false;
      }
      case Condition.SAC_ONLY_CHILD_CONDITION:
      case Condition.SAC_ONLY_TYPE_CONDITION:
      case Condition.SAC_POSITIONAL_CONDITION:
      case Condition.SAC_CONTENT_CONDITION:
      default:
      {
        // todo
        return false;
      }
    }
  }

  private boolean isOneOfAttributes(final String attrValue, final String value)
  {
    if (attrValue == null)
    {
      return false;
    }
    if (attrValue.equals(value))
    {
      return true;
    }

    final StringTokenizer strTok = new StringTokenizer(attrValue);
    while (strTok.hasMoreTokens())
    {
      final String token = strTok.nextToken();
      if (token.equals(value))
      {
        return true;
      }
    }
    return false;
  }

  private boolean isBeginHyphenAttribute(final String attrValue, final String value)
  {
    if (attrValue == null)
    {
      return false;
    }
    if (value == null)
    {
      return false;
    }
    return (attrValue.startsWith(value));

  }

  private boolean isDescendantMatch(final LayoutElement node,
                                    final Selector selector)
  {
    LayoutElement parent = node.getParent();
    while (parent != null)
    {
      if (isMatch(parent, selector))
      {
        return true;
      }
      parent = parent.getParent();
    }
    return false;
  }

  private boolean isSilblingMatch(final LayoutElement node,
                                  final SiblingSelector select)
  {
    LayoutElement pred = node.getPrevious();
    while (pred != null)
    {
      if (isMatch(pred, select))
      {
        return true;
      }
      pred = pred.getPrevious();
    }
    return false;
  }

  public CSSPageRule[] getPageRule(final CSSValue pageName, final PseudoPage[] pseudoPages)
  {
    final CSSPageRule[] pageRules = this.pageRules;
    final ArrayList rules = new ArrayList();
    for (int i = 0; i < pageRules.length; i++)
    {
      final CSSPageRule rule = pageRules[i];
      final String rulePageName = rule.getName();
      // Check the page name.
      if (rulePageName != null)
      {
        if (rulePageName.equals(pageName) == false)
        {
          continue;
        }
      }

      // And the pseudo page ..
      final String rulePseudoPage = rule.getPseudoPage();
      if (rulePseudoPage != null)
      {
        for (int j = 0; j < pseudoPages.length; j++)
        {
          final PseudoPage pseudoPage = pseudoPages[j];
          if (pseudoPage.toString().equalsIgnoreCase(rulePseudoPage))
          {
            rules.add(rule);
          }
        }
        continue;
      }

      rules.add(rule);
    }

    return (CSSPageRule[]) rules.toArray(new CSSPageRule[rules.size()]);
  }
}
