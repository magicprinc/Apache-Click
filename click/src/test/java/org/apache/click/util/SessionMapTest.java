/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.click.util;

import junit.framework.TestCase;
import org.apache.click.MockContainer;
import org.apache.click.pages.SessionMapPage;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for SessionMap.
 */
public class SessionMapTest extends TestCase  {
    /**
     * Test SessionMap with a null session.
     */
    public void testNoSession() {
        SessionMap sm = new SessionMap(null);
       
        assertEquals(0, sm.size());
        assertEquals(0, sm.keySet().size());
        assertEquals(0, sm.entrySet().size());
        assertEquals(0, sm.values().size());
        
        assertNull(sm.get("attrib1"));
        assertNull(sm.get(null));
        assertTrue(sm.isEmpty());
        
        assertFalse(sm.containsKey("attrib1"));
        assertFalse(sm.containsKey(null));
        
        assertNull(sm.put("attrib1", "value1"));
        assertNull(sm.remove("attrib1"));
    }
    
    /**
     * Test with a session.
     */
    public void testSession() {
        MockContainer container = new MockContainer("web");
        container.start();
        HttpSession session = container.getRequest().getSession();
        SessionMap sm = new SessionMap(session);
       
        assertEquals(0, sm.size());
        assertEquals(0, sm.keySet().size());
        assertEquals(0, sm.entrySet().size());
        assertEquals(0, sm.values().size());
        
        assertNull(sm.get("attrib1"));
        assertNull(sm.get(null));
        assertTrue(sm.isEmpty());
        
        session.setAttribute("attrib1", "value1");
        
        assertEquals("value1", sm.get("attrib1"));
        
        assertEquals(1, sm.size());
        assertEquals(1, sm.keySet().size());
        assertTrue(sm.keySet().contains("attrib1"));
        
        assertEquals(1, sm.entrySet().size());
        Map.Entry<String, Object> entry = sm.entrySet().iterator().next();
        assertEquals("attrib1", entry.getKey());
        assertEquals("value1", entry.getValue());

        assertEquals(1, sm.values().size());
        assertTrue(sm.values().contains("value1"));

        container.stop();
    }

    /**
     * Test changes to the SessionMap.
     */
    public void testPuts() {
        MockContainer container = new MockContainer("web");
        container.start();
        HttpSession session = container.getRequest().getSession();
        SessionMap sm = new SessionMap(session);
       
        assertEquals(0, sm.size());
        
        sm.put("attrib1", "value1");
        
        assertEquals("value1", session.getAttribute("attrib1"));
        
        sm.put("attrib1", "value2");

        assertEquals("value2", session.getAttribute("attrib1"));

        assertEquals("value2", sm.remove("attrib1"));
        
        assertEquals(0, sm.size());
        
        sm.putAll(null);

        assertEquals(0, sm.size());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("attrib2", "value2");
        
        sm.putAll(map);
        assertEquals(1, sm.size());

        sm.clear();
        
        assertEquals(0, sm.size());
        
        container.stop();
    }
    
    /**
     * Test iteration over entrySet from velocity.
     */
    public void testPage() {
        MockContainer container = new MockContainer("web");
        container.start();
        HttpSession session = container.getRequest().getSession(true);
        
        session.setAttribute("attrib1", "value1");
        
        container.getRequest().setMethod("GET");

        container.testPage(SessionMapPage.class);
        assertTrue(container.getHtml(), container.getHtml().contains("attrib1=value1"));
        
        container.stop();
    }
}