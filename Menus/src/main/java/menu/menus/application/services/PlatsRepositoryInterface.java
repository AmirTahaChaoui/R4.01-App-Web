package menu.menus.application.services;

import menu.menus.domain.entities.Plat;
import java.util.ArrayList;

/**
 * Interface définissant le contrat pour accéder à l'API des plats
 * Couche Application - Contrat pour les implémentations du repository
 */
public interface PlatsRepositoryInterface {

    /**
     * Récupère un plat par son id
     * @param id identifiant du plat
     * @return le plat trouvé ou null
     */
    Plat getPlat(int id);

    /**
     * Récupère tous les plats
     * @return liste de tous les plats
     */
    ArrayList<Plat> getAllPlats();

    /**
     * Ferme la connexion au repository
     */
    void close();
}
