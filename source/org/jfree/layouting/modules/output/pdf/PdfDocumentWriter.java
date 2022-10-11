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
 * $Id: PdfDocumentWriter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.pdf;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.layouting.LibLayoutInfo;
import org.jfree.layouting.modules.output.graphics.LogicalPageDrawable;
import org.jfree.layouting.modules.output.graphics.PhysicalPageDrawable;
import org.jfree.layouting.output.pageable.LogicalPageKey;
import org.jfree.layouting.output.pageable.PhysicalPageKey;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.page.PageGrid;
import org.jfree.layouting.renderer.model.page.PhysicalPageBox;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.pentaho.reporting.libraries.base.config.Configuration;
import org.pentaho.reporting.libraries.base.util.DebugLog;
import org.pentaho.reporting.libraries.fonts.itext.BaseFontSupport;
import org.pentaho.reporting.libraries.fonts.itext.ITextFontRegistry;

/**
 * Creation-Date: 02.12.2006, 17:49:47
 *
 * @author Thomas Morgner
 */
public class PdfDocumentWriter
{

  /**
   * A useful constant for specifying the PDF creator.
   */
  private static final String CREATOR = LibLayoutInfo.getInstance().getName() + " version "
          + LibLayoutInfo.getInstance().getVersion();

  /**
   * A bytearray containing an empty password. iText replaces the owner password with
   * random values, but Adobe allows to have encryption without an owner password set.
   * Copied from iText
   */
  private static final byte[] PDF_PASSWORD_PAD = {
    (byte) 0x28, (byte) 0xBF, (byte) 0x4E, (byte) 0x5E, (byte) 0x4E, (byte) 0x75,
    (byte) 0x8A, (byte) 0x41, (byte) 0x64, (byte) 0x00, (byte) 0x4E, (byte) 0x56,
    (byte) 0xFF, (byte) 0xFA, (byte) 0x01, (byte) 0x08, (byte) 0x2E, (byte) 0x2E,
    (byte) 0x00, (byte) 0xB6, (byte) 0xD0, (byte) 0x68, (byte) 0x3E, (byte) 0x80,
    (byte) 0x2F, (byte) 0x0C, (byte) 0xA9, (byte) 0xFE, (byte) 0x64, (byte) 0x53,
    (byte) 0x69, (byte) 0x7A};

  private Document document;
  private OutputStream out;
  private PdfWriter writer;
  private boolean awaitOpenDocument;
  private Configuration config;

  /**
   * The PDF font support.
   */
  private BaseFontSupport fontSupport;

  public PdfDocumentWriter(final Configuration config,
                           final OutputStream out,
                           final PdfOutputProcessorMetaData metaData)
  {
    this.out = out;
    this.config = config;
    this.fontSupport = new BaseFontSupport((ITextFontRegistry) metaData.getITextFontStorage().getFontRegistry());
  }

  public Document getDocument()
  {
    return document;
  }

  public void setDocument(final Document document)
  {
    this.document = document;
  }

  public void open () throws DocumentException
  {
    setDocument(new Document());
    //pageSize, marginLeft, marginRight, marginTop, marginBottom));

    writer = PdfWriter.getInstance(getDocument(), out);
    writer.setLinearPageMode();


    final char version = getVersion();
    writer.setPdfVersion(version);

    final String encrypt = config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.security.Encryption");

    if (encrypt != null)
    {
      if (encrypt.equals(PdfOutputModule.SECURITY_ENCRYPTION_128BIT) == true
              || encrypt.equals(PdfOutputModule.SECURITY_ENCRYPTION_40BIT) == true)
      {
        final String userpassword = config.getConfigProperty("org.jfree.layouting.modules.output.pdf.security.UserPassword");
        final String ownerpassword = config.getConfigProperty("org.jfree.layouting.modules.output.pdf.security.OwnerPassword");
        //Log.debug ("UserPassword: " + userpassword + " - OwnerPassword: " + ownerpassword);
        final byte[] userpasswordbytes = DocWriter.getISOBytes(userpassword);
        byte[] ownerpasswordbytes = DocWriter.getISOBytes(ownerpassword);
        if (ownerpasswordbytes == null)
        {
          ownerpasswordbytes = PDF_PASSWORD_PAD;
        }
        writer.setEncryption(userpasswordbytes, ownerpasswordbytes, getPermissions(),
                encrypt.equals(PdfOutputModule.SECURITY_ENCRYPTION_128BIT));
      }
    }

    /**
     * MetaData can be set when the writer is registered to the document.
     */
    final String title = config.getConfigProperty("org.jfree.layouting.modules.output.pdf.Title");
    final String subject = config.getConfigProperty("org.jfree.layouting.modules.output.pdf.Subject");
    final String author = config.getConfigProperty("org.jfree.layouting.modules.output.pdf.Author");
    final String keyWords = config.getConfigProperty("org.jfree.layouting.modules.output.pdf.Keywords");

    if (title != null)
    {
      document.addTitle(title);
    }
    if (author != null)
    {
      document.addAuthor(author);
    }
    if (keyWords != null)
    {
      document.addKeywords(keyWords);
    }
    if (keyWords != null)
    {
      document.addSubject(subject);
    }

    getDocument().addCreator(CREATOR);
    getDocument().addCreationDate();

    //getDocument().open();
    awaitOpenDocument = true;
  }


  protected void processPhysicalPage(final PageGrid pageGrid,
                                     final LogicalPageBox logicalPage,
                                     final int row,
                                     final int col,
                                     final PhysicalPageKey pageKey)
      throws DocumentException
  {
    final PhysicalPageBox page = pageGrid.getPage(row, col);
    final float width = (float) StrictGeomUtility.toExternalValue(page.getWidth());
    final float height = (float) StrictGeomUtility.toExternalValue(page.getHeight());

    final Rectangle pageSize = new Rectangle(width, height);

    final float marginLeft = (float)
        StrictGeomUtility.toExternalValue(page.getImageableX());
    final float marginRight =
            (float) StrictGeomUtility.toExternalValue
                (page.getWidth()
            - page.getImageableWidth()
            - page.getImageableX());
    final float marginTop = (float) StrictGeomUtility.toExternalValue
        (page.getImageableY());
    final float marginBottom =
            (float) StrictGeomUtility.toExternalValue
                (page.getHeight()
            - page.getImageableHeight()
            - page.getImageableY());

    getDocument().setPageSize(pageSize);
    getDocument().setMargins(marginLeft, marginRight, marginTop, marginBottom);

    if (awaitOpenDocument)
    {
      getDocument().open();
    }
    // todo: We should set some clipping or spanned pages will look funny ..
    // and now process the box ..
    final Graphics2D graphics = writer.getDirectContent().createGraphics(width, height, fontSupport);

    final LogicalPageDrawable logicalPageDrawable = new LogicalPageDrawable(logicalPage);
    final PhysicalPageDrawable drawable = new PhysicalPageDrawable(logicalPageDrawable, page);
    drawable.draw(graphics, new Rectangle2D.Double(0,0, width, height));

    graphics.dispose();

    getDocument().newPage();
  }

  protected void processLogicalPage(final LogicalPageKey key,
                                    final LogicalPageBox logicalPage)
      throws DocumentException
  {

    final float width = (float) StrictGeomUtility.toExternalValue(logicalPage.getPageWidth());
    final float height = (float) StrictGeomUtility.toExternalValue(logicalPage.getPageHeight());

    final Rectangle pageSize = new Rectangle(width, height);

    getDocument().setPageSize(pageSize);
    getDocument().setMargins(0, 0, 0, 0);

    if (awaitOpenDocument)
    {
      getDocument().open();
    }

    final Graphics2D graphics = writer.getDirectContent().createGraphics(width, height, fontSupport);
    // and now process the box ..
    final LogicalPageDrawable logicalPageDrawable = new LogicalPageDrawable(logicalPage);
    logicalPageDrawable.draw(graphics, new Rectangle2D.Double(0,0, width, height));

    graphics.dispose();

    getDocument().newPage();
  }

  /**
   * Closes the document.
   */
  public void close ()
  {
    this.getDocument().close();
    this.fontSupport.close();
    try
    {
      this.out.flush();
    }
    catch (IOException e)
    {
      DebugLog.log("Flushing the PDF-Export-Stream failed.");
    }
    this.document = null;
    this.writer = null;
  }

  /**
   * Extracts the to be generated PDF version as iText parameter from the given property
   * value. The value has the form "1.x" where x is the extracted version.
   *
   * @return the itext character defining the version.
   */
  private char getVersion ()
  {
    final String version = config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.Version");

    if (version == null)
    {
      return '4';
    }
    if (version.length() < 3)
    {
      DebugLog.log("PDF version specification is invalid, using default version '1.4'.");
      return '4';
    }
    final char retval = version.charAt(2);
    if (retval < '2' || retval > '9')
    {
      DebugLog.log("PDF version specification is invalid, using default version '1.4'.");
      return '4';
    }
    return retval;
  }


  /**
   * Extracts the permissions for this PDF. The permissions are returned as flags in the
   * integer value. All permissions are defined as properties which have to be set before
   * the target is opened.
   *
   * @return the permissions.
   */
  private int getPermissions ()
  {
    final String printLevel = config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.security.PrintLevel");

    final boolean allowPrinting = "none".equals(printLevel) == false;
    final boolean allowDegradedPrinting = "degraded".equals(printLevel);

    final boolean allowModifyContents = "true".equals(config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.security.AllowModifyContents"));
    final boolean allowModifyAnn = "true".equals(config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.security.AllowModifyAnnotations"));

    final boolean allowCopy = "true".equals(config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.security.AllowCopy"));
    final boolean allowFillIn = "true".equals(config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.security.AllowFillIn"));
    final boolean allowScreenReaders = "true".equals(config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.security.AllowScreenReader"));
    final boolean allowAssembly = "true".equals(config.getConfigProperty
        ("org.jfree.layouting.modules.output.pdf.security.AllowAssembly"));

    int permissions = 0;
    if (allowPrinting)
    {
      permissions |= PdfWriter.AllowPrinting;
    }
    if (allowModifyContents)
    {
      permissions |= PdfWriter.AllowModifyContents;
    }
    if (allowModifyAnn)
    {
      permissions |= PdfWriter.AllowModifyAnnotations;
    }
    if (allowCopy)
    {
      permissions |= PdfWriter.AllowCopy;
    }
    if (allowFillIn)
    {
      permissions |= PdfWriter.AllowFillIn;
    }
    if (allowScreenReaders)
    {
      permissions |= PdfWriter.AllowScreenReaders;
    }
    if (allowAssembly)
    {
      permissions |= PdfWriter.AllowAssembly;
    }
    if (allowDegradedPrinting)
    {
      permissions |= PdfWriter.AllowDegradedPrinting;
    }
    return permissions;
  }

}
