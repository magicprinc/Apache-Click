package org.apache.click.service;

import lombok.SneakyThrows;
import org.apache.click.servlet.MockServletContext;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.util.concurrent.atomic.AtomicInteger;


public class MVELPropertyServiceTest extends PropertyServiceTestCase {

	final AtomicInteger hit = new AtomicInteger();
	final AtomicInteger total = new AtomicInteger();

	volatile Throwable testName;

	@Override protected void setUp(){
		hit.set(0);
		total.set(0);
		propertyService = new MVELPropertyService(){
			@Override protected Serializable cacheOrParse (Object root, String name) {
				if (testName == null){
					testName = new RuntimeException("Location");
				}

				total.incrementAndGet();
				if (EXPRESSION_CACHE.asMap().containsKey(PropertyService.distinctClassName(root)+'#'+name)){
					hit.incrementAndGet();
				}
				return super.cacheOrParse(root, name);
			}

			@Override protected @Nullable AccessibleObject getCached (CacheKey key) {
				if (testName == null){
					testName = new RuntimeException("Location");
				}

				total.incrementAndGet();
				var cached = super.getCached(key);
				if (cached != null){
					hit.incrementAndGet();
				}
				return cached;
			}
		};

		try {
			propertyService.onInit(new MockServletContext());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override @SneakyThrows
	protected void tearDown(){
		String testNameStr = "unknown?!!!";
		if ("Location".equals(testName.getMessage())){
			for (var stackTrace : testName.getStackTrace()){
				if (stackTrace.getClassName().startsWith("org.apache.click.service.")){
					testNameStr = stackTrace.getMethodName();
				}
			}
		} else {
			testNameStr = testName.getMessage();
		}
		System.err.println("===" +testNameStr+':');
		System.err.println("\tMVELPropertyServiceTest: Cache HIT/TOTAL = "+hit.get()+" / "+total.get()+" ~ "+(hit.get()*100.0/total.get()));

		propertyService.onDestroy();//throws Exception
	}


	public void testClassName2 () {
		testName = new RuntimeException("testClassName2");
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