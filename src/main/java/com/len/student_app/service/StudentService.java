package com.len.student_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.len.student_app.dto.StudentRequestDTO;
import com.len.student_app.dto.StudentResponseDTO;
import com.len.student_app.entity.Student;
import com.len.student_app.exception.DuplicateEmailException;
import com.len.student_app.exception.StudentNotFoundException;
import com.len.student_app.repository.StudentRepository;

@Service
public class StudentService {

	private final StudentRepository studentRepository;

	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public StudentResponseDTO saveStudent(StudentRequestDTO requestDTO) {

	    if (studentRepository.existsByEmail(requestDTO.getEmail())) {
	        throw new DuplicateEmailException("Email already exists");
	    }

	    Student student = new Student();
	    student.setName(requestDTO.getName());
	    student.setEmail(requestDTO.getEmail());
	    student.setCourse(requestDTO.getCourse());
	    student.setAge(requestDTO.getAge());

	    Student savedStudent = studentRepository.save(student);

	    return convertToDTO(savedStudent);
	}

	public StudentResponseDTO convertToDTO(Student student) {
		return new StudentResponseDTO(student.getId(), student.getName(), student.getEmail(), student.getCourse());
	}

	public Page<StudentResponseDTO> getAllStudentsAsDTO(Pageable pageable) {

		Page<Student> students = studentRepository.findAll(pageable);

		return students.map(this::convertToDTO);
	}

	public Optional<StudentResponseDTO> getStudentById(Long id) {
	    return studentRepository.findById(id)
	            .map(this::convertToDTO);
	}

	public StudentResponseDTO updateStudent(
	        Long id, StudentRequestDTO requestDTO) {

	    Student existingStudent = studentRepository.findById(id)
	            .orElseThrow(() ->
	                    new StudentNotFoundException("Student does not exist"));

	    if (studentRepository.existsByEmailAndIdNot(
	            requestDTO.getEmail(), id)) {
	        throw new DuplicateEmailException("Email already exists");
	    }

	    existingStudent.setName(requestDTO.getName());
	    existingStudent.setEmail(requestDTO.getEmail());
	    existingStudent.setCourse(requestDTO.getCourse());
	    existingStudent.setAge(requestDTO.getAge());

	    Student updatedStudent = studentRepository.save(existingStudent);

	    return convertToDTO(updatedStudent);
	}

	public void deleteStudent(Long id) {
		if (!studentRepository.existsById(id)) {
			throw new StudentNotFoundException("Student does not exist");
		}
		studentRepository.deleteById(id);

	}

	public List<StudentResponseDTO> getStudentsByCourse(String course) {
		return studentRepository.findByCourse(course)
	            .stream()
	            .map(this::convertToDTO)
	            .toList();
	}

	public List<StudentResponseDTO> searchStudentsByName(String name) {
	    return studentRepository.findByNameContainingIgnoreCase(name)
	            .stream()
	            .map(this::convertToDTO)
	            .toList();
	}

	public List<StudentResponseDTO> findByNameContainingIgnoreCaseAndCourse(String name, String course) {
		 return studentRepository.findByNameContainingIgnoreCaseAndCourse(name,course)
		            .stream()
		            .map(this::convertToDTO)
		            .toList();
		}
}
