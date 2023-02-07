package org.apache.click.extras.control;

import org.apache.click.control.AbstractControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Andrey Fink [aprpda@gmail.com]
 * @version 0.0 30.01.2010
 */
public abstract class GoogleChart extends AbstractControl  {

  static final int MAX_LENGTH = 1000;
  static final int MAX_PIXELS = 300000;

  /** Hexadecimal characters. Use ClickUtils HEXADECIMAL */
  static final char[] HEXADECIMAL = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


  public static <E> List<E> list () { return new ArrayList<>(); }

  public static <E> List<E> list (int size) { return new ArrayList<>(size); }

  @SafeVarargs public static <E> List<E> list (E... items) { return new ArrayList<>(Arrays.asList(items)); }

  public static <E> List<E> list (Collection<E> c) {return new ArrayList<>(c);}


  public static String join (Iterator<?> it, String delimiter) {
    final String sep = delimiter != null ? delimiter : "";
    final StringBuilder sb = new StringBuilder(100);
    while (it.hasNext()) {
      sb.append(it.next()).append(delimiter);
    }//while
    if (sb.length() >= sep.length()) { sb.setLength(sb.length() - sep.length());}//remove last ,

    return sb.toString();
  }//join


  public static String join (Iterable<?> it, String delimiter) {
    return join(it.iterator(), delimiter);
  }//join


  public static final int TRANSPARENT = 0;
  public static final int OPAQUE = 0xFF;

  //Specify a color with at least a 6-letter string of hexadecimal values in the format RRGGBB. For example:
  public static final int RED   = 0xFF0000;
  public static final int GREEN = 0x00FF00;
  public static final int BLUE  = 0x0000FF;
  public static final int BLACK = 0x000000;
  public static final int WHITE = 0xFFFFFF;

  //You can optionally specify transparency by appending a hexadecimal value between 00 and FF where 00 is completely transparent and FF completely opaque. For example:
  public static final int SOLID_BLUE = 0xFF0000FF;
  public static final int TRANSPARENT_BLUE = 0x000000FF;


  public static String color (int colorARGB) {
    int c, size;
    if ((colorARGB & 0xFF000000) != 0) {//alpha
      c = Integer.rotateLeft(colorARGB, 8);
      size = 4;
    } else {
      c = colorARGB;
      size = 3;
    }
    final char[] d = new char[size*2];

    for (int i=size*2-1; size > 0; size--) {
      d[i--] = HEXADECIMAL[c & 0xF];
      c >>= 4;
      d[i--] = HEXADECIMAL[c & 0xF];
      c >>= 4;
    }

    return new String(d);
  }//color


  public static String color (int alpha, int red, int green, int blue) {
    return color(alpha << 24 | (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
  }//color


  public static String color (int red, int green, int blue) {
    return color((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
  }//color


  public static List<Double> scale (List<Number> values, double srcMin, double srcMax,
      double dstMin, double dstMax) {
    assert values != null : "scale: values must be not null";
    assert srcMin <= srcMax : "scale: srcMin > srcMax!";
    assert dstMin <= dstMax : "scale: dstMin > dstMax!";

    double src = srcMax - srcMin;
    double dst = dstMax - dstMin;

    final List<Double> scaledData = list(values.size());
    for (int i = 0, size = values.size(); i < size; i++) {
      scaledData.set(i, (values.get(i).doubleValue() - srcMin) * dst / src + dstMin);
    }
    return scaledData;
  }//scale


  public static List<Double> scale (List<Double> values, double dstMin, double dstMax)
  {
    assert values != null : "scale: values must be not null";
    assert dstMin <= dstMax : "scale: dstMin > dstMax!";

    double srcMin = Collections.min(values);
    double srcMax = Collections.max(values);

    double src = srcMax - srcMin;
    double dst = dstMax - dstMin;

    final List<Double> scaledData = list(values.size());
    for (int i = 0, size = values.size(); i < size; i++) {
      scaledData.set(i, ( values.get(i) - srcMin) * dst / src + dstMin);
    }
    return scaledData;
  }//scale


  /** Safe anyObj.toString. */
  public static String str (Object anyObj) {
    return anyObj != null ? anyObj.toString() : "";
  }//str


  @Override public String getTag () { return "img"; }


  private String apiUrl = "http://chart.apis.google.com/chart?";
  private int width;
  private int height;
  private String title;
  protected String chartType;//cht=


  public void setApiUrl (String apiUrl) {
    if (apiUrl == null || apiUrl.length() < 3) {
      throw new IllegalArgumentException("apiUrl = "+apiUrl);
    }
    this.apiUrl = apiUrl;
  }//setApiUrl


  /**
   * Chart width. <strong>Required.</strong>
   * <p><a href="http://code.google.com/apis/chart/basics.html#chart_size">Google Chart API</a>
   */
  public void setWidth (int value) {
    if (value < 1 || value > MAX_LENGTH) {
      throw new IllegalArgumentException("Width must be in range 0.."+MAX_LENGTH+", but "+value);
    }
    if (height*value > MAX_PIXELS) {
      throw new IllegalArgumentException("The largest possible area for all charts except maps is "+
          MAX_PIXELS+" pixels, but "+ height * value);
    }
    width = value;
  }//setWidth


  /**
   * Chart height. <strong>Required.</strong>
   * <p><a href="http://code.google.com/apis/chart/basics.html#chart_size">Google Chart API</a>
   */
  public void setHeight (int value) {
    if (value < 1 || value > MAX_LENGTH) {
      throw new IllegalArgumentException("Height must be in range 0.." + MAX_LENGTH + ", but " + value);
    }
    if (width * value > MAX_PIXELS) {
      throw new IllegalArgumentException("The largest possible area for all charts except maps is " +
          MAX_PIXELS + " pixels, but " + width * value);
    }
    height = value;
  }//setHeight


  public String getApiUrl () { return apiUrl; }


  public int getWidth () { return width; }


  public int getHeight () { return height; }


  public String getTitle () { return title;}


  public void setTitle (String value) { title = value; }


  //public static enum DataEncoding {}
  //public static IDataHolder data (int, double, min, max, auto)


  protected abstract static class BarChart extends GoogleChart {

    private boolean automaticallyResizeBars;//chbh=a

    protected BarChart (String chartType) {
      this.chartType = chartType;
    }//new

    public void setAutomaticallyResizeBars (boolean value) { automaticallyResizeBars = value; }

  }//BarChart


  protected static class HorizontalBarChartWithStackedBars extends BarChart {
    protected HorizontalBarChartWithStackedBars () {
      super("bhs");
    }//new
  }//HorizontalBarChartWithStackedBars

  protected static class VerticalBarChartWithStackedBars extends BarChart {
    protected VerticalBarChartWithStackedBars () {
      super("bvs");
    }//new
  }//VerticalBarChartWithStackedBars

  protected static class HorizontalBarChartWithGroupedBars extends BarChart {
    protected HorizontalBarChartWithGroupedBars () {
      super("bhg");
    }//new
  }//HorizontalBarChartWithGroupedBars

}