package notebot.controllers;

import jakarta.websocket.server.PathParam;
import notebot.Note;
import notebot.User_;
import notebot.data.NoteRepository;
import notebot.data.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping(value = "/note",produces = "application/json")
public class NoteController {
    private final NoteRepository noteRepo;
    private final UserRepository userRepo;

    private final int maxSymbols;
    public NoteController(NoteRepository noteRepo, UserRepository userRepo, @Value("${notebot.maxsymbols}") int maxSymbols) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
        this.maxSymbols = maxSymbols;
    }
    @PostMapping(consumes = "application/json")
    public Note processNote(@RequestBody Note note){
            if (note.getText().length() <= maxSymbols) {
            Long chatId = Optional.ofNullable(note.getUser())
                    .map(User_::getChatId)
                    .orElse(null);
            note.setUser(userRepo.findByChatId(chatId).get(0));
            return noteRepo.save(note);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Превышено максимальное допустимое количество символов");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Note> deleteNote(@PathParam("chatId")Long chatId, @PathVariable long id){
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
        Page<Note> notePage = noteRepo.findAllByUserChatId(pageable,chatId);
        return ResponseEntity.ok()
                .body(notePage.getContent());
    }
    @GetMapping("/count")
    public ResponseEntity<Integer>getCountNotesForChatId(@RequestParam("chatId") Long chatId){
        Optional<Integer> notePage = noteRepo.countNotesByUser_ChatId(chatId);
        return ResponseEntity.ok()
                .body(notePage.orElse(null));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNote(@PathParam("chatId")Long chatId, @PathVariable long id) throws SQLException {
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
    @PutMapping(consumes = "application/json")
    public  ResponseEntity<Note> updateNote(Note note){
        Note note1=noteRepo.findById(note.getId()).orElse(null);
        if(note1==null){
           throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Заметки с таким id не существует");
        }else {
            note1=note;
            noteRepo.save(note1);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
