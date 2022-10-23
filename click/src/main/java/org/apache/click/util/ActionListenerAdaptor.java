/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click.util;

import lombok.NonNull;
import org.apache.click.ActionListener;
import org.apache.click.Control;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * Provides an ActionListener adaptor instance.
 *
 * [was deprecated] ActionListener only has a single method to implement, there is
 * no need for an adaptor
 */
public class ActionListenerAdaptor implements ActionListener, Serializable {
  @Serial private static final long serialVersionUID = 1L;

  /** The target listener object. */
  final Object listener;
  /** The target listener method name. */
  final Method method;

  /**
   * Create an ActionListener adaptor instance for the given listener target
   * object and listener method.
   *
   * @param listenerTarget the listener object
   * @param methodName the target listener method name
   */
  public ActionListenerAdaptor (@NonNull Object listenerTarget, @NonNull String methodName) throws IllegalArgumentException {
    listener = listenerTarget;
    method = ClickUtils.getMethod(listener, methodName);
  }

  /**
   * @see ActionListener#onAction(Control)
   *
   * @param source the source of the action event
   * @return true if control and page processing should continue or false otherwise.
   */
  public boolean onAction(Control source) {
    return ClickUtils.invokeListener(listener, method);
  }

}