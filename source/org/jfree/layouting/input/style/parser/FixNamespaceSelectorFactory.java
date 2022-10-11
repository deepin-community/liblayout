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
 * $Id: FixNamespaceSelectorFactory.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

/**
 * Creation-Date: 23.04.2006, 15:06:07
 *
 * @author Thomas Morgner
 */
public class FixNamespaceSelectorFactory implements SelectorFactory
{
  private SelectorFactory parent;


  public FixNamespaceSelectorFactory(final SelectorFactory parent)
  {
    if (parent == null)
    {
      throw new NullPointerException();
    }
    this.parent = parent;
  }

  public ConditionalSelector createConditionalSelector(final SimpleSelector selector,
                                                       final Condition condition)
          throws CSSException
  {
    return parent.createConditionalSelector(selector, condition);
  }

  public SimpleSelector createAnyNodeSelector() throws CSSException
  {
    return parent.createAnyNodeSelector();
  }

  public SimpleSelector createRootNodeSelector() throws CSSException
  {
    return parent.createRootNodeSelector();
  }

  public NegativeSelector createNegativeSelector(final SimpleSelector selector)
          throws CSSException
  {
    return parent.createNegativeSelector(selector);
  }

  public ElementSelector createElementSelector(final String namespaceURI,
                                               final String tagName)
          throws CSSException
  {

    if (namespaceURI != null)
    {
      return parent.createElementSelector(namespaceURI, tagName);
    }
    else
    {
      if (tagName == null)
      {
        return parent.createElementSelector(null, null);
      }
      else
      {
        final String[] ns = StyleSheetParserUtil.parseNamespaceIdent(tagName);
        return parent.createElementSelector(ns[0], ns[1]);
      }
    }
  }

  public CharacterDataSelector createTextNodeSelector(final String data)
          throws CSSException
  {
    return parent.createTextNodeSelector(data);
  }

  public CharacterDataSelector createCDataSectionSelector(final String data)
          throws CSSException
  {
    return parent.createCDataSectionSelector(data);
  }

  public ProcessingInstructionSelector createProcessingInstructionSelector(final String target,
                                                                           final String data)
          throws CSSException
  {
    return parent.createProcessingInstructionSelector(target, data);
  }

  public CharacterDataSelector createCommentSelector(final String data) throws
          CSSException
  {
    return parent.createCommentSelector(data);
  }

  public ElementSelector createPseudoElementSelector(final String namespaceURI,
                                                     final String pseudoName)
          throws
          CSSException
  {
    if (namespaceURI != null)
    {
      return parent.createPseudoElementSelector(namespaceURI, pseudoName);
    }
    else
    {
      final String[] ns = StyleSheetParserUtil.parseNamespaceIdent(pseudoName);
      return parent.createPseudoElementSelector(ns[0], ns[1]);
    }
  }

  public DescendantSelector createDescendantSelector(final Selector parent,
                                                     final SimpleSelector descendant)
          throws
          CSSException
  {
    return this.parent.createDescendantSelector(parent, descendant);
  }

  public DescendantSelector createChildSelector(final Selector parent,
                                                final SimpleSelector child)
          throws
          CSSException
  {
    return this.parent.createChildSelector(parent, child);
  }

  public SiblingSelector createDirectAdjacentSelector(final short nodeType,
                                                      final Selector child,
                                                      final SimpleSelector directAdjacent)
          throws
          CSSException
  {
    return parent.createDirectAdjacentSelector(nodeType, child, directAdjacent);
  }
}
