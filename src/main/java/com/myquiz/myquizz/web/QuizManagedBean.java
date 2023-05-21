package com.myquiz.myquizz.web;

import com.myquiz.myquizz.metier.*;
import com.myquiz.myquizz.repository.PoseurRepository;
import com.myquiz.myquizz.repository.QuestionRepository;
import com.myquiz.myquizz.repository.QuizRepository;

import com.myquiz.myquizz.repository.ReponseRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ManagedProperty;
import jakarta.faces.bean.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean(name = "quizManagedBean")
@SessionScoped
public class QuizManagedBean implements Serializable {
    private QuizRepository quizRepository;
    private QuestionRepository questionRepository;
    private List<Question> questions;
    private List<Quiz> quizzes;
    private Quiz quiz;
    private Quiz selectedQuiz;
    private Poseur poseur;
    @ManagedProperty("#{param.id}")
    private int id;
    private int score;
    private Map<Question, Reponse> quizMap;
    public QuizManagedBean() {
        Utilisateur utilisateur = (Utilisateur) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("utilisateur");
        quizRepository = new QuizRepository();
        questionRepository = new QuestionRepository();
        quiz = new Quiz();
        selectedQuiz = new Quiz();
        quizMap = new HashMap<>();
        questions = questionRepository.findAllByPoseur(utilisateur.getId());
        // Initialize the reponses for each question
        for (int i = 0; i < questions.size(); i++) {
            Question questionWithReponses = questionRepository.findWithReponses(questions.get(i).getId());
            questions.set(i, questionWithReponses);
        }
        quizzes = quizRepository.findAllByPoseur(utilisateur.getId());
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quizWithQuestions = quizRepository.findWithQuestions(quizzes.get(i).getId());
            quizzes.set(i, quizWithQuestions);
        }
        poseur = new Poseur();
        poseur.setLogin(utilisateur.getLogin());
        poseur.setPassword(utilisateur.getPassword());
        poseur.setNom(utilisateur.getNom());
        poseur.setPrenom(utilisateur.getPrenom());
        poseur.setId(utilisateur.getId());
    }
    public List<Quiz> getAll(){
        return quizRepository.findAll();
    }

    public void createQuiz() {
        this.quiz.setPoseur(poseur);
        quizRepository.save(this.quiz);
        quizzes.add(0, this.quiz);
        this.quiz = new Quiz();
        resetQuizQuestions();
    }
    public void updateQuiz() {
        quizRepository.update(quiz);
        quiz = new Quiz();
        resetQuizQuestions();
    }
    public void fillQuiz(Quiz quiz) {
        setQuiz(quiz);
        this.quiz.setQuestions(quiz.getQuestions());
    }
    public void resetQuizQuestions() {
        quiz.setQuestions(new ArrayList<>());
    }
    public void resetQuiz() {
        setQuiz(new Quiz());
    }
    public void deleteQuiz(Quiz quiz) {
        quizRepository.delete(quiz.getId());
        quizzes.remove(quiz);
        this.quiz = new Quiz();
    }
    public void addQuestionToQuiz(Question question) {
        if (!this.quiz.getQuestions().contains(question)) {
            this.quiz.getQuestions().add(question);
            question.getQuizzes().add(this.quiz);
        }
    }
    public void deleteQuestionToQuiz(Question question) {
        this.quiz.getQuestions().remove(question);
        question.getQuizzes().remove(this.quiz);
    }
    public boolean canIAddThisQuestion(Question question){
        return !quiz.getQuestions().contains(question);
    }
    public boolean canIDeleteThisQuestion(Question question){
        return quiz.getQuestions().contains(question);
    }

    // Getters and setters
    public QuizRepository getQuizRepository() {
        return quizRepository;
    }
    public void setQuizRepository(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }
    public QuestionRepository getQuestionRepository() {
        return questionRepository;
    }
    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }
    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
    public List<Quiz> getQuizzes() {
        quizzes = quizRepository.findAllByPoseur(poseur.getId());
        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quizWithQuestions = quizRepository.findWithQuestions(quizzes.get(i).getId());
            quizzes.set(i, quizWithQuestions);
        }
        return quizzes;
    }
    public Quiz getQuiz() {
        return quiz;
    }
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
    public Poseur getPoseur() {
        return poseur;
    }
    public void setPoseur(Poseur poseur) {
        this.poseur = poseur;
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    public Quiz getSelectedQuiz() throws IOException {
        Quiz quiz1 = quizRepository.findWithQuestions(this.id);
        if (quiz1 == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("workspace.xhtml");
            return null;
        }

        List<Question> selectedQuestions = new ArrayList<>(quiz1.getQuestions());
        for (int i = 0; i < selectedQuestions.size(); i++) {
            Question questionWithResponses = questionRepository.findWithReponses(selectedQuestions.get(i).getId());
            selectedQuestions.set(i, questionWithResponses);
        }
        quiz1.setQuestions(selectedQuestions);

        return quiz1;
    }
    public void setSelectedQuiz(Quiz setSelectedQuiz) {
        this.selectedQuiz = setSelectedQuiz;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Map<Question, Reponse> getQuizMap() {
        return quizMap;
    }
    public List<Map.Entry<Question, Reponse>> getMapEntries() {
        return new ArrayList<>(quizMap.entrySet());
    }

    public void setQuizMap(Map<Question, Reponse> quizMap) {
        this.quizMap = quizMap;
    }

    public void addQuestionReponse(Question question, Reponse reponse) {
        quizMap.put(question, reponse);
    }

    public void deleteQuestion(Question question) {
        quizMap.remove(question);
    }

    public boolean isQuestionReponseExists(Question question, Reponse reponse) {
        return quizMap.containsKey(question) && quizMap.get(question).equals(reponse);
    }

    public Reponse getReponseForQuestion(Question question) {
        return quizMap.get(question);
    }

    public boolean isQuizMapEmpty() {
        return quizMap.isEmpty();
    }

    public int getScore() {
        return quizMap.entrySet().stream()
                .filter(entry -> entry.getValue().isEtat())
                .mapToInt(entry -> Integer.parseInt(entry.getKey().getNote_question()))
                .sum();
    }
    public int getScorePossible() {
        return quizMap.entrySet().stream()
                .mapToInt(entry -> Integer.parseInt(entry.getKey().getNote_question()))
                .sum();
    }
    public void setScore(int score) {
        this.score = score;
    }

    public void returnToQuiz() throws IOException {
        if(selectedQuiz != null && id > 0){
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("quiz.xhtml");
        }else{
            viewAllQuizzes();
        }
    }

    public void repeatQuiz() throws IOException {
        quizMap = new HashMap<>();
        this.returnToQuiz();
    }

    public void viewAllQuizzes() throws IOException {
        quizMap = new HashMap<>();
        selectedQuiz = new Quiz();
        id = 0;
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().redirect("workspace.xhtml");
    }
}
