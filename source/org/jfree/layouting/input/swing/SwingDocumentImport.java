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
 * $Id: SwingDocumentImport.java 3524 2007-10-16 11:26:31Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */

package org.jfree.layouting.input.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.jfree.layouting.DefaultLayoutProcess;
import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.LibLayoutBoot;
import org.jfree.layouting.input.swing.converter.CharacterConverter;
import org.jfree.layouting.input.swing.converter.ColorConverter;
import org.jfree.layouting.input.swing.converter.DocumentConverter;
import org.jfree.layouting.input.swing.converter.FontConverter;
import org.jfree.layouting.input.swing.converter.ParagraphConverter;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.feed.InputFeedException;
import org.jfree.layouting.modules.output.html.StreamingHtmlOutputProcessor;

/**
 * Right now, we do not convert Swing-styles into CSS styles. Hey, we should,
 * but we don't.
 * <p/>
 * todo parse styles
 */
public class SwingDocumentImport
{
  public static final String NAMESPACE = "http://www.w3.org/1999/xhtml";
  public static final String STYLESHEET_TYPE = "type";
  public static final String STYLESHEET_PCDATA = "#pcdata";
  public static final String STYLESHEET = "stylesheet";
  public static final String ELEMENT_STYLE_ATTRIBUTE = "style";

  private Map styleNames;
  private InputFeed feed;
  private static Map styleConstantsMap;

  static
  {
    styleConstantsMap = new HashMap();
    // font
    final FontConverter fontConverter = new FontConverter();
    styleConstantsMap.put(StyleConstants.FontFamily, fontConverter);
    styleConstantsMap.put(StyleConstants.FontSize, fontConverter);
    styleConstantsMap.put(StyleConstants.Bold, fontConverter);
    styleConstantsMap.put(StyleConstants.Italic, fontConverter);

    // text
    final ParagraphConverter paragraphConverter = new ParagraphConverter();
    styleConstantsMap.put(StyleConstants.Alignment, paragraphConverter);
    styleConstantsMap.put(StyleConstants.LeftIndent, paragraphConverter);
    styleConstantsMap.put(StyleConstants.RightIndent, paragraphConverter);
    styleConstantsMap.put(StyleConstants.SpaceAbove, paragraphConverter);
    styleConstantsMap.put(StyleConstants.SpaceBelow, paragraphConverter);
    styleConstantsMap.put(StyleConstants.FirstLineIndent, paragraphConverter);

    // character
    final CharacterConverter characterConverter = new CharacterConverter();
    styleConstantsMap.put(StyleConstants.Underline, characterConverter);
    styleConstantsMap.put(StyleConstants.StrikeThrough, characterConverter);
    styleConstantsMap.put(StyleConstants.BidiLevel, characterConverter);
    styleConstantsMap.put(StyleConstants.Superscript, characterConverter);
    styleConstantsMap.put(StyleConstants.Subscript, characterConverter);
    styleConstantsMap.put(CharacterConverter.RTF_CAPS, characterConverter);
    styleConstantsMap.put(CharacterConverter.RTF_SMALLCAPS, characterConverter);
    styleConstantsMap.put(CharacterConverter.RTF_OUTLINE, characterConverter);

    // color
    final ColorConverter colorConverter = new ColorConverter();
    styleConstantsMap.put(StyleConstants.Foreground, colorConverter);
    styleConstantsMap.put(StyleConstants.Background, colorConverter);

    // document
    final DocumentConverter documentConverter = new DocumentConverter();
    styleConstantsMap.put(DocumentConverter.RTF_PAGEWIDTH, documentConverter);
    styleConstantsMap.put(DocumentConverter.RTF_PAGEHEIGHT, documentConverter);
    styleConstantsMap.put(DocumentConverter.RTF_MARGINBOTTOM, documentConverter);
    styleConstantsMap.put(DocumentConverter.RTF_MARGINLEFT, documentConverter);
    styleConstantsMap.put(DocumentConverter.RTF_MARGINRIGHT, documentConverter);
    styleConstantsMap.put(DocumentConverter.RTF_MARGINTOP, documentConverter);
    styleConstantsMap.put(DocumentConverter.RTF_LANDSCAPE, documentConverter);
    //styleConstantsMap.put(DocumentConverter.RTF_GUTTERWIDTH, documentConverter);

  }

