package sk.uniza.fri.resources;

import sk.uniza.fri.Requester;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/settings")
public class SettingsResource {
    private Requester requester;

    public SettingsResource(Requester requester) {
        this.requester = requester;
    }

    @POST
    @Path("/cities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setCities(@QueryParam("cityIDs") final List<String> IDs){
        if(IDs != null){
            requester.setCityIdList(new ArrayList<>(IDs));
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }

}
