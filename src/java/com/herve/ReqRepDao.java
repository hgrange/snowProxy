package com.herve;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PersistenceContext;

@RequestScoped
public class ReqRepDao {
    @PersistenceContext(name = "jpa-unit")
    private EntityManager em;

    public void createReqRep(ReqRep reqrep) {
    	try {
           em.persist(reqrep);
    	} catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public ReqRep readReqRep(String reqrep) {
        return em.find(ReqRep.class, reqrep);
    }

    public void updateReqRep(ReqRep reqrep) {
        em.merge(reqrep);
    }

    public void deleteReqRep(ReqRep reqrep) {
        em.remove(reqrep);
    }

    public List<ReqRep> readAllReqReps() {
        return em.createNamedQuery("ReqRep.findAll", ReqRep.class).getResultList();
    }

    public List<ReqRep> findRequest(String request) {
        return em.createNamedQuery("ReqRep.findReqRep", ReqRep.class)
            .setParameter("request", request).getResultList();
    }

}

