package fr.univamu.iut.commandes.web;

import fr.univamu.iut.commandes.application.CommandeService;
import fr.univamu.iut.commandes.domain.Commande;
import fr.univamu.iut.commandes.domain.CommandeRepository;
import fr.univamu.iut.commandes.infrastructure.external.MenuRepositoryAPI;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/commandes")
@RequestScoped
public class CommandeResource {

    private CommandeService service;

    public CommandeResource() {}

    @Inject
    public CommandeResource(CommandeRepository commandeRepo) {
        this.service = new CommandeService(
                commandeRepo,
                new MenuRepositoryAPI("http://localhost:3004/")
        );
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response creerCommande(Commande commande) {
        String result = service.creerCommandeJSON(commande);
        if (result == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.CREATED).entity(result).build();
    }
}
