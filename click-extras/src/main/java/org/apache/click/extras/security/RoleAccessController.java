package org.apache.click.extras.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides a Role based access controller class. This access controller uses the
 * JEE servlet container to determine whether an authenticated user has access
 * to a specified role.
 * <p/>
 * This class is used as the default AccessController by the Menu class.
 */
public class RoleAccessController implements AccessController {

  /**
   * Return true if the user is in the specified security access role.
   * <p/>
   * <b>Please note:</b> if role is <tt>null</tt> this method returns true,
   * meaning user has access to resources without roles defined.
   *
   * @see AccessController#hasAccess(HttpServletRequest, String)
   *
   * @param request the user request
   * @param role the security access role to check
   * @return true if the user is in the specified role
   */
  @Override public boolean hasAccess(HttpServletRequest request, String role) {
    if (role == null) {
      return true;
    } else {
      return request.isUserInRole(role);
    }
  }

}