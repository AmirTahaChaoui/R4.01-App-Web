package menu.menus.application.services;

import menu.menus.domain.entities.Menu;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface définissant le contrat pour accéder aux données des menus
 * Couche Application - Contrat pour les implémentations du repository
 */
public interface MenuRepositoryInterface {

    /**
     * Récupère un menu par son id
     * @param id identifiant du menu
     * @return le menu trouvé ou null
     */
    Menu getMenu(int id);

    /**
     * Récupère tous les menus
     * @return liste de tous les menus
     */
    ArrayList<Menu> getAllMenus();

    /**
     * Ajoute un nouveau menu
     * @param menu le menu à ajouter
     * @return l'ID du menu créé, ou -1 si l'ajout a échoué
     */
    int addMenu(Menu menu);

    /**
     * Supprime un menu
     * @param id identifiant du menu à supprimer
     * @return true si suppression réussie
     */
    boolean deleteMenu(int id);

    /**
     * Met à jour un menu
     * @param id identifiant du menu à mettre à jour
     * @param nom nouveau nom
     * @param platsIds nouvelle liste de plats
     * @param auteur auteur
     * @param dateModification date de modification
     * @return true si mise à jour réussie
     */
    boolean updateMenu(int id, String nom, List<Integer> platsIds, String auteur, String dateModification);

    /**
     * Ferme la connexion au repository
     */
    void close();
}
