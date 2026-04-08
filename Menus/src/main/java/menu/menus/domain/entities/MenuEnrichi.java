package menu.menus.domain.entities;

import java.util.List;

/**
 * Entité représentant un menu enrichi avec les informations complètes des plats
 * POJO pur - AUCUNE dépendance framework
 */
public class MenuEnrichi {

    private int id;
    private String nom;
    private List<Plat> plats;
    private int createurId;
    private String createurNom;
    private String dateCreation;
    private String dateModification;
    private double prixTotal;

    /**
     * Constructeur par défaut
     */
    public MenuEnrichi() {
    }

    /**
     * Constructeur de menu enrichi
     * @param id identifiant du menu
     * @param nom nom du menu
     * @param plats liste des plats avec détails
     * @param createurId id du créateur
     * @param createurNom nom du créateur
     * @param dateCreation date de création
     * @param dateModification date de modification
     * @param prixTotal prix total du menu
     */
    public MenuEnrichi(int id, String nom, List<Plat> plats, int createurId, String createurNom,
                       String dateCreation, String dateModification, double prixTotal) {
        this.id = id;
        this.nom = nom;
        this.plats = plats;
        this.createurId = createurId;
        this.createurNom = createurNom;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.prixTotal = prixTotal;
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

    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }

    public int getCreateurId() {
        return createurId;
    }

    public void setCreateurId(int createurId) {
        this.createurId = createurId;
    }

    public String getCreateurNom() {
        return createurNom;
    }

    public void setCreateurNom(String createurNom) {
        this.createurNom = createurNom;
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

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    @Override
    public String toString() {
        return "MenuEnrichi{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", plats=" + plats +
                ", createurId=" + createurId +
                ", createurNom='" + createurNom + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", dateModification='" + dateModification + '\'' +
                ", prixTotal=" + prixTotal +
                '}';
    }
}
