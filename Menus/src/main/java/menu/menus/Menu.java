package fr.univamu.iut.menu;

import java.util.List;

/**
 * Classe représentant un menu
 */
public class Menu {

    /**
     * Identifiant du menu
     */
    protected int id;

    /**
     * Nom du menu
     */
    protected String nom;

    /**
     * Liste des identifiants des plats composant le menu
     */
    protected List<Integer> platsIds;

    /**
     * Nom de la personne ayant créé le menu
     */
    protected String auteur;

    /**
     * Date de création ou de mise à jour du menu
     */
    protected String dateCreation;

    /**
     * Constructeur par défaut
     */
    public Menu() {
    }

    /**
     * Constructeur de menu
     * @param id identifiant du menu
     * @param nom nom du menu
     * @param platsIds liste des ids des plats
     * @param auteur créateur du menu
     * @param dateCreation date de création
     */
    public Menu(int id, String nom, List<Integer> platsIds, String auteur, String dateCreation) {
        this.id = id;
        this.nom = nom;
        this.platsIds = platsIds;
        this.auteur = auteur;
        this.dateCreation = dateCreation;
    }

    /**
     * Accéder à l'id du menu
     */
    public int getId() {
        return id;
    }

    /**
     * Accéder au nom du menu
     */
    public String getNom() {
        return nom;
    }

    /**
     * Accéder aux ids des plats
     */
    public List<Integer> getPlatsIds() {
        return platsIds;
    }

    /**
     * Accéder à l'auteur
     */
    public String getAuteur() {
        return auteur;
    }

    /**
     * Accéder à la date de création
     */
    public String getDateCreation() {
        return dateCreation;
    }

    /**
     * Modifier l'id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Modifier le nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Modifier la liste des plats
     */
    public void setPlatsIds(List<Integer> platsIds) {
        this.platsIds = platsIds;
    }

    /**
     * Modifier l'auteur
     */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
     * Modifier la date de création
     */
    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", platsIds=" + platsIds +
                ", auteur='" + auteur + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                '}';
    }
}