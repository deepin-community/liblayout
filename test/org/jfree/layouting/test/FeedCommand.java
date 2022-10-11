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
 * $Id$
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.test;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LayoutProcessState;
import org.jfree.layouting.StateException;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedException;

/**
 * Creation-Date: Jan 5, 2007, 2:09:03 PM
 *
 * @author Thomas Morgner
 */
public class FeedCommand
{
  private String method;
  private String namespace;
  private String name;
  private Object attr;

  public FeedCommand(final String method)
  {
    this.method = method;
  }

  private FeedCommand(final String method,
                      final String namespace,
                      final String name,
                      final Object attr)
  {
    this.method = method;
    this.namespace = namespace;
    this.name = name;
    this.attr = attr;
  }

  public LayoutProcessState execute(LayoutProcess lp)
      throws StateException, InputFeedException
  {
    final InputFeed inputFeed = lp.getInputFeed();

    if ("startDocument".equals(method))
    {
      inputFeed.startDocument();
    }
    else if ("startElement".equals(method))
    {
      inputFeed.startElement(namespace, name);
    }
    else if ("startMetaInfo".equals(method))
    {
      inputFeed.startMetaInfo();
    }
    else if ("startMetaNode".equals(method))
    {
      inputFeed.startMetaNode();
    }
    else if ("addContent".equals(method))
    {
      inputFeed.addContent(attr.toString());
    }
    else if ("addDocumentAttribute".equals(method))
    {
      inputFeed.addDocumentAttribute(name, attr);
    }
    else if ("endDocument".equals(method))
    {
      inputFeed.endDocument();
    }
    else if ("endElement".equals(method))
    {
      inputFeed.endElement();
    }
    else if ("endMetaInfo".equals(method))
    {
      inputFeed.endMetaInfo();
    }
    else if ("endMetaNode".equals(method))
    {
      inputFeed.endMetaNode();
    }
    else if ("setAttribute".equals(method))
    {
      inputFeed.setAttribute(namespace, name, attr);
    }
    else if ("setMetaNodeAttribute".equals(method))
    {
      inputFeed.setMetaNodeAttribute(name, attr);
    }
    else
    {
      throw new IllegalStateException();
    }

    return lp.saveState();
  }


  /**
   * Starts the document processing. This is
   * the first method to call. After calling
   * this method, the meta-data should be fed
   * into the inputfeed.
   */
  public static FeedCommand startDocument()
  {
    return new FeedCommand("startDocument");
  }

  /**
   * Signals, that meta-data follows. Calling this method is only valid directly
   * after startDocument has been called.
   */
  public static FeedCommand startMetaInfo()
  {
    return new FeedCommand("startMetaInfo");
  }

  /**
   * Adds document attributes. Document attributes hold object factories and
   * document wide resources which appear only once.
   *
   * @param name
   * @param attr
   */
  public static FeedCommand addDocumentAttribute(String name, Object attr)
  {
    return new FeedCommand("addDocumentAttribute", null, name, attr);
  }

  /**
   * Starts a new meta-node structure. Meta-Nodes are used to hold content that
   * can appear more than once (like stylesheet declarations).
   * <p/>
   * For now, only stylesheet declarations are defined as meta-node content;
   * more content types will surely arise in the future.
   * <p/>
   * Calling this method is only valid after 'startMetaInfo' has been called.
   */
  public static FeedCommand startMetaNode()
  {
    return new FeedCommand("startMetaInfo");
  }

  /**
   * Defines an attribute for the meta-nodes. For each meta node, at least the
   * 'type' attribute (namespace: LibLayout) should be defined.
   *
   * @param name
   * @param attr
   */
  public static FeedCommand setMetaNodeAttribute(String name, Object attr)
  {
    return new FeedCommand("setMetaNodeAttribute", null, name, attr);
  }

  public static FeedCommand endMetaNode()
  {
    return new FeedCommand("endMetaNode");
  }

  public static FeedCommand endMetaInfo()
  {
    return new FeedCommand("endMetaInfo");
  }

  public static FeedCommand startElement(String namespace, String name)
  {
    return new FeedCommand("startElement", namespace, name, null);
  }

  public static FeedCommand setAttribute(String namespace,
                                         String name,
                                         Object attr)
  {
    return new FeedCommand("setAttribute", namespace, name, attr);
  }

  public static FeedCommand addContent(String text)
  {
    return new FeedCommand("addContent", null, null, text);
  }

  public static FeedCommand endElement()
  {
    return new FeedCommand("endElement");
  }

  public static FeedCommand endElement(String namespace, String name)
  {
    return new FeedCommand("endElement", namespace, name, null);
  }

  public static FeedCommand endDocument()
  {
    return new FeedCommand("endDocument");
  }


  public String toString()
  {
    return "FeedCommand{" +
        "method='" + method + '\'' +
        ", namespace='" + namespace + '\'' +
        ", name='" + name + '\'' +
        ", attr=" + attr +
        '}';
  }
}
