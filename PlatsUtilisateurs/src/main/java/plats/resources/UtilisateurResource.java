package plats.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import plats.model.Utilisateur;
import plats.dao.UtilisateurDAO;

import java.util.List;

@Path("/utilisateurs")
public class UtilisateurResource {

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Utilisateur> getAll() {
        return utilisateurDAO.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        Utilisateur utilisateur = utilisateurDAO.getById(id);
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
        utilisateurDAO.create(utilisateur);
        return Response.status(Response.Status.CREATED).entity(utilisateur).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Utilisateur utilisateurUpdate) {
        utilisateurUpdate.setId(id);
        Utilisateur utilisateur = utilisateurDAO.update(utilisateurUpdate);
        if (utilisateur != null) {
            return Response.ok(utilisateur).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (utilisateurDAO.delete(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
