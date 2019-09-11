package com.library.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="book_authors")
public class BookAuthor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name="Author_id")
	private Author author;
	
	@Id
	@ManyToOne
	@JoinColumn(name="Isbn")
	private Book book;
 
	public BookAuthor() {
		
	}

	public BookAuthor(Author author){
		this.author = author;		
	}
	public BookAuthor(Book book){
		this.book = book;
	}
	
	public BookAuthor(Author author, Book book) {
		this.author = author;
		this.book = book;
		
	}
	
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
}
