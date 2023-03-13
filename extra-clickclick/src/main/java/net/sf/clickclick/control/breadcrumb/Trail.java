package net.sf.clickclick.control.breadcrumb;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides a trail for the breadcrumb control.
 * <p/>
 * This class provides a FIFO (First In First Out) cache clearance. Thus the
 * first entry added will be the first entry being removed when necessary.
 */
public class Trail<K, V> extends LinkedHashMap<K,V> {
  private static final long serialVersionUID = -7601683307826837232L;

  /** The breadcrumb control = the Trail's breadcrumb control. */
  @Getter @Setter private volatile transient Breadcrumb breadcrumb;

  /**
   * Create a Trail for the given breadcrumb.
   *
   * @param breadcrumb the breadcrumb control
   */
  public Trail(Breadcrumb breadcrumb) {
    this.breadcrumb = breadcrumb;
  }

  /**
   * Return true if the older entry must be removed, false otherwise.
   *
   * @param eldest the eldest entry in the Map
   * @return true if the eldest entry must be removed, false otherwise
   */
  @Override protected boolean removeEldestEntry (Map.Entry eldest){
    return size() > getBreadcrumb().getTrailLength();
  }
}