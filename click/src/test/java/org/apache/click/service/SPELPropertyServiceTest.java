package org.apache.click.service;

import org.apache.click.extras.spring.SPELPropertyService;
import org.apache.click.servlet.MockServletContext;

import java.util.concurrent.atomic.AtomicInteger;


public class SPELPropertyServiceTest extends PropertyServiceTestCase {

	final AtomicInteger hit = new AtomicInteger();
	final AtomicInteger total = new AtomicInteger();

	@Override protected void setUp(){
		hit.set(0); 	total.set(0);
		propertyService = new SPELPropertyService(){
			@Override public Object getValue (Object source, String name) {
				total.incrementAndGet();
				if (EXPRESSION_CACHE.asMap().containsKey(PropertyService.distinctClassName(source)+'#'+name.trim())){
					hit.incrementAndGet();
				}
				return super.getValue(source, name);
			}
			@Override public void setValue (Object target, String name, Object value) {
				total.incrementAndGet();
				if (EXPRESSION_CACHE.asMap().containsKey(PropertyService.distinctClassName(target)+'#'+name.trim())){
					hit.incrementAndGet();
				}
				super.setValue(target, name, value);
			}
		};

		try {
			propertyService.onInit(new MockServletContext());
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override protected void tearDown(){
		System.err.println("SPELPropertyServiceTest: Cache HIT/TOTAL = "+hit.get()+" / "+total.get()+" ~ "+(hit.get()*100.0/total.get()));
		try {
			propertyService.onDestroy();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

}