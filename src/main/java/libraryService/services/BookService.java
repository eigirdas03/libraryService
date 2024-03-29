package libraryService.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import libraryService.exceptions.LibraryServiceException;
import libraryService.models.Book;
import libraryService.models.BookPlantMapper;
import libraryService.repositories.BookRepository;

@Service
public class BookService
{
	BookRepository bookRepository;
	BookLibraryMapperService bookLibraryMapperService;
	BookPlantMapperService bookPlantMapperService;
	
	public BookService(BookRepository bookRepository, BookLibraryMapperService bookLibraryMapperService, 
			BookPlantMapperService bookPlantMapperService) throws LibraryServiceException
	{
		this.bookRepository = bookRepository;
		this.bookLibraryMapperService = bookLibraryMapperService;
		
		this.bookPlantMapperService = bookPlantMapperService;
		
        bookRepository.save(new Book("name1 surname1", "title1", 2000));
        bookRepository.save(new Book("name2 surname2", "title2", 2001));
        bookRepository.save(new Book("name3 surname3", "title3", 2002));
	}
	
	public void checkIfBookExists(Optional<Book> book) throws LibraryServiceException
	{
		if(book.isPresent() == false)
		{
			throw new LibraryServiceException("Book does not exist", HttpStatus.NOT_FOUND);
		}
	}
	
	public void checkIfBookExists(long id) throws LibraryServiceException
	{
		if(bookRepository.findById(id).isPresent() == false)
		{
			throw new LibraryServiceException("Book does not exist", HttpStatus.NOT_FOUND);
		}
	}
	
	public void checkIfBooksExist(List<Long> books) throws LibraryServiceException
	{
		for(int i = 0; i < books.size(); ++i)
		{
			if(bookRepository.findById(books.get(i)).isPresent() == false)
			{
				throw new LibraryServiceException("Book with id " + books.get(i) + " does not exist", HttpStatus.NOT_FOUND);
			};
		}
	}
	
	public void checkIfBookDoesNotExist(long id) throws LibraryServiceException
	{
		if(bookRepository.findById(id).isPresent())
		{
			throw new LibraryServiceException("Book already exists", HttpStatus.BAD_REQUEST);
		}
	}
	
	public List<Book> getAllBooks() throws LibraryServiceException
	{
		List<Book> books = bookRepository.findAll();
		
		if(books.size() == 0)
		{
			throw new LibraryServiceException("No books exist", HttpStatus.NOT_FOUND);
		}
		
		for(int i = 0; i < books.size(); ++i)
		{
			Book book = books.get(i);
			book.setPlants(bookPlantMapperService.getPlantsLinkedToBook(book.getId()));
		}
		
		return books;
	}

	public Book getBookById(long id) throws LibraryServiceException
	{
		Optional<Book> book = bookRepository.findById(id);
		
		checkIfBookExists(book);
		
		book.get().setPlants(bookPlantMapperService.getPlantsLinkedToBook(id));
		
		return book.get();
	}
	
	public Book addBook(Book book) throws LibraryServiceException
	{
		Book newBook = bookRepository.save(new Book(book));
		return newBook;
	}
	
	public Book updateBook(long id, Book newBookData) throws LibraryServiceException
	{
		Optional<Book> book = bookRepository.findById(id);
		checkIfBookExists(book);
		
		Book bookData = book.get();
		
		bookData.setAuthor(newBookData.getAuthor());
		bookData.setTitle(newBookData.getTitle());
		bookData.setPublished(newBookData.getPublished());
		
		Book updatedBook = bookRepository.save(bookData);
		
		return updatedBook;
	}
	
	
	public void deleteBook(long id) throws LibraryServiceException
	{
		Optional<Book> book = bookRepository.findById(id);
		checkIfBookExists(book);
		
		bookRepository.deleteById(id);
		
		bookLibraryMapperService.deleteByBookFromRepository(id);
		
		List<BookPlantMapper> bookPlantMapperData = bookPlantMapperService.findAllFromRepository();
		
		for(int i = 0; i < bookPlantMapperData.size(); ++i)
		{
			BookPlantMapper mapperData = bookPlantMapperData.get(i);
			
			if(mapperData.getBook() == id)
			{
				bookPlantMapperService.deleteFromRepository(mapperData);
			}
		}
	}
}
