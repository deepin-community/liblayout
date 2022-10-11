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
 * $Id: DefaultRenderableTextFactory.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting.renderer.text;

import java.util.ArrayList;

import org.jfree.layouting.LayoutProcess;
import org.jfree.layouting.State;
import org.jfree.layouting.StateException;
import org.jfree.layouting.StatefullComponent;
import org.jfree.layouting.input.style.keys.text.TextStyleKeys;
import org.jfree.layouting.input.style.keys.text.TextWrap;
import org.jfree.layouting.input.style.keys.text.WhitespaceCollapse;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.FontSpecification;
import org.jfree.layouting.layouter.context.LayoutContext;
import org.jfree.layouting.layouter.style.CSSValueResolverUtility;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.jfree.layouting.renderer.model.RenderNode;
import org.jfree.layouting.renderer.model.RenderableText;
import org.jfree.layouting.renderer.model.SpacerRenderNode;
import org.pentaho.reporting.libraries.fonts.text.LanguageClassifier;
import org.pentaho.reporting.libraries.fonts.text.ClassificationProducer;
import org.pentaho.reporting.libraries.fonts.text.SpacingProducer;
import org.pentaho.reporting.libraries.fonts.text.GraphemeClusterProducer;
import org.pentaho.reporting.libraries.fonts.text.Spacing;
import org.pentaho.reporting.libraries.fonts.text.DefaultLanguageClassifier;
import org.pentaho.reporting.libraries.fonts.text.StaticSpacingProducer;
import org.pentaho.reporting.libraries.fonts.text.classifier.GlyphClassificationProducer;
import org.pentaho.reporting.libraries.fonts.text.classifier.LinebreakClassificationProducer;
import org.pentaho.reporting.libraries.fonts.text.classifier.WhitespaceClassificationProducer;
import org.pentaho.reporting.libraries.fonts.text.font.KerningProducer;
import org.pentaho.reporting.libraries.fonts.text.font.FontSizeProducer;
import org.pentaho.reporting.libraries.fonts.text.font.GlyphMetrics;
import org.pentaho.reporting.libraries.fonts.text.font.VariableFontSizeProducer;
import org.pentaho.reporting.libraries.fonts.text.font.NoKerningProducer;
import org.pentaho.reporting.libraries.fonts.text.breaks.BreakOpportunityProducer;
import org.pentaho.reporting.libraries.fonts.text.breaks.LineBreakProducer;
import org.pentaho.reporting.libraries.fonts.text.breaks.WordBreakProducer;
import org.pentaho.reporting.libraries.fonts.text.whitespace.WhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.text.whitespace.DiscardWhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.text.whitespace.PreserveWhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.text.whitespace.PreserveBreaksWhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.text.whitespace.CollapseWhiteSpaceFilter;
import org.pentaho.reporting.libraries.fonts.registry.FontMetrics;
import org.pentaho.reporting.libraries.base.util.ObjectUtilities;

/**
 * For the sake of completeness, we would now also need a script-type classifier
 * and from there we would need a BaseLineInfo-factory.
 *
 * @author Thomas Morgner
 */
public class DefaultRenderableTextFactory implements RenderableTextFactory
{
  protected static class DefaultRenderableTextFactoryState implements State
  {
    private GraphemeClusterProducer clusterProducer;
    private boolean startText;
    private boolean produced;
    private FontSizeProducer fontSizeProducer;
    private KerningProducer kerningProducer;
    private SpacingProducer spacingProducer;
    private BreakOpportunityProducer breakOpportunityProducer;
    private WhiteSpaceFilter whitespaceFilter;
    private CSSValue whitespaceCollapseValue;
    private ClassificationProducer classificationProducer;
    private LanguageClassifier languageClassifier;

    private ArrayList words;
    private ArrayList glyphList;
    private long leadingMargin;
    private int lastLanguage;

    protected DefaultRenderableTextFactoryState(final DefaultRenderableTextFactory factory)
        throws StateException
    {
      try
      {
        this.lastLanguage = factory.lastLanguage;
        this.leadingMargin = factory.leadingMargin;
        this.glyphList = (ArrayList) factory.glyphList.clone();
        this.words = (ArrayList) factory.words.clone();
        this.languageClassifier = (LanguageClassifier) factory.languageClassifier.clone();
        this.clusterProducer = (GraphemeClusterProducer) factory.clusterProducer.clone();
        this.produced = factory.produced;
        this.startText = factory.startText;

        if (factory.layoutContext != null)
        {
          this.classificationProducer = (ClassificationProducer) factory.classificationProducer.clone();
          this.whitespaceCollapseValue = factory.whitespaceCollapseValue;
          this.whitespaceFilter = (WhiteSpaceFilter) factory.whitespaceFilter.clone();
          this.breakOpportunityProducer = (BreakOpportunityProducer) factory.breakOpportunityProducer.clone();
          this.spacingProducer = (SpacingProducer) factory.spacingProducer.clone();
          this.kerningProducer = (KerningProducer) factory.kerningProducer.clone();
          this.fontSizeProducer = (FontSizeProducer) factory.fontSizeProducer.clone();
        }
      }
      catch(CloneNotSupportedException cne)
      {
        throw new StateException("Failed to save state", cne);
      }
    }

    /**
     * Creates a restored instance of the saved component.
     * <p/>
     * By using this factory-like approach, we gain independence from having to
     * know the actual implementation. This makes things a lot easier.
     *
     * @param layoutProcess the layout process that controls it all
     * @return the saved state
     * @throws org.jfree.layouting.StateException
     *
     */
    public StatefullComponent restore(final LayoutProcess layoutProcess)
        throws StateException
    {
      try
      {
        final DefaultRenderableTextFactory factory =
            new DefaultRenderableTextFactory(layoutProcess, false);
        factory.dims = null;
        factory.lastLanguage = this.lastLanguage;
        factory.leadingMargin = this.leadingMargin;
        factory.glyphList = (ArrayList) this.glyphList.clone();
        factory.words = (ArrayList) this.words.clone();
        factory.languageClassifier = (LanguageClassifier) this.languageClassifier.clone();
        factory.clusterProducer = (GraphemeClusterProducer) this.clusterProducer.clone();
        factory.produced = this.produced;
        factory.startText = this.startText;

        if (factory.classificationProducer != null)
        {
          factory.classificationProducer = (GlyphClassificationProducer) this.classificationProducer.clone();
          factory.whitespaceCollapseValue = this.whitespaceCollapseValue;
          factory.whitespaceFilter = (WhiteSpaceFilter) this.whitespaceFilter.clone();

          factory.breakOpportunityProducer = (BreakOpportunityProducer) this.breakOpportunityProducer.clone();
          factory.spacingProducer = (SpacingProducer) this.spacingProducer.clone();
          factory.kerningProducer = (KerningProducer) this.kerningProducer.clone();
          factory.fontSizeProducer = (FontSizeProducer) this.fontSizeProducer.clone();
        }

        return factory;
      }
      catch(CloneNotSupportedException cne)
      {
        throw new StateException("Restoring the state failed", cne);
      }
    }
  }

  private LayoutProcess layoutProcess;
  private GraphemeClusterProducer clusterProducer;
  private boolean startText;
  private boolean produced;
  private FontSizeProducer fontSizeProducer;
  private KerningProducer kerningProducer;
  private SpacingProducer spacingProducer;
  private BreakOpportunityProducer breakOpportunityProducer;
  private WhiteSpaceFilter whitespaceFilter;
  private CSSValue whitespaceCollapseValue;
  private GlyphClassificationProducer classificationProducer;
  private LayoutContext layoutContext;
  private LanguageClassifier languageClassifier;

  private transient GlyphMetrics dims;

  private ArrayList words;
  private ArrayList glyphList;
  private long leadingMargin;
  private int lastLanguage;

  // todo: This is part of a cheap hack.
  private transient FontMetrics fontMetrics;
  private static final int[] EMPTY_EXTRA_CHARS = new int[0];
  private static final RenderNode[] EMPTY_RENDER_NODE = new RenderNode[0];
  private static final RenderableText[] EMPTY_TEXT = new RenderableText[0];

  public DefaultRenderableTextFactory(final LayoutProcess layoutProcess)
  {
    this(layoutProcess, true);
  }

  protected DefaultRenderableTextFactory(final LayoutProcess layoutProcess,
                                         final boolean init)
  {
    this.layoutProcess = layoutProcess;
    if (init)
    {
      this.clusterProducer = new GraphemeClusterProducer();
      this.languageClassifier = new DefaultLanguageClassifier();
      this.startText = true;
      this.words = new ArrayList();
      this.glyphList = new ArrayList();
      this.dims = new GlyphMetrics();
    }
  }


