package com.myquiz.myquizz.metier;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Quiz implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String libelle;
    @Column
    private String theme;
    @Column
    private String note;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    private Poseur poseur;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Question> questions = new ArrayList<>();

    public Quiz(int id, String libelle, String theme, String note, Poseur poseur, Collection<Question> questions) {
        this.id = id;
        this.libelle = libelle;
        this.theme = theme;
        this.note = note;
        this.poseur = poseur;
        this.questions = questions;
    }

    public Quiz() {
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", theme='" + theme + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public Poseur getPoseur() {
        return poseur;
    }

    public void setPoseur(Poseur poseur) {
        this.poseur = poseur;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
