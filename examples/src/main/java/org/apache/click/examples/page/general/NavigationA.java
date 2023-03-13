package org.apache.click.examples.page.general;

import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.examples.page.BorderPage;



/**
 * Provides a navigation example Page demonstrating forward and redirect page navigation.
 */
public class NavigationA extends BorderPage {
  private static final long serialVersionUID = 1L;

  private final ActionLink forwardLink = new ActionLink("forwardLink", this, "onForwardClick");
  private final ActionLink forwardParamLink = new ActionLink("forwardParamLink", this, "onForwardParamClick");
  private final ActionLink redirectLink = new ActionLink("redirectLink", this, "onRedirectClick");
  private final ActionLink redirectParamLink = new ActionLink("redirectParamLink", this::onRedirectParamClick);


  /** @see org.apache.click.Page#onInit() */
  @Override public void onInit() {
    super.onInit();

    addControl(forwardLink);
    addControl(forwardParamLink);
    addControl(redirectLink);
    addControl(redirectParamLink);

    // Initialise param ActionLink values from any parameters passed through
    // from other pages via forwards or redirects.
    int number = 1;

    // If request has been forwarded
    if (getContext().isForward()) {
      // If a request attribute was passed increment its value.
      Integer param = (Integer) getContext().getRequestAttribute("param");
      if (param != null) {
        number = param + 1;
      }

    // Else request may have been redirected
    } else {
      String param = getContext().getRequest().getParameter("param");
      if (param != null) {
        number = Integer.parseInt(param) + 1;
      }
    }

    forwardParamLink.setValue(Integer.toString(number));
    redirectParamLink.setValue(Integer.toString(number));
  }

  public boolean onForwardClick() {
    setForward(getTarget());
    return false;
  }

  public boolean onForwardParamClick() {
    Integer param = forwardParamLink.getValueInteger();
    getContext().setRequestAttribute("param", param);
    setForward(getTarget());
    return false;
  }

  public boolean onRedirectClick() {
    setRedirect(getTarget());
    return false;
  }

  public boolean onRedirectParamClick (Control source){
    setRedirect(getTarget() + "?param=" + redirectParamLink.getValue());
    return false;
  }


  public String getTarget() { return "/general/navigation-b.htm"; }

}