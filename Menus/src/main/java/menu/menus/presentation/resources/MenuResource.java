package menu.menus.presentation.resources;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

import menu.menus.application.services.MenuService;
import menu.menus.domain.entities.Menu;
import menu.menus.domain.entities.MenuEnrichi;
import menu.menus.domain.repositories.MenuRepositoryInterface;
import menu.menus.domain.repositories.PlatsRepositoryInterface;

import java.util.ArrayList;

/**
 * Ressource REST pour l'API des menus
 * Couche Présentation - expose les endpoints HTTP
 */
@Path("/menus")
@ApplicationScoped
public class MenuResource {

    private MenuService service;

    /**
     * Constructeur par défaut (requis par CDI)
     */
    public MenuResource() {
    }

    /**
     * Constructeur avec injection des dépendances
     * @param menuRepo repository des menus
     * @param platsRepo repository des plats
     */
    @Inject
    public MenuResource(MenuRepositoryInterface menuRepo, PlatsRepositoryInterface platsRepo) {
        this.service = new MenuService(menuRepo, platsRepo);
    }

    /**
     * GET /api/menus
     * Récupère tous les menus enrichis (avec détails des plats)
     * @return liste de tous les menus au format JSON
     */
    @GET
    @Produces("application/json")
    public String getAllMenus() {
        try {
            ArrayList<MenuEnrichi> allMenus = service.getAllMenusEnrichis();

            try (Jsonb jsonb = JsonbBuilder.create()) {
                return jsonb.toJson(allMenus);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des menus: " + e.getMessage());
            throw new InternalServerErrorException("Erreur serveur");
        }
    }

    /**
     * GET /api/menus/{id}
     * Récupère un menu spécifique enrichi
     * @param id identifiant du menu
     * @return menu enrichi au format JSON
     */
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getMenu(@PathParam("id") int id) {
        MenuEnrichi menu = service.getMenuEnrichi(id);

        if (menu == null) {
            throw new NotFoundException("Menu non trouvé");
        }

        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(menu);
        } catch (Exception e) {
            System.err.println("Erreur lors de la sérialisation du menu: " + e.getMessage());
            throw new InternalServerErrorException("Erreur serveur");
        }
    }

    /**
     * POST /api/menus
     * Crée un nouveau menu
     * @param menu menu à créer (au format JSON)
     * @return réponse avec le menu créé et ses détails complets (statut 201)
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createMenu(Menu menu) {
        try {
            if (menu == null || menu.getNom() == null || menu.getNom().isEmpty()) {
                return Response.status(400).entity("Données invalides: le nom est requis").build();
            }

            if (service.addMenu(menu)) {
                // Récupérer le menu créé avec tous les détails
                ArrayList<Menu> allMenus = new ArrayList<>();
                // Pour obtenir l'ID, il faudrait le retourner du service
                // Pour l'instant on retourne 201 avec un message
                try (Jsonb jsonb = JsonbBuilder.create()) {
                    return Response.status(201)
                            .entity(jsonb.toJson(menu))
                            .build();
                }
            } else {
                return Response.status(400).entity("Erreur lors de la création du menu: plats invalides").build();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du menu: " + e.getMessage());
            return Response.status(500).entity("Erreur serveur").build();
        }
    }

    /**
     * PUT /api/menus/{id}
     * Met à jour un menu existant
     * @param id identifiant du menu à mettre à jour
     * @param menu nouvelles données du menu
     * @return réponse avec le menu mis à jour et ses détails complets enrichis
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateMenu(@PathParam("id") int id, Menu menu) {
        try {
            if (menu == null || menu.getNom() == null || menu.getNom().isEmpty()) {
                return Response.status(400).entity("Données invalides: le nom est requis").build();
            }

            if (service.updateMenu(id, menu)) {
                // Récupérer le menu mis à jour avec tous les détails enrichis
                MenuEnrichi menuUpdated = service.getMenuEnrichi(id);
                try (Jsonb jsonb = JsonbBuilder.create()) {
                    return Response.ok()
                            .entity(jsonb.toJson(menuUpdated))
                            .build();
                }
            } else {
                return Response.status(404).entity("Menu non trouvé ou plats invalides").build();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du menu: " + e.getMessage());
            return Response.status(500).entity("Erreur serveur").build();
        }
    }

    /**
     * DELETE /api/menus/{id}
     * Supprime un menu
     * @param id identifiant du menu à supprimer
     * @return réponse de succès ou erreur
     */
    @DELETE
    @Path("{id}")
    public Response deleteMenu(@PathParam("id") int id) {
        try {
            if (service.deleteMenu(id)) {
                return Response.ok("Menu supprimé avec succès").build();
            } else {
                return Response.status(404).entity("Menu non trouvé").build();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du menu: " + e.getMessage());
            return Response.status(500).entity("Erreur serveur").build();
        }
    }
}
