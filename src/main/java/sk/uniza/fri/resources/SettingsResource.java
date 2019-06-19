package sk.uniza.fri.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import sk.uniza.fri.Requester;
import sk.uniza.fri.api.CityApiKey;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/settings")
@Api(value = "Settings")
public class SettingsResource {
    private Requester requester;

    public SettingsResource(Requester requester) {
        this.requester = requester;
    }

    @POST
    @Path("/cities")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Sets new cityID-key pairs of cities for which will be this server requesting data for. Old id-kay pairs  will be removed")
    public Response setCities(List<CityApiKey> cities){
        if(cities != null){
            requester.setCityKeyList(new ArrayList<>(cities));
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok().build();
    }

}
