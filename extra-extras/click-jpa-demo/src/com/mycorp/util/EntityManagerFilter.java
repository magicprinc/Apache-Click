package com.mycorp.util;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.click.service.ConfigService;
import org.apache.click.service.LogService;
import org.apache.click.util.ClickUtils;

public class EntityManagerFilter implements Filter {

	protected LogService logger;

	protected FilterConfig filterConfig = null;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		EntityManager em = null;

		if (logger == null) {
            ServletContext servletContext = getFilterConfig().getServletContext();
            ConfigService configService = ClickUtils.getConfigService(servletContext);
            logger = configService.getLogService();
		}

		try {
			em = EMF.getEMF().createEntityManager();
			em.getTransaction().begin();
			EMF.setEM(em);
			chain.doFilter(request, response);

			// Guard against services that already committed transaction
			if (em.isOpen() && em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}

			EMF.setEM(null);
		} finally {
			try {
				if (em != null) {
					if (em.isOpen()) {
						if (em.getTransaction().isActive()) {
							 logger.error("Rolling back the transaction");
				             em.getTransaction().rollback();
						}
						em.close();
					} else {
						logger.error("EntityManager is already closed!");
					}
				}
			} catch (Throwable t) {
				logger.error("Error closing EntityManager", t);
			}
		}
	}

	public void init(FilterConfig config) {
		filterConfig = config;
		destroy();
	}

	public void destroy() {
		if (EMF.getEMF() != null) {
			EMF.getEMF().close();
		}
	}

    public void setFilterConfig(FilterConfig filterConfig) {
        init(filterConfig);
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }
}
