package com.len.student_app.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.len.student_app.dto.StudentRequestDTO;
import com.len.student_app.dto.StudentResponseDTO;
import com.len.student_app.entity.Student;
import com.len.student_app.service.StudentService;

import jakarta.validation.Valid;

@RestController
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("/students")
	public Page<StudentResponseDTO> getAllStudents(Pageable pageable) {
		return studentService.getAllStudentsAsDTO(pageable);
	}

	@PostMapping("/students")
	public ResponseEntity<StudentResponseDTO> createStudent(
	@Valid @RequestBody StudentRequestDTO requestDTO) {

	
	StudentResponseDTO createdStudent =
	        studentService.saveStudent(requestDTO);

	return ResponseEntity
	        .status(HttpStatus.CREATED)
	        .body(createdStudent);
	

	}

	@GetMapping("/students/{id}")
	public ResponseEntity<StudentResponseDTO> getStudentById(
	        @PathVariable Long id) {

	    return studentService.getStudentById(id)
	            .map(ResponseEntity::ok)
	            .orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/students/{id}")
	public StudentResponseDTO updateStudent(
	        @PathVariable Long id,
	        @Valid @RequestBody StudentRequestDTO requestDTO) {

	    return studentService.updateStudent(id, requestDTO);
	}

	@DeleteMapping("/students/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		studentService.deleteStudent(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/students/course/{course}")
	public List<StudentResponseDTO> getStudentsByCourse(
	        @PathVariable String course) {

	    return studentService.getStudentsByCourse(course);
	}

	@GetMapping("/students/search")
	public List<StudentResponseDTO> searchStudentsByName(
	        @RequestParam String name) {

	    return studentService.searchStudentsByName(name);
	}

	@GetMapping("/students/search/filter")
	public List<StudentResponseDTO> searchStudentsByNameAndCourse(@RequestParam String name, @RequestParam String course) {

		return studentService.findByNameContainingIgnoreCaseAndCourse(name, course);

	}

}
