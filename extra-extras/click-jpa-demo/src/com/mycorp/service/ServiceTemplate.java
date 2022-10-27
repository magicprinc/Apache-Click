package com.mycorp.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.mycorp.util.EMF;

public class ServiceTemplate {

	protected EntityManager getEntityManager() {
		return EMF.getEM();
	}

	protected List performQuery(String str) {
		Query query = getEntityManager().createQuery(str);
		return query.getResultList ();
	}

	protected void deleteObject(Object entity) {
		EntityManager em = getEntityManager();
        em.remove(entity);
	}

	protected void saveObject(Object entity){
		EntityManager em = getEntityManager();
		em.persist(entity);
	}

	protected Object getObjectForPK(Class cls, Object id){
		if (id == null) {
			return null;
		}
        return getEntityManager().find(cls, id);
	}
}
