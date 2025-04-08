package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@Path("/logout")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LogoutResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory tokenKeyFactory = datastore.newKeyFactory().setKind("UserToken");

    public LogoutResource() {}

    @POST
    @Path("/")
    public Response logout(@Context HttpHeaders headers) {
        String tokenID = headers.getHeaderString("Authorization");

        if (tokenID == null || tokenID.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing Authorization token.")
                    .build();
        }

        Key tokenKey = tokenKeyFactory.newKey(tokenID);
        Entity token = datastore.get(tokenKey);

        if (token == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Token not found or already revoked.")
                    .build();
        }

        datastore.delete(tokenKey);
        return Response.ok().entity("Logout successful. Token revoked.").build();
    }
}