  public SwingDocumentImport()
  {
    styleNames = new HashMap();
  }

  public InputFeed getFeed()
  {
    return feed;
  }

  public void setFeed(final InputFeed feed)
  {
    this.feed = feed;
  }

  private String convertStyleName(final String name)
  {
    //todo implement me
    return name;
  }

  public String getNormalizedStyleName(final String name)
  {
    if (name == null)
    {
      throw new IllegalArgumentException("The style name must not be null");
    }

    String o = (String) styleNames.get(name);
    if (o == null)
    {
      o = convertStyleName(name);
      if (o == null)
      {
        throw new IllegalStateException("Unable to convert style name");
      }

      final Object res = styleNames.put(name, o);
      if (res != null)
      {
        throw new IllegalStateException("Style name clash during convertion");
      }
    }
    return o;
  }

  public ConverterAttributeSet convertAttributes(final AttributeSet attr, final Element context)
      throws InputFeedException
  {
    final ConverterAttributeSet cssAttr = new ConverterAttributeSet();
    final Enumeration attributeNames = attr.getAttributeNames();
    while (attributeNames.hasMoreElements())
    {
      final Object key = attributeNames.nextElement();
      final Object value = attr.getAttribute(key);

      final Converter converter = (Converter) styleConstantsMap.get(key);
      if (converter != null)
      {
        final ConverterAttributeSet attributeSet = converter.convertToCSS(key, value, cssAttr, context);
        if (attributeSet != null)
        {
          cssAttr.addAttributes(attributeSet);
        }
        else
        {
          debugAttribut("No convertion for ", key, value);
          cssAttr.addAttribute(key, value);
        }
      }
      else
      {
        debugAttribut("No converter for ", key, value);
        cssAttr.addAttribute(key, value);
      }
    }

    return cssAttr;
  }

  private void debugAttribut(final String name, final Object key, final Object value)
  {
    System.out.println(name + "attribute [" + key.getClass().getName() + "] " + key + " = " + value + " [" + value.getClass().getName() + ']');
  }

  protected void handleElement(final Element element)
      throws BadLocationException, InputFeedException
  {
    if (element == null)
    {
      return;
    }


    System.out.println("Stating Element: " + element.getName());
    feed.startElement(NAMESPACE, element.getName());
    final AttributeSet as = element.getAttributes();

    final AttributeSet cssAttr = convertAttributes(as, element);

    final Enumeration attributeNames = cssAttr.getAttributeNames();
    while (attributeNames.hasMoreElements())
    {
      final Object key = attributeNames.nextElement();
      final Object value = cssAttr.getAttribute(key);

      // forget element name because we handled it just before
      if (key == StyleConstants.NameAttribute)
      {
        continue;
      }

      if (key == StyleConstants.ResolveAttribute)
      {
        // style name
        if (value instanceof Style)
        {
          final Style style = (Style) value;
          feed.setAttribute(NAMESPACE, ELEMENT_STYLE_ATTRIBUTE, getNormalizedStyleName(style.getName()));
          continue;
        }
      }

      debugAttribut("Element ", key, value);
      feed.setAttribute(NAMESPACE, key.toString(), value);
    }

    final String text = getElementText(element);
    if (text != null || !"".equals(text))
    {
      if (element.isLeaf())
      {
        System.out.println('\'' + text + '\'');
        feed.addContent(text);
      }
    }


    final int size = element.getElementCount();
    for (int i = 0; i < size; i++)
    {
      final Element e = element.getElement(i);
      handleElement(e);
    }

    feed.endElement();
  }

