package menu.menus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import menu.menus.domain.repositories.MenuRepositoryInterface;
import menu.menus.infrastructure.repositories.MenuRepositoryMariadb;

/**
 * Point d'entrée pour l'application REST
 * Définit le chemin de base pour tous les endpoints API (/api)
 */
@ApplicationPath("/api")
@ApplicationScoped
public class MenuApplication extends Application {

    /**
     * Méthode appelée par l'API CDI pour injecter la connexion à la base de données
     * @return un objet implémentant l'interface MenuRepositoryInterface
     */
    @Produces
    public MenuRepositoryInterface openDbConnection() {
        MenuRepositoryMariadb db = null;

        try {
            db = new MenuRepositoryMariadb(
                    "jd::b/c:mariadb://mysql-dashmeenusd-site.alwaysdata.net/menus",
                    "dashmed-site_labubu",
                    "DreamTeam1"
            );
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return db;
    }

    /**
     * Méthode permettant de fermer la connexion à la base de données
     * @param menuRepo la connexion à fermer
     */
    public void closeDbConnection(@Disposes MenuRepositoryInterface menuRepo) {
        menuRepo.close();
    }
}
