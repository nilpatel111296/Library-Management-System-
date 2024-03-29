package com.library.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="book_loan")
public class BookLoan{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Loan_id")
	private int loanId;
	
	@ManyToOne
	@JoinColumn(name="Isbn")
	private Book book;
	
	@ManyToOne
	@JoinColumn(name="Card_id")
	private Borrower borrower;
	
	@Column(name="date_out")
	private Date dateOut;
	
	@Column(name="date_in")
	private Date dateIn;
	
	@Column(name="due_date")
	private Date dueDate;
	
	@OneToOne(mappedBy="bookLoan")
	private Fine fine;
	
	
	public BookLoan() {
		// TODO Auto-generated constructor stub
	}
	
	
	public BookLoan(Book book, Borrower borrower){
		this.book = book;
		this.borrower = borrower;
	}
	
	public int getLoanId() {
		return loanId;
	}
	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}
	public Date getDateOut() {
		return dateOut;
	}
	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}
	public Date getDateIn() {
		return dateIn;
	}
	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Borrower getBorrower() {
		return borrower;
	}
	public void setBorrower(Borrower borrower) {
		this.borrower = borrower;
	}
	
	public Fine getFine() {
		return fine;
	}
	public void setFine(Fine fine) {
		this.fine = fine;
	}
	
}

