package telran.java47.book.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
	String isbn;
	String title;
	Set<AuthorDto> authors;
	String publisher;
}
