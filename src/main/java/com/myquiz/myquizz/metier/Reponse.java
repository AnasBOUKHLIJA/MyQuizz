package com.myquiz.myquizz.metier;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Reponse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String enonce;
    @Column
    private boolean etat;
    @ManyToOne
    private Question question;
    public Reponse(int id, String enonce, boolean etat) {
        this.id = id;
        this.enonce = enonce;
        this.etat = etat;
    }
    public Reponse() { }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", enonce='" + enonce + '\'' +
                ", etat=" + etat +
                '}';
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reponse reponse = (Reponse) o;
        return id == reponse.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
