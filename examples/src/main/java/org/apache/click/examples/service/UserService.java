package org.apache.click.examples.service;

import org.apache.click.examples.domain.User;
import org.apache.click.extras.cayenne.CayenneTemplate;
import org.springframework.stereotype.Component;

/**
 * Provides a User Service.
 *
 * @see User
 */
@Component
public class UserService extends CayenneTemplate {

  public boolean isAuthenticatedUser(User user) {
    User user2 = getUser(user.getUsername());

    return user2 != null && user2.getPassword().equals(user.getPassword());
  }

  public User getUser(String username) {
    return findObject(User.class, "username", username);
  }

  public User createUser(String fullName, String email, String username, String password) {
    User user = new User();
    getDataContext().registerNewObject(user);

    user.setEmail(email);
    user.setFullname(fullName);
    user.setUsername(username);
    user.setPassword(password);

    commitChanges();

    return user;
  }
}