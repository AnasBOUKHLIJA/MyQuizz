package com.myquiz.myquizz.repository;

import com.myquiz.myquizz.metier.Poseur;

import jakarta.faces.bean.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Named("poseurRepository")
@ApplicationScoped
public class PoseurRepository {

    private static final String PERSISTENCE_UNIT_NAME = "MyQuizz";
    private EntityManagerFactory entityManagerFactory;

    public PoseurRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public List<Poseur> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Poseur> poseurs = entityManager.createQuery("SELECT p FROM Poseur p order by p.id desc ", Poseur.class).getResultList();
        entityManager.close();
        return poseurs;
    }

    public void save(Poseur poseur) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(poseur);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void update(Poseur poseur) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(poseur);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Poseur get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Poseur poseur = entityManager.find(Poseur.class, id);
        entityManager.getTransaction().commit();
        return poseur;
    }
    public void delete(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Poseur poseur = entityManager.find(Poseur.class, id);
        entityManager.remove(poseur);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Poseur getByLoginAndPassword(String login, String password) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Poseur> query = entityManager.createQuery("SELECT p FROM Poseur p WHERE p.login = :login AND p.password = :password", Poseur.class);
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
