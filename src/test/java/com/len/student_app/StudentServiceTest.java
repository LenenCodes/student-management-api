package com.len.student_app;

import com.len.student_app.dto.StudentRequestDTO;
import com.len.student_app.dto.StudentResponseDTO;
import com.len.student_app.entity.Student;
import com.len.student_app.exception.DuplicateEmailException;
import com.len.student_app.repository.StudentRepository;
import com.len.student_app.service.StudentService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getStudentById_shouldReturnStudent_whenStudentExists() {

        Student student = new Student();
        student.setId(1L);
        student.setName("Arun");
        student.setEmail("arun@example.com");
        student.setCourse("Java");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        Optional<StudentResponseDTO> result =
                studentService.getStudentById(1L);

        assertTrue(result.isPresent());

        StudentResponseDTO actualStudent = result.orElseThrow();

        assertEquals(1L, actualStudent.getId());
        assertEquals("Arun", actualStudent.getName());
        assertEquals("arun@example.com", actualStudent.getEmail());
        assertEquals("Java", actualStudent.getCourse());
    }
    
    @Test
    void getStudentById_shouldReturnEmpty_whenStudentDoesNotExist() {

        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<StudentResponseDTO> result = studentService.getStudentById(99L);

        assertTrue(result.isEmpty());
    }
    @Test
    void saveStudent_shouldReturnStudentDTO_whenStudentIsSaved() {

        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setName("Arun");
        requestDTO.setEmail("arun@example.com");
        requestDTO.setCourse("Java");
        requestDTO.setAge(22);

        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setName("Arun");
        savedStudent.setEmail("arun@example.com");
        savedStudent.setCourse("Java");
        savedStudent.setAge(22);

        when(studentRepository.existsByEmail("arun@example.com"))
                .thenReturn(false);

        when(studentRepository.save(any(Student.class)))
                .thenReturn(savedStudent);

        StudentResponseDTO result = studentService.saveStudent(requestDTO);

        assertEquals(1L, result.getId());
        assertEquals("Arun", result.getName());
        assertEquals("arun@example.com", result.getEmail());
        assertEquals("Java", result.getCourse());
    }
    @Test
    void saveStudent_shouldThrowException_whenEmailAlreadyExists() {

        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setName("Arun");
        requestDTO.setEmail("arun@example.com");
        requestDTO.setCourse("Java");
        requestDTO.setAge(22);

        when(studentRepository.existsByEmail("arun@example.com"))
                .thenReturn(true);

        DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> studentService.saveStudent(requestDTO)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(studentRepository, never()).save(any(Student.class));
    }
}