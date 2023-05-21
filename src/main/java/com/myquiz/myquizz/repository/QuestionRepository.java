package com.myquiz.myquizz.repository;

import com.myquiz.myquizz.metier.Question;

import com.myquiz.myquizz.metier.Quiz;
import com.myquiz.myquizz.metier.Reponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class QuestionRepository {

    private static final String PERSISTENCE_UNIT_NAME = "MyQuizz";
    private EntityManagerFactory entityManagerFactory;

    public QuestionRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    /*public List<Question> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Question> questions = entityManager.createQuery("SELECT q FROM Question q order by q.id desc", Question.class).getResultList();

        // Loop through each question and initialize the reponses collection
        for (Question question : questions) {
            question.getReponses().size(); // Forces the initialization of the reponses collection
        }

        entityManager.close();
        return questions;
    }*/
    public List<Question> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Question> questions = null;
        try {
            String query = "SELECT q FROM Question q JOIN FETCH q.poseur ORDER BY q.id DESC";
            TypedQuery<Question> typedQuery = entityManager.createQuery(query, Question.class);
            questions = typedQuery.getResultList();

            // Loop through each question and initialize the responses collection
            for (Question question : questions) {
                question.getReponses().size(); // Forces the initialization of the responses collection
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception as needed
        } finally {
            entityManager.close();
        }
        return questions;
    }

    public List<Question> findAllByPoseur(int poseurId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Question> questions = null;
        try {
            questions = entityManager.createQuery("SELECT q FROM Question q JOIN FETCH q.poseur WHERE q.poseur.id = :poseurId order by q.id desc", Question.class)
                    .setParameter("poseurId", poseurId)
                    .getResultList();
            // Loop through each question and initialize the responses collection
            for (Question question : questions) {
                question.getReponses().size(); // Forces the initialization of the responses collection
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception as needed
        } finally {
            entityManager.close();
        }
        return questions;
    }

    public Question findWithReponses(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Question question = entityManager.find(Question.class, id);

        // Initialize the reponses collection
        question.getReponses().size();

        entityManager.close();
        return question;
    }
    public void saveReponseWithQuestion(Question question, Reponse reponse) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        if (!entityManager.contains(question)) {
            question = entityManager.merge(question);
        }

        entityManager.persist(reponse);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    /*public void save(Question question) {
        question.setCreatedAt(LocalDateTime.now());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Collection<Quiz> quizzes = question.getQuizzes();
        Collection<Quiz> mergedQuizzes = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            if (quiz.getId() != 0) { // Existing Quiz
                quiz = entityManager.merge(quiz);
            }
            mergedQuizzes.add(quiz);
        }

        question.setQuizzes(mergedQuizzes);
        entityManager.persist(question);

        entityManager.getTransaction().commit();
        entityManager.close();
    }*/
    public void save(Question question) {
        question.setCreatedAt(LocalDateTime.now());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Collection<Quiz> quizzes = question.getQuizzes();
        Collection<Quiz> managedQuizzes = new ArrayList<>();

        for (Quiz quiz : quizzes) {
            if (quiz.getId() != 0) { // Existing Quiz
                quiz = entityManager.merge(quiz);
            } else {
                entityManager.persist(quiz);
            }
            managedQuizzes.add(quiz);
        }

        question.setQuizzes(managedQuizzes);
        entityManager.persist(question);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Question findQuestionById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Question question = entityManager.find(Question.class, id);

        // Initialize the reponses collection
        question.getReponses().size();

        entityManager.close();
        return question;
    }

    public void update(Question question) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(question);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

  /*  public void delete(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Question question = entityManager.find(Question.class, id);
        entityManager.remove(question);
        entityManager.getTransaction().commit();
        entityManager.close();
    }*/
  public void delete(int id) {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      Question question = entityManager.find(Question.class, id);

      // Remove the relationship between Question and Quiz
      for (Quiz quiz : question.getQuizzes()) {
          quiz.getQuestions().remove(question);
      }
      question.setQuizzes(new ArrayList<>());

      entityManager.merge(question);
      entityManager.remove(question);
      entityManager.getTransaction().commit();
      entityManager.close();
  }

    public void deleteReponse(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Reponse reponse = entityManager.find(Reponse.class, id);
        entityManager.remove(reponse);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
