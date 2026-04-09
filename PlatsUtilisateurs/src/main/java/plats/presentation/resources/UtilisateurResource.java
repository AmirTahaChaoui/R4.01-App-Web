package plats.presentation.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import plats.domain.entities.Utilisateur;
import plats.application.services.UtilisateurService;

import java.util.List;

@Path("/utilisateurs")
public class UtilisateurResource {

    @Inject
    private UtilisateurService utilisateurService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Utilisateur> getAll() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);
        if (utilisateur != null) {
            return Response.ok(utilisateur).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Utilisateur utilisateur) {
        utilisateurService.addUtilisateur(utilisateur);
        return Response.status(Response.Status.CREATED).entity(utilisateur).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Utilisateur utilisateurUpdate) {
        utilisateurUpdate.setId(id);
        Utilisateur utilisateur = utilisateurService.updateUtilisateur(utilisateurUpdate);
        if (utilisateur != null) {
            return Response.ok(utilisateur).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (utilisateurService.deleteUtilisateur(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
