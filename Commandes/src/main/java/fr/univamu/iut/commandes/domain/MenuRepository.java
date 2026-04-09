package fr.univamu.iut.commandes.domain;

public interface MenuRepository {
    void close();
    Menu getMenu(Long id);
}
