package notebot.data;

import notebot.Note;

import org.springframework.data.repository.CrudRepository;



public interface NoteRepository extends CrudRepository<Note,Long> {
}
