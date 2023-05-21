package com.myquiz.myquizz.repository;

import com.myquiz.myquizz.metier.Quiz;
import com.myquiz.myquizz.metier.Reponse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ReponseRepository {

    private static final String PERSISTENCE_UNIT_NAME = "MyQuizz";
    private EntityManagerFactory entityManagerFactory;

    public ReponseRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public void save(Reponse reponse) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(reponse);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    public Reponse get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Reponse reponse = null;
        try {
            reponse = entityManager.find(Reponse.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception as needed
        } finally {
            entityManager.close();
        }
        return reponse;
    }
}
