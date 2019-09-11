package com.library.controller;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.library.dao.AuthorDAO;
import com.library.dao.BookDAO;
import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookLoan;
import com.library.entity.Borrower;
import com.library.entity.Fine;
import com.library.rest.BookLoanRequest;
import com.library.rest.BorrowerData;
import com.library.rest.CheckInBook;
import com.library.rest.FineResponse;
import com.library.rest.RestResponse;
import com.library.rest.SearchQuery;
import com.library.rest.SearchResult;




@Controller
@RequestMapping("/home")
public class HomeController {

	@Autowired 
	private AuthorDAO authorDAO;
	
	@Autowired 
	private BookDAO bookDAO;
	
	@Autowired 
	private SessionFactory sessionFactory;
	
	
	@RequestMapping("/listAuthors")
	public String listAuthors(Model theModel) {
		List<Author > theAuthor = authorDAO.getAuthors();
		theModel.addAttribute("authors",theAuthor);
		return "list-authors";
	}
	
	@RequestMapping("/listBooks")
	public String listBooks(Model theModel) {
		List<Book > theBook = bookDAO.getBooks();
		theModel.addAttribute("books",theBook);
		return "list-books";
	}
	
	@RequestMapping("/viewSearchPage")
	public String viewSearchPage(Model theModel) {
		
		theModel.addAttribute("flag","0");
		return "search";
	}
	
	@GetMapping("/search")
	@Transactional 
	public String search(@RequestParam("searchString") String search,Model theModel) {
		
//		String search = "Williams";
		String queryString1 = "from Book b, authors a, BookAuthor ba where a.author_id= "
				+ "ba.author.author_id and b.ISBN = ba.book.ISBN and b.title like '%"+ 	search  + "%'";
//						+ "and a.name like '%" + search  + "%'";
//		
		String queryString2 = "from Book b, authors a, BookAuthor ba where a.author_id= "
				+ "ba.author.author_id and b.ISBN = ba.book.ISBN and a.name like '%"+ 	search  + "%'";
		
		String queryString3 = "from Book b, authors a, BookAuthor ba where a.author_id= "
				+ "ba.author.author_id and b.ISBN = ba.book.ISBN and b.ISBN like '%"+ 	search  + "%'";
		
//		String queryString3 = "from Book b, authors a, BookAuthor ba where a.author_id= "
//				+ "ba.author.author_id and b.ISBN = ba.book.ISBN and  like '%"+ 	search  + "%'";

		
		Session currentSession = sessionFactory.getCurrentSession();
		Query query1 = currentSession.createQuery(queryString1);
		// query.setMaxResults(100);
		List<Object > list1 = query1.list();
		
		Query query2 = currentSession.createQuery(queryString2);
		// query.setMaxResults(100);
		List<Object > list2 = query2.list();
		
		list1.addAll(list2);
		
		Query query3 = currentSession.createQuery(queryString3);
		// query.setMaxResults(100);
		List<Object > list3 = query3.list();
		
		list1.addAll(list3);
		

		List<SearchResult> result = new ArrayList<SearchResult>();
		
		prepareSearchResult(result, list1);
		
//		System.out.print(result.get(0).getISBN());
		
		theModel.addAttribute("searchResults",result);
		theModel.addAttribute("flag",1);
	
		BookLoanRequest bookLoanRequest = new BookLoanRequest();
		
		theModel.addAttribute("bookLoanRequest",bookLoanRequest);
		
		

		return "search";
		
	}
	
