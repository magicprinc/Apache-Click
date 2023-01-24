package net.sf.click.examples.page;

import net.sf.click.extras.graph.JSChart;
import org.apache.click.Page;
import org.apache.click.extras.control.Menu;
import org.apache.click.extras.control.MenuFactory;
import org.apache.click.util.ClickUtils;

import java.io.Serial;
import java.util.Random;

public class BorderPage extends Page {
  @Serial private static final long serialVersionUID = 3721746656550335085L;

  private final Menu rootMenu = new MenuFactory().getRootMenu();

  protected JSChart getChart(){
    return null;// HomePage, etc
  }


  public BorderPage() {
    String className = getClass().getName();

    String shortName = className.substring(className.lastIndexOf('.') + 1);
    String title = ClickUtils.toLabel(shortName);
    addModel("title", title);

    String srcPath = className.replace('.', '/') + ".java";
    addModel("srcPath", srcPath);

    addControl(rootMenu);
  }

  /** @see #getTemplate() */
  @Override public String getTemplate (){ return "border-template.htm"; }

  /** @see org.apache.click.Page#onInit() */
  @Override public void onInit() {
    super.onInit();

    var chart = getChart();
    if (chart != null){
      chart.setChartHeight(400);
      chart.setChartWidth(600);

      fillChartWithDataSeries();
    }
  }

  protected void fillChartWithDataSeries (){
    var r = new Random();
    int bars = r.nextInt(5, 20);

    getChart().addPoint("x", 5);
    for (int i=0; i<bars; i++){
      getChart().addPoint(Integer.toHexString(i), r.nextInt(-10, 99));
    }
  }
}