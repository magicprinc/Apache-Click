package net.sf.click.chart.jqplot;

import static org.junit.Assert.*;

import net.sf.click.chart.Data;

import org.junit.Test;

public class JQplotSeriesTest {

	@Test
	public void testRender() {
		JQPlotDataSet series = new JQPlotDataSet();
		series.add( new Data<Integer,Integer>(0,6));
		series.add( 1.2,7.1);
		series.add( 1,8.1);
		
		assertEquals( "[[0,6] , [1.2,7.1] , [1.0,8.1]]", series.render());
		series.add( 1897.256,8.1456);
		assertEquals( "[[0,6] , [1.2,7.1] , [1.0,8.1] , [1897.256,8.1456]]", series.render());
	}

}
