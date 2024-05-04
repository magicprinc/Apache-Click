package com.mycorp.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Slf4j
public class EntityManagerFilter implements Filter {

	@Getter
	protected FilterConfig filterConfig = null;

	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		EntityManager em = null;
	   // ServletContext servletContext = getFilterConfig().getServletContext(); ConfigService configService = ClickUtils.getConfigService(servletContext); logger = configService.getLogService();

		try {
			em = EMF.entityManagerFactory().createEntityManager();
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
							 log.error("Rolling back the transaction");
							 em.getTransaction().rollback();
						}
						em.close();
					} else {
						log.error("EntityManager is already closed!");
					}
				}
			} catch (Throwable t) {
				log.error("Error closing EntityManager", t);
			}
		}
	}

	@Override
	public void init(FilterConfig config) {
		filterConfig = config;
		destroy();
	}

	@Override
	public void destroy() {
		if (EMF.entityManagerFactory() != null) {
			EMF.entityManagerFactory().close();
		}
	}

    public void setFilterConfig(FilterConfig filterConfig) {
        init(filterConfig);
    }
}