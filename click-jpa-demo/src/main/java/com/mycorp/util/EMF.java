package com.mycorp.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {

	private static final ThreadLocal<EntityManager> THREAD_LOCAL = new ThreadLocal<EntityManager>();

	private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("transactions-optional");

	private EMF() {
	}
	
	public static EntityManagerFactory getEMF() {
		return entityManagerFactory;
	}

	public static EntityManager getEM() {
		EntityManager em = THREAD_LOCAL.get();
		if (em == null || !em.isOpen()) {
			// If EM isn't open, create a new one
			em = getEMF().createEntityManager();
			setEM(em);
			return em;
		} else {
			return em;
		}
	}

	public static void setEM(EntityManager em) {
		THREAD_LOCAL.set(em);
	}
}
