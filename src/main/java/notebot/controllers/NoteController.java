package notebot.controllers;

import jakarta.websocket.server.PathParam;
import notebot.Note;
import notebot.data.NoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/note",produces = "application/json")
public class NoteController {
    private final NoteRepository noteRepo;

    private final int maxSymbols;
    public NoteController(NoteRepository noteRepo,@Value("${notebot.maxsymbols}") int maxSymbols) {
        this.noteRepo = noteRepo;
        this.maxSymbols = maxSymbols;
    }
    @PostMapping(consumes = "application/json")
    public Note processNote(@RequestBody Note note){
        if(note.getText().length()<=maxSymbols){
            return noteRepo.save(note);
        }else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Превышено максимальное допустимое количество символов");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Note> deleteNote(@PathParam("chatId")String chatId, @PathVariable long id){
        Note note=noteRepo.findById(id).orElse(null);
        if(note==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(Objects.equals(note.getUser().getChatId(), chatId)){
            noteRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping
    public ResponseEntity<List<Note>> getNotes(
            @RequestParam("chatId") Long chatId,
            @RequestParam("sizePage") int sizePage,
            @RequestParam("offset") int offset) {

        Pageable pageable = PageRequest.of(offset, sizePage);
        Page<Note> notePage = noteRepo.findAllByChatId(pageable,chatId);
        return ResponseEntity.ok()
                .body(notePage.getContent());
    }
    @GetMapping("/count")
    public ResponseEntity<Integer>getCountNotesForChatId(@RequestParam("chatId") Long chatId){
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Note> notePage = noteRepo.findAllByChatId(pageable,chatId);
        return ResponseEntity.ok()
                .body(notePage.getContent().size());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNote(@PathParam("chatId")String chatId, @PathVariable long id){
        Note note=noteRepo.findById(id).orElse(null);
        if(note==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(Objects.equals(note.getUser().getChatId(), chatId)){
            return new ResponseEntity<>(note,HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
