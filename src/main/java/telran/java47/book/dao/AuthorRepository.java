package telran.java47.book.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import telran.java47.book.model.Author;

public interface AuthorRepository extends PagingAndSortingRepository<Author, String> {

}
