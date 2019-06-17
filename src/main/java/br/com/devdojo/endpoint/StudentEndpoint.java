package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        verifyIfStudentExists(id);
        Optional<Student> student = studentDAO.findById(id);
        return new ResponseEntity<>(student,HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable String name){
        return new ResponseEntity<>(studentDAO.findByNameIgnoreCaseContaining(name),HttpStatus.OK);
    }


    @PostMapping
    @Transactional // por padrão só faz rollback com exception do tipo checked, senã otem que por no construtor??
    public ResponseEntity<?>  save(@Valid @RequestBody Student student) { // @Valid valida regras como não poder estar vazio
        return new ResponseEntity<>(studentDAO.save(student), HttpStatus.OK);
    }



    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?>  delete(@PathVariable("id") Long id) {
        verifyIfStudentExists(id);
        studentDAO.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@RequestMapping(method= RequestMethod.PUT)
    @PutMapping
    public ResponseEntity<?>  update(@RequestBody Student student) {
        verifyIfStudentExists(student.getId());
        studentDAO.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void verifyIfStudentExists(Long id){
        if (!studentDAO.findById(id).isPresent()){
            throw new ResourceNotFoundException("Student Not Found for ID: " + id);
        }

    }

}
