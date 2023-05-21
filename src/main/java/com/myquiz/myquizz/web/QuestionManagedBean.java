package com.myquiz.myquizz.web;

import com.myquiz.myquizz.metier.Poseur;
import com.myquiz.myquizz.metier.Question;
import com.myquiz.myquizz.metier.Reponse;
import com.myquiz.myquizz.metier.Utilisateur;
import com.myquiz.myquizz.repository.QuestionRepository;

import java.io.Serializable;
import java.util.List;

import com.myquiz.myquizz.repository.ReponseRepository;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ManagedProperty;
import jakarta.faces.bean.SessionScoped;
import jakarta.faces.context.FacesContext;


@ManagedBean(name = "questionManagedBean")
@SessionScoped
public class QuestionManagedBean implements Serializable {
    private QuestionRepository questionRepository;
    private List<Question> questions;
    private Question question;
    private Reponse reponse;
    private Poseur poseur;

    public QuestionManagedBean() {
        Utilisateur utilisateur = (Utilisateur) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("utilisateur");
        questionRepository = new QuestionRepository();
        questions = questionRepository.findAllByPoseur(utilisateur.getId());
        // Initialize the reponses for each question
        for (int i = 0; i < questions.size(); i++) {
            Question questionWithReponses = questionRepository.findWithReponses(questions.get(i).getId());
            questions.set(i, questionWithReponses);
        }
        question = new Question();
        reponse = new Reponse();
        poseur = new Poseur();
        poseur.setLogin(utilisateur.getLogin());
        poseur.setPassword(utilisateur.getPassword());
        poseur.setNom(utilisateur.getNom());
        poseur.setPrenom(utilisateur.getPrenom());
        poseur.setId(utilisateur.getId());
    }
    public void fillQuestion(Question question) {
        Question fetchedQuestion = questionRepository.findQuestionById(question.getId());
        setQuestion(fetchedQuestion);
    }

    public void resetQuestion() {
        setQuestion(new Question());
    }
    public void createQuestion() {
        if(poseur != null){
            question.setPoseur(poseur);
            questionRepository.save(question);
            questions.add(0, question);
            resetQuestion();
        }
    }
    public void updateQuestion() {
        questionRepository.update(question);
        resetQuestion();
    }
    public void deleteQuestion(Question question) {
        questionRepository.delete(question.getId());
        questions.remove(question);
        resetQuestion();
    }
    public void addReponseToQuestion() {
        reponse.setQuestion(question);
        question.getReponses().add(reponse);
        questionRepository.saveReponseWithQuestion(question, reponse);
        reponse = new Reponse();
    }
    public void deleteReponse(Reponse reponse) {
        question.getReponses().remove(reponse);
        questionRepository.deleteReponse(reponse.getId());
        fillQuestion(question);
    }

    // Getters and setters

    public Poseur getPoseur() {
        return poseur;
    }

    public void setPoseur(Poseur poseur) {
        this.poseur = poseur;
    }

    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }

    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
    }
}
