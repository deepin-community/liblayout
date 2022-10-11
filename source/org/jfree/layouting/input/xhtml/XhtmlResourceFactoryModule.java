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
 * $Id: XhtmlResourceFactoryModule.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.xhtml;

import org.pentaho.reporting.libraries.resourceloader.factory.AbstractFactoryModule;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceCreationException;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;


public class XhtmlResourceFactoryModule extends AbstractFactoryModule
{
  private static final String[] MIMETYPES =
          {
             "application/xhtml+xml",
             "application/vnd.pwg-xhtml-print+xml",
             "text/html", "text/xml", "application/xml"
          };

  private static final String[] EXTENSIONS =
          {
                  "xht", "xhtml",
                  "htm", "html", "shtml"
          };

  public XhtmlResourceFactoryModule ()
  {
  }

  protected int[] getFingerPrint()
  {
    return new int[0];
  }

  protected String[] getMimeTypes()
  {
    return MIMETYPES;
  }

  protected String[] getFileExtensions()
  {
    return EXTENSIONS;
  }

  public int getHeaderFingerprintSize ()
  {
    return 0;
  }

  public Resource create(final ResourceManager caller,
                         final ResourceData data,
                         final ResourceKey context)
          throws ResourceCreationException, ResourceLoadingException
  {
    return null;
  }

//
//  public XhtmlDocument createDocument (final byte[] data,
//                                  final URL source,
//                                  final String fileName,
//                                  final String mimeType)
//          throws IOException
//  {
//    try
//    {
//      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//      final DocumentBuilder db = dbf.newDocumentBuilder();
//      InputSource is = new InputSource(new ByteArrayInputStream(data));
//      Document doc = db.parse(is);
//      return new XhtmlDocument(source, data, doc);
//    }
//    catch(IOException ioe)
//    {
//      throw ioe;
//    }
//    catch (ParserConfigurationException e)
//    {
//      throw new IOException(e.getMessage());
//    }
//    catch (SAXException e)
//    {
//      throw new IOException(e.getMessage());
//    }
//  }
//
//
//  public XhtmlDocument createDocument (final InputStream data,
//                                  final URL source,
//                                  final String fileName,
//                                  final String mimeType)
//          throws IOException
//  {
//    try
//    {
//      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//      final DocumentBuilder db = dbf.newDocumentBuilder();
//      InputSource is = new InputSource(data);
//      Document doc = db.parse(is);
//      return new XhtmlDocument(source, null, doc);
//    }
//    catch(IOException ioe)
//    {
//      throw ioe;
//    }
//    catch (ParserConfigurationException e)
//    {
//      throw new IOException(e.getMessage());
//    }
//    catch (SAXException e)
//    {
//      throw new IOException(e.getMessage());
//    }
//  }
}
