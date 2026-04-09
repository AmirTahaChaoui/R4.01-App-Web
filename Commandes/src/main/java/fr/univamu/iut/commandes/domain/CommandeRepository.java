package fr.univamu.iut.commandes.domain;

public interface CommandeRepository {
    void close();
    Commande getCommande(Long id);
    boolean addCommande(Commande commande);
}
