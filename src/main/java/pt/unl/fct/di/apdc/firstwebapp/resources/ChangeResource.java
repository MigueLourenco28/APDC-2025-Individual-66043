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
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeRole(ChangeRoleData data) {

        if(data.changer == null || data.user == null || data.old_role == null || data.new_role == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a changer, user, old role and new role.")
                    .build();
        }

        Key changerKey = userKeyFactory.newKey(data.changer);
        Entity changer = datastore.get(changerKey);

        if(changer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Changer not found.")
                    .build();
        }

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        boolean backoffice = data.changer.equals("backoffice") && !((data.old_role.equals("enduser") && data.new_role.equals("partner")) ||
                                                                    (data.old_role.equals("partner") && data.new_role.equals("enduser")));
        boolean enduser = data.changer.equals("enduser");

        if(backoffice || enduser) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Changer does not have permission to change the user's role.")
                    .build();
        }

        if(!data.old_role.equals(user.getString("user_role"))) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please enter the correct old role.")
                    .build();
        }

        if(data.old_role.equals(data.new_role)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please enter a different new role.")
                    .build();
        }

        Entity updatedUser = Entity.newBuilder(user)
                .set("user_role", data.new_role)
                .build();

        datastore.update(updatedUser);

        return Response.ok().entity("Role changed successfully.").build();
    }

    @POST
    @Path("/state")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeState(ChangeStateData data) {

        if(data.changer == null || data.user == null || data.old_state == null || data.new_state == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a changer, user, old state and new state.")
                    .build();
        }

        Key changerKey = userKeyFactory.newKey(data.changer);
        Entity changer = datastore.get(changerKey);

        if(changer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Changer not found.")
                    .build();
        }

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        boolean backoffice = data.changer.equals("backoffice") && !((data.old_state.equals("active") && data.new_state.equals("inactive")) ||
                                                                    (data.old_state.equals("inactive") && data.new_state.equals("active")));

        if(backoffice) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Changer does not have permission to change the user's state.")
                    .build();
        }

        if(!data.old_state.equals(user.getString("user_state"))) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please enter the correct old state.")
                    .build();
        }

        if(data.old_state.equals(data.new_state)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please enter a different new state.")
                    .build();
        }

        Entity updatedUser = Entity.newBuilder(user)
                .set("user_state", data.new_state)
                .build();

        datastore.update(updatedUser);

        return Response.ok().entity("State changed successfully.").build();
    }

    @POST
    @Path("/attributes")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeAttributes(ChangeAttributesData data) {

        if(data.changer == null || data.user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a changer and an user.")
                    .build();
        }

        Key changerKey = userKeyFactory.newKey(data.changer);
        Entity changer = datastore.get(changerKey);

        if(changer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Changer not found.")
                    .build();
        }

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        boolean enduser = changer.getString("user_role").equals("enduser") && user.getString("user_role").equals("enduser")
                            && data.id != null && data.email != null && data.full_name != null && data.role != null && data.account_state != null;
        boolean backoffice = changer.getString("user_role").equals("backoffice") &&
                                ((user.getString("user_role").equals("admin") && user.getString("user_role").equals("backoffice")) ||
                                (((user.getString("user_role").equals("enduser") || user.getString("user_role").equals("partner"))) && data.email != null && data.id != null));

        if(enduser || backoffice) {
            return Response.status(Response.Status.FORBIDDEN)
                        .entity("Changer does not have permission to change the user's attributes.")
                        .build();
        }



        return null;
    }

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
}
