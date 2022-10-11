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
 * $Id: FixNamespaceConditionFactory.java 2739 2007-04-02 11:41:22Z taqua $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.parser;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.PositionalCondition;

/**
 * Creation-Date: 23.04.2006, 15:13:10
 *
 * @author Thomas Morgner
 */
public class FixNamespaceConditionFactory implements ConditionFactory
{
  private ConditionFactory parent;

  public FixNamespaceConditionFactory(final ConditionFactory parent)
  {
    if (parent == null)
    {
      throw new NullPointerException();
    }
    this.parent = parent;
  }

  public CombinatorCondition createAndCondition(final Condition first,
                                                final Condition second)
          throws CSSException
  {
    return parent.createAndCondition(first, second);
  }

  public CombinatorCondition createOrCondition(final Condition first,
                                               final Condition second)
          throws CSSException
  {
    return parent.createOrCondition(first, second);
  }

  public NegativeCondition createNegativeCondition(final Condition condition)
          throws CSSException
  {
    return parent.createNegativeCondition(condition);
  }

  public PositionalCondition createPositionalCondition(final int position,
                                                       final boolean typeNode,
                                                       final boolean type)
          throws CSSException
  {
    return parent.createPositionalCondition(position, typeNode, type);
  }

  public AttributeCondition createAttributeCondition(final String localName,
                                                     final String namespaceURI,
                                                     final boolean specified,
                                                     final String value)
          throws CSSException
  {
    if (namespaceURI != null)
    {
      return parent.createAttributeCondition
              (localName, namespaceURI, specified, value);
    }
    else
    {
      final String[] ns = StyleSheetParserUtil.parseNamespaceIdent(localName);
      return parent.createAttributeCondition(ns[1], ns[0], specified, value);
    }
  }

  public AttributeCondition createIdCondition(final String value)
          throws CSSException
  {
    return parent.createIdCondition(value);
  }

  public LangCondition createLangCondition(final String lang)
          throws CSSException
  {
    return parent.createLangCondition(lang);
  }

  public AttributeCondition createOneOfAttributeCondition(final String localName,
                                                          final String namespaceURI,
                                                          final boolean specified,
                                                          final String value)
          throws CSSException
  {
    if (namespaceURI != null)
    {
      return parent.createOneOfAttributeCondition
              (localName, namespaceURI, specified, value);
    }
    else
    {
      final String[] ns = StyleSheetParserUtil.parseNamespaceIdent(localName);
      return parent.createOneOfAttributeCondition(ns[1], ns[0], specified, value);
    }
  }

  public AttributeCondition createBeginHyphenAttributeCondition(final String localName,
                                                                final String namespaceURI,
                                                                final boolean specified,
                                                                final String value)
          throws CSSException
  {
    if (namespaceURI != null)
    {
      return parent.createBeginHyphenAttributeCondition
              (localName, namespaceURI, specified, value);
    }
    else
    {
      final String[] ns = StyleSheetParserUtil.parseNamespaceIdent(localName);
      return parent.createBeginHyphenAttributeCondition(ns[1], ns[0], specified, value);
    }
  }

  public AttributeCondition createClassCondition(final String namespaceURI,
                                                 final String value)
          throws CSSException
  {
    return parent.createClassCondition(namespaceURI, value);
  }

  public AttributeCondition createPseudoClassCondition(final String namespaceURI,
                                                       final String value)
          throws CSSException
  {
    if (namespaceURI != null)
    {
      return parent.createPseudoClassCondition(namespaceURI, value);
    }
    else
    {
      final String[] ns = StyleSheetParserUtil.parseNamespaceIdent(value);
      return parent.createPseudoClassCondition(ns[0], ns[1]);
    }
  }

  public Condition createOnlyChildCondition()
          throws CSSException
  {
    return parent.createOnlyChildCondition();
  }

  public Condition createOnlyTypeCondition()
          throws CSSException
  {
    return parent.createOnlyTypeCondition();
  }

  public ContentCondition createContentCondition(final String data)
          throws CSSException
  {
    return parent.createContentCondition(data);
  }
}
