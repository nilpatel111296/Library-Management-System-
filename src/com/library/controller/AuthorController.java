package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import com.library.dao.AuthorDAO;
import com.library.entity.Author;

@Controller
@RequestMapping("/author")
public class AuthorController {

	@Autowired 
	private AuthorDAO authorDAO;
	
	@RequestMapping("/Authorlist")
	public String listAuthors(Model theModel) {
		List<Author > theAuthor = authorDAO.getAuthors();
		theModel.addAttribute("authors",theAuthor);
		return "list-authors";
	}
	

}
