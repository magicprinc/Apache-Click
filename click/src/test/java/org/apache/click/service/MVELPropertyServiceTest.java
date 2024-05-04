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
				if (EXPRESSION_CACHE.asMap().containsKey(distinctClassName(root)+'#'+name)){
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
    var ps = new MVELPropertyService();
    assertEquals("org.apache.click.service.MVELPropertyServiceTest", ps.distinctClassName(this));
		assertEquals("org.apache.click.service.MVELPropertyServiceTest", ps.distinctClassName(new Demo(){}));// because org.apache.click.service.MVELPropertyServiceTest$1
		assertEquals("org.apache.click.service.PropertyServiceTestCase$Demo", ps.distinctClassName(new Demo()));
		assertEquals("org.apache.click.service.PropertyServiceTestCase$Demo$Kaka", ps.distinctClassName(new Demo.Kaka()));
		assertEquals("java.lang.String", ps.distinctClassName(""));
		assertEquals("java.lang.Class", ps.distinctClassName("".getClass()));
		assertEquals("java.lang.Character$UnicodeBlock", ps.distinctClassName(java.lang.Character.UnicodeBlock.LATIN_1_SUPPLEMENT));
		String className = ps.distinctClassName((Runnable) ()->{ });
		System.err.println(className);
		assertTrue(className, className.startsWith("org.apache.click.service.MVELPropertyServiceTest$$Lambda"));
	}
}