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
 * $Id: XhtmlDocument.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.input.xhtml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.pentaho.reporting.libraries.base.util.IOUtils;

/**
 * Creation-Date: 03.01.2006, 14:08:39
 *
 * @author Thomas Morgner
 */
public class XhtmlDocument implements Serializable
{
  private transient Document document;
  private transient byte[] data;
  private URL source;
  private transient boolean hasErrors;

  public XhtmlDocument(final URL source,
                       final byte[] data,
                       final Document document)
  {
    if (source == null)
    {
      throw new NullPointerException();
    }
    this.data = data;
    this.source = source;
    this.document = document;
  }

  protected byte[] getData()
  {
    if (data == null)
    {
      try
      {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final InputStream inputStream = source.openStream();
        IOUtils.getInstance().copyStreams(inputStream, bout);
        data = bout.toByteArray();
      }
      catch (IOException e)
      {
        data = new byte[0];
      }
    }
    return data;
  }

  public URL getSource()
  {
    return source;
  }

  public Document getDocument()
  {
    if (hasErrors)
    {
      return null;
    }
    if (document == null)
    {
      try
      {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final InputSource is = new InputSource(new ByteArrayInputStream(getData()));
        final Document doc = db.parse(is);
        //doc.setDocumentURI(source.toExternalForm());
        return doc;
      }
      catch (Exception ioe)
      {
        document = null;
        hasErrors = true;
      }
    }
    return document;
  }
}
