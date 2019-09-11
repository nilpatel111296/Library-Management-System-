package com.library.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.library.entity.Author;

@Repository 
public class AuthorDAOImpl implements AuthorDAO {

	
	@Autowired 
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional 
	public List<Author> getAuthors() {
	
		Session currentSession = sessionFactory.getCurrentSession();
		Query <Author> theQuery = currentSession.createQuery("from authors",Author.class);
		List<Author> theAuthor = theQuery.getResultList();
		
		return theAuthor;

	}

	@Override
	public void saveCustomer(Author theAuthor) {
		// TODO Auto-generated method stub

	}

	@Override
	public Author getCustomer(int authorId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCustomer(int authorId) {
		// TODO Auto-generated method stub

	}

}
