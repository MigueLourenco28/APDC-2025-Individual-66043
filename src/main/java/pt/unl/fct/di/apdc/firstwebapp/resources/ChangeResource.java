package pt.unl.fct.di.apdc.firstwebapp.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;

@Path("/change")
public class ChangeResource {

    public ChangeResource() {}

    @POST
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(RegisterData data) {
        return null;
    }


}
