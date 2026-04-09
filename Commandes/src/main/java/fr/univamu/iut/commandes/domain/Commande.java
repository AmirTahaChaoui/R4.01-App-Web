package fr.univamu.iut.commandes.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String adresseLivraison;
    protected String dateLivraison;
    protected Double prixTotal;
    @ElementCollection
    protected List<Long> menusIds;

    public Commande() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdresseLivraison() { return adresseLivraison; }
    public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }

    public String getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(String dateLivraison) { this.dateLivraison = dateLivraison; }

    public Double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(Double prixTotal) { this.prixTotal = prixTotal; }

    public List<Long> getMenusIds() { return menusIds; }
    public void setMenusIds(List<Long> menusIds) { this.menusIds = menusIds; }
}
