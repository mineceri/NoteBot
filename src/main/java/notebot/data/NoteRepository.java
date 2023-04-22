package notebot.data;

import notebot.Note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;



public interface NoteRepository extends CrudRepository<Note,Long> {
    Page<Note> findAllByChatId(Pageable pageable,Long chatId);

}
