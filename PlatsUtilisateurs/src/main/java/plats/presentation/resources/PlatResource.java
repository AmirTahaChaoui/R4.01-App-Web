package plats.presentation.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import plats.domain.entities.Plat;
import plats.application.services.PlatService;

import java.util.List;

@Path("/plats")
public class PlatResource {

    @Inject
    private PlatService platService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Plat> getAll() {
        return platService.getAllPlats();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        Plat plat = platService.getPlatById(id);
        if (plat != null) {
            return Response.ok(plat).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Plat plat) {
        platService.addPlat(plat);
        return Response.status(Response.Status.CREATED).entity(plat).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Plat platUpdate) {
        platUpdate.setId(id);
        Plat plat = platService.updatePlat(platUpdate);
        if (plat != null) {
            return Response.ok(plat).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (platService.deletePlat(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
