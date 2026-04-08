package menu.menus;

import java.util.*;

/**
 * Interface d'accès aux données des menus
 */
public interface MenuRepositoryInterface {

    /**
     * Méthode fermant le dépôt où sont stockées les informations sur les menus
     */
    public void close();

    /**
     * Méthode retournant le menu dont l'id est passé en paramètre
     * @param id identifiant du menu recherché
     * @return un objet Menu représentant le menu recherché
     */
    public Menu getMenu(int id);

    /**
     * Méthode retournant la liste des menus
     * @return une liste d'objets Menu
     */
    public ArrayList<Menu> getAllMenus();

    /**
     * Méthode permettant d'ajouter un menu
     * @param menu menu à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addMenu(Menu menu);

    /**
     * Méthode permettant de mettre à jour un menu
     * @param id identifiant du menu à modifier
     * @param nom nouveau nom
     * @param platsIds nouvelle liste de plats
     * @param auteur auteur du menu
     * @param dateCreation date de mise à jour
     * @return true si le menu existe et la mise à jour a été faite, false sinon
     */
    public boolean updateMenu(int id, String nom, List<Integer> platsIds, String auteur, String dateCreation);

    /**
     * Méthode permettant de supprimer un menu
     * @param id identifiant du menu à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteMenu(int id);
}