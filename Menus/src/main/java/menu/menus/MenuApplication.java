package menu.menus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import menu.menus.domain.repositories.MenuRepositoryInterface;
import menu.menus.domain.repositories.PlatsRepositoryInterface;
import menu.menus.infrastructure.repositories.MenuRepositoryMariadb;
import menu.menus.infrastructure.repositories.PlatsRepositoryJSON;

/**
 * Point d'entrée pour l'application REST Jakarta EE
 * Configure :
 * - Le chemin de base pour tous les endpoints API (/api)
 * - La production des beans CDI pour l'injection des dépendances
 * - La gestion du cycle de vie des ressources (repositories)
 */
@ApplicationPath("/api")
@ApplicationScoped
public class MenuApplication extends Application {

    /**
     * Produit l'implémentation du repository des menus (base de données MariaDB)
     * configurée comme bean CDI injectable
     * @return instance de MenuRepositoryInterface
     */
    @Produces
    public MenuRepositoryInterface produceMenuRepository() {
        try {
            return new MenuRepositoryMariadb(
                    "jdbc:mariadb://mysql-dashmed-site.alwaysdata.net:3306/dashmed-site_r401-app-web",
                    "dashmed-site_labubu",
                    "DreamTeam1"
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du repository MenuRepository: " + e.getMessage());
            throw new RuntimeException("Impossible de se connecter à la base de données", e);
        }
    }

    /**
     * Dispose (ferme) la connexion au repository des menus
     * Appelé par CDI quand le bean est détruit
     * @param menuRepo repository à fermer
     */
    public void disposeMenuRepository(@Disposes MenuRepositoryInterface menuRepo) {
        menuRepo.close();
    }

    /**
     * Produit l'implémentation du repository des plats (fichier JSON)
     * configurée comme bean CDI injectable
     * @return instance de PlatsRepositoryInterface
     */
    @Produces
    public PlatsRepositoryInterface producePlatsRepository() {
        // Chemin vers le fichier JSON au-dessus de la racine du projet Menus
        String filePath = "../plats-utilisateurs.json";
        return new PlatsRepositoryJSON(filePath);
    }

    /**
     * Dispose (ferme) la connexion au repository des plats
     * Appelé par CDI quand le bean est détruit
     * @param platsRepo repository à fermer
     */
    public void disposePlatsRepository(@Disposes PlatsRepositoryInterface platsRepo) {
        platsRepo.close();
    }
}
