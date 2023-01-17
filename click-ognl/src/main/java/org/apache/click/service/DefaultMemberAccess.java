package org.apache.click.service;

import ognl.AbstractMemberAccess;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Map;

public class DefaultMemberAccess extends AbstractMemberAccess {

  /**
   * Returns true if the given member is accessible or can be made accessible
   * by this object.
   *
   * @param context      the current execution context (not used).
   * @param target       the Object to test accessibility for (not used).
   * @param member       the Member to test accessibility for.
   * @param propertyName the property to test accessibility for (not used).
   * @return true if the member is accessible in the context, false otherwise.
   */
  @Override public boolean isAccessible (Map context, Object target, Member member, String propertyName) {
    int modifiers = member.getModifiers();
    return Modifier.isPublic(modifiers);
  }
}