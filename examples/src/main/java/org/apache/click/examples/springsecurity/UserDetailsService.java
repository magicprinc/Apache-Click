package org.apache.click.examples.springsecurity;

import org.apache.click.examples.domain.User;
import org.apache.click.examples.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Provides a Spring Security (ACEGI) UserDetailsService for loading users.
 */
@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService  {

  @Resource(name="userService")
  private UserService userService;

  /**
   * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(String)
   */
  @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

    User user = userService.getUser(username);

    if (user != null) {
      return new UserDetailsAdaptor(user);

    } else {
      throw new UsernameNotFoundException("UserDetailsService.loadUserByUsername()");
    }
  }

}