package menu.menus.domain.entities;

import java.util.List;

/**
 * Entité représentant un menu
 * POJO pur - AUCUNE dépendance framework
 */
public class Menu {

    private int id;
    private String nom;
    private List<Integer> platsIds;
    private String auteur;
    private String dateCreation;
    private String dateModification;

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
     * @param dateModification date de modification
     */
    public Menu(int id, String nom, List<Integer> platsIds, String auteur, String dateCreation, String dateModification) {
        this.id = id;
        this.nom = nom;
        this.platsIds = platsIds;
        this.auteur = auteur;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Integer> getPlatsIds() {
        return platsIds;
    }

    public void setPlatsIds(List<Integer> platsIds) {
        this.platsIds = platsIds;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDateModification() {
        return dateModification;
    }

    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", platsIds=" + platsIds +
                ", auteur='" + auteur + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", dateModification='" + dateModification + '\'' +
                '}';
    }
}
