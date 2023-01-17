package org.apache.click.service;

import org.apache.click.servlet.MockServletContext;

import java.util.HashMap;
import java.util.Map;

public class OGNLPropertyServiceTest extends PropertyServiceTestCase {

	@Override
	protected void setUp() {
		propertyService = new OGNLPropertyService();

		try {
			propertyService.onInit(new MockServletContext());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void tearDown() {
		try {
			propertyService.onDestroy();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public static class Demo {
		public final Map<String,Integer> map = new HashMap<>();

		public Demo () {
			map.put("foo", 42);
		}
	}

	public void testSimple () {
		var s = new OGNLPropertyService();

		var d = new Demo();

		s.setValue(d, "map.bar", 17);
		assertEquals(17, s.getValue(d, "map.bar"));
		assertNull(s.getValue(d, "map.zoo"));
		assertEquals(42, s.getValue(d, "map.foo"));
	}
}