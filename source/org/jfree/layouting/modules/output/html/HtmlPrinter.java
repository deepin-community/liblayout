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
 * $Id: HtmlPrinter.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.modules.output.html;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import org.jfree.layouting.DocumentContextUtility;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.style.StyleKey;
import org.jfree.layouting.input.style.StyleKeyRegistry;
import org.jfree.layouting.input.style.keys.border.BorderStyle;
import org.jfree.layouting.input.style.keys.border.BorderStyleKeys;
import org.jfree.layouting.input.style.keys.box.BoxStyleKeys;
import org.jfree.layouting.input.style.keys.box.DisplayRole;
import org.jfree.layouting.input.style.keys.color.ColorStyleKeys;
import org.jfree.layouting.input.style.keys.color.HtmlColors;
import org.jfree.layouting.input.style.keys.font.FontStyleKeys;
import org.jfree.layouting.input.style.keys.line.LineStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.values.CSSColorValue;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.context.LayoutStyle;
import org.jfree.layouting.namespace.Namespaces;
import org.jfree.layouting.renderer.model.BlockRenderBox;
import org.jfree.layouting.renderer.model.ComputedLayoutProperties;
import org.jfree.layouting.renderer.model.InlineRenderBox;
import org.jfree.layouting.renderer.model.MarkerRenderBox;
import org.jfree.layouting.renderer.model.NodeLayoutProperties;
import org.jfree.layouting.renderer.model.ParagraphRenderBox;
import org.jfree.layouting.renderer.model.RenderBox;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableReplacedContent;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.SpacerRenderNode;
import org.jfree.layouting.renderer.model.page.LogicalPageBox;
import org.jfree.layouting.renderer.model.table.TableCellRenderBox;
import org.jfree.layouting.renderer.model.table.TableRenderBox;
import org.jfree.layouting.renderer.model.table.TableRowRenderBox;
import org.jfree.layouting.renderer.model.table.TableSectionRenderBox;
import org.jfree.layouting.renderer.model.table.cols.TableColumn;
import org.jfree.layouting.renderer.model.table.cols.TableColumnModel;
import org.jfree.layouting.renderer.process.IterateStructuralProcessStep;
import org.jfree.layouting.util.ImageUtils;
import org.jfree.layouting.util.geom.StrictGeomUtility;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.pentaho.reporting.libraries.xmlns.writer.DefaultTagDescription;
import org.pentaho.reporting.libraries.xmlns.writer.HtmlCharacterEntities;
import org.pentaho.reporting.libraries.xmlns.common.AttributeList;
import org.pentaho.reporting.libraries.base.util.FastStack;
import org.pentaho.reporting.libraries.base.util.StackableRuntimeException;
import org.pentaho.reporting.libraries.base.util.IOUtils;
import org.pentaho.reporting.libraries.base.util.PngEncoder;
import org.pentaho.reporting.libraries.base.util.WaitingImageObserver;
import org.pentaho.reporting.libraries.repository.ContentLocation;
import org.pentaho.reporting.libraries.repository.NameGenerator;
import org.pentaho.reporting.libraries.repository.ContentItem;
import org.pentaho.reporting.libraries.repository.ContentIOException;
import org.pentaho.reporting.libraries.repository.LibRepositoryBoot;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.ResourceKey;
import org.pentaho.reporting.libraries.resourceloader.ResourceData;
import org.pentaho.reporting.libraries.resourceloader.ResourceLoadingException;
import org.pentaho.reporting.libraries.resourceloader.factory.drawable.DrawableWrapper;

/**
 * Creation-Date: 25.11.2006, 18:17:57
 *
 * @author Thomas Morgner
 */
public class HtmlPrinter extends IterateStructuralProcessStep
{
  private static class ContextElement
  {
    private StyleBuilder builder;
    private boolean omitted;

    protected ContextElement(final StyleBuilder builder)
    {
      this.builder = builder;
    }

    public StyleBuilder getBuilder()
    {
      return builder;
    }

    public boolean isOmitted()
    {
      return omitted;
    }

    public void setOmitted(final boolean omitted)
    {
      this.omitted = omitted;
    }
  }

  private static final String[] XHTML_HEADER = {
      "<!DOCTYPE html",
      "     PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
      "     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"};

  public static final String TAG_DEF_PREFIX = "org.jfree.layouting.modules.output.html.";
  public static final float CORRECTION_FACTOR_PX_TO_POINT = 72f / 96f;
  public static final float CORRECTION_FACTOR_POINT_TO_PX = 96f / 72f;

  private XmlWriter xmlWriter;
  private FastStack contexts;
  private DecimalFormat pointConverter;
  private NumberFormat pointIntConverter;
  private boolean assumeZeroMargins;
  private boolean assumeZeroBorders;
  private boolean assumeZeroPaddings;

  private ContentLocation contentLocation;
  private NameGenerator contentNameGenerator;
  private ContentLocation dataLocation;
  private NameGenerator dataNameGenerator;
  private ResourceManager resourceManager;
  private HashMap knownResources;
  private HashSet validRawTypes;

  private String encoding;
  private URLRewriter urlRewriter;
  private ContentItem documentContentItem;
  private boolean generateFragment;

  public HtmlPrinter()
  {
    this.encoding = "ASCII";
    this.knownResources = new HashMap();
    this.validRawTypes = new HashSet();
    this.validRawTypes.add("image/gif");
    this.validRawTypes.add("image/x-xbitmap");
    this.validRawTypes.add("image/gi_");
    this.validRawTypes.add("image/jpeg");
    this.validRawTypes.add("image/jpg");
    this.validRawTypes.add("image/jp_");
    this.validRawTypes.add("application/jpg");
    this.validRawTypes.add("application/x-jpg");
    this.validRawTypes.add("image/pjpeg");
    this.validRawTypes.add("image/pipeg");
    this.validRawTypes.add("image/vnd.swiftview-jpeg");
    this.validRawTypes.add("image/x-xbitmap");
    this.validRawTypes.add("image/png");
    this.validRawTypes.add("application/png");
    this.validRawTypes.add("application/x-png");

    contexts = new FastStack();
    pointConverter = new DecimalFormat
        ("0.####", new DecimalFormatSymbols(Locale.US));
    pointIntConverter = new DecimalFormat
        ("0", new DecimalFormatSymbols(Locale.US));

    assumeZeroMargins = true;
    assumeZeroBorders = true;
    assumeZeroPaddings = true;

    // this primitive implementation assumes that the both repositories are
    // the same ..
    urlRewriter = new FileSystemURLRewriter();

    generateFragment = false;
  }

  public URLRewriter getUrlRewriter()
  {
    return urlRewriter;
  }

  public void setUrlRewriter(final URLRewriter urlRewriter)
  {
    this.urlRewriter = urlRewriter;
  }

  public NameGenerator getDataNameGenerator()
  {
    return dataNameGenerator;
  }

  public ContentLocation getDataLocation()
  {
    return dataLocation;
  }

  public NameGenerator getContentNameGenerator()
  {
    return contentNameGenerator;
  }

  public ContentLocation getContentLocation()
  {
    return contentLocation;
  }

  public String getEncoding()
  {
    return encoding;
  }

  public void setEncoding(final String encoding)
  {
    this.encoding = encoding;
  }

