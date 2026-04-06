package plats.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import plats.model.Plat;
import plats.dao.PlatDAO;

import java.util.List;

@Path("/plats")
public class PlatResource {

    private final PlatDAO platDAO = new PlatDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Plat> getAll() {
        return platDAO.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) {
        Plat plat = platDAO.getById(id);
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
        platDAO.create(plat);
        return Response.status(Response.Status.CREATED).entity(plat).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Plat platUpdate) {
        platUpdate.setId(id);
        Plat plat = platDAO.update(platUpdate);
        if (plat != null) {
            return Response.ok(plat).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (platDAO.delete(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
