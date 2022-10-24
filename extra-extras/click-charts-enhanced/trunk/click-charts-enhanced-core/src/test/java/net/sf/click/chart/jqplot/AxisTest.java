package net.sf.click.chart.jqplot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer;
import net.sf.click.chart.jqplot.renderer.AxisTickRenderer.MarkLocation;

import org.junit.Test;


public class AxisTest {
	@Test
	public void testNeedsRendering() {
		Axis axis = new Axis();
		assertFalse( axis.needsRendering() );
		axis.setShow( false );
		assertTrue( axis.needsRendering() );
	}//met

	@Test
	public void testrender() {
		Axis axis = new Axis();
		assertEquals("legend is not empty", "{}", axis.render( -100 ) );
		axis.setShow( false );
		assertEquals("show property not defined properly", "{show: false}", axis.render( -100 ) );
		axis.setMax( 12.1d );
		assertEquals("show property not defined properly", "{max: 12.1,show: false}", axis.render( -100 ) );
	}//met
	
	@Test
	public void testRender2() {
		Axis axis = new Axis();
		assertEquals("legend is not empty", "{}", axis.render( -100 ) );
		axis.setTickRenderer( new AxisTickRenderer() );
		assertEquals("show property not defined properly", "{tickRenderer: $.jqplot.AxisTickRenderer}", axis.render( -100 ) );
		((AxisTickRenderer) axis.getTickRenderer()).setMark( MarkLocation.CROSS );
		assertEquals("show property not defined properly", "{tickOptions: {mark: 'cross'},tickRenderer: $.jqplot.AxisTickRenderer}", axis.render( -100 ) );
		((AxisTickRenderer) axis.getTickRenderer()).setFontSize( "11px" ); 
		assertEquals("show property not defined properly", "{tickOptions: {fontSize: '11px',mark: 'cross'},tickRenderer: $.jqplot.AxisTickRenderer}", axis.render( -100 ) );
	}//met
}
