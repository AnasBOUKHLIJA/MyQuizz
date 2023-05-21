package com.myquiz.myquizz.repository;

import com.myquiz.myquizz.metier.Apprenant;
import jakarta.faces.bean.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.util.List;

@Named("ApprenantRepository")
@ApplicationScoped
public class ApprenantRepository {

    private static final String PERSISTENCE_UNIT_NAME = "MyQuizz";
    private EntityManagerFactory entityManagerFactory;

    public ApprenantRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public List<Apprenant> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Apprenant> apprenants = entityManager.createQuery("SELECT a FROM Apprenant a order by a.id desc ", Apprenant.class).getResultList();
        entityManager.close();
        return apprenants;
    }

    public void save(Apprenant apprenant) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(apprenant);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void update(Apprenant apprenant) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(apprenant);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Apprenant get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Apprenant apprenant = entityManager.find(Apprenant.class, id);
        entityManager.getTransaction().commit();
        return apprenant;
    }

    public void delete(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Apprenant apprenant = entityManager.find(Apprenant.class, id);
        entityManager.remove(apprenant);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Apprenant getByLoginAndPassword(String login, String password) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Apprenant> query = entityManager.createQuery("SELECT a FROM Apprenant a WHERE a.login = :login AND a.password = :password", Apprenant.class);
            query.setParameter("login", login);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (Exception exception) {
            return null;
        } finally {
            entityManager.close();
        }
    }
    }
