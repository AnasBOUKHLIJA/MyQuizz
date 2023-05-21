package com.myquiz.myquizz.metier;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@DiscriminatorValue("pos")
public class Poseur extends Utilisateur implements Serializable {
    @OneToMany(mappedBy = "poseur")
    private Collection<Quiz> quizzes = new ArrayList<>();

    @OneToMany(mappedBy = "poseur")
    private Collection<Question> questions = new ArrayList<>();

    public Poseur() {
        super();
    }

    public Poseur(int id, String nom, String prenom, String login, String password) {
        super(id, nom, prenom, login, password);
    }

    @Override
    public String toString() {
        return "Poseur{" +
                ", id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", login='" + getLogin() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }

    public Collection<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(Collection<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}