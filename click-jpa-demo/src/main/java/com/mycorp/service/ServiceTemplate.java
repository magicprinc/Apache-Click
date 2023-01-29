package com.mycorp.service;

import com.mycorp.util.EMF;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

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