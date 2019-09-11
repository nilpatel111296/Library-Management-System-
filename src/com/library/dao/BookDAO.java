package com.library.dao;

import java.util.List;

import com.library.entity.Book;

public interface BookDAO {


	 public List<Book> getBooks();
	 
	 public void saveCustomer(Book theBook);
	 
	 public Book getBook(int Isbn);
	 
	 public void deleteDelete(int Isbn);
	
	
}
