package com.myquiz.myquizz.repository;

import com.myquiz.myquizz.metier.Question;
import com.myquiz.myquizz.metier.Quiz;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizRepository {
    private static final String PERSISTENCE_UNIT_NAME = "MyQuizz";
    private EntityManagerFactory entityManagerFactory;

    public QuizRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    /*public List<Quiz> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Quiz> quizzes = null;
        try {
            quizzes = entityManager.createQuery("SELECT q FROM Quiz q JOIN FETCH q.poseur order by q.id desc", Quiz.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception as needed
        } finally {
            entityManager.close();
        }
        return quizzes;
    }*/
    public List<Quiz> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Quiz> quizzes = null;
        try {
            String query = "SELECT q FROM Quiz q JOIN FETCH q.poseur ORDER BY q.id DESC";
            quizzes = entityManager.createQuery(query, Quiz.class).getResultList();
            // Loop through each quiz and initialize the questions collection
            for (Quiz quiz : quizzes) {
                quiz.getQuestions().size(); // Forces the initialization of the responses collection
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception as needed
        } finally {
            entityManager.close();
        }
        return quizzes;
    }

    public List<Quiz> findAllByPoseur(int poseurId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Quiz> quizzes = null;
        try {
            quizzes = entityManager.createQuery("SELECT q FROM Quiz q JOIN FETCH q.poseur WHERE q.poseur.id = :poseurId order by q.id desc", Quiz.class)
                    .setParameter("poseurId", poseurId)
                    .getResultList();
            // Loop through each quiz and initialize the questions collection
            for (Quiz quiz : quizzes) {
                quiz.getQuestions().size(); // Forces the initialization of the responses collection
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception as needed
        } finally {
            entityManager.close();
        }
        return quizzes;
    }
    public Quiz findWithQuestions(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Quiz quiz = entityManager.find(Quiz.class, id);

        // Initialize the reponses collection
        quiz.getQuestions().size();

        entityManager.close();
        return quiz;
    }
    public Quiz get(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Quiz quiz = null;
        try {
            quiz = entityManager.find(Quiz.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception as needed
        } finally {
            entityManager.close();
        }
        return quiz;
    }

    /*    public void save(Quiz quiz) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(quiz);
        entityManager.getTransaction().commit();
        entityManager.close();
    }*/
public void save(Quiz quiz) {
    quiz.setCreatedAt(LocalDateTime.now());
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    try {
        entityManager.getTransaction().begin();

        // Persist the quiz
        entityManager.persist(quiz);

        // Update the quiz-question relationship
        for (Question question : quiz.getQuestions()) {
            question.getQuizzes().add(quiz);
            entityManager.merge(question);
        }

        entityManager.getTransaction().commit();
    } catch (Exception e) {
        entityManager.getTransaction().rollback();
        e.printStackTrace();
    } finally {
        entityManager.close();
    }
}

    /*public void update(Quiz quiz) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(quiz);
        entityManager.getTransaction().commit();
        entityManager.close();
    }*/
    public void update(Quiz quiz) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();

            // Fetch the existing quiz from the database
            Quiz existingQuiz = entityManager.find(Quiz.class, quiz.getId());

            // Remove the relationship between the existing quiz and its questions
            for (Question question : existingQuiz.getQuestions()) {
                question.getQuizzes().remove(existingQuiz);
                entityManager.merge(question);
            }

            // Update the quiz-question relationship for the updated quiz
            for (Question question : quiz.getQuestions()) {
                question.getQuizzes().add(quiz);
                entityManager.merge(question);
            }

            // Merge the updated quiz
            entityManager.merge(quiz);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    /*public void delete(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Quiz quiz = entityManager.find(Quiz.class, id);
        entityManager.remove(quiz);
        entityManager.getTransaction().commit();
        entityManager.close();
    }*/
    public void delete(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Quiz quiz = entityManager.find(Quiz.class, id);

        // Remove the relationship between Quiz and Question
        for (Question question : quiz.getQuestions()) {
            question.getQuizzes().remove(quiz);
        }
        quiz.setQuestions(new ArrayList<>());

        entityManager.merge(quiz);
        entityManager.remove(quiz);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
