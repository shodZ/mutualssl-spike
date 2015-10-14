import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("blah")
public class AResource {
    
    @GET public Response sayHello() {
        return Response.ok().build();
    }
}