  public void generate(final LogicalPageBox box,
                       final DocumentContext documentContext)
      throws IOException, ContentIOException
  {
    // update the resource manager, we may need it later ..
    resourceManager = documentContext.getResourceManager();

    final DefaultTagDescription tagDescription = new DefaultTagDescription();
    tagDescription.configure
        (LibLayoutBoot.getInstance().getGlobalConfig(), HtmlPrinter.TAG_DEF_PREFIX);

    documentContentItem = contentLocation.createItem
        (contentNameGenerator.generateName(null, "text/html"));
    final OutputStream out = documentContentItem.getOutputStream();

    final OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
    xmlWriter = new XmlWriter(writer, tagDescription);
    xmlWriter.setAlwaysAddNamespace(false);
    xmlWriter.setAssumeDefaultNamespace(true);

    if (generateFragment == false)
    {
      xmlWriter.writeXmlDeclaration(encoding);
      for (int i = 0; i < XHTML_HEADER.length; i++)
      {
        xmlWriter.writeText(XHTML_HEADER[i]);
        xmlWriter.writeNewLine();
      }
      final AttributeList htmlAttList = new AttributeList();
      htmlAttList.addNamespaceDeclaration("", Namespaces.XHTML_NAMESPACE);

      xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "html",  XmlWriter.OPEN);
      xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "head", XmlWriter.OPEN);
      xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "title", XmlWriter.OPEN);
      xmlWriter.writeText("Yeah, sure, I *should* grab a sensible title from somewhere");
      xmlWriter.writeCloseTag();
      xmlWriter.writeCloseTag();
      xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "body", XmlWriter.OPEN);
    }
    else
    {
      xmlWriter.addImpliedNamespace(Namespaces.XHTML_NAMESPACE, "");
    }

    contexts.clear();

    final LayoutStyle initialStyle =
        DocumentContextUtility.getInitialStyle(documentContext);

    final StyleBuilder inialBuilder = new StyleBuilder(false);
    final StyleKey[] keys = StyleKeyRegistry.getRegistry().getKeys();
    for (int i = 0; i < keys.length; i++)
    {
      final StyleKey key = keys[i];
      if (key.isInherited())
      {
        inialBuilder.append(key, initialStyle.getValue(key));
      }
    }
    contexts.push(new ContextElement(inialBuilder));

    startBlockBox(box);
    processBoxChilds(box);
    finishBlockBox(box);

    if (generateFragment == false)
    {
      xmlWriter.writeCloseTag();
      xmlWriter.writeCloseTag();
    }

    xmlWriter.close();
    xmlWriter = null;
  }
  // Todo: Text height is not yet applied by the layouter ..

  public boolean isGenerateFragment()
  {
    return generateFragment;
  }

  public void setGenerateFragment(final boolean generateFragment)
  {
    this.generateFragment = generateFragment;
  }

  protected boolean startInlineBox(final InlineRenderBox box)
  {
    try
    {
      final StyleBuilder builder = createStyleBuilder();
      final ContextElement context = new ContextElement(builder);
      contexts.push(context);

      buildStyle(box, builder);

      final AttributeList attList = new AttributeList();
      if (builder.isEmpty() == false)
      {
        attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
      }

      if (attList.isEmpty() == false)
      {
        xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "span", attList, false);
      }
      else
      {
        context.setOmitted(true);
      }
      return true;
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }

  private void buildStyle(final RenderBox box, final StyleBuilder builder)
  {
    final LayoutContext layoutContext = box.getLayoutContext();
    if (layoutContext == null)
    {
      // this is either the logical page box or one of the direct anchestors
      // of said box.
      return;
    }

    final FontSpecification fs = layoutContext.getFontSpecification();
    final double fontSize = fs.getFontSize();
    builder.append(FontStyleKeys.FONT_SIZE, toPointString(fontSize), "pt");
    builder.append(FontStyleKeys.FONT_FAMILY, fs.getFontFamily());
    builder.append(FontStyleKeys.FONT_WEIGHT,
        layoutContext.getValue(FontStyleKeys.FONT_WEIGHT));
    builder.append(FontStyleKeys.FONT_STYLE,
        layoutContext.getValue(FontStyleKeys.FONT_STYLE));
    builder.append(TextStyleKeys.TEXT_ALIGN, layoutContext.getValue(TextStyleKeys.TEXT_ALIGN));
    builder.append(TextStyleKeys.TEXT_ALIGN_LAST, layoutContext.getValue(TextStyleKeys.TEXT_ALIGN_LAST));

    final NodeLayoutProperties nlp = box.getNodeLayoutProperties();
    //final BoxLayoutProperties blp = box.getBoxLayoutProperties();
    final ComputedLayoutProperties sblp = box.getComputedLayoutProperties();
    builder.append(LineStyleKeys.VERTICAL_ALIGN, nlp.getVerticalAlignment());

    if (sblp.getPaddingTop() > 0 ||
        sblp.getPaddingLeft() > 0 ||
        sblp.getPaddingBottom() > 0 ||
        sblp.getPaddingRight() > 0)
    {
      if (sblp.getPaddingTop() > 0 || assumeZeroPaddings == false)
      {
        builder.append(BoxStyleKeys.PADDING_TOP,
            toPointString(sblp.getPaddingTop()), "pt");
      }
      if (sblp.getPaddingLeft() > 0 || assumeZeroPaddings == false)
      {
        builder.append(BoxStyleKeys.PADDING_LEFT,
            toPointString(sblp.getPaddingLeft()), "pt");
      }
      if (sblp.getPaddingBottom() > 0 || assumeZeroPaddings == false)
      {
        builder.append(BoxStyleKeys.PADDING_BOTTOM,
            toPointString(sblp.getPaddingBottom()), "pt");
      }
      if (sblp.getPaddingRight() > 0 || assumeZeroPaddings == false)
      {
        builder.append(BoxStyleKeys.PADDING_RIGHT,
            toPointString(sblp.getPaddingRight()), "pt");
      }
    }
    else if (assumeZeroPaddings == false)
    {
      builder.append("padding", false, "0");
    }

    if (sblp.getMarginLeft() != 0 ||
        sblp.getMarginRight() != 0 ||
        sblp.getMarginTop() != 0 ||
        sblp.getMarginBottom() != 0)
    {
      if (sblp.getMarginLeft() > 0 || assumeZeroMargins == false)
      {
        builder.append(BoxStyleKeys.MARGIN_LEFT,
            toPointString(sblp.getMarginLeft()), "pt");
      }
      if (sblp.getMarginRight() > 0 || assumeZeroMargins == false)
      {
        builder.append(BoxStyleKeys.MARGIN_RIGHT,
            toPointString(sblp.getMarginRight()), "pt");
      }
      if (sblp.getMarginTop() > 0 || assumeZeroMargins == false)
      {
        builder.append(BoxStyleKeys.MARGIN_TOP,
            toPointString(sblp.getMarginTop()), "pt");
      }
      if (sblp.getMarginBottom() > 0 || assumeZeroMargins == false)
      {
        builder.append(BoxStyleKeys.MARGIN_BOTTOM,
            toPointString(sblp.getMarginBottom()), "pt");
      }
    }
    else if (assumeZeroMargins == false)
    {
      builder.append("margin", false, "0");
    }

    final String bgColor = toColorString(layoutContext.getValue
        (BorderStyleKeys.BACKGROUND_COLOR));
    if (bgColor != null)
    {
      builder.append(BorderStyleKeys.BACKGROUND_COLOR, bgColor);
    }
    final String fgColor = toColorString(layoutContext.getValue(ColorStyleKeys.COLOR));
    if (fgColor != null)
    {
      builder.append(ColorStyleKeys.COLOR, fgColor);
    }

    if (sblp.getBorderTop() > 0 || sblp.getBorderLeft() > 0 ||
        sblp.getBorderBottom() > 0 || sblp.getBorderRight() > 0)
    {
      if (sblp.getBorderTop() > 0)
      {
        builder.append(BorderStyleKeys.BORDER_TOP_COLOR, layoutContext.getValue(BorderStyleKeys.BORDER_TOP_COLOR));
        builder.append(BorderStyleKeys.BORDER_TOP_STYLE, layoutContext.getValue(BorderStyleKeys.BORDER_TOP_STYLE));
        builder.append(BorderStyleKeys.BORDER_TOP_WIDTH, toPointString(sblp.getBorderTop()), "pt");
      }
      else if (assumeZeroBorders == false)
      {
        builder.append(BorderStyleKeys.BORDER_TOP_STYLE, BorderStyle.NONE);
      }

      if (sblp.getBorderLeft() > 0)
      {
        builder.append(BorderStyleKeys.BORDER_LEFT_COLOR, layoutContext.getValue(BorderStyleKeys.BORDER_LEFT_COLOR));
        builder.append(BorderStyleKeys.BORDER_LEFT_STYLE, layoutContext.getValue(BorderStyleKeys.BORDER_LEFT_STYLE));
        builder.append(BorderStyleKeys.BORDER_LEFT_WIDTH, toPointString(sblp.getBorderLeft()), "pt");
      }
      else if (assumeZeroBorders == false)
      {
        builder.append(BorderStyleKeys.BORDER_LEFT_STYLE, BorderStyle.NONE);
      }

      if (sblp.getBorderBottom() > 0)
      {
        builder.append(BorderStyleKeys.BORDER_BOTTOM_COLOR, layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_COLOR));
        builder.append(BorderStyleKeys.BORDER_BOTTOM_STYLE, layoutContext.getValue(BorderStyleKeys.BORDER_BOTTOM_STYLE));
        builder.append(BorderStyleKeys.BORDER_BOTTOM_WIDTH, toPointString(sblp.getBorderBottom()), "pt");
      }
      else if (assumeZeroBorders == false)
      {
        builder.append(BorderStyleKeys.BORDER_BOTTOM_STYLE, BorderStyle.NONE);
      }

      if (sblp.getBorderRight() > 0)
      {
        builder.append(BorderStyleKeys.BORDER_RIGHT_COLOR, layoutContext.getValue(BorderStyleKeys.BORDER_RIGHT_COLOR));
        builder.append(BorderStyleKeys.BORDER_RIGHT_STYLE, layoutContext.getValue(BorderStyleKeys.BORDER_RIGHT_STYLE));
        builder.append(BorderStyleKeys.BORDER_RIGHT_WIDTH, toPointString(sblp.getBorderRight()), "pt");
      }
      else if (assumeZeroBorders == false)
      {
        builder.append(BorderStyleKeys.BORDER_RIGHT_STYLE, BorderStyle.NONE);
      }
    }
    else if (assumeZeroBorders == false)
    {
      builder.append("border-style", true, "none");
    }
  }

  private StyleBuilder createStyleBuilder()
  {
    final StyleBuilder builder;
    if (contexts.isEmpty())
    {
      builder = new StyleBuilder(true);
    }
    else
    {
      final ContextElement contextElement = (ContextElement) contexts.peek();
      builder = new StyleBuilder(true, contextElement.getBuilder());
    }
    return builder;
  }

  private String toColorString(final CSSValue color)
  {
    if (color == null)
    {
      return null;
    }

    if (color instanceof CSSColorValue == false)
    {
      // This should not happen ..
      return color.getCSSText();
    }

    final CSSColorValue colorValue = (CSSColorValue) color;
    if (colorValue.getAlpha() == 0)
    {
      return null;
    }

    try
    {
      final Field[] fields = HtmlColors.class.getFields();
      for (int i = 0; i < fields.length; i++)
      {
        final Field f = fields[i];
        if (Modifier.isPublic(f.getModifiers())
            && Modifier.isFinal(f.getModifiers())
            && Modifier.isStatic(f.getModifiers()))
        {
          final String name = f.getName();
          final Object oColor = f.get(null);
          if (oColor instanceof Color)
          {
            if (color.equals(oColor))
            {
              return name.toLowerCase();
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      //
    }

    return colorValue.getCSSText();
  }

  private String toPointString(final double value)
  {
    if (Math.floor(value) == value)
    {
      return pointIntConverter.format(value);
    }

    return pointConverter.format(value);
  }

  private String toPointString(final long value)
  {
    final int remainder = (int) value % 1000;
    if (remainder == 0)
    {
      final double d = StrictGeomUtility.toExternalValue(value);
      return pointIntConverter.format(d);
    }

    final double d = StrictGeomUtility.toExternalValue(value);
    return pointConverter.format(d);
  }

  protected void finishInlineBox(final InlineRenderBox box)
  {
    try
    {
      final ContextElement element = (ContextElement) contexts.pop();
      if (element.isOmitted() == false)
      {
        xmlWriter.writeCloseTag();
      }
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }

  protected boolean startBlockBox(final BlockRenderBox box)
  {
    try
    {
      final StyleBuilder builder = createStyleBuilder();
      contexts.push(new ContextElement(builder));

      if (box instanceof TableRenderBox)
      {
        return startTable((TableRenderBox) box, builder);
      }
      else if (box instanceof TableSectionRenderBox)
      {
        final TableSectionRenderBox section = (TableSectionRenderBox) box;
        final CSSValue displayRole = section.getDisplayRole();
        if (DisplayRole.TABLE_HEADER_GROUP.equals(displayRole))
        {
          return startTableHeader((TableSectionRenderBox) box, builder);
        }
        else if (DisplayRole.TABLE_FOOTER_GROUP.equals(displayRole))
        {
          return startTableFooter((TableSectionRenderBox) box, builder);
        }
        else
        {
          return startTableBody((TableSectionRenderBox) box, builder);
        }
      }
      else if (box instanceof TableRowRenderBox)
      {
        return startTableRow((TableRowRenderBox) box, builder);
      }
      else if (box instanceof TableCellRenderBox)
      {
        // or a th, it depends ..
        return startTableCell((TableCellRenderBox) box, builder);
      }
      else if (box instanceof ParagraphRenderBox)
      {
        return startParagraph((ParagraphRenderBox) box, builder);
      }
      else if (box instanceof LogicalPageBox)
      {
        return startPageBox(box, builder);
      }
      else if (box instanceof MarkerRenderBox)
      {
        return startMarkerContents(box, builder);
      }
      else
      {
        return startOtherBlockBox(box, builder);
      }
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }


  protected void finishBlockBox(final BlockRenderBox box)
  {
    try
    {
      if (box instanceof TableRenderBox)
      {
        finishTable((TableRenderBox) box);
      }
      else if (box instanceof TableSectionRenderBox)
      {
        final TableSectionRenderBox section = (TableSectionRenderBox) box;
        final CSSValue displayRole = section.getDisplayRole();
        if (DisplayRole.TABLE_HEADER_GROUP.equals(displayRole))
        {
          finishTableHeader((TableSectionRenderBox) box);
        }
        else if (DisplayRole.TABLE_FOOTER_GROUP.equals(displayRole))
        {
          finishTableFooter((TableSectionRenderBox) box);
        }
        else
        {
          finishTableBody((TableSectionRenderBox) box);
        }
      }
      else if (box instanceof TableRowRenderBox)
      {
        finishTableRow((TableRowRenderBox) box);
      }
      else if (box instanceof TableCellRenderBox)
      {
        // or a th, it depends ..
        finishTableCell((TableCellRenderBox) box);
      }
      else if (box instanceof ParagraphRenderBox)
      {
        finishParagraph((ParagraphRenderBox) box);
      }
      else if (box instanceof LogicalPageBox)
      {
        finishPageBox(box);
      }
      else if (box instanceof MarkerRenderBox)
      {
        finishMarkerBox(box);
      }
      else
      {
        finishOtherBlockBox(box);
      }

      contexts.pop();
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
  }

  private void finishMarkerBox(final RenderBox box) throws IOException
  {
    xmlWriter.writeCloseTag();
  }


  protected boolean startMarkerContents(final RenderBox box,
                                        final StyleBuilder builder)
      throws IOException
  {
    // This box is somewhat special ..
    buildStyle(box, builder);
    builder.append("white-space", false, "nowrap");

    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeComment("Marker-Box");
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "span", attList, XmlWriter.OPEN);

    // the next child is a block-level child ..
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      // process that node as well. It should be a paragraph ..
      if (node instanceof ParagraphRenderBox)
      {
        processParagraphChilds((ParagraphRenderBox) node);
      }
      else if (node instanceof RenderBox)
      {
        processBoxChilds((RenderBox) node);
      }
      else
      {
        startProcessing(node);
      }

      final RenderNode next = node.getNext();
      if (next == null)
      {
        break;
      }
      if (next.isIgnorableForRendering() == false)
      {
        xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "br", XmlWriter.CLOSE);
      }
      node = next;
    }
    return false;
  }

  protected boolean startPageBox(final RenderBox box,
                                 final StyleBuilder builder) throws IOException
  {
    buildStyle(box, builder);
    builder.append(BoxStyleKeys.WIDTH, toPointString(box.getWidth()), "pt");

    final AttributeList attList = new AttributeList();
    attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "div", attList, XmlWriter.OPEN);
    return true;
  }

  protected void finishPageBox(final RenderBox box) throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected boolean startOtherBlockBox(final BlockRenderBox box,
                                       final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "div", attList, XmlWriter.OPEN);
    return true;
  }

  protected void finishOtherBlockBox(final BlockRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected boolean startParagraph(final ParagraphRenderBox box,
                                   final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }

    // We have to use divs here, as paragraphs have margins by default.
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "div", attList, XmlWriter.OPEN);
    return true;
  }

  protected void finishParagraph(final ParagraphRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected boolean startTableCell(final TableCellRenderBox box,
                                   final StyleBuilder builder)
      throws IOException
  {
    final int colSpan = box.getColSpan();
    final int rowSpan = box.getRowSpan();

    final AttributeList attrList = new AttributeList();
    if (colSpan != 0)
    {
      attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "colspan", String.valueOf(colSpan));
    }
    if (rowSpan != 0)
    {
      attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "rowspan", String.valueOf(rowSpan));
    }

    buildStyle(box, builder);
    if (builder.isEmpty() == false)
    {
      attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "td", attrList, XmlWriter.OPEN);
    return true;

  }

  protected void finishTableCell(final TableCellRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected boolean startTableRow(final TableRowRenderBox box,
                                  final StyleBuilder builder) throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "tr", attList, XmlWriter.OPEN);
    return true;
  }

  protected void finishTableRow(final TableRowRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected boolean startTableHeader(final TableSectionRenderBox box,
                                     final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "thead", attList, XmlWriter.OPEN);
    return true;
  }

  protected void finishTableHeader(final TableSectionRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected boolean startTableBody(final TableSectionRenderBox box,
                                   final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "tbody", attList, XmlWriter.OPEN);
    return true;
  }

  protected void finishTableBody(final TableSectionRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected boolean startTableFooter(final TableSectionRenderBox box,
                                     final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);
    final AttributeList attList = new AttributeList();
    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "tfoot", attList, XmlWriter.OPEN);
    return true;
  }

  protected void finishTableFooter(final TableSectionRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }

  protected boolean startTable(final TableRenderBox box,
                               final StyleBuilder builder)
      throws IOException
  {
    buildStyle(box, builder);


    final AttributeList attList = new AttributeList();
    attList.setAttribute(Namespaces.XHTML_NAMESPACE, "cellspacing", "0");
    attList.setAttribute(Namespaces.XHTML_NAMESPACE, "cellpadding", "0");

    if (builder.isEmpty() == false)
    {
      attList.setAttribute(Namespaces.XHTML_NAMESPACE, "style", builder.toString());
    }
    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
        "table", attList, XmlWriter.OPEN);

    xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "colgroup", XmlWriter.OPEN);

    final TableColumnModel columnModel = box.getColumnModel();
    final int columnCount = columnModel.getColumnCount();
    for (int i = 0; i < columnCount; i++)
    {
      final TableColumn column = columnModel.getColumn(i);
      final long effectiveSize = column.getEffectiveSize();
      final StyleBuilder colbuilder = new StyleBuilder(true);
      colbuilder.append(BoxStyleKeys.WIDTH, toPointString(effectiveSize), "pt");
      xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE,
          "col", "style", colbuilder.toString(), XmlWriter.CLOSE);
    }
    xmlWriter.writeCloseTag();
    return true;
  }

  protected void finishTable(final TableRenderBox tableRenderBox)
      throws IOException
  {
    xmlWriter.writeCloseTag();
  }


  protected void startOtherNode(final RenderNode node)
  {
    try
    {
      if (node instanceof RenderableText)
      {
        final RenderableText text = (RenderableText) node;
        final String rawText = text.getRawText();
        final String encodedText =
            HtmlCharacterEntities.getEntityParser().encodeEntities(rawText);
        xmlWriter.writeText(encodedText);
      }
      else if (node instanceof SpacerRenderNode)
      {
        xmlWriter.writeText(" ");
      }
      else if (node instanceof RenderableReplacedContent)
      {
        final RenderableReplacedContent rc = (RenderableReplacedContent) node;
        final ResourceKey source = rc.getSource();
        // We have to do three things here. First, w have to check what kind
        // of content we deal with.
        if (source != null)
        {
          // Cool, we have access to the raw-data. Thats always nice as we
          // dont have to recode the whole thing.

          if (knownResources.containsKey(source) == false)
          {

            // Write image reference; return the name of the reference ..
            final String name = writeRaw(source);
            if (name != null)
            {
              // Write image reference ..
              final AttributeList attrList = new AttributeList();
              attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "src", name);
              // width and height and scaling and so on ..
              xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "img", attrList, XmlWriter.CLOSE);

              knownResources.put(source, name);
              return;
            }
          }
        }
        // Fallback: (At the moment, we only support drawables and images.)
        final Object rawObject = rc.getRawObject();
        if (rawObject instanceof Image)
        {
          // Make it a PNG file ..
          xmlWriter.writeComment("Image content:" + source);
          final String name = writeImage((Image) rawObject);
          if (name != null)
          {
            // Write image reference ..
            final AttributeList attrList = new AttributeList();
            attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "src", name);
            // width and height and scaling and so on ..
            xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "img", attrList, XmlWriter.CLOSE);
          }
        }
        else if (rawObject instanceof DrawableWrapper)
        {
          // render it into an Buffered image and make it a PNG file.
          xmlWriter.writeComment("Drawable content:" + source);
          final Image image = generateImage(node, (DrawableWrapper) rawObject);
          final String name = writeImage(image);
          if (name != null)
          {
            // Write image reference ..
            final AttributeList attrList = new AttributeList();
            attrList.setAttribute(Namespaces.XHTML_NAMESPACE, "src", name);
            // width and height and scaling and so on ..
            xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "img", attrList, XmlWriter.CLOSE);
          }
        }
      }
    }
    catch (IOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
    catch (ContentIOException e)
    {
      throw new StackableRuntimeException("Failed", e);
    }
    catch (URLRewriteException e)
    {
      throw new StackableRuntimeException("Rewriting the URL failed", e);
    }
  }

  private String writeRaw(final ResourceKey source) throws IOException
  {
    try
    {
      final ResourceData resourceData = resourceManager.load(source);
      final String mimeType = queryMimeType(resourceData);
      if (isValidImage(mimeType))
      {

        // lets do some voodo ..
        final ContentItem item = dataLocation.createItem
            (dataNameGenerator.generateName(extractFilename(resourceData), mimeType));
        if (item.isWriteable())
        {
          item.setAttribute(LibRepositoryBoot.REPOSITORY_DOMAIN,
              LibRepositoryBoot.CONTENT_TYPE, mimeType);

          // write it out ..
          final InputStream stream =
              resourceData.getResourceAsStream(resourceManager);
          final OutputStream outputStream = item.getOutputStream();
          IOUtils.getInstance().copyStreams
              (stream, outputStream);
          outputStream.close();
          stream.close();

          return urlRewriter.rewrite(documentContentItem, item);
        }
      }
    }
    catch (ResourceLoadingException e)
    {
      // Ok, loading the resource failed. Not a problem, so we will
      // recode the raw-object instead ..
    }
    catch (ContentIOException e)
    {
      // ignore it ..
    }
    catch (URLRewriteException e)
    {
      throw new StackableRuntimeException("Rewriting the URL failed.", e);
    }
    return null;
  }

  private String writeImage(final Image image)
      throws ContentIOException, IOException, URLRewriteException
  {
    // encode the image into a PNG
    // quick caching ... use a weak list ...
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();

    final PngEncoder encoder = new PngEncoder(image,
        PngEncoder.ENCODE_ALPHA, PngEncoder.FILTER_NONE, 5);
    final byte[] data = encoder.pngEncode();

    // write the encoded picture ...
    final ContentItem dataFile = dataLocation.createItem
        (dataNameGenerator.generateName("picture", "image/png"));

    // a png encoder is included in JCommon ...
    final OutputStream out = new BufferedOutputStream(dataFile.getOutputStream());
    out.write(data);
    out.flush();
    out.close();

    return urlRewriter.rewrite(documentContentItem, dataFile);
  }

  private Image generateImage(final RenderNode node, final DrawableWrapper drawable)
  {
    final int imageWidthPt = (int) StrictGeomUtility.toExternalValue(node.getWidth());
    final int imageHeightPt = (int) StrictGeomUtility.toExternalValue(node.getHeight());
    // dont know whether we will need that one ..
    final boolean iResMapActive = false;
    // getLayoutSupport().isImageResolutionMappingActive();

    if (imageWidthPt == 0 || imageHeightPt == 0)
    {
      return null;
    }
    final double scale = CORRECTION_FACTOR_POINT_TO_PX;
    final Image image = ImageUtils.createTransparentImage
        ((int) (imageWidthPt * scale), (int) (imageHeightPt * scale));
    final Graphics2D g2 = (Graphics2D) image.getGraphics();

    g2.scale(scale, scale);
    final Rectangle2D.Double drawBounds =
        new Rectangle2D.Double(0, 0, imageWidthPt, imageHeightPt);
    g2.clip(drawBounds);
    drawable.draw(g2, drawBounds);
    g2.dispose();
    return image;
  }

  private String extractFilename(final ResourceData resourceData)
  {
    final String filename = (String)
        resourceData.getAttribute(ResourceData.FILENAME);
    if (filename == null)
    {
      return "image";
    }

    return IOUtils.getInstance().stripFileExtension(filename);
  }

  private String queryMimeType(final ResourceData resourceData)
      throws ResourceLoadingException, IOException
  {
    final Object contentType =
        resourceData.getAttribute(ResourceData.CONTENT_TYPE);
    if (contentType instanceof String)
    {
      return (String) contentType;
    }

    // now we are getting very primitive .. (Kids, dont do this at home)
    final byte[] data = new byte[12];
    resourceData.getResource(resourceManager, data, 0, data.length);
    if (isGIF(new ByteArrayInputStream(data)))
    {
      return "image/gif";
    }
    if (isJPEG(new ByteArrayInputStream(data)))
    {
      return "image/jpeg";
    }
    if (isPNG(new ByteArrayInputStream(data)))
    {
      return "image/png";
    }
    return null;
  }

  private boolean isPNG(final ByteArrayInputStream data)
  {
    final int[] PNF_FINGERPRINT = {137, 80, 78, 71, 13, 10, 26, 10};
    for (int i = 0; i < PNF_FINGERPRINT.length; i++)
    {
      if (PNF_FINGERPRINT[i] != data.read())
      {
        return false;
      }
    }
    return true;
  }

  private boolean isJPEG(final InputStream data) throws IOException
  {
    final int[] JPG_FINGERPRINT_1 = {0xFF, 0xD8, 0xFF, 0xE0};
    for (int i = 0; i < JPG_FINGERPRINT_1.length; i++)
    {
      if (JPG_FINGERPRINT_1[i] != data.read())
      {
        return false;
      }
    }
    // then skip two bytes ..
    if (data.read() == -1)
    {
      return false;
    }
    if (data.read() == -1)
    {
      return false;
    }

    final int[] JPG_FINGERPRINT_2 = {0x4A, 0x46, 0x49, 0x46, 0x00};
    for (int i = 0; i < JPG_FINGERPRINT_2.length; i++)
    {
      if (JPG_FINGERPRINT_2[i] != data.read())
      {
        return false;
      }
    }
    return true;
  }

  private boolean isGIF(final InputStream data) throws IOException
  {
    final int[] GIF_FINGERPRINT = {'G', 'I', 'F', '8'};
    for (int i = 0; i < GIF_FINGERPRINT.length; i++)
    {
      if (GIF_FINGERPRINT[i] != data.read())
      {
        return false;
      }
    }
    return true;
  }

  private boolean isValidImage(final String data)
  {
    return validRawTypes.contains(data);
  }

  protected boolean startOtherBox(final RenderBox box)
  {
//    try
//    {
//      if (box instanceof TableColumnGroupNode)
//      {
//        xmlWriter.writeTag(Namespaces.XHTML_NAMESPACE, "colgroup", XmlWriter.OPEN);
//      }
//    }
//    catch (IOException e)
//    {
//      throw new StackableRuntimeException("Failed", e);
//    }
    return true;
  }

  protected void processParagraphChilds(final ParagraphRenderBox box)
  {
    RenderNode node = box.getFirstChild();
    while (node != null)
    {
      // A paragraph always has only its line-boxes as direct childs.
      processBoxChilds((RenderBox) node);
      final RenderNode next = node.getNext();
      if (next == null)
      {
        break;
      }
      try
      {
        xmlWriter.writeText(" ");
      }
      catch (IOException e)
      {
        throw new StackableRuntimeException("Failed", e);
      }
      node = node.getNext();
    }
  }

  public void setDataWriter(final ContentLocation dataLocation,
                            final NameGenerator dataNameGenerator)
  {
    this.dataNameGenerator = dataNameGenerator;
    this.dataLocation = dataLocation;
  }

  public void setContentWriter(final ContentLocation contentLocation,
                               final NameGenerator contentNameGenerator)
  {
    this.contentNameGenerator = contentNameGenerator;
    this.contentLocation = contentLocation;
  }
}
