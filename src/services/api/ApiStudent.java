package services.api;

import java.net.URI;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import model.Student;
import service.StudentService;

@Path("/students")
public class ApiStudent {
	
StudentService ss = StudentService.getInstance();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTest() {
		ArrayList<Student> result = ss.getAll();
		if(result.size() == 0) {
			return Response.status(Status.NO_CONTENT).build();
		}
		else {
			return Response.ok(result).status(Status.OK).build();
		}		
	}
		
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") int id) {
		
		Student result = ss.getById(id);
		if(result == null) {
			System.out.println("TEST");
			return Response.status(Status.NOT_FOUND).build();
		}
		else {
			return Response.ok(result).status(Status.OK).build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addStudent(Student student, @Context UriInfo uriInfo) {
		
		Student result = ss.addStudent(student);	
	    int studentId = result.getId();
	    UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	    builder.path(Integer.toString(studentId));
	    
	    return Response.created(builder.build()).entity(result).build();		
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudent(@PathParam("id") int id, Student student) {
			
		student.setId(id);
		
		boolean result = ss.updateStudent(student);
		if(!result) {
			return Response.status(Status.CREATED).build();
		}
		else {
			return Response.ok(student).status(Status.OK).build();
		}
	}
	
	@Path("/{id}")
	@DELETE	
	public Response deleteById(@PathParam("id") int id) {
		boolean result = ss.removeStudent(id);
		if(result) {
			return Response.status(204).build();
		}
		else {
			return Response.status(404).build();
		}
	}
	
	
}