	private void prepareSearchResult(List<SearchResult> result, List<Object> list) {
		
		Book book = new Book();
		
		Author author = new Author();
		
		List<Author> authorList = new ArrayList<Author>();

		Map<String, SearchResult> isbn = new HashMap<String, SearchResult>();

		for (Object b : list) {

			Object[] arr = (Object[]) b;

			book = (Book) arr[0];
			author = (Author) arr[1];
			int authorFlag= 0;
			
//System.out.println(book.getISBN());

			if (isbn.containsKey(book.getISBN())) {
				authorList = isbn.get(book.getISBN()).getAuthor();
				
				for(Author a : authorList) {
					if (a.getName().equals(author.getName())) {
						authorFlag = 1;
					}
				}
			
				if(authorFlag ==0) {
					authorList.add(author);
					SearchResult  searchRe = new SearchResult();
					searchRe = isbn.get(book.getISBN());
					searchRe.setAuthor(authorList);

					isbn.put(book.getISBN(), searchRe);
				}		
//	System.out.println(book.getISBN() + "Book Title: "+ book.getTitle() + " Author Name : " +author.getName());

				

			} else {
				SearchResult result2 = new SearchResult();
				result2.setAvailable(book.isAvailable());
				result2.setISBN(book.getISBN());
				result2.setTitle(book.getTitle());
				
				
			
		
				List<Author> authors = new ArrayList<Author>();
				authors.add(author);
				result2.setAuthor(authors);
				
				isbn.put(book.getISBN(),result2);
			}
		}

		for (Map.Entry<String, SearchResult> map : isbn.entrySet()) {
			result.add(map.getValue());
//	
//			System.out.println(map.getValue().getISBN());
		}
	}
	
	@PostMapping(value= "/viewCheckOutForm")
	@Transactional 
	public @ResponseBody  String viewCheckOutForm(@RequestParam("isbn") String isbn, @RequestParam("borrowerId") String borrowerId ,Model theModel) {
		
		
		System.out.println(isbn );
		System.out.print(borrowerId  );
		
		RestResponse response = new RestResponse();
		String borrowerQuery = "from Borrower where cardId=" + Integer.parseInt(borrowerId);

	

		Session currentSession = sessionFactory.getCurrentSession();
		Query query = currentSession.createQuery(borrowerQuery);
		Object object = query.uniqueResult();
		

		if (object == null) {

			response.setError("Borrower not in the database");
			System.out.print("Borrower not in the database");
			response.setSuccess(false);
			return response.getError();

		} else {

			Borrower borrower = (Borrower) object;

			String loanQuery = "from BookLoan where borrower=" + Integer.parseInt(borrowerId) + " and dateIn IS null";
			Query query2 = currentSession.createQuery(loanQuery);
			List<BookLoan> bookLoans = query2.list();
			
			for(BookLoan bLoan : bookLoans ) {
				
				System.out.println(bLoan.getBorrower().getbName());
			}
			
			if (bookLoans.size() > 2) {
				response.setError("3 Book have been issued to the borrower");
				System.out.print("3 Book have been issued to the borrower");
				
				response.setSuccess(false);
				
				return response.getError();

			} else {	

				try {
					String bookQuery = "from Book where ISBN LIKE '" + isbn + "'";
					Query bookQ = currentSession.createQuery(bookQuery);

					Object object2 = bookQ.uniqueResult();

					Book book = (Book) object2;

					if (book == null || !book.isAvailable()) {
						response.setSuccess(false);
						response.setError("Book is not available. It's checked out");
						System.out.print("Book is not available. It's checked out");

						return response.getError();
					} else {

						BookLoan bookLoan = new BookLoan(book, borrower);

						
						Date date = new Date();
						bookLoan.setDateOut(date);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.add(Calendar.DAY_OF_YEAR, 14);

						bookLoan.setDueDate(calendar.getTime());
	
						response.setResult("Added Succesfull");
						response.setSuccess(true);

						book.setAvailable(false);
						
						currentSession.saveOrUpdate(bookLoan );
					
						currentSession.saveOrUpdate(book);
					}

				} catch (Exception e) {
					response.setSuccess(false);
					response.setError("Issue in Database");
//					transaction.rollback();

				}
			}
		}

		return response.getResult();
	  }
	
	
	@RequestMapping("/viewCheckInPage")
	public String viewCheckInPage(Model theModel) {
		
		CheckInBook checkInBook = new CheckInBook();
		
		theModel.addAttribute("checkInBook",checkInBook);
		
		theModel.addAttribute("flag","0");
		return "checkIn";
	}
	
	
	
