package services.api;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Student;
import service.StudentService;

@Path("/test")
public class ApiJAXRSStudent {

	StudentService ss = StudentService.getInstance();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Student> getTest() {	
		return ss.getAll();
	}
		
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Student getById(@PathParam("id") long id) {
		
		return ss.getById(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Student addStudent(Student student) {
		return ss.addStudent(student);
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Student updateStudent(@PathParam("id") long id, Student student) {
		student.setId(id);
		ss.updateStudent(student);
		return student;
	}
	
	@Path("/{id}")
	@DELETE	
	public boolean deleteById(@PathParam("id") long id) {
		return ss.removeStudent(id);
	}
	
	
	
}
