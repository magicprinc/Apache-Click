package com.mycorp.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EMF {

	private static final ThreadLocal<EntityManager> THREAD_LOCAL = new ThreadLocal<>();

	@Getter @Accessors(fluent = true)
	private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("transactions-optional");


	public static EntityManager getEM() {
		EntityManager em = THREAD_LOCAL.get();
		if (em == null || !em.isOpen()){
			// If EM isn't open, create a new one
			em = entityManagerFactory().createEntityManager();
			setEM(em);
		}
		return em;
	}

	public static void setEM (EntityManager em) {
		THREAD_LOCAL.set(em);
	}
}