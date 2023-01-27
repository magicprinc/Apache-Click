package net.sf.click.chart.jqplot;

import net.sf.click.chart.Color;
import net.sf.click.chart.jqplot.renderer.LineRenderer;
import net.sf.click.chart.jqplot.renderer.MarkerRenderer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeriesTest {

	@Test
	public void testNeedsRendering() {
		Axis axis = new Axis();
		assertFalse( axis.needsRendering() );

		Series series = new Series();
		assertTrue( series.needsRendering() );
		series.setColor( Color.RED );
		assertTrue( series.needsRendering() );
	}//met

	@Test
	public void testRender1() {
		Series series = new Series();
		assertEquals("Series is not empty", "{}", series.render( -100 ) );
		series.setColor( Color.RED );
		assertEquals("color property not defined properly", "{color: 'rgb(255,0,0)'}", series.render( -100 ) );
	}//met

	@Test
	public void testRender2() {
		Series series = new Series();
		series.setBorderWidth( 5.2d );
		assertEquals("BorderWith property not defined properly", "{borderWidth: 5.2}", series.render( -100 ) );
		series.setFillAlpha( 1.8d );
		series.setFillAndStroke( false );
		assertEquals("Combo borderWidth, fillAlpha, fillAndStroke not defined properly", "{borderWidth: 5.2,fillAlpha: 1.8,fillAndStroke: false}", series.render( -100 ) );
		series.setShadow( true );
		assertEquals("Combo borderWidth, fillAlpha, fillAndStroke, shadow not defined properly", "{borderWidth: 5.2,fillAlpha: 1.8,fillAndStroke: false,shadow: true}", series.render( -100 ) );
	}//met

	@Test
	public void testRender3() {
		Series series = new Series();
		series.setMarkerRenderer( new MarkerRenderer() );
		assertEquals("markerRenderer not defined properly", "{markerRenderer: $.jqplot.MarkerRenderer}", series.render( -100 ) );
		((MarkerRenderer)series.getMarkerRenderer()).setColor( Color.blue );
		assertEquals("markerRenderer not defined properly", "{markerOptions: {color: 'rgb(0,0,255)'},markerRenderer: $.jqplot.MarkerRenderer}", series.render( -100 ) );
	}//met

	@Test
	public void testRender4() {
		Series series = new Series();
		series.setRenderer( new LineRenderer() );
		assertEquals("lineRenderer not defined properly", "{renderer: $.jqplot.LineRenderer}", series.render( -100 ) );
	}//met
}