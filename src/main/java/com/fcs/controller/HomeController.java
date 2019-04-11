package com.fcs.controller;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuthException;

@Controller

public class HomeController {
	static Logger logger = Logger.getLogger(HomeController.class.getName());
	static {
		FirebaseApp.initializeApp();
	}

	@GetMapping(value = "/")
	public String index() throws InterruptedException, ExecutionException, FirebaseAuthException {

		return "views/home/index";
	}

	@GetMapping(value = "/welcome")
	public String welcome(@CookieValue("idToken") String idToken)
			throws InterruptedException, ExecutionException, FirebaseAuthException {

		return "welcome";
	}

	@GetMapping(value = "/403")
	public String error403() throws InterruptedException, ExecutionException {

		return "views/error/403";
	}
}