	@RequestMapping("/searchCheckedInBooks")
	@Transactional 
	public String searchCheckedInBooks(@ModelAttribute("checkInBook") CheckInBook book ,Model theModel) {

		Session currentSession = this.sessionFactory.getCurrentSession();
			
		if (book.getIsbn().length() > 0) {
			
//			String queryString = "from Book b, authors a, BookAuthor ba where a.author_id= ba.author.author_id"
//					+ " and b.ISBN = ba.book.ISBN "
//					+ "and b.ISBN LIKE '"
//					+ book.getIsbn()+"'";
			
		
//			Query query = currentSession.createQuery(queryString);
			String queryString = "SELECT bl.Card_id, bl.Isbn, b.Title, bl.date_out, bl.due_date FROM book_loan  AS bl JOIN book AS b  "
					+ "on bl.Isbn = b.Isbn WHERE bl.Isbn LIKE '%" + book.getIsbn() + "%' AND b.available=0";
			
			System.out.print(queryString );
			
			Query query = currentSession.createSQLQuery(queryString);			
			
			List<Object[]> bookLoan = query.list();
			
			System.out.print(bookLoan.size());
					
			List<SearchResult> result = new ArrayList<SearchResult>();
			
			for(int i = 0 ; i< bookLoan.size(); i++) {
				
				Object[] objects = bookLoan.get(i);
				SearchResult result1 = new SearchResult();
				result1.setBorrower_id((int)objects[0]);	
				result1.setISBN((String)objects[1]);
				result1.setTitle((String)objects[2]);
				result1.setDate_out((Date)objects[3]);
				result1.setDue_date((Date)objects[4]);
				
				result.add(result1);
			}	
			theModel.addAttribute("list",result);
			theModel.addAttribute("flag",1);
		
		} else if (book.getCardId() != null & book.getName() != null) {
			
			String name = book.getName();
			String cardId = book.getCardId();
			
			// Transaction transaction = session.beginTransaction();
			String queryString = "";
			if (name.length() > 0 && cardId.length() > 0) {
				queryString = "from Borrower where cardId=" + Integer.parseInt(cardId) + " or bName like '%" + name
						+ "%'";
			} else if (cardId.length() == 0 && name.length() > 0) {
				queryString = "from Borrower where bName like '%" + name + "%'";
			} else if (cardId.length() > 0 && name.length() == 0) {
				queryString = "from Borrower where cardId=" + Integer.parseInt(cardId);
			}

			Query query = currentSession.createQuery(queryString);

			Object object = query.list();

			List<SearchResult> list = new ArrayList<SearchResult>();

			List<Borrower> borrower = (List<Borrower>) object;

			for (Borrower borrower2 : borrower) {

				List<BookLoan> bookLoans = borrower2.getBookLoans();

				for (BookLoan bookLoan : bookLoans) {
					SearchResult result = new SearchResult();
					if (bookLoan.getDateIn() == null) {
						result.setISBN(bookLoan.getBook().getISBN());
						result.setTitle(bookLoan.getBook().getTitle());
						
						result.setDate_out(bookLoan.getDateOut());
						result.setDue_date(bookLoan.getDueDate());
						result.setDate_in(bookLoan.getDateIn());
						result.setBorrower(borrower2);
						list.add(result);
					}

				}

			}
			
			theModel.addAttribute("list",list);
			theModel.addAttribute("flag",1);
			
			CheckInBook checkInBook = new CheckInBook();
			theModel.addAttribute("checkInBook",checkInBook);
			
			theModel.addAttribute("borrowerId",book.getCardId());
			if (book.getName() != null) {
				theModel.addAttribute("name",book.getName());
			}
			
		}
			
			return "checkIn";
			
//			return list;
		}

	
	@PostMapping("/checkIn")
	@Transactional
	public @ResponseBody  String checkInBook(@RequestParam("isbn") String isbn,@RequestParam("borrowerId") String borrowerId, Model theModel) {

		System.out.print(isbn + " " + borrowerId);
		String messageString = "";
		RestResponse response = new RestResponse();
		Session currentSession = this.sessionFactory.getCurrentSession();
	
		try {
				if(isbn.length() != 13) {
					
					for(int i = isbn.length() ; i< 10 ;i++) {
						isbn = "0" + isbn ;
					}
					
				}
			String paramString = "from BookLoan where book.ISBN='" + (isbn) + "' and borrower.cardId="+ Integer.parseInt(borrowerId);
		
			System.out.print(paramString );
			Query query = currentSession.createQuery(paramString);

			BookLoan bookLoan = (BookLoan) query.uniqueResult();

		
			java.util.Date utilDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			

			System.out.print(sqlDate );
			
			System.out.print(bookLoan.getLoanId());

			bookLoan.setDateIn(utilDate);
			
			bookLoan.getBook().setAvailable(true);

			currentSession.saveOrUpdate(bookLoan);

			response.setSuccess(true);
			response.setResult("Book Checked In Succesfully");
			
			messageString="Book Checked In Succesfully";
			System.out.print(messageString);
		} 
		catch (Exception exception) {

			response.setSuccess(false);
			response.setError("Error Occurred. Try after some time");
			messageString = "Error Occurred. Try after some time";
			System.out.print(messageString + exception.getLocalizedMessage());
		}


		return messageString;
		}
	
	
		@RequestMapping("/addBorrowerPage")
		public String addBorrowerPage(Model theModel) {
			
			BorrowerData borrowerData = new BorrowerData();
			
			theModel.addAttribute("borrowerData",borrowerData);
			
			return "addBorrower";
			
		}
		
		@GetMapping("/addBorrower")
		@Transactional  
		public @ResponseBody String addBorrower(@ModelAttribute("borrowerData") BorrowerData borrowerData , Model theModel) {
			
			RestResponse response = new RestResponse();
			Session currentSession = this.sessionFactory.getCurrentSession();
			
			Borrower borrower = new Borrower();
			borrower.setAddress(borrowerData.getAddress());
			borrower.setSsn(borrowerData.getSsn());
			borrower.setPhone(borrowerData .getPhone());
			borrower.setbName(borrowerData.getName());
			

			String queryString = "from Borrower where ssn=:ssn";
			Query query = currentSession.createQuery(queryString);
			query.setString("ssn", borrower.getSsn());
			Object object = query.uniqueResult();

			if (object != null) {
				response.setError("Account already exists");
				System.out.print("Account already exists");
				response.setSuccess(false);
				return "Account already exists";

			} else {

				currentSession.saveOrUpdate(borrower);
				query = currentSession.createQuery(queryString);
				query.setParameter("ssn", borrower.getSsn());
				object = query.uniqueResult();
			
				Borrower borrower2 = (Borrower) object;
				response.setSuccess(true);
				response.setResult(String.valueOf(borrower2.getCardId()));
				System.out.print(borrower2.getCardId());
				
				System.out.print("Successful borrower added");
				return "Successful borrower added";
			}
			
			
		}
		
		@GetMapping("/viewFinePage")
		public String viewFinePage(Model theModel ) {
			theModel.addAttribute("showFineFlag",0);
			theModel.addAttribute("showPayFine",0);
			theModel.addAttribute("finePaidFlag",0);
			return "fine";
		}
		
		
		@GetMapping("/addFine")
		@Transactional 
		public String addFine(Model theModel) {

			RestResponse response = new RestResponse();
			Session currentSession = this.sessionFactory.getCurrentSession();


			try {
				String paramString = "from BookLoan where dateIn = null";
				//String paramString = "select * from book_loan where date_in is null";
				Query query = currentSession.createQuery(paramString);

				List<BookLoan> bookLoans = query.list();

				Fine fine = null;
				Date date = new Date();
				
				java.util.Date utilDate = new java.util.Date();
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				
				for (BookLoan bookLoan : bookLoans) {
					
					currentSession = sessionFactory.getCurrentSession();

					if (bookLoan.getDueDate().before(sqlDate)) {

						
						if(bookLoan.getFine()==null){
							fine = new Fine(bookLoan);
							
						}else{
							fine = bookLoan.getFine();
						}
						
//						System.out.println(bookLoan.getDueDate());
						
							
						long diff = sqlDate.getTime() - bookLoan.getDueDate().getTime();
			
						
						long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
						System.out.println(fine.getBookLoan().getLoanId() +" "+days + "  " + Double.toString(days * 0.25));
						
						
						
						fine.setFineAmount(Double.toString(days * 0.25));
						
						currentSession.saveOrUpdate(fine);
					}

				}
				response.setSuccess(true);
				response.setResult("Fine updated successfully");
				System.out.print("Fine updated successfully");

			} catch (Exception e) {
			
				response.setError("Error while updating. Try Again");
				response.setSuccess(false);
				System.out.print("Error while updating. Try Again");
			}
			
			theModel.addAttribute("showFineFlag",0);	
			theModel.addAttribute("showPayFine",0);
			theModel.addAttribute("finePaidFlag",1);
			return "fine";
		}
		
		
		@GetMapping("/showFines")
		@Transactional 
		public String getAllFines(Model theModel){
			
			//System.out.println("Paid " + paid);
			Session session = this.sessionFactory.openSession();
			//Transaction transaction = session.beginTransaction();
			
			StringBuilder builder = new StringBuilder();
			
			builder.append("select b.card_id , sum(fine_amt) from fine f ,book_loan b where b.loan_id= f.loan_id and f.paid=0");

			
			builder.append(" group by b.card_id");
			
			Query query = session.createSQLQuery(builder.toString());
			
			List<Object[]> object = query.list();
			
			List<FineResponse> fineResponses = new ArrayList<FineResponse>();
			
			for(Object[] object2:object){
				FineResponse fineResponse = new FineResponse();
				fineResponse.setCardId((Integer)object2[0]);
				fineResponse.setAmount(Double.toString((Double)object2[1]));
				
				
				fineResponses.add(fineResponse);
			}
			
			theModel.addAttribute("showFineFlag",1);
			theModel.addAttribute("finePaidFlag",0);
			theModel.addAttribute("showPayFine",0);
			theModel.addAttribute("fineResponses",fineResponses);
			
			return "fine";
			
//			return fineResponses;
		}
		
		@GetMapping("/viewPayFinePage")
		public String  viewPayFinePage(Model theModel) {
			
			theModel.addAttribute("showFineFlag",0);
			
			theModel.addAttribute("showPayFine",1);
			theModel.addAttribute("finePaidFlag",0);
			
			return "fine";			
			
		}
		@PostMapping("getFineForCardId")
		@Transactional 
		public String getFineForCardId(HttpServletRequest request, Model theModel){
		

			System.out.print(request.getParameter("borrowerId"));
			
			Session currentSession = this.sessionFactory.openSession();
			String param = "from BookLoan where borrower.cardId="+ Integer.parseInt(request.getParameter("borrowerId"));
					
			Query query = currentSession.createQuery(param);
			
			List<BookLoan> bookLoans = query.list();
			
			List<Fine> fines = new ArrayList<Fine>();
			
			
			for(BookLoan bookLoan :bookLoans){
				if(bookLoan.getFine()!=null){
					if(bookLoan.getFine().isPaid()){
						fines.add(bookLoan.getFine());
						System.out.println(bookLoan.getLoanId() +" " + bookLoan.getFine().getFineAmount());
					}
					if(!bookLoan.getFine().isPaid()){
						fines.add(bookLoan.getFine());
						
						System.out.println(bookLoan.getLoanId() +" " + bookLoan.getFine().getFineAmount());
					}
				}			
			}
			
			theModel.addAttribute("fines",fines);
			theModel.addAttribute("finePaidFlag",0);
			theModel.addAttribute("showPayFine",1);
			theModel.addAttribute("showFineFlag",0);
			
			theModel.addAttribute("borroweId",request.getParameter("borrowerId"));
			
//			return "fine";
			return "fine";
		}
		
		
		
		@PostMapping("/payFine")
		@Transactional 
		public @ResponseBody String payFine(@RequestParam("fineId") String fineId) {

			RestResponse response = new RestResponse();
			Session currentSession = this.sessionFactory.getCurrentSession();
	
			String paramString = "from Fine where fineId="+ Integer.parseInt(fineId);// + " and dateIn !=null";
			System.out.print(paramString);
			Query query = currentSession.createQuery(paramString);
			
			Fine fine = (Fine) query.uniqueResult();
			System.out.print(fine.getFineAmount());
			
			if(fine!=null && fine.getBookLoan().getDateIn()!=null){
				fine.setPaid(true);
			
				currentSession.saveOrUpdate(fine);
				
				response.setResult("Fine paid successfully");
				
				
			}else{
				response.setResult("Book is not checked in");
			}
			
			System.out.print(response.getResult());
			 
			return response.getResult();
			
		}
	

	}

