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
package org.apache.click;

import lombok.Getter;
import lombok.NonNull;
import org.apache.click.service.ConfigService;
import org.apache.click.service.LogService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Provides a centralized registry where Controls can be registered and interact
 * with the Click runtime.
 * <p/>
 * The primary use of the ControlRegistry is for Controls to register themselves
 * as potential <tt>targets</tt> of Ajax requests
 * (If a control is an Ajax request target, it's <tt>onProcess()</tt>
 * method is invoked while other controls are not processed).
 * <p/>
 * Registering controls as Ajax targets serves a dual purpose. In addition to
 * being potential Ajax targets, these controls will have all their Behaviors
 * processed by the Click runtime.
 * <p/>
 * Thus the ControlRegistry provides the Click runtime  with easy access to Controls
 * that want to be processed for Ajax requests. It also provides quick access
 * to Controls that have Behaviors, and particularly AjaxBehaviors that want to
 * handle and respond to Ajax requests.
 *
 * <h3>Register Control as an Ajax Target</h3>
 * Below is an example of a Control registering itself as an Ajax target:
 *
 * <pre class="prettyprint">
 * public class AbstractControl implements Control {
 *
 *     public void addBehavior(Behavior behavior) {
 *         getBehaviors().add(behavior);
 *         // Adding a behavior also registers the Control as an Ajax target
 *         ControlRegistry.registerAjaxTarget(this);
 *     }
 * } </pre>
 *
 * <h3>Register Interceptor</h3>
 * Below is an example of a Container registering a Behavior in order to intercept
 * and decorate its child controls:
 *
 * <pre class="prettyprint">
 * public class MyContainer extends AbstractContainer {
 *
 *     public void onInit() {
 *         Behavior controlInterceptor = getInterceptor();
 *         ControlRegistry.registerInterceptor(this, controlInterceptor);
 *     }
 *
 *     private Behavior getInterceptor() {
 *         Behavior controlInterceptor = new Behavior() {
 *
 *             // This method is invoked before the controls are rendered to the client
 *             public void preResponse(Control source) {
 *                 // Here we can add a CSS class attribute to each child control
 *                 addCssClassToChildControls();
 *             }
 *
 *             // This method is invoked before the HEAD elements are retrieved for each Control
 *             public void preRenderHeadElements(Control source) {
 *             }
 *
 *             // This method is invoked before the Control onDestroy event
 *             public void preDestroy(Control source) {
 *             }
 *         };
 *         return controlInterceptor;
 *     }
 * } </pre>
 */
public class ControlRegistry {

  /** The thread local registry holder. */
  private static final ThreadLocal<ControlRegistry> THREAD_LOCAL_REGISTRY_STACK = new ThreadLocal<>();


  /** The set of Ajax target controls. */
  Set<Control> ajaxTargetControls;

  /** The list of registered interceptors. */
  List<InterceptorHolder> interceptors;

  /** The application log service. */
  final LogService logger;


  /**
   * Construct the ControlRegistry with the given ConfigService.
   *
   * @param configService the click application configuration service
   */
  public ControlRegistry(ConfigService configService) {
    this.logger = configService.getLogService();
  }//new


  /**
   * Return the thread local ControlRegistry instance.
   *
   * @return the thread local ControlRegistry instance.
   * @throws RuntimeException if a ControlRegistry is not available on the
   * thread
   */
  public static ControlRegistry getThreadLocalRegistry() {
    return THREAD_LOCAL_REGISTRY_STACK.get();
  }

  /**
   * Returns true if a ControlRegistry instance is available on the current
   * thread, false otherwise.
   * <p/>
   * Unlike {@link #getThreadLocalRegistry()} this method can safely be used
   * and will not throw an exception if a ControlRegistry is not available on
   * the current thread.
   *
   * @return true if an ControlRegistry instance is available on the
   * current thread, false otherwise
   */
  public static boolean hasThreadLocalRegistry() {
    return THREAD_LOCAL_REGISTRY_STACK.get() != null;
  }

