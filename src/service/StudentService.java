package service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import dao.StudentDao;
import model.Login;
import model.Student;
import redis.clients.jedis.Jedis;

public class StudentService implements StudentDao {
	
	private static Jedis jedis;
	private static StudentService studentService;
	private static int counter = 1; 
	private static final String DATA_NAME = "test4"; 
	
	private StudentService() {
			
		jedis = new Jedis("localhost");

	}
	
	public boolean checkToken(String username, String token) {
		System.out.println(jedis.get(username));
		System.out.println(token);
		return jedis.get(username).equals(token);
	}
	
	public String login(Login login) {
		
		Student student = getByUsername(login.getUsername());
		if(student==null)
			return null;
		String token = null;
		if(student.getPassword().equals(login.getPassword())) {		
			token = UUID.randomUUID().toString().toUpperCase() + "#" + System.currentTimeMillis();
			jedis.set(login.getUsername(), token);
			
		}
		// wrong password
		return token;
	}
	
	private Student getByUsername(String username) {
		List<String> list = jedis.lrange(DATA_NAME, 0 , counter); 
		
		Student result = null;
		
		for(int i = 0; i<list.size(); i++) { 
			
			String obj = list.get(i);
			
			Gson gson = new Gson();
			Student s = gson.fromJson(obj, Student.class);
			
			if(s.getUsername().equals(username))
				return s;
		
		} 
		
		return result;
	}
	
	public static StudentService getInstance() {
		if(studentService == null) {
			studentService = new StudentService();
		}
		return studentService;
	}
	
	@Override
	public Student addStudent(Student student) {
		
		//student.setId(counter++);
		if(student.getId() == 0)
			student.setId(System.currentTimeMillis());
		//System.out.println(counter);
		jedis.lpush(DATA_NAME, new Gson().toJson(student));
		
		return student;
	}

	@Override
	public ArrayList<Student> getAll() {
		
		List<String> list = jedis.lrange(DATA_NAME, 0 , counter); 
		
		ArrayList<Student> result = new ArrayList<Student>();
		
		for(int i = 0; i<list.size(); i++) { 
			
			String obj = list.get(i);
			
			Gson gson = new Gson();
				
			Student t = gson.fromJson(obj, Student.class);
			
		//	Student s = gson.fromJson(obj, Student.class);
		
			result.add(t);		
		} 
		
		return result;
	}

	@Override
	public Student getById(long id) {
		
		List<String> list = jedis.lrange(DATA_NAME, 0 , counter); 
	
		Student result = null;
		
		for(int i = 0; i<list.size(); i++) { 
			
			String obj = list.get(i);
			
			Gson gson = new Gson();
			Student s = gson.fromJson(obj, Student.class);
			
			if(s.getId() == id)
				return s;
		
		} 
		
		return result;
		
	}

	@Override
	public boolean updateStudent(Student student) {
		
		List<String> list = jedis.lrange(DATA_NAME, 0 , counter); 
		
		Student result = null;
		
		for(int i = 0; i<list.size(); i++) { 
			
			String obj = list.get(i);
			
			Gson gson = new Gson();
			Student s = gson.fromJson(obj, Student.class);
			
			if(s.getId() == student.getId()) {
				
				jedis.lset(DATA_NAME, i, gson.toJson(student));
				return true;
				
			}
		
		} 
		
		result = addStudent(student);
		
		return false;
	}	

	@Override
	public boolean removeStudent(long id) {
		
		Student student = getById(id);
		
		if(student == null)
			return false;
		
		List<String> list = jedis.lrange(DATA_NAME, 0 , counter); 
	
		for(int i = 0; i<list.size(); i++) { 
			
			String obj = list.get(i);
			
			Gson gson = new Gson();
			Student s = gson.fromJson(obj, Student.class);
			
			if(s.getId() == id) {
				jedis.lrem(DATA_NAME, 1, gson.toJson(student));
				return true;
				
			}
		} 
		
		return false;
	}
	

	
}
