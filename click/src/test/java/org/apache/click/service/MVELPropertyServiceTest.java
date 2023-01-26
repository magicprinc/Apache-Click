package org.apache.click.service;

import org.apache.click.servlet.MockServletContext;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;


public class MVELPropertyServiceTest extends PropertyServiceTestCase {

	final AtomicInteger hit = new AtomicInteger();
	final AtomicInteger total = new AtomicInteger();

	@Override protected void setUp(){
		hit.set(0);
		total.set(0);
		propertyService = new MVELPropertyService(){
			@Override protected Serializable cacheOrParse (Object root, String name) {
				total.incrementAndGet();
				if (EXPRESSION_CACHE.asMap().containsKey(PropertyService.distinctClassName(root)+'#'+name)){
					hit.incrementAndGet();
				}
				return super.cacheOrParse(root, name);
			}
		};

		try {
			propertyService.onInit(new MockServletContext());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override protected void tearDown(){
		System.err.println("MVELPropertyServiceTest: Cache HIT/TOTAL = "+hit.get()+" / "+total.get()+" ~ "+(hit.get()*100.0/total.get()));
		try {
			propertyService.onDestroy();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public void testClassName2 () {
		assertEquals("org.apache.click.service.MVELPropertyServiceTest", PropertyService.distinctClassName(this));
		assertEquals("org.apache.click.service.MVELPropertyServiceTest", PropertyService.distinctClassName(new Demo(){}));// because org.apache.click.service.MVELPropertyServiceTest$1
		assertEquals("org.apache.click.service.PropertyServiceTestCase$Demo", PropertyService.distinctClassName(new Demo()));
		assertEquals("org.apache.click.service.PropertyServiceTestCase$Demo$Kaka", PropertyService.distinctClassName(new Demo.Kaka()));
		assertEquals("java.lang.String", PropertyService.distinctClassName(""));
		assertEquals("java.lang.Class", PropertyService.distinctClassName("".getClass()));
		assertEquals("java.lang.Character$UnicodeBlock", PropertyService.distinctClassName(java.lang.Character.UnicodeBlock.LATIN_1_SUPPLEMENT));
		assertTrue(PropertyService.distinctClassName((Runnable)()->{}).startsWith("org.apache.click.service.MVELPropertyServiceTest$$Lambda$"));

	}
}