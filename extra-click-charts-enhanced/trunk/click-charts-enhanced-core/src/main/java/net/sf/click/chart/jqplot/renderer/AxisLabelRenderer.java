package net.sf.click.chart.jqplot.renderer;

/**
 * 
 * @author steff
 *http://www.jqplot.com/docs/files/jqplot-axisLabelRenderer-js.html
 */
public class AxisLabelRenderer extends LabelRenderer {

	public AxisLabelRenderer() {
		super( "$.jqplot.AxisLabelRenderer" );
	}//cons

	/** wether or not to show the tick (mark and label).*/
	private boolean show = true;
	/** The text or html for the label.*/
	private String label = "";
	/** true to escape HTML entities in the label.*/
	private boolean escapeHTML = false;

	public boolean isShow() {
		return show;
	}//met
	public void setShow(boolean show) {
		this.show = show;
		options.add( "show", show );
	}//met
	public String getLabel() {
		return label;
	}//met
	public void setLabel(String label) {
		this.label = label;
		options.add( "label", label );
	}//met
	public boolean isEscapeHTML() {
		return escapeHTML;
	}//met
	public void setEscapeHTML(boolean escapeHTML) {
		this.escapeHTML = escapeHTML;
		options.add( "escapeHTML",escapeHTML );
	}//met
}//class
