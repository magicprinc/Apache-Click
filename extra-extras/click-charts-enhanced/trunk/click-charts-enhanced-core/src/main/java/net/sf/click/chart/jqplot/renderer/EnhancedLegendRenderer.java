package net.sf.click.chart.jqplot.renderer;

import java.util.ArrayList;
import java.util.List;

import net.sf.click.chart.jqplot.Legend;
import net.sf.click.chart.jqplot.plugins.Plugin;

/**
 * 
 * @author steff
 * 
 */
public class EnhancedLegendRenderer extends LegendRenderer implements Plugin {

	public EnhancedLegendRenderer() {
		this( null );
	}//cons
	
	public EnhancedLegendRenderer(Legend l) {
		super( "$.jqplot.EnhancedLegendRenderer",l );
	}//cons

	@Override
	public List<String> getListJavaScriptImport() {
		List<String> listJavaScriptImport = new ArrayList<String>();
		//javascript imports
		listJavaScriptImport.add("/jqplot/plugins/jqplot.enhancedLegendRenderer.js");
		return listJavaScriptImport;
	}
	
	public enum FadeSpeed
	{
		FAST( "fast" ),
		SLOW( "slow" ),
		NORMAL( "normal" );
		
		private String speed;
		private FadeSpeed( String s ) {
			this.speed = s;			
		}//cons
		@Override
		public String toString() {
			return speed;
		}//met
	}//enum
	
        /** Maximum number of rows in the legend.  0 or null for unlimited.*/
        private Integer numberRows = null;
        /** Maximum number of columns in the legend.  0 or null for unlimited.*/
        private Integer numberColumns = null;
        /** false to not enable series on/off toggling on the legend.*/
        private boolean seriesToggle = true;
        /** true or a fadein/fadeout speed (number of milliseconds or 'fast', 'normal', 'slow') 
         to enable show/hide of series on click of legend item.*/
        private FadeSpeed seriesToggleSpeed = FadeSpeed.NORMAL;
        /* true to toggle series with a show/hide method only and not allow fading in/out.  
         This is to overcome poor performance of fade in some versions of IE.*/
        private boolean disableIEFading = true;
		public Integer getNumberRows() {
			return numberRows;
		}//met
		public void setNumberRows(Integer numberRows) {
			this.numberRows = numberRows;
			options.add( "numberRows", numberRows );
		}//met
		public Integer getNumberColumns() {
			return numberColumns;
		}//met
		public void setNumberColumns(Integer numberColumns) {
			this.numberColumns = numberColumns;
			options.add( "numberColumns", numberColumns );
		}//met
		public boolean isSeriesToggle() {
			return seriesToggle;
		}//met
		public void setSeriesToggle(boolean seriesToggle) {
			this.seriesToggle = seriesToggle;
			options.add( "seriesToggle", seriesToggle );
		}//met
		public FadeSpeed getSeriesToggleSpeed() {
			return seriesToggleSpeed;
		}//met
		public void setSeriesToggleSpeed(FadeSpeed seriesToggleSpeed) {
			this.seriesToggleSpeed = seriesToggleSpeed;
			options.add( "seriesToggle", seriesToggleSpeed.toString() );
		}//met
		public void setSeriesToggleSpeed(int speed) {
			//TODO multitype, check that with jqplot
			options.add( "seriesToggle", speed );
		}//met
		public boolean isDisableIEFading() {
			return disableIEFading;
		}//met
		public void setDisableIEFading(boolean disableIEFading) {
			this.disableIEFading = disableIEFading;
			options.add( "disableIEFading", disableIEFading );
		}//met
}//class
