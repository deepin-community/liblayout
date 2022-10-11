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
 * $Id: ChainingLayoutProcess.java 6489 2008-11-28 14:53:40Z tmorgner $
 * ------------
 * (C) Copyright 2006-2007, by Pentaho Corporation.
 */
package org.jfree.layouting;

import org.jfree.layouting.input.style.PseudoPage;
import org.jfree.layouting.input.style.values.CSSValue;
import org.jfree.layouting.layouter.context.DocumentContext;
import org.jfree.layouting.layouter.feed.InputFeed;
import org.jfree.layouting.layouter.style.resolver.StyleResolver;
import org.jfree.layouting.normalizer.content.NormalizationException;
import org.jfree.layouting.normalizer.content.Normalizer;
import org.jfree.layouting.output.ChainingOutputProcessor;
import org.jfree.layouting.output.OutputProcessor;
import org.jfree.layouting.output.OutputProcessorMetaData;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

/**
 * Creation-Date: 16.06.2006, 14:42:57
 *
 * @author Thomas Morgner
 */
public class ChainingLayoutProcess implements LayoutProcess
{
  private static class ChainingLayoutProcessState implements LayoutProcessState
  {
    private LayoutProcessState chainedLayoutProcess;
    private State inputFeed;

    private ChainingLayoutProcessState()
    {
    }

    public LayoutProcessState getChainedLayoutProcess()
    {
      return chainedLayoutProcess;
    }

    public void setChainedLayoutProcess(final LayoutProcessState chainedLayoutProcess)
    {
      this.chainedLayoutProcess = chainedLayoutProcess;
    }

    public State getInputFeed()
    {
      return inputFeed;
    }

    public void setInputFeed(final State inputFeed)
    {
      this.inputFeed = inputFeed;
    }

    public LayoutProcess restore(final OutputProcessor outputProcessor)
        throws StateException
    {
//      Log.error ("START ++++++++++++++++++++++++++++++++++++++++++++++++ ");

      final LayoutProcess root = chainedLayoutProcess.restore(outputProcessor);
      final ChainingLayoutProcess proc = new ChainingLayoutProcess(root);
      proc.outputProcessor = new ChainingOutputProcessor(outputProcessor);
      if (inputFeed != null)
      {
        proc.inputFeed = (InputFeed) inputFeed.restore(proc);
        if (proc.inputFeed == null)
        {
          throw new StateException();
        }
      }
//      Log.error ("DONE  ++++++++++++++++++++++++++++++++++++++++++++++++ ");

      return proc;
    }
  }

  private LayoutProcess chainedLayoutProcess;
  private ChainingOutputProcessor outputProcessor;
  private InputFeed inputFeed;

  public ChainingLayoutProcess(final LayoutProcess layoutProcess)
  {
    this.chainedLayoutProcess = layoutProcess;
    this.outputProcessor = new ChainingOutputProcessor
        (layoutProcess.getOutputProcessor());
  }

  public InputFeed getInputFeed()
  {
    if (inputFeed == null)
    {
      inputFeed = outputProcessor.createInputFeed(this);
    }
    return inputFeed;
  }

  public StyleResolver getStyleResolver()
  {
    return chainedLayoutProcess.getStyleResolver();
  }

  /**
   * The document context holds global information, like the used stylesheets.
   * It also holds the caches for loading external contents.
   *
   * @return the document context.
   */
  public DocumentContext getDocumentContext()
  {
    return chainedLayoutProcess.getDocumentContext();
  }

  public OutputProcessorMetaData getOutputMetaData()
  {
    return chainedLayoutProcess.getOutputMetaData();
  }

  public OutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  public ResourceManager getResourceManager()
  {
    return chainedLayoutProcess.getResourceManager();
  }

  public void pageBreakEncountered(final CSSValue pageName,
                                   final PseudoPage[] pseudoPages)
      throws NormalizationException
  {
    getInputFeed().handlePageBreakEncountered(pageName, pseudoPages);
  }

  /**
   * A flag that indicates, whether one or more pagebreak have been encountered
   * during the last operation. The flag does not necessarily state that the
   * pagebreak(s) have been triggered by the last operation, it can as well be a
   * delayed pagebreak indication due to caching or layouting effects (as it
   * happens with pending or moved content).
   *
   * @return true, if a pagebreak as been encountered somewhere in the past,
   *         false otherwise.
   */
  public boolean isPagebreakEncountered()
  {
    return getInputFeed().isPagebreakEncountered();
  }

  public LayoutProcess getChainedLayoutProcess()
  {
    return chainedLayoutProcess;
  }

  public void setChainedLayoutProcess(final LayoutProcess chainedLayoutProcess)
  {
    this.chainedLayoutProcess = chainedLayoutProcess;
  }

  public LayoutProcessState saveState() throws StateException
  {
//    Log.error ("SAVING  ++++++++++++++++++++++++++++++++++++++++++++++++ ");
    final ChainingLayoutProcessState state = new ChainingLayoutProcessState();
    state.setInputFeed(inputFeed.saveState());
    state.setChainedLayoutProcess(chainedLayoutProcess.saveState());
//    Log.error ("DONE SAVING  +++++++++++++++++++++++++++++++++++++++++++ ");
    return state;
  }

  public Normalizer getNormalizer()
  {
    if (inputFeed == null)
    {
      throw new IllegalStateException
          ("We cant have come that far without an input feed.");
    }
    return inputFeed.getCurrentNormalizer();
  }


}
