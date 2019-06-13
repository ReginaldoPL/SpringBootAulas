package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("students")
public class StudentEndpoint {

    private final StudentRepository studentDAO;
    @Autowired
    public StudentEndpoint(StudentRepository studentDAO)
    {
        this.studentDAO = studentDAO;
    }


    @RequestMapping
    public ResponseEntity<?>  listAll(){
        return new ResponseEntity<>(studentDAO.findAll(), HttpStatus.OK);
    }

    //@RequestMapping(method= RequestMethod.GET, path = "/{id}")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?>  getStudentById(@PathVariable("id") Long id) {
        Optional<Student> student = studentDAO.findById(id);

        if (! student.isPresent())
            return new ResponseEntity<>(
                    new CustomErrorType("Student not found"), HttpStatus.NOT_FOUND
            );
        return new ResponseEntity<>(student,HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?>  save(@RequestBody Student student) {
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.OK);
    }



    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?>  delete(@PathVariable("id") Long id) {
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@RequestMapping(method= RequestMethod.PUT)
    @PutMapping
    public ResponseEntity<?>  update(@RequestBody Student student) {
        studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
