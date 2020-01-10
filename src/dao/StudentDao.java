package dao;

import java.util.ArrayList;

import model.Student;

public interface StudentDao {
	Student addStudent(Student student);
	ArrayList<Student> getAll();
	Student getById(int id);
	boolean updateStudent(Student student);
	boolean removeStudent(int id);
}



