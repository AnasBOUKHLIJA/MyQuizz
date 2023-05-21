package com.myquiz.myquizz.metier;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity

public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String lenonce;
    @Column
    private String note_question;
    @Column(columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
    private String justification;
    @ManyToOne(fetch = FetchType.EAGER)
    private Poseur poseur;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Collection<Reponse> reponses = new ArrayList<>();
    @ManyToMany(mappedBy = "questions", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Quiz> quizzes = new ArrayList<>();
    public Question(int id, String lenonce, String note_question, String justification, LocalDateTime createdAt) {
        this.id = id;
        this.lenonce = lenonce;
        this.note_question = note_question;
        this.justification = justification;
        this.createdAt = createdAt;
    }

    public Question() {}

    public Poseur getPoseur() {
        return poseur;
    }

    public void setPoseur(Poseur poseur) {
        this.poseur = poseur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLenonce() {
        return lenonce;
    }

    public void setLenonce(String lenonce) {
        this.lenonce = lenonce;
    }

    public String getNote_question() {
        return note_question;
    }

    public void setNote_question(String note_question) {
        this.note_question = note_question;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Collection<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(Collection<Reponse> reponses) {
        this.reponses = reponses;
    }

    public Collection<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(Collection<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