  public RenderNode[] createText(final int[] text,
                                 final int offset,
                                 final int length,
                                 final LayoutContext layoutContext)
  {
    if (layoutContext == null)
    {
      throw new NullPointerException();
    }

    kerningProducer = createKerningProducer(layoutContext);
    fontSizeProducer = createFontSizeProducer(layoutContext);
    spacingProducer = createSpacingProducer(layoutContext);
    breakOpportunityProducer = createBreakProducer(layoutContext);
    whitespaceFilter = createWhitespaceFilter(layoutContext);
    classificationProducer = createGlyphClassifier(layoutContext);
    this.layoutContext = layoutContext;

    if (startText)
    {
      whitespaceFilter.filter(ClassificationProducer.START_OF_TEXT);
      breakOpportunityProducer.createBreakOpportunity(ClassificationProducer.START_OF_TEXT);
      kerningProducer.getKerning(ClassificationProducer.START_OF_TEXT);
      startText = false;
      produced = false;
    }

    // Todo: This is part of a cheap hack ..
    final FontSpecification fontSpecification =
        layoutContext.getFontSpecification();
    final OutputProcessorMetaData outputMetaData =
        layoutProcess.getOutputMetaData();
    fontMetrics = outputMetaData.getFontMetrics(fontSpecification);

    return processText(text, offset, length);
  }

  protected RenderNode[] processText(final int[] text,
                                     final int offset,
                                     final int length)
  {
    int clusterStartIdx = -1;
    final int maxLen = Math.min(length + offset, text.length);
    for (int i = offset; i < maxLen; i++)
    {
      final int codePoint = text[i];
      final boolean clusterStarted =
          this.clusterProducer.createGraphemeCluster(codePoint);
      // ignore the first cluster start; we need to see the whole cluster.
      if (clusterStarted)
      {
        if (i > offset)
        {
          final int extraCharLength = i - clusterStartIdx - 1;
          if (extraCharLength > 0)
          {
            final int[] extraChars = new int[extraCharLength];
            System.arraycopy(text, clusterStartIdx + 1, extraChars, 0, extraChars.length);
            addGlyph(text[clusterStartIdx], extraChars);
          }
          else
          {
            addGlyph(text[clusterStartIdx], EMPTY_EXTRA_CHARS);
          }
        }

        clusterStartIdx = i;
      }
    }

    // Process the last cluster ...
    if (clusterStartIdx >= offset)
    {
      final int extraCharLength = maxLen - clusterStartIdx - 1;
      if (extraCharLength > 0)
      {
        final int[] extraChars = new int[extraCharLength];
        System.arraycopy(text, clusterStartIdx + 1, extraChars, 0, extraChars.length);
        addGlyph(text[clusterStartIdx], extraChars);
      }
      else
      {
        addGlyph(text[clusterStartIdx], EMPTY_EXTRA_CHARS);
      }
    }

    if (words.isEmpty() == false)
    {
      final RenderNode[] renderableTexts = (RenderNode[])
          words.toArray(new RenderNode[words.size()]);
      words.clear();
      produced = true;
      return renderableTexts;
    }
    else
    {
      // we did not produce any text.
      return EMPTY_RENDER_NODE;
    }
  }

  protected void addGlyph(final int rawCodePoint, final int[] extraChars)
  {
    //  Log.debug ("Processing " + rawCodePoint);

    if (rawCodePoint == ClassificationProducer.END_OF_TEXT)
    {
      whitespaceFilter.filter(rawCodePoint);
      classificationProducer.getClassification(rawCodePoint);
      kerningProducer.getKerning(rawCodePoint);
      breakOpportunityProducer.createBreakOpportunity(rawCodePoint);
      spacingProducer.createSpacing(rawCodePoint);
      fontSizeProducer.getCharacterSize(rawCodePoint, dims);

      if (leadingMargin != 0 || glyphList.isEmpty() == false)
      {
        addWord(false);
      }
      else
      {
        // finish up ..
        glyphList.clear();
        leadingMargin = 0;
      }
      return;
    }

    int codePoint = whitespaceFilter.filter(rawCodePoint);
    final boolean stripWhitespaces;

    // No matter whether we will ignore the result, we have to keep our
    // factories up and running. These beasts need to see all data, no
    // matter what get printed later.
    if (codePoint == WhiteSpaceFilter.STRIP_WHITESPACE)
    {
      // if we dont have extra-chars, ignore the thing ..
      if (extraChars.length == 0)
      {
        stripWhitespaces = true;
      }
      else
      {
        // convert it into a space. This might be invalid, but will work for now.
        codePoint = ' ';
        stripWhitespaces = false;
      }
    }
    else
    {
      stripWhitespaces = false;
    }

    int glyphClassification = classificationProducer.getClassification(codePoint);
    final long kerning = kerningProducer.getKerning(codePoint);
    int breakweight = breakOpportunityProducer.createBreakOpportunity(codePoint);
    final Spacing spacing = spacingProducer.createSpacing(codePoint);
    dims = fontSizeProducer.getCharacterSize(codePoint, dims);
    int width = (dims.getWidth() & 0x7FFFFFFF);
    int height = (dims.getHeight() & 0x7FFFFFFF);
    lastLanguage = languageClassifier.getScript(codePoint);

    for (int i = 0; i < extraChars.length; i++)
    {
      final int extraChar = extraChars[i];
      dims = fontSizeProducer.getCharacterSize(extraChar, dims);
      width = Math.max(width, (dims.getWidth() & 0x7FFFFFFF));
      height = Math.max(height, (dims.getHeight() & 0x7FFFFFFF));
      breakweight = breakOpportunityProducer.createBreakOpportunity(extraChar);
      glyphClassification = classificationProducer.getClassification(extraChar);
    }

    if (stripWhitespaces)
    {
      //  Log.debug ("Stripping whitespaces");
      return;
    }

    if (Glyph.SPACE_CHAR == glyphClassification && isWordBreak(breakweight))
    {

      // Finish the current word ...
      final boolean forceLinebreak = breakweight == BreakOpportunityProducer.BREAK_LINE;
      if (glyphList.isEmpty() == false || forceLinebreak)
      {
        addWord(forceLinebreak);
      }

      // This character can be stripped. We increase the leading margin of the
      // next word by the character's width.
      leadingMargin += width;
      //   Log.debug ("Increasing Margin");
      return;
    }

    final Glyph glyph = new Glyph(codePoint, breakweight,
        glyphClassification, spacing, width, height,
        dims.getBaselinePosition(), (int) kerning, extraChars);
    glyphList.add(glyph);
    // Log.debug ("Adding Glyph");

    // does this finish a word? Check it!
    if (isWordBreak(breakweight) && glyphList.isEmpty() == false)
    {
      final boolean forceLinebreak = breakweight == BreakOpportunityProducer.BREAK_LINE;
      addWord(forceLinebreak);
    }
  }

  protected void addWord(final boolean forceLinebreak)
  {
    if (glyphList.isEmpty())
    {
      // create an trailing margin element. This way, it can collapse with
      // the next element.
      if (forceLinebreak)
      {
        final RenderableText text = new RenderableText
            (TextUtility.createBaselineInfo('\n', fontMetrics), new Glyph[0], 0,
                0, lastLanguage, forceLinebreak);
        text.appyStyle(layoutContext, layoutProcess.getOutputMetaData());
        words.add(text);
      }
      else if (produced == true)
      {
        final SpacerRenderNode spacer = new SpacerRenderNode(leadingMargin, 0, false);
        spacer.appyStyle(layoutContext, layoutProcess.getOutputMetaData());
        words.add(spacer);
      }
    }
    else
    {
      // ok, it does.
      final Glyph[] glyphs = (Glyph[]) glyphList.toArray(new Glyph[glyphList.size()]);
      if (leadingMargin > 0)// && words.isEmpty() == false)
      {
        final SpacerRenderNode spacer = new SpacerRenderNode(leadingMargin, 0, false);
        spacer.appyStyle(layoutContext, layoutProcess.getOutputMetaData());
        words.add(spacer);
      }

      // todo: this is cheating ..
      final int codePoint = glyphs[0].getCodepoint();

      final RenderableText text = new RenderableText
          (TextUtility.createBaselineInfo(codePoint, fontMetrics), glyphs, 0,
              glyphs.length, lastLanguage, forceLinebreak);
      text.appyStyle(layoutContext, layoutProcess.getOutputMetaData());
      words.add(text);
      glyphList.clear();
    }
    leadingMargin = 0;
  }

  private boolean isWordBreak(final int breakOp)
  {
    if (BreakOpportunityProducer.BREAK_WORD == breakOp ||
        BreakOpportunityProducer.BREAK_LINE == breakOp)
    {
      return true;
    }
    return false;
  }

  protected WhiteSpaceFilter createWhitespaceFilter(final LayoutContext layoutContext)
  {
    final CSSValue wsColl = layoutContext.getValue(TextStyleKeys.WHITE_SPACE_COLLAPSE);

    if (ObjectUtilities.equal(whitespaceCollapseValue, wsColl))
    {
      if (whitespaceFilter != null)
      {
        whitespaceFilter.reset();
        return whitespaceFilter;
      }
    }

    whitespaceCollapseValue = wsColl;

    if (WhitespaceCollapse.DISCARD.equals(wsColl))
    {
      return new DiscardWhiteSpaceFilter();
    }
    if (WhitespaceCollapse.PRESERVE.equals(wsColl))
    {
      return new PreserveWhiteSpaceFilter();
    }
    if (WhitespaceCollapse.PRESERVE_BREAKS.equals(wsColl))
    {
      return new PreserveBreaksWhiteSpaceFilter();
    }
    return new CollapseWhiteSpaceFilter();
  }

  protected GlyphClassificationProducer createGlyphClassifier(final LayoutContext layoutContext)
  {
    final CSSValue wsColl = layoutContext.getValue(TextStyleKeys.WHITE_SPACE_COLLAPSE);
    if (WhitespaceCollapse.PRESERVE.equals(wsColl))
    {
      return new LinebreakClassificationProducer();
    }
    return new WhitespaceClassificationProducer();
  }

  protected BreakOpportunityProducer createBreakProducer
      (final LayoutContext layoutContext)
  {
    final CSSValue wordBreak = layoutContext.getValue(TextStyleKeys.TEXT_WRAP);
    if (TextWrap.NONE.equals(wordBreak))
    {
      // surpress all but the linebreaks. This equals the 'pre' mode of HTML
      return new LineBreakProducer();
    }

    // allow other breaks as well. The wordbreak producer does not perform
    // advanced break-detection (like syllable based breaks).
    return new WordBreakProducer();
  }

  protected SpacingProducer createSpacingProducer
      (final LayoutContext layoutContext)
  {

    final CSSValue minValue = layoutContext.getValue(TextStyleKeys.X_MIN_LETTER_SPACING);
    final CSSValue optValue = layoutContext.getValue(TextStyleKeys.X_OPTIMUM_LETTER_SPACING);
    final CSSValue maxValue = layoutContext.getValue(TextStyleKeys.X_MAX_LETTER_SPACING);

    final OutputProcessorMetaData outputMetaData =
        layoutProcess.getOutputMetaData();

    final int minIntVal = (int) CSSValueResolverUtility.convertLengthToDouble
        (minValue, layoutContext, outputMetaData);
    final int optIntVal = (int) CSSValueResolverUtility.convertLengthToDouble
        (optValue, layoutContext, outputMetaData);
    final int maxIntVal = (int) CSSValueResolverUtility.convertLengthToDouble
        (maxValue, layoutContext, outputMetaData);
    final Spacing spacing = new Spacing(minIntVal, optIntVal, maxIntVal);
//    Log.debug("Using some static spacing: " + spacing);
    return new StaticSpacingProducer(spacing);
  }

  protected FontSizeProducer createFontSizeProducer
      (final LayoutContext layoutContext)
  {
    final FontSpecification fontSpecification =
        layoutContext.getFontSpecification();
    final OutputProcessorMetaData outputMetaData =
        layoutProcess.getOutputMetaData();
    final FontMetrics fontMetrics =
        outputMetaData.getFontMetrics(fontSpecification);
    return new VariableFontSizeProducer(fontMetrics);
  }

  protected KerningProducer createKerningProducer(final LayoutContext layoutContext)
  {
    return new NoKerningProducer();
//
//    if (KerningMode.NONE.equals(layoutContext.getValue(TextStyleKeys.KERNING_MODE)))
//    {
//      return new NoKerningProducer();
//    }
//
//    final FontSpecification fontSpecification =
//            layoutContext.getFontSpecification();
//    final OutputProcessorMetaData outputMetaData =
//            layoutProcess.getOutputMetaData();
//    final FontMetrics fontMetrics =
//            outputMetaData.getFontMetrics(fontSpecification);
//    return new DefaultKerningProducer(fontMetrics);
  }

  public RenderNode[] finishText()
  {
    if (layoutContext == null)
    {
      return EMPTY_TEXT;
    }

    final RenderNode[] text = processText
        (new int[]{ClassificationProducer.END_OF_TEXT}, 0, 1);
    layoutContext = null;
    classificationProducer = null;
    kerningProducer = null;
    fontSizeProducer = null;
    spacingProducer = null;
    breakOpportunityProducer = null;

    return text;
  }

  public void startText()
  {
    startText = true;
  }

  public State saveState() throws StateException
  {
    return new DefaultRenderableTextFactoryState(this);
  }
}
