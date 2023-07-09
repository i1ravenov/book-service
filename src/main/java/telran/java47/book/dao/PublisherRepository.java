package telran.java47.book.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import telran.java47.book.model.Publisher;

public interface PublisherRepository extends PagingAndSortingRepository<Publisher, String> {

}
