package org.apache.click.extras.spring.jdbc;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Extension of {@link JdbcDaoSupport} fix: {@link NamedParameterJdbcDaoSupport}
 * that exposes a {@link #getSimpleJdbcTemplate() SimpleJdbcTemplate} as well.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see SimpleJdbcTemplate
 * deprecated since Spring 3.1 in favor of {@link JdbcDaoSupport} and
 * {@link org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport}. The JdbcTemplate and
 * NamedParameterJdbcTemplate now provide all the functionality of the SimpleJdbcTemplate.
 */
public class SimpleJdbcDaoSupport extends NamedParameterJdbcDaoSupport {

	private SimpleJdbcTemplate simpleJdbcTemplate;

	/** Create a SimpleJdbcTemplate based on the configured JdbcTemplate. */
	@Override protected void initTemplateConfig() {
    super.initTemplateConfig();
		simpleJdbcTemplate = new SimpleJdbcTemplate(getNamedParameterJdbcTemplate());
	}

	/** Return a SimpleJdbcTemplate wrapping the configured JdbcTemplate. */
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
	  return this.simpleJdbcTemplate;
	}

}