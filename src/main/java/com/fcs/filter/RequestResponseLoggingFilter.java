package com.fcs.filter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fcs.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

public class RequestResponseLoggingFilter implements Filter {
	Logger logger = Logger.getLogger(RequestResponseLoggingFilter.class.getName());

	public RequestResponseLoggingFilter() {
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		logger.info("LogFilter init!");
	}

	@Override
	public void destroy() {
		logger.info("LogFilter destroy!");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		Cookie[] cookies = req.getCookies();
		String idToken = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("idToken")) {
					idToken = cookie.getValue();
				}
			}
		}

		logger.info("idToken: " + idToken);

		FirebaseToken decodedToken;
		String email = null;
		try {
			decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
			email = decodedToken.getEmail();
		} catch (FirebaseAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("email: " + email);

		if (email == null) {
			logger.info("email null");
			res.sendRedirect("/error403");
		}
		if (!email.equals("ongnoi@gmail.com")) {
			res.sendRedirect("/error403");
		} else {
			// check role in user
			Firestore db = FirestoreOptions.getDefaultInstance().getService();
			ApiFuture<QuerySnapshot> future = db.collection("User").whereEqualTo("email", email).get();
			List<QueryDocumentSnapshot> documents;
			User user = null;

			try {
				documents = future.get().getDocuments();
				for (DocumentSnapshot document : documents) {
					user = document.toObject(User.class);
				}
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (user == null) {
				logger.info("user null");
				res.sendRedirect("/error403");
			}
			if (user.getRole().equals("Admin") || user.getRole().equals("Manager")) {
				chain.doFilter(request, response);
			} else {
				logger.info("check role not exist.");
				res.sendRedirect("/error403");
			}

		}

//		chain.doFilter(request, response);
	}

}
