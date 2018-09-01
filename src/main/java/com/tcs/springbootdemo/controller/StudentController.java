package com.tcs.springbootdemo.controller;

import com.tcs.springbootdemo.data.StudentRepository;
import com.tcs.springbootdemo.entities.Student;
import com.tcs.springbootdemo.util.CustomErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/students")
    public ResponseEntity<List<Student>> retrieveAllStudents() {
        return new ResponseEntity<List<Student>>(studentRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> retrieveStudent(@PathVariable long id) {

        Optional<Student> student = studentRepository.findById(id);

        if (!student.isPresent()) {
            return new ResponseEntity(new CustomErrorType("Student with id " + id
                    + " not found"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Student>(student.get(), HttpStatus.OK);

    }


    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {

        Student savedStudent = studentRepository.save(student);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.getId()).toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<Student>(headers, HttpStatus.CREATED);

    }


    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable long id) {

        Optional<Student> studentOptional = studentRepository.findById(id);

        if (!studentOptional.isPresent())
            return new ResponseEntity(new CustomErrorType("Unable to update. Student with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);

        Student _student = studentOptional.get();

        _student.setName(student.getName());
        _student.setPassportNumber(student.getPassportNumber());

        studentRepository.save(_student);

        return new ResponseEntity<Student>(_student, HttpStatus.OK);
    }


    @DeleteMapping("/students/{id}")
    public ResponseEntity<?>  deleteStudent(@PathVariable long id) {

        Optional<Student> studentOptional = studentRepository.findById(id);

        if (!studentOptional.isPresent())
            return new ResponseEntity(new CustomErrorType("Unable to detele. Student with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);

        studentRepository.deleteById(id);

        return new ResponseEntity<Student>(HttpStatus.NO_CONTENT);

    }






}