  /**
   * Register the control to be processed by the Click runtime if the control
   * is the Ajax target. A control is an Ajax target if the
   * {@link Control#isAjaxTarget(org.apache.click.Context)} method returns true.
   * Once a target control is identified, Click invokes its
   * {@link Control#onProcess()} method.
   * <p/>
   * This method serves a dual purpose as all controls registered here
   * will also have their Behaviors (if any) processed. Processing
   * {@link org.apache.click.Behavior Behaviors}
   * means their interceptor methods will be invoked during the request
   * life cycle, passing the control as the argument.
   *
   * @param control the control to register as an Ajax target
   */
  public static void registerAjaxTarget (@NonNull Control control) {
    ControlRegistry instance = getThreadLocalRegistry();
    instance.internalRegisterAjaxTarget(control);
  }

  /**
   * Register a control event interceptor for the given Control and Behavior.
   * The control will be passed as the source control to the Behavior
   * interceptor methods:
   * {@link org.apache.click.Behavior#preRenderHeadElements(org.apache.click.Control) preRenderHeadElements(Control)},
   * {@link org.apache.click.Behavior#preResponse(org.apache.click.Control) preResponse(Control)} and
   * {@link org.apache.click.Behavior#preDestroy(org.apache.click.Control) preDestroy(Control)}.
   *
   * @param control the interceptor source control
   * @param controlInterceptor the control interceptor to register
   */
  public static void registerInterceptor (@NonNull Control control, @NonNull Behavior controlInterceptor) {
    ControlRegistry instance = getThreadLocalRegistry();
    instance.internalRegisterInterceptor(control, controlInterceptor);
  }


  static int getRegistryStackSize () {
    int total = 0;
    var x = THREAD_LOCAL_REGISTRY_STACK.get();
    while (x != null){
      total++;
      x = x.prev;
    }
    return total;
  }


  /**
   * Allow the registry to handle the error that occurred.
   *
   * @param throwable the error which occurred during processing
   */
  protected void errorOccurred(Throwable throwable) {
    clear();
  }

  // Package Private Methods ------------------------------------------------

  /**
   * Remove all interceptors and ajax target controls from this registry.
   */
  void clear() {
    if (hasInterceptors()) {
      getInterceptors().clear();
    }

    if (hasAjaxTargetControls()) {
      getAjaxTargetControls().clear();
    }
  }

  /**
   * Register the AJAX target control.
   *
   * @param control the AJAX target control
   */
  void internalRegisterAjaxTarget (@NonNull Control control) {
    getAjaxTargetControls().add(control);
  }

  /**
   * Register the source control and associated interceptor.
   *
   * @param source the interceptor source control
   * @param controlInterceptor the control interceptor to register
   */
  void internalRegisterInterceptor (@NonNull Control source, @NonNull Behavior controlInterceptor) {
    InterceptorHolder interceptorHolder = new InterceptorHolder(source, controlInterceptor);

    // Guard against adding duplicate interceptors
    List<InterceptorHolder> localInterceptors = getInterceptors();
    if (!localInterceptors.contains(interceptorHolder)) {
      localInterceptors.add(interceptorHolder);
    }
  }

  void processPreResponse (Context context) {
    if (hasAjaxTargetControls()) {
      for (Control control : getAjaxTargetControls()) {
        for (Behavior behavior : control.getBehaviors()) {
          behavior.preResponse(control);
        }
      }
    }

    if (hasInterceptors()) {
      for (InterceptorHolder interceptorHolder : getInterceptors()) {
        Behavior interceptor = interceptorHolder.getInterceptor();
        Control control = interceptorHolder.getControl();
        interceptor.preResponse(control);
      }
    }
  }

  void processPreRenderHeadElements(Context context) {
    if (hasAjaxTargetControls()) {
      for (Control control : getAjaxTargetControls()) {
        for (Behavior behavior : control.getBehaviors()) {
          behavior.preRenderHeadElements(control);
        }
      }
    }

    if (hasInterceptors()) {
      for (InterceptorHolder interceptorHolder : getInterceptors()) {
        Behavior interceptor = interceptorHolder.getInterceptor();
        Control control = interceptorHolder.getControl();
        interceptor.preRenderHeadElements(control);
      }
    }
  }

