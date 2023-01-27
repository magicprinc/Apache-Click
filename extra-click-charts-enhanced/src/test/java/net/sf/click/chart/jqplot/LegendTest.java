package net.sf.click.chart.jqplot;

import net.sf.click.chart.jqplot.Legend.Compass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class LegendTest {

	@Test
	public void testNeedsRendering() {
		Legend legend = new Legend();
		assertFalse( legend.needsRendering() );
		legend.setShow( false );
		assertTrue( legend.needsRendering() );
	}//met

	@Test
	public void testrender() {
		Legend legend = new Legend();
		assertEquals("legend is not empty", "{}", legend.render( -100 ) );
		legend.setShow( false );
		assertEquals("show property not defined properly", "{show: false}", legend.render( -100 ) );
		legend.setLocation( Compass.NORTH_EAST );
		assertEquals("show property not defined properly", "{location: 'ne',show: false}", legend.render( -100 ) );
	}//met


}//class