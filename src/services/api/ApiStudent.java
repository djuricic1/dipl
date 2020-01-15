package services.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

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

import model.Login;
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
			return Response.ok(result).build();
		}
		else {
			return Response.ok(result).status(Status.OK).build();
		}		
	}
		
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("id") long id) {
		
		Student result = ss.getById(id);
		
		if(result == null) {
			System.out.println("TEST");
			return Response.status(Status.NO_CONTENT).build();
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
	    long studentId = result.getId();
	    UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	    builder.path(Long.toString(studentId));
	    
	    return Response.created(builder.build()).build();		
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateStudent(@PathParam("id") long id, Student student, @Context UriInfo uriInfo) {
			
		student.setId(id);
		
		boolean result = ss.updateStudent(student);
		if(!result) {
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();		
			return Response.created(builder.build()).build();
		}
		else {
			return Response.status(Status.OK).build();
		}
	}
	
	@Path("/{id}")
	@DELETE	
	public Response deleteById(@PathParam("id") long id) {
		boolean result = ss.removeStudent(id);
		if(result) {
			return Response.status(204).build();
		}
		else {
			return Response.status(404).build();
		}
	}
	
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(Login login) {
		String token = ss.login(login);
		if(token == null) { // unsuccessful login
			return Response.status(401).build();
		}
		else { // successful login
			return Response.ok(token).build();
		}
			
	}
	
	@Path("/time")
	@POST
	@Consumes(MediaType.TEXT_PLAIN) 
	@Produces(MediaType.TEXT_PLAIN)
	public Response getTime(String params) {
		String username = "";
		String token = "";
		try {
			username = params.split("!")[0];
			token = params.split("!")[1];
		} catch (Exception ex) {
			System.out.println("TEST");
			return Response.status(403).build();
		}
		System.out.println(username);
		System.out.println(token);
		if(ss.checkToken(username, token)) {
			return Response.ok().entity(System.currentTimeMillis()).build();
		}
		else {
			return Response.status(403).build();
		}
	}
	
	
}
