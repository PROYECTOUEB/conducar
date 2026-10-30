package co.edu.unbosque.condulog.controller;

import co.edu.unbosque.condulog.model.instructor.InstructorResumen;
import co.edu.unbosque.condulog.service.InstructorService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/instructores")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    public List<InstructorResumen> listar() {
        return instructorService.listar();
    }
}
