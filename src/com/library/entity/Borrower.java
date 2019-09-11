package com.library.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="borrower")
public class Borrower {

	@Id
	@Column(name="Card_id")
	private int cardId;
	
	@Column(name="Ssn")
	private String ssn;
	
	@Column(name="bname")
	private String bName;
	
	@Column(name="address")
	private String address;
	
	@Column(name="phone")
	private String phone;

	@OneToMany(mappedBy="borrower")
	private List<BookLoan> bookLoans;
	
	public Borrower() {
		
	}
	
	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<BookLoan> getBookLoans() {
		return bookLoans;
	}

	public void setBookLoans(List<BookLoan> bookLoans) {
		this.bookLoans = bookLoans;
	}
	
	
	
}



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
//@Table(name="BORROWER")
//public class Borrower {
//
//	@Id 
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="Card_id")
//	private long Card_id;
//	
//	@Column(name="Ssn")
//	private String Ssn;
//	
//	@Column(name="B_firstname")
//	private String B_firstname;
//	
//
//	@Column(name="B_lastname")
//	private String B_lastname;
//	
//	
//	@Column(name="Email")
//	private String Email;
//	
//	@Column(name="Address")
//	private String Address;
//	
//	@Column(name="City")
//	private String City;
//	
//	@Column(name="State")
//	private String State;
//	
//	@Column(name="Phone")
//	private String Phone;
//
//
//	public String getB_lastname() {
//		return B_lastname;
//	}
//
//
//	public void setB_lastname(String b_lastname) {
//		B_lastname = b_lastname;
//	}
//
//
//	public Borrower() {
//		
//	}
//	
//	
//	public long getCard_id() {
//		return Card_id;
//	}
//
//	public void setCard_id(long card_id) {
//		Card_id = card_id;
//	}
//
//	public String getSsn() {
//		return Ssn;
//	}
//
//	public void setSsn(String ssn) {
//		Ssn = ssn;
//	}
//
//	public String getB_firstname() {
//		return B_firstname;
//	}
//
//	public void setB_firstname(String b_firstname) {
//		B_firstname = b_firstname;
//	}
//
//	public String getEmail() {
//		return Email;
//	}
//
//	public void setEmail(String email) {
//		Email = email;
//	}
//
//	public String getAddress() {
//		return Address;
//	}
//
//	public void setAddress(String address) {
//		Address = address;
//	}
//
//	public String getCity() {
//		return City;
//	}
//
//	public void setCity(String city) {
//		City = city;
//	}
//
//	public String getState() {
//		return State;
//	}
//
//	public void setState(String state) {
//		State = state;
//	}
//
//	public String getPhone() {
//		return Phone;
//	}
//
//	public void setPhone(String phone) {
//		Phone = phone;
//	}
//
//
//	@Override
//	public String toString() {
//		return "Borrower [Card_id=" + Card_id + ", Ssn=" + Ssn + ", B_firstname=" + B_firstname + ", B_lastname="
//				+ B_lastname + ", Email=" + Email + ", Address=" + Address + ", City=" + City + ", State=" + State
//				+ ", Phone=" + Phone + "]";
//	}
//	
//	
//}
