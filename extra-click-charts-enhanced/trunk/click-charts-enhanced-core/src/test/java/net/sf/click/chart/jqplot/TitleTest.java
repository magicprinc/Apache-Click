package net.sf.click.chart.jqplot;

import static org.junit.Assert.*;

import net.sf.click.chart.jqplot.renderer.DivTitleRenderer;
import org.junit.Test;

public class TitleTest {

	@Test
	public void testNeedsRendering() {
		Title title = new Title();
		assertFalse( title.needsRendering() );
		title.setShow( false );
		assertTrue( title.needsRendering() );
	}//met

	@Test
	public void testRender1() {
		Title title = new Title();
		assertEquals("Title is not empty", "{}", title.render( -100 ) );
		title.setShow( false );
		assertEquals("show property not defined properly", "{show: false}", title.render( -100 ) );
	}//met
	
	@Test
	public void testRender2() {
		Title title = new Title();
		title.setRenderer( new DivTitleRenderer() );
		assertEquals("divRenderer not defined properly", "{renderer: $.jqplot.DivTitleRenderer}", title.render( -100 ) );
	}//met
}//class
