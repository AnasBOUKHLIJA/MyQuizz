package com.myquiz.myquizz.web;

import com.myquiz.myquizz.metier.Apprenant;
import com.myquiz.myquizz.metier.Poseur;
import com.myquiz.myquizz.metier.Utilisateur;
import com.myquiz.myquizz.repository.ApprenantRepository;
import com.myquiz.myquizz.repository.PoseurRepository;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean  implements Serializable {
    public Utilisateur utilisateur;
    private PoseurRepository poseurRepository;
    private ApprenantRepository apprenantRepository;

    private String type;

    public LoginBean(){
        utilisateur = new Utilisateur();
        poseurRepository = new PoseurRepository();
        apprenantRepository = new ApprenantRepository();
    }
    public void connexion() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if(utilisateur.getLogin() != null && utilisateur.getPassword() != null){
            Poseur poseur = poseurRepository.getByLoginAndPassword(utilisateur.getLogin(),utilisateur.getPassword());
            Apprenant apprenant = apprenantRepository.getByLoginAndPassword(utilisateur.getLogin(),utilisateur.getPassword());
            if(poseur != null){
                this.utilisateur = poseur;
                context.getExternalContext().getSessionMap().put("utilisateur", utilisateur);
                context.getExternalContext().getSessionMap().put("type", "pos");
                context.getExternalContext().redirect("quizzes.xhtml");
            }else if(apprenant != null){
                this.utilisateur = apprenant;
                context.getExternalContext().getSessionMap().put("utilisateur", utilisateur);
                context.getExternalContext().getSessionMap().put("type", "app");
                context.getExternalContext().redirect("workspace.xhtml");
            }
        }
    }
    public void deconnexion(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        };
    }
    public void enregistre(){
        if(type.equals("pos")) {
            Poseur poseur = new Poseur();
            poseur.setNom(utilisateur.getNom());
            poseur.setPrenom(utilisateur.getPrenom());
            poseur.setLogin(utilisateur.getLogin());
            poseur.setPassword(utilisateur.getPassword());
            poseurRepository.save(poseur);
        } else if(type.equals("app")) {
            Apprenant apprenant = new Apprenant();
            apprenant.setNom(utilisateur.getNom());
            apprenant.setPrenom(utilisateur.getPrenom());
            apprenant.setLogin(utilisateur.getLogin());
            apprenant.setPassword(utilisateur.getPassword());
            apprenantRepository.save(apprenant);
        }
    }

    public void checkLogin() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        Utilisateur utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("utilisateur");
        if (utilisateur == null || utilisateur.getId() == 0) {
            context.getExternalContext().redirect("connexion.xhtml");
        }
    }

    public boolean isPoseur() {
        FacesContext context = FacesContext.getCurrentInstance();
        String userType = (String) context.getExternalContext().getSessionMap().get("type");
        return "pos".equals(userType);
    }

    public boolean isApparenant() {
        FacesContext context = FacesContext.getCurrentInstance();
        String userType = (String) context.getExternalContext().getSessionMap().get("type");
        return "app".equals(userType);
    }

    // getter and setter methods
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
