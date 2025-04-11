package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.util.*;

@Path("/change")
public class ChangeResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    public ChangeResource() {}

    @POST
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordData data) {

        if(data.user == null || data.old_password == null || data.new_password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a user, old password and new password.")
                    .build();
        }

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        String old_password_hashed = DigestUtils.sha512Hex(data.old_password);
        String new_password_hashed = DigestUtils.sha512Hex(data.new_password);

        if(!user.getString("user_pwd").equals(old_password_hashed))
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Old password does not match.")
                    .build();

        if(user.getString("user_pwd").equals(new_password_hashed))
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please provide a new password different from the old password.")
                    .build();

        Entity updatedUser = Entity.newBuilder(user)
                .set("user_pwd", new_password_hashed)
                .build();

        datastore.update(updatedUser);

        return Response.ok().entity("Password changed successfully.").build();
    }

    @POST
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeRole(ChangeRoleData data) {
        return null;
    }

    @POST
    @Path("/attributes")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeAttributes(ChangeAttributesData data) {
        return null;
    }

    @POST
    @Path("/state")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeState(ChangeStateData data) {
        return null;
    }
}
