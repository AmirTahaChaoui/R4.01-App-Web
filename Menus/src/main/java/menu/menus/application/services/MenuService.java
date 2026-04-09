package menu.menus.application.services;

import menu.menus.domain.entities.Menu;
import menu.menus.domain.entities.MenuEnrichi;
import menu.menus.domain.entities.Plat;
import menu.menus.domain.repositories.MenuRepositoryInterface;
import menu.menus.domain.repositories.PlatsRepositoryInterface;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Service applicatif pour les menus
 * Implémente la logique métier indépendante du framework
 * Utilise l'injection de dépendances des interfaces du domain
 */
public class MenuService {

    private final MenuRepositoryInterface menuRepo;
    private final PlatsRepositoryInterface platsRepo;

    /**
     * Constructeur avec injection des dépendances
     * @param menuRepo dépôt des menus
     * @param platsRepo dépôt des plats
     */
    public MenuService(MenuRepositoryInterface menuRepo, PlatsRepositoryInterface platsRepo) {
        this.menuRepo = menuRepo;
        this.platsRepo = platsRepo;
    }

    /**
     * Enrichit un menu avec les informations complètes des plats
     * @param menu le menu à enrichir
     * @return un MenuEnrichi avec toutes les infos des plats
     */
    private MenuEnrichi enrichirMenu(Menu menu) {
        ArrayList<Plat> platsComplets = new ArrayList<>();
        double prixTotal = 0;

        // Récupérer les informations complètes de chaque plat
        for (Integer platId : menu.getPlatsIds()) {
            Plat plat = platsRepo.getPlat(platId);
            if (plat != null) {
                platsComplets.add(plat);
                prixTotal += plat.getPrix();
            }
        }

        // Extraire l'ID du créateur du nom
        int createurId = extraireCreateurId(menu.getAuteur());
        String createurNom = menu.getAuteur();

        return new MenuEnrichi(
                menu.getId(),
                menu.getNom(),
                platsComplets,
                createurId,
                createurNom,
                menu.getDateCreation(),
                menu.getDateModification(),
                prixTotal
        );
    }

    /**
     * Extrait un ID du créateur (simplifié)
     * @param auteur nom de l'auteur
     * @return un ID basé sur le hash
     */
    private int extraireCreateurId(String auteur) {
        return Math.abs(auteur.hashCode()) % 100;
    }

    /**
     * Valide que tous les plats du menu existent dans la base de données
     * @param platsIds liste des IDs des plats à valider
     * @return true si tous les plats existent, false sinon
     */
    private boolean validerPlats(java.util.List<Integer> platsIds) {
        if (platsIds == null || platsIds.isEmpty()) {
            System.err.println("Erreur : Le menu doit contenir au moins un plat");
            return false;
        }

        for (Integer platId : platsIds) {
            Plat plat = platsRepo.getPlat(platId);
            if (plat == null) {
                System.err.println("Erreur : Le plat avec l'ID " + platId + " n'existe pas");
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne la liste des plats du menu enrichis avec tous les détails
     * @param platsIds liste des IDs des plats
     * @return liste des plats complets
     */
    public ArrayList<Plat> getPlatsEnrichis(java.util.List<Integer> platsIds) {
        ArrayList<Plat> platsComplets = new ArrayList<>();
        
        for (Integer platId : platsIds) {
            Plat plat = platsRepo.getPlat(platId);
            if (plat != null) {
                platsComplets.add(plat);
            }
        }
        
        return platsComplets;
    }

    /**
     * Récupère tous les menus enrichis
     * @return liste de tous les menus avec infos complètes des plats
     */
    public ArrayList<MenuEnrichi> getAllMenusEnrichis() {
        ArrayList<Menu> allMenus = menuRepo.getAllMenus();
        ArrayList<MenuEnrichi> allMenusEnrichis = new ArrayList<>();

        for (Menu menu : allMenus) {
            allMenusEnrichis.add(enrichirMenu(menu));
        }

        return allMenusEnrichis;
    }

    /**
     * Récupère un menu enrichi par id
     * @param id identifiant du menu
     * @return le menu enrichi ou null
     */
    public MenuEnrichi getMenuEnrichi(int id) {
        Menu myMenu = menuRepo.getMenu(id);
        if (myMenu != null) {
            return enrichirMenu(myMenu);
        }
        return null;
    }

    /**
     * Ajoute un nouveau menu après validation des plats
     * @param menu le menu à ajouter
     * @return true si ajout réussi, false si les plats ne sont pas valides
     */
    public boolean addMenu(Menu menu) {
        // Valider que tous les plats existent
        if (!validerPlats(menu.getPlatsIds())) {
            return false;
        }

        // Initialiser la date de création si elle est nulle
        if (menu.getDateCreation() == null) {
            menu.setDateCreation(String.valueOf(LocalDate.now()));
        }
        // Initialiser la date de modification
        menu.setDateModification(String.valueOf(LocalDate.now()));

        return menuRepo.addMenu(menu);
    }

    /**
     * Met à jour un menu après validation des plats
     * @param id id du menu à mettre à jour
     * @param menu les nouvelles informations
     * @return true si mise à jour réussie, false si les plats ne sont pas valides
     */
    public boolean updateMenu(int id, Menu menu) {
        // Valider que tous les plats existent
        if (!validerPlats(menu.getPlatsIds())) {
            return false;
        }

        return menuRepo.updateMenu(
                id,
                menu.getNom(),
                menu.getPlatsIds(),
                menu.getAuteur(),
                String.valueOf(LocalDate.now())
        );
    }

    /**
     * Supprime un menu
     * @param id id du menu à supprimer
     * @return true si suppression réussie
     */
    public boolean deleteMenu(int id) {
        return menuRepo.deleteMenu(id);
    }
}