  void processPreDestroy(Context context) {
    if (hasAjaxTargetControls()) {
      for (Control control : getAjaxTargetControls()) {
        for (Behavior behavior : control.getBehaviors()) {
          behavior.preDestroy(control);
        }
      }
    }

    if (hasInterceptors()) {
      for (InterceptorHolder interceptorHolder : getInterceptors()) {
        Behavior interceptor = interceptorHolder.getInterceptor();
        Control control = interceptorHolder.getControl();
        interceptor.preDestroy(control);
      }
    }
  }

  /**
   * Checks if any AJAX target control have been registered.
   */
  boolean hasAjaxTargetControls() {
    return ajaxTargetControls != null && !ajaxTargetControls.isEmpty();
  }

  /**
   * Return the set of potential Ajax target controls.
   *
   * @return the set of potential Ajax target controls
   */
  Set<Control> getAjaxTargetControls() {
    if (ajaxTargetControls == null) {
      ajaxTargetControls = new LinkedHashSet<>();
    }
    return ajaxTargetControls;
  }

  /**
   * Checks if any control interceptors have been registered.
   */
  boolean hasInterceptors() {
    return interceptors != null && !interceptors.isEmpty();
  }

  /**
   * Return the set of registered control interceptors.
   *
   * @return set of registered interceptors
   */
  List<InterceptorHolder> getInterceptors() {
    if (interceptors == null) {
      interceptors = new ArrayList<>();
    }
    return interceptors;
  }

  volatile ControlRegistry prev;

  /**
   * Adds the specified ControlRegistry on top of the registry stack.
   *
   * @param controlRegistry the ControlRegistry to add
   */
  static void pushThreadLocalRegistry (@NonNull ControlRegistry controlRegistry){
    var ctx = THREAD_LOCAL_REGISTRY_STACK.get();
    if (ctx == controlRegistry)
      return;

    controlRegistry.prev = ctx;
    THREAD_LOCAL_REGISTRY_STACK.set(controlRegistry);
  }

  /**
   * Remove and return the controlRegistry instance on top of the
   * registry stack.
   */
  static void popThreadLocalRegistry() {
    var ctx = THREAD_LOCAL_REGISTRY_STACK.get();
    if (ctx == null || ctx.prev == null)
      THREAD_LOCAL_REGISTRY_STACK.remove();
    else
      THREAD_LOCAL_REGISTRY_STACK.set(ctx.prev);
  }

  static void clearThreadLocalRegistry() {
    THREAD_LOCAL_REGISTRY_STACK.remove();
  }


  static class InterceptorHolder {

    @Getter final private Behavior interceptor;

    @Getter final private Control control;

    public InterceptorHolder (Control control, Behavior interceptor) {
      this.control = control;
      this.interceptor = interceptor;
    }//new

    /**
     * @see Object#equals(java.lang.Object)
     *
     * @param o the reference object with which to compare
     * @return true if this object equals the given object
     */
    @Override  public boolean equals(Object o) {

      //1. Use the == operator to check if the argument is a reference to this object.
      if (o == this) {
        return true;
      }

      //2. Use the instanceof operator to check if the argument is of the correct type.
      if (!( o instanceof InterceptorHolder that )) {
        return false;
      }

      //3. Cast the argument to the correct type.

      boolean equals = Objects.equals(this.control, that.control);
      if (!equals) {
        return false;
      }

      return Objects.equals(this.interceptor, that.interceptor);
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     * @return the InterceptorHolder hashCode
     */
    @Override public int hashCode() {
      int result = 17;
      result = 37 * result + (control == null ? 0 : control.hashCode());
      result = 37 * result + (interceptor == null ? 0 : interceptor.hashCode());
      return result;
    }
  }
}