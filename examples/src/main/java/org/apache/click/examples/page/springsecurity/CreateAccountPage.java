package org.apache.click.examples.page.springsecurity;

import org.apache.click.ActionListener;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.PasswordField;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.examples.domain.User;
import org.apache.click.examples.page.BorderPage;
import org.apache.click.examples.page.springsecurity.secure.SecurePage;
import org.apache.click.examples.service.UserService;
import org.apache.click.extras.control.EmailField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Provides a Spring Security (ACEGI) enabled account creation page.
 */
@Component
public class CreateAccountPage extends BorderPage {
  private static final long serialVersionUID = -723735445289269858L;

  private final Form form = new Form("form");
  private final TextField fullNameField = new TextField(User.FULLNAME_PROPERTY, "Full Name", true);
  private final EmailField emailField = new EmailField(User.EMAIL_PROPERTY);
  private final TextField userNameField = new TextField(User.USERNAME_PROPERTY, true);
  private final PasswordField passwordField = new PasswordField("password", true);
  private final PasswordField passwordAgainField = new PasswordField("passwordAgain", "Password again", true);
  private final HiddenField redirectField = new HiddenField("redirect", String.class);

  @Autowired(required = false) //@Resource(name="authenticationManager")  todo deleted Spring Security!!! see also: spring-bean.xml
  private AuthenticationManager authenticationManager;

  @Resource(name="userService")
  private UserService userService;


  public CreateAccountPage() {
    addControl(form);

    form.setDefaultFieldSize(30);

    form.add(fullNameField);
    form.add(emailField);
    form.add(userNameField);
    form.add(passwordField);
    form.add(passwordAgainField);

    Submit submit = new Submit("create");
    submit.setActionListener((ActionListener) source->onCreate());
    form.add(submit);

    form.add(redirectField);
  }//new

  // Event Handlers ---------------------------------------------------------

  @Override
  public void onInit() {
    super.onInit();

    if (getContext().isGet()) {
      redirectField.setValue(getContext().getRequestParameter("redirect"));
    }
  }

  public boolean onCreate() {
    if (form.isValid()) {

      String fullName = fullNameField.getValue();
      String email = emailField.getValue();
      String username = userNameField.getValue();
      String password1 = passwordField.getValue();
      String password2 = passwordAgainField.getValue();

      if (!password1.equals(password2)) {
        passwordField.setError("Password and password again do not match");
        return true;
      }

      User user = userService.getUser(username);

      if (user != null) {
        userNameField.setError(getMessage("usernameExistsError"));
        return true;
      }

      user = userService.createUser(fullName, email, username, password1);

      Authentication token = new UsernamePasswordAuthenticationToken(username, password1);
      Authentication result = authenticationManager.authenticate(token);
      SecurityContext securityContext = new SecurityContextImpl();
      securityContext.setAuthentication(result);
      SecurityContextHolder.setContext(securityContext);

      String path = redirectField.getValue();
      if (StringUtils.isNotBlank(path)) {
        setRedirect(path);
      } else {
        setRedirect(SecurePage.class);
      }
    }

    return true;
  }
}