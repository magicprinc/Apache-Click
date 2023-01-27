package net.sf.click.chart;

import org.apache.click.ActionListener;
import org.apache.click.control.AbstractControl;
import org.apache.click.element.Element;
import org.apache.click.util.HtmlStringBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class of the charting components hierarchy.
 * @author steff
 *
 */
public abstract class Chart<S extends DataSet> extends AbstractControl
{
	private static final long serialVersionUID = 1L;
	protected ChartOptions options = new ChartOptions();
	protected List<S> listDataSet = new ArrayList<S>();
	protected boolean demoMode = false;

	// ----------------------------------------------------------- Constructors

	/**
	 * Create a line chart with no name defined.
	 * <p/>
	 * <b>Please note</b> the control's name must be defined before it is valid.
	 */
	public Chart() {
	}

	/**
	 * Create a line chart with the given name.
	 *
	 * @param name the button name
	 */
	public Chart(String name) {
		super.setName(name);
	}

	/**
	 * Create a line chart with the given name and label.
	 *
	 * @param name the name of the chart control
	 * @param label the label of the chart that will be displayed
	 */
	public Chart(String name, String label) {
		super.setName(name);
		setLabel(label);
	}


	public boolean isDemoMode() {
		return demoMode;
	}

	public void setDemoMode(boolean demoMode) {
		this.demoMode = demoMode;
	}

	public void add( S s )
	{
		this.listDataSet.add( s )  ;
	}//met

	// Instance Variables -----------------------------------------------------

	/**
	 * Height of the DIV element that encloses this chart, default height 350 px.
	 */
	protected int height = 350;

	/**
	 * Width of the DIV element that encloses this chart, default width 380 px.
	 */
	protected int width = 480;

	/** The chart display label. */
	protected String label = "Chart";



	/**
	 * Return the width of the chart (the enclosing DIV element).
	 *
	 * @return the width of the chart
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width of the chart (of the enclosing DIV element), as a
	 * pixel value.
	 *
	 * @param chartWidth the chart width in pixels.
	 */
	public void setWidth(int chartWidth) {
		this.width = chartWidth;
	}

	/**
	 * Return the height of the chart (the enclosing DIV element).
	 *
	 * @return the height of the chart
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the height of the chart (of the enclosing DIV element), as a
	 * pixel value.
	 *
	 * @param chartHeight the chart height in pixels.
	 */
	public void setHeight(int chartHeight) {
		this.height = chartHeight;
	}

	/**
	 * Return the label of the chart.
	 *
	 * @return the label of the chart
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the chart display caption.
	 *
	 * @param label the display label of the chart
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	public ChartOptions getOptions() {
		return options;
	}

	public void setOptions(ChartOptions options) {
		this.options = options;
	}


	@Override public void setActionListener (ActionListener actionListener) {}


	/**
	 * Render the HTML representation of the chart.
	 *
	 * @see #toString()
	 *
	 * @param buffer the specified buffer to render the control's output to
	 */
	@Override
	public void render(HtmlStringBuffer buffer) {
		buffer.elementStart("div");
		buffer.appendAttribute("id", getId());
		buffer.appendAttribute("style", "height:" + getHeight() + "px; width:" + getWidth() + "px;");
		buffer.elementEnd("div");
	}

	/**
	 * Return the HTML HEAD elements for the javascript files used by this
	 * control.
	 *
	 * @see org.apache.click.Control#getHeadElements()
	 *
	 * @return the HTML HEAD elements for the javascript files used by this
	 * control.
	 */
	@Override
	public final List<Element> getHeadElements() {
		if (headElements == null) {
			headElements = super.getHeadElements();
			headElements.addAll(getChartHeadElements());
			headElements.addAll( getJsElements() );
		}
		return headElements;
	}

	public abstract List<Element> getChartHeadElements();

	public abstract List<Element> getJsElements();


	/**
	 * Return the HTML rendered chart.
	 *
	 * @return the HTML rendered chart string
	 */
	@Override
	public String toString() {
		HtmlStringBuffer buffer = new HtmlStringBuffer(getControlSizeEst());
		render(buffer);
		return buffer.toString();
	}

	// Event Handlers ---------------------------------------------------------

	/**
	 * Returns true, as javascript charts perform no server side logic.
	 *
	 * @return true
	 */
	@Override
	public boolean onProcess() {
		return true;
	}


}//class