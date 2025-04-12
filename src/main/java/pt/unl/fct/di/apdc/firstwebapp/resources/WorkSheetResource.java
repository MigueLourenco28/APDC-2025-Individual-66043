package pt.unl.fct.di.apdc.firstwebapp.resources;


import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.util.WorkSheetData;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/worksheet")
public class WorkSheetResource {

    private static final Logger LOG = Logger.getLogger(WorkSheetResource.class.getName());
    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    public WorkSheetResource() {}

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createWorkSheet(WorkSheetData data) {
        LOG.fine("Attempt to register a Work Sheet.");

        if(!data.validRegistration())
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();

        try {
            Key workSheetKey = datastore.newKeyFactory().setKind("WorkSheet").newKey(data.work_ref);

            Entity workSheet = Entity.newBuilder(workSheetKey)
                    .set("work_ref", data.work_ref)
                    .set("work_description", data.work_description)
                    .set("work_target", data.work_target)
                    .set("work_adjudication_state", data.work_adjudication_state)
                    .set("workSheet_creation_time", Timestamp.now())
                    .build();

            datastore.add(workSheet);
            LOG.info("User registered " + data.work_ref);

            return Response.ok().entity("Work Sheet registered " + data.work_ref).build();
        }
        catch(DatastoreException e) {
            LOG.log(Level.ALL, e.toString());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getReason()).build();
        }
    }

}
