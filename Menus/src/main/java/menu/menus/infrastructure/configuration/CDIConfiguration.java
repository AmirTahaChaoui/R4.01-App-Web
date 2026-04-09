package menu.menus.infrastructure.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import menu.menus.domain.repositories.MenuRepositoryInterface;
import menu.menus.domain.repositories.PlatsRepositoryInterface;
import menu.menus.infrastructure.repositories.MenuRepositoryMariadb;
import menu.menus.infrastructure.repositories.PlatsRepositoryAPI;

/**
 * Configuration CDI pour l'injection des dépendances
 * Gère la création et la libération des ressources
 */
@ApplicationPath("/api")
@ApplicationScoped
public class CDIConfiguration extends Application {

    /**
     * Produit l'implémentation du repository des menus (base de données)
     * configurée comme bean CDI injectable
     * @return implémentation du MenuRepositoryInterface
     */
    @Produces
    public MenuRepositoryInterface produceMenuRepository() {
        try {
            return new MenuRepositoryMariadb(
                    "jd::b/c:mariadb://mysql-dashmeenusd-site.alwaysdata.netme",
                    "dashmed-site_labubu",
                    "DreamTeam1"
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du repository MenuRepository: " + e.getMessage());
            throw new RuntimeException("Impossible de se connecter à la base de données", e);
        }
    }

    /**
     * Ferme la connexion au repository des menus
     * @param menuRepo repository à fermer
     */
    public void disposeMenuRepository(@Disposes MenuRepositoryInterface menuRepo) {
        menuRepo.close();
    }

    /**
     * Produit l'implémentation du repository des plats (API)
     * configurée comme bean CDI injectable
     * @return implémentation du PlatsRepositoryInterface
     */
    @Produces
    public PlatsRepositoryInterface producePlatsRepository() {
        return new PlatsRepositoryAPI("http://localhost:8080/api");
    }

    /**
     * Ferme la connexion au repository des plats
     * @param platsRepo repository à fermer
     */
    public void disposePlatsRepository(@Disposes PlatsRepositoryInterface platsRepo) {
        platsRepo.close();
    }
}
