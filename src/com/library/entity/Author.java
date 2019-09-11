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
//@Table(name="AUTHORS")
//public class Author {
//	
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name="Author_id")
//	private int Author_id;
//	
//	@Column(name="Author_name")
//	private String Author_name;
//
//	public String getAuthor_name() {
//		return Author_name;
//	}
//
//	public void setAuthor_name(String author_name) {
//		Author_name = author_name;
//	}
//
//	public int getAuthor_id() {
//		return Author_id;
//	}
//	
//	public Author() {
//		
//	}
//
//	public void setAuthor_id(int author_id) {
//		Author_id = author_id;
//	}
//
//	@Override
//	public String toString() {
//		return "Author [Author_id=" + Author_id + ", Author_name=" + Author_name + "]";
//	}
//
//	
//
//}


package com.library.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity(name="authors")
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Author_id")
	private int author_id;
	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<BookAuthor> bookAuthor;
	
	@Column(name="Author_name")
	private String name;
	

	public int getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<BookAuthor> getBookAuthor() {
		return bookAuthor;
	}
	public void setBookAuthor(List<BookAuthor> bookAuthor) {
		this.bookAuthor = bookAuthor;
	}
	
	
	
}

