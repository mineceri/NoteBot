package notebot.data;

import notebot.Note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface NoteRepository extends CrudRepository<Note,Long> {
    Page<Note> findAllByUserChatId(Pageable pageable,Long chatId);
    Optional<Integer> countNotesByUser_ChatId(Long chatId);
}
