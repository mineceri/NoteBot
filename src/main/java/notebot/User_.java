package notebot;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

import java.util.List;


@Data
@Entity
public class User_ {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long chatId;
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "Имя должно иметь только одно слово")
    private String name;
    @OneToMany
    private List<Note> noteList;

}
