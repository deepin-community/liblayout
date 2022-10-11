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
 * $Id: CSSAttrFunction.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.style.values;

/**
 * Creation-Date: 05.12.2005, 20:41:01
 *
 * @author Thomas Morgner
 */
public class CSSAttrFunction extends CSSFunctionValue
{
  private String namespace;
  private String name;
  private String type;

  public CSSAttrFunction(final String namespace,
                         final String name,
                         final String type)
  {
    super("attr", produceParameters(namespace, name, type));
    this.namespace = namespace;
    this.name = name;
    this.type = type;
  }

  public CSSAttrFunction(final String namespace,
                         final String name)
  {
    this(namespace, name, null);
  }

  private static CSSValue[] produceParameters(final String namespace,
                                              final String name,
                                              final String type)
  {
    if (name == null)
    {
      throw new NullPointerException();
    }
    final CSSConstant nameConst;
    if (namespace == null)
    {
      nameConst = new CSSConstant("");
    }
    else
    {
      nameConst = new CSSConstant(namespace);
    }
    if (type == null)
    {
      return new CSSValue[]{nameConst, new CSSConstant(name)};
    }
    else
    {
      return new CSSValue[]{nameConst, new CSSConstant(name), new CSSConstant(
              type)};
    }
  }

  public String getName()
  {
    return name;
  }

  public String getType()
  {
    return type;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public String getCSSText()
  {
    if (type != null)
    {
      if (namespace == null)
      {
        return "attr(|" + name + ", " + type + ')';
      }
      else
      {
        return "attr(" + namespace + '|' + name + ", " + type + ')';
      }
    }
    else
    {
      if (namespace == null)
      {
        return "attr(|" + name + ')';
      }
      else
      {
        return "attr(" + namespace + '|' + name + ')';
      }
    }
  }
}
