package menu.menus.domain.entities;

/**
 * Entité représentant un plat
 * POJO pur - AUCUNE dépendance framework
 */
public class Plat {

    private int id;
    private String nom;
    private String description;
    private double prix;

    /**
     * Constructeur par défaut
     */
    public Plat() {
    }

    /**
     * Constructeur de plat
     * @param id identifiant du plat
     * @param nom nom du plat
     * @param description description du plat
     * @param prix prix du plat
     */
    public Plat(int id, String nom, String description, double prix) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Plat{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                '}';
    }
}