  /**
   * Returns the text content of an element.
   *
   * @param element The element containing text.
   * @return The text.
   * @throws BadLocationException If the text position is invalid.
   */
  protected String getElementText(final Element element)
      throws BadLocationException
  {
    final Document document = element.getDocument();
    final String text = document.getText
        (element.getStartOffset(),
            element.getEndOffset() - element.getStartOffset());
    return text;
  }


  /**
   * Processes the style definitions of a styled document. Style definitions are
   * declared once in the document and are reused by styled elements.
   *
   * @param document The source document.
   * @throws InputFeedException If a problem occured with the feed.
   */
  protected void processStyleElements(final DefaultStyledDocument document)
      throws InputFeedException
  {
    final Enumeration names = document.getStyleNames();
    while (names.hasMoreElements())
    {
      final String styleName = (String) names.nextElement();
      final Style s = document.getStyle(styleName);

      if (s == null)
      {
        continue;
      }
      /*if (s.getAttributeCount() <= 1 &&
          s.isDefined(StyleConstants.NameAttribute))
      {
        continue;
      }*/

      // registering  & converting style name
      final String convertedStyleName = getNormalizedStyleName(styleName);
      System.out.println("Processing style: " + styleName + '(' +convertedStyleName+ ')');
      //convert attributes to css attributes
      final AttributeSet cssAttr = convertAttributes(s, null);
      final Enumeration attributeNames = cssAttr.getAttributeNames();
      final StringBuffer buffer = new StringBuffer(cssAttr.getAttributeCount()*4+5);

      feed.startMetaNode();
      // making stylesheet selector
      buffer.append(convertedStyleName);
      buffer.append(' ');
      buffer.append(getParentSelector(s.getResolveParent()));
      buffer.append("{\n");
      // generate stylesheet properties
      while (attributeNames.hasMoreElements())
      {
        final Object key = attributeNames.nextElement();
        final Object value = cssAttr.getAttribute(key);

        if (key == StyleConstants.NameAttribute || key == StyleConstants.ResolveAttribute)
        {
          continue;
        }

        debugAttribut("Style ", key, value);
        buffer.append(key.toString());
        buffer.append(':');
        buffer.append(value.toString());
        buffer.append(";\n");
      }
      buffer.append('}');

      // adding stylesheet
      feed.setMetaNodeAttribute(STYLESHEET_TYPE, STYLESHEET);
      feed.setMetaNodeAttribute(STYLESHEET_PCDATA, buffer.toString());
      feed.endMetaNode();
    }
  }

  /**
   * Returns the parent CSS selector of the given <code>AttributeSet</code>.
   *
   * @param attributeSet The style AttributeSet to use.
   * @return The parent CSS selector or an empty String if there is no parent.
   */
  private String getParentSelector(final AttributeSet attributeSet) {
    if(attributeSet != null)
    {
      final Object o = attributeSet.getAttribute(StyleConstants.ResolveAttribute);

      if(o != null)
      {
        final Style s = (Style)o;

        return getNormalizedStyleName(s.getName())+ ' ' + getParentSelector(attributeSet.getResolveParent());
      }
    }
    else
    {
      return "";
    }

    return "";
  }

  /**
   * Processes the document properties. These properties defined once for the
   * whole document.
   *
   * @param document The document source.
   * @throws InputFeedException If a problem occured with the feed.
   */
  protected void processDocumentProperties(final DefaultStyledDocument document)
      throws InputFeedException
  {
    //final Object title = document.getProperty(DefaultStyledDocument.TitleProperty);
    final SimpleAttributeSet attributeSet = new SimpleAttributeSet();
    final Dictionary documentProperties = document.getDocumentProperties();
    final Enumeration keys = documentProperties.keys();
    while (keys.hasMoreElements())
    {
      final Object key = keys.nextElement();
      final Object value = documentProperties.get(key);
      attributeSet.addAttribute(key, value);
    }

    final ConverterAttributeSet convertedSet = convertAttributes(attributeSet, null);
    processRules(convertedSet);

    // add other attributes as document attributes
    final AttributeSet documentAttributes = convertedSet.getAttributesByType(ConverterAttributeSet.NOT_TYPED);
    final Enumeration names = documentAttributes.getAttributeNames();
    while(names.hasMoreElements())
    {
      final Object name = names.nextElement();
      final Object value = documentAttributes.getAttribute(name);
      debugAttribut("Document Property ", name, value);
      feed.addDocumentAttribute(name.toString(), value);
    }

    //todo copy XhtmlInputDriver code for HMLT headers
  }

