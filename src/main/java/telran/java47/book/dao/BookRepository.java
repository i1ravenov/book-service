package telran.java47.book.dao;

import java.util.stream.Stream;

import org.springframework.data.repository.PagingAndSortingRepository;

import telran.java47.book.model.Author;
import telran.java47.book.model.Book;
import telran.java47.book.model.Publisher;

public interface BookRepository extends PagingAndSortingRepository<Book, String> {

	Stream<Book> findBooksByAuthorsContains(Author author);
	
	Stream<Book> findBooksByPublisher(Publisher publisher);

}
