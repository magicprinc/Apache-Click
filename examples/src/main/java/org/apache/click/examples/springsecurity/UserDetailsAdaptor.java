package org.apache.click.examples.springsecurity;

import org.apache.click.examples.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Provides a Spring Security (ACEGI) UserDetails adaptor class, which wraps the
 * User class in the Spring Security UserDetails interface.
 */
public class UserDetailsAdaptor implements UserDetails {
  private static final long serialVersionUID = 4878947916871901019L;

  private final User user;

  public UserDetailsAdaptor(User user) {
    this.user = user;
  }//new

  @Override public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptySet();
  }

  @Override public String getPassword() {
    return user.getPassword();
  }

  @Override public String getUsername() {
    return user.getUsername();
  }
  @Override public boolean isAccountNonExpired() {
    return true;
  }

  @Override public boolean isAccountNonLocked() {
    return true;
  }

  @Override public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override public boolean isEnabled() {
    return true;
  }

}