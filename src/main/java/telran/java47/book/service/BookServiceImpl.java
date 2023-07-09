package telran.java47.book.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java47.book.dao.AuthorRepository;
import telran.java47.book.dao.BookRepository;
import telran.java47.book.dao.PublisherRepository;
import telran.java47.book.dto.AuthorDto;
import telran.java47.book.dto.BookDto;
import telran.java47.book.dto.exceptions.EntityNotFoundException;
import telran.java47.book.model.Author;
import telran.java47.book.model.Book;
import telran.java47.book.model.Publisher;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if (bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		// Publisher
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElse(publisherRepository.save(new Publisher(bookDto.getPublisher())));
		// Authors
		Set<Author> authors = bookDto.getAuthors().stream()
				.map(a -> authorRepository.findById(a.getName())
						.orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
				.collect(Collectors.toSet());
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto removeBook(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		bookRepository.deleteById(isbn);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBookTitle(String isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public List<BookDto> findBooksByAuthor(String author) {
		return bookRepository.findBooksByAuthorsContains(new Author(author, LocalDate.now()))
				.map(b -> modelMapper.map(b, BookDto.class))
				.toList();
	}

	@Override
	@Transactional
	public List<BookDto> findBooksByPublisher(String publisher) {
		return bookRepository.findBooksByPublisher(new Publisher(publisher))
				.map(b -> modelMapper.map(b, BookDto.class))
				.toList();
	}

	@Override
	public List<AuthorDto> findAuthorsByBook(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return book.getAuthors().stream()
				.map(b -> modelMapper.map(b, AuthorDto.class))
				.toList();
	}

	@Override
	@Transactional
	public List<String> findPublisherByAuthor(String author) {
		List<Book> booksByAuthor = bookRepository.findBooksByAuthorsContains(new Author(author, LocalDate.now()))
												 .toList();
		return booksByAuthor.stream()
							.map(b -> b.getPublisher().getPublisherName())
							.distinct()
							.toList();
		
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		List<Book> booksByAuthor = bookRepository.findBooksByAuthorsContains(author)
												 .toList();
		for (Book book : booksByAuthor) {
			bookRepository.deleteById(book.getIsbn());
		}
		authorRepository.deleteById(authorName);
		return modelMapper.map(author, AuthorDto.class);
	}

}