  private void processRules(final ConverterAttributeSet convertedSet) throws InputFeedException
  {
    final AttributeSet pageRuleAttributes = convertedSet.getAttributesByType(DocumentConverter.PAGE_RULE_TYPE);
    if(pageRuleAttributes.getAttributeCount() > 0)
    {
      final StringBuffer buffer = new StringBuffer();
      buffer.append("@page {\n");
      final Enumeration names = pageRuleAttributes.getAttributeNames();
      while(names.hasMoreElements())
      {
        final Object name = names.nextElement();
        final Object value = pageRuleAttributes.getAttribute(name);
        debugAttribut("Page rule attribute ", name, value);
        buffer.append(name.toString());
        buffer.append(':');
        buffer.append(value.toString());
        buffer.append(";\n");
      }
      buffer.append('}');
      feed.startMetaNode();
      // adding stylesheet
      feed.setMetaNodeAttribute(STYLESHEET_TYPE, STYLESHEET);
      feed.setMetaNodeAttribute(STYLESHEET_PCDATA, buffer.toString());
      feed.endMetaNode();
    }
  }

  public void parseDocument(final DefaultStyledDocument doc, final InputFeed feed)
      throws BadLocationException, InputFeedException
  {
    setFeed(feed);
    feed.startDocument();
    feed.startMetaInfo();
    processDocumentProperties(doc);
    processStyleElements(doc);
    feed.endMetaInfo();

    handleElement(doc.getDefaultRootElement());
    feed.endDocument();
  }


  public static void main(final String[] args)
      throws IOException, BadLocationException,
      InputFeedException
  {
    //final URL initialPage = new URL("http://www.google.com");
    //final URL initialPage = new URL("http://www.tug.org/tex-archive/obsolete/info/RTF/RTF-Spec.rtf");
    final URL initialPage = new URL("http://interglacial.com/rtf/rtf_book_examples/example_documents/p060_landscape_a4.rtf");
    //final URL initialPage = new URL("file:///d:/temp/1.rtf");
    initialPage.getContent();
    final JEditorPane pane = new JEditorPane(initialPage);
    pane.setEditable(false);
    final JFrame frame = new JFrame("HTML Viewer");
    frame.setSize(800, 600);
    // JDK 1.2.2 has no EXIT_ON_CLOSE ..
    frame.addWindowListener(new WindowAdapter()
    {
      /**
       * Invoked when a window has been closed.
       */
      public void windowClosed(final WindowEvent e)
      {
        System.exit(0);
      }
    });


    final JScrollPane scroll = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    frame.getContentPane().add(scroll);
    frame.setVisible(true);

    LibLayoutBoot.getInstance().start();

    final long startTime = System.currentTimeMillis();

    final StreamingHtmlOutputProcessor outputProcessor =
        new StreamingHtmlOutputProcessor(LibLayoutBoot.getInstance().getGlobalConfig());
    final LayoutProcess process = new DefaultLayoutProcess(outputProcessor);


    final SwingDocumentImport imprt = new SwingDocumentImport();
    imprt.parseDocument((DefaultStyledDocument) pane.getDocument(), process.getInputFeed());

    final long endTime = System.currentTimeMillis();

    System.out.println("Done!: " + (endTime - startTime));
  }
}
