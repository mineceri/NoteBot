package notebot.controllers;

import notebot.Note;
import notebot.data.NoteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/note",produces = "application/json")
public class NoteController {
    private final NoteRepository noteRepo;

    public NoteController(NoteRepository noteRepo) {this.noteRepo = noteRepo;}
    @PostMapping(consumes = "application/json")
    public Note processNote(@RequestBody Note note){return  noteRepo.save(note);}

    
}
