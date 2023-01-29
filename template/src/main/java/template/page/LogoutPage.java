package template.page;

import java.io.Serial;

public class LogoutPage extends BorderPage {
  @Serial private static final long serialVersionUID = -6439596627924555651L;

  public String remoteUser;
  public String title = "Logout";

  @Override
  public void onInit() {
    super.onInit();
    remoteUser = getContext().getRequest().getRemoteUser();
    getContext().getSession().invalidate();
  }

}