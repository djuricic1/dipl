package dao;

import java.util.ArrayList;

import model.Student;

public interface StudentDao {
	Student addStudent(Student student);
	ArrayList<Student> getAll();
	Student getById(long id);
	boolean updateStudent(Student student);
	boolean removeStudent(long id);
}



