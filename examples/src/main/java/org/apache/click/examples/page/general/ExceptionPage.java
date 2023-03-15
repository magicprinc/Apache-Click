package org.apache.click.examples.page.general;

import org.apache.click.Page;
import org.apache.click.control.ActionLink;
import org.apache.click.examples.page.BorderPage;

/**
 * Provides examples of the Click Exception handling.
 */
public class ExceptionPage extends BorderPage {

  private final ActionLink nullPointerLink = new ActionLink("nullPointerLink", this, "onNullPointerClick");
  private final ActionLink illegalArgumentLink = new ActionLink("illegalArgumentLink", this, "onIllegalArgumentExceptionClick");
  private final ActionLink missingMethodLink = new ActionLink("missingMethodLink", this, "onMissingMethodClick");
  private final ActionLink brokenRendererLink = new ActionLink("brokenRendererLink", this, "onBrokenRendererClick");
  private final ActionLink brokenBorderLink = new ActionLink("brokenBorderLink", this, "onBrokenBorderClick");
  private final ActionLink brokenContentLink = new ActionLink("brokenContentLink", this, "onBrokenContentClick");

  private String templateValue;

  public ExceptionPage() {
    addControl(nullPointerLink);
    addControl(illegalArgumentLink);
    addControl(missingMethodLink);
    addControl(brokenRendererLink);
    addControl(brokenBorderLink);
    addControl(brokenContentLink);
  }

  @SuppressWarnings({"null", "DataFlowIssue", "ReturnValueIgnored", "ResultOfMethodCallIgnored"})
  public boolean onNullPointerClick() {
    Object object = null;
    object.hashCode();
    return true;
  }

  public boolean onIllegalArgumentExceptionClick() {
    // Null model value should throw IllegalArgumentException
    addModel("param-1", null);
    return true;
  }

  public boolean onBrokenRendererClick() {
    addModel("brokenRenderer", new BrokenRenderer());
    return true;
  }

  public boolean onBrokenBorderClick() {
    setPath("broken-border.htm");
    templateValue = "/general/broken-border.htm";
    return true;
  }

  public boolean onBrokenContentClick() {
    setPath("/general/broken-content.htm");
    return true;
  }

  // Public Methods ---------------------------------------------------------

  /**
   * Override getTemplate so we can stuff things up.
   *
   * @see Page#getTemplate()
   */
  @Override
  public String getTemplate() {
    return (templateValue != null) ? templateValue : super.getTemplate();
  }


  /**
   * Provides a rendering object which will throw a NPE when merged by
   * velocity in the template.
   */
  public static class BrokenRenderer {

    /**
     * Guaranteed to fail, or you money back.
     *
     * @see Object#toString()
     */
    @SuppressWarnings({"null", "DataFlowIssue"}) @Override
    public String toString() {
      Object object = null;
      return object.toString();
    }
  }

}