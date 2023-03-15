package org.apache.click.extras.spring.jdbc;

import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatcher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 @see org.mockito.Answers
 @see org.mockito.internal.stubbing.answers.AnswersWithDelay
 @see AdditionalMatchers
 @see org.mockito.Mockito#argThat(ArgumentMatcher)
 */
public class SimpleJdbcDaoSupportTest {

  @Test public void _main () {
    SimpleJdbcDaoSupport dao = new SimpleJdbcDaoSupport();
    JdbcTemplate jt = mock(JdbcTemplate.class);
    dao.setJdbcTemplate(jt);
    dao.afterPropertiesSet();// as in Spring

    NamedParameterJdbcTemplate njt = dao.getNamedParameterJdbcTemplate();
    assertNotNull(njt);

    SimpleJdbcTemplate sjt = dao.getSimpleJdbcTemplate();
    assertSame(njt, sjt.getNamedParameterJdbcOperations());
    assertSame(jt, sjt.getJdbcOperations());

    when(jt.queryForObject(eq("select 1"), AdditionalMatchers.aryEq(new Object[]{1,2,3}), eq(Integer.class))).thenReturn(42);
    assertEquals(42, sjt.queryForInt("select 1", 1,2,3));

    when(jt.queryForObject(eq("select b"), eq(Integer.class))).thenReturn(2);
    assertEquals(2, sjt.queryForInt("select b"));

  }
}