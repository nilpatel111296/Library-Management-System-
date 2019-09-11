package com.library.dao;

import java.util.List;

import com.library.entity.Author;


public interface AuthorDAO {
	
	
	 public List<Author> getAuthors();
	 
	 public void saveCustomer(Author theAuthor);
	 
	 public Author getCustomer(int authorId);
	 
	 public void deleteCustomer(int authorId);

}
