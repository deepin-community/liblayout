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
 * $Id: XhtmlInputDriver.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.xhtml;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.jfree.layouting.DefaultLayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedException;
import org.jfree.layouting.modules.output.html.StreamingHtmlOutputProcessor;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.util.NullOutputStream;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XhtmlInputDriver
{
  public static final String NAMESPACE = "http://www.w3.org/1999/xhtml";

  private InputFeed feed;

  public XhtmlInputDriver (final InputFeed feed)
  {
    this.feed = feed;
  }

  public void processDomTree (final Document doc)
          throws InputFeedException
  {
    final Element rootElement = doc.getDocumentElement();
    if ("html".equalsIgnoreCase(rootElement.getTagName()))
    {
      // go the long way ...
      processFullDocument(rootElement);
    }
    else
    {
      // the short way will be enough
      feed.startDocument();
      processBodyElement(rootElement);
      feed.endDocument();
    }
  }

  private void processFullDocument (final Element rootElement)
          throws InputFeedException
  {
    feed.startDocument();
    feed.startMetaInfo();
    final NodeList headList = rootElement.getElementsByTagName("head");
    for (int i = 0; i < headList.getLength(); i++)
    {
      final Element headerElement = (Element) headList.item(i);
      final NodeList titles = headerElement.getElementsByTagName("title");
      for (int t = 0; t < titles.getLength(); t++)
      {
        final Element title = (Element) titles.item(t);
        feed.addDocumentAttribute(DocumentContext.TITLE_ATTR, getCData(title));
      }

      final NodeList metas = headerElement.getChildNodes();
      for (int t = 0; t < metas.getLength(); t++)
      {
        final Node n = metas.item(t);
        if (n instanceof Element == false)
        {
          continue;
        }

        final Element meta = (Element) metas.item(t);
        if ("title".equalsIgnoreCase(meta.getTagName()))
        {
          continue;
        }

        feed.startMetaNode();
        feed.setMetaNodeAttribute("type", meta.getTagName());
        final NamedNodeMap nnm = meta.getAttributes();
        for (int ac = 0; ac < nnm.getLength(); ac++)
        {
          final Attr attr = (Attr) nnm.item(ac);
          feed.setMetaNodeAttribute(attr.getName(), attr.getValue());
        }
        feed.setMetaNodeAttribute("#pcdata", getCData(meta));
        feed.endMetaNode();
      }
    }
    feed.endMetaInfo();

    processBodyElement(rootElement);
    feed.endDocument();
  }

  private String getCData (final Element element)
  {
    final StringBuffer buffer = new StringBuffer();
    final NodeList nl = element.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++)
    {
      final Node n = nl.item(i);
      if (n instanceof Text)
      {
        final Text text = (Text) n;
        buffer.append(text.getData());
      }
    }
    return buffer.toString();
  }

  private void processBodyElement (final Element element)
          throws InputFeedException
  {
    feed.startElement(NAMESPACE, element.getTagName());

    final NamedNodeMap nnm = element.getAttributes();
    for (int ac = 0; ac < nnm.getLength(); ac++)
    {
      final Attr attr = (Attr) nnm.item(ac);
      feed.setAttribute(NAMESPACE, attr.getName(), attr.getValue());
    }

    final NodeList childs = element.getChildNodes();
    for (int t = 0; t < childs.getLength(); t++)
    {
      final Node n = childs.item(t);
      if (n instanceof Element)
      {
        final Element childElement = (Element) n;
        processBodyElement(childElement);
      }
      else if (n instanceof Text)
      {
        final Text tx = (Text) n;
        feed.addContent(tx.getData());
      }
    }

    // process all other elements ...
    feed.endElement();
  }

  public static void main (final String[] args)
          throws IOException, NormalizationException
  {
    LibLayoutBoot.getInstance().start();

    final OutputStream out = new NullOutputStream();

    final URL url = new URL ("file:///home/src/jfreereport/head/liblayout/styletest/simple.html");
    final XhtmlResourceFactoryModule module = new XhtmlResourceFactoryModule();
   // XhtmlDocument doc = module.createDocument(url.openStream(), url, url.toExternalForm(), "text/html");

    final long startTime = System.currentTimeMillis();
    for (int i = 0; i < 10; i++)
    {
      final DefaultLayoutProcess process = new DefaultLayoutProcess
                  (new StreamingHtmlOutputProcessor
                      (LibLayoutBoot.getInstance().getGlobalConfig()));
      final XhtmlInputDriver idrDriver = new XhtmlInputDriver(process.getInputFeed());
     // idrDriver.processDomTree(doc.getDocument());
    }
    final long endTime = System.currentTimeMillis();

    System.out.println("Done!: " + (endTime -startTime));
  }
}
