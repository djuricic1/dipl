package service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import dao.StudentDao;
import model.Student;
import redis.clients.jedis.Jedis;

public class StudentService implements StudentDao {
	
	private static Jedis jedis;
	private static StudentService studentService;
	private static int counter = 1; 
	private static final String DATA_NAME = "test2"; 
	
	private StudentService() {
		// make connection to redis server
		
		jedis = new Jedis("localhost");
		//Student student1 = new Student();
		//student1.setId(counter++);
		//student1.setName("Marko Markovic");
		
		//Gson gson = new Gson();
		//String s = gson.toJson(student1);
		
		//jedis.lpush(DATA_NAME, s);
		
	}
	
	public static StudentService getInstance() {
		if(studentService == null) {
			studentService = new StudentService();
		}
		return studentService;
	}
	
	@Override
	public Student addStudent(Student student) {
		
		student.setId(counter++);
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
			
			//System.out.println(obj);	
			Student t = gson.fromJson(obj, Student.class);
			System.out.println(t.getName());
		//	Student s = gson.fromJson(obj, Student.class);
		
			result.add(t);		
		} 
		
		return result;
	}

	@Override
	public Student getById(int id) {
		
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
	public boolean removeStudent(int id) {
		
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
