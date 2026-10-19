package com.len.student_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.len.student_app.entity.Student;

public interface StudentRepository extends JpaRepository<Student,Long> {

	boolean existsByEmail(String email);
	boolean existsByEmailAndIdNot(String email,Long id);
	List<Student> findByCourse(String course);
	List<Student> findByNameContainingIgnoreCase(String name);
	List<Student> findByNameContainingIgnoreCaseAndCourse(String name, String course);
}
