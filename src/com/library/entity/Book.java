//package com.library.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@Entity 
//@Table(name="Book")
//public class Book {
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="Isbn")
//	private long Isbn;
//	
//	@Column(name="Title")
//	private String Title;
//
//	@Override
//	public String toString() {
//		return "Book [Isbn=" + Isbn + ", Title=" + Title + "]";
//	}
//	
//	public Book() {
//		
//	}
//
//	public long getIsbn() {
//		return Isbn;
//	}
//
//	public void setIsbn(long isbn) {
//		Isbn = isbn;
//	}
//
//	public String getTitle() {
//		return Title;
//	}
//
//	public void setTitle(String title) {
//		Title = title;
//	}
//	
//}

package com.library.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="book")
public class Book {

	@Id
	@Column(name="Isbn")
	private String ISBN;
	
	@Column(name="Title")
	private String title;
	
	@Column(name="available")
	private boolean available;

	@OneToMany(mappedBy="book", cascade = CascadeType.ALL)
	private List<BookAuthor> author;
	
	@OneToMany(mappedBy="book", cascade = CascadeType.ALL)
	private List<BookLoan> bookLoan;
	
	public List<BookAuthor> getAuthor() {
		return author;
	}
	public void setAuthor(List<BookAuthor> author) {
		this.author = author;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public List<BookLoan> getBookLoan() {
		return bookLoan;
	}
	public void setBookLoan(List<BookLoan> bookLoan) {
		this.bookLoan = bookLoan;
	}

	
}

