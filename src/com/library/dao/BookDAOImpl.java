package com.library.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.entity.Book;


@Repository 
public class BookDAOImpl implements BookDAO {

	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional 
	public List<Book> getBooks() {
		// TODO Auto-generated method stub
		Session currentSession = sessionFactory.getCurrentSession();
		Query <Book> theQuery = currentSession.createQuery("from Book",Book.class);
		List<Book> theBooks = theQuery.getResultList();	
		return theBooks;

	}

	@Override
	public void saveCustomer(Book theBook) {
		// TODO Auto-generated method stub

	}

	@Override
	public Book getBook(int Isbn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteDelete(int Isbn) {
		// TODO Auto-generated method stub

	}

}
