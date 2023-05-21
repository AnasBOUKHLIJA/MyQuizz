package com.myquiz.myquizz.metier;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
@DiscriminatorValue("app")
public class Apprenant  extends Utilisateur implements Serializable {
    public Apprenant(int id, String nom, String prenom, String login, String password) {
        super(id, nom, prenom, login, password);
    }

    public Apprenant() {
    }

}
