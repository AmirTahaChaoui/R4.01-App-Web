package fr.univamu.iut.commandes.domain;

public class Menu {
    protected Long id;
    protected Double prix;

    public Menu() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
}
