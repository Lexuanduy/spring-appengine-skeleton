package com.fcs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fcs.entity.Client;
import com.fcs.entity.Transaction;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

@RestController(value = "OrderController")
public class OrderController {
	Logger logger = Logger.getLogger(OrderController.class.getName());

	@RequestMapping(value = "/listCustomer", method = RequestMethod.GET)
	public @ResponseBody String createCard(@RequestParam(required = true, value = "fromDate") long fromDate,
			@RequestParam(required = true, value = "toDate") long toDate)
			throws InterruptedException, ExecutionException, IOException {
		Firestore db = FirestoreClient.getFirestore();
		HashMap<String, Object> mapData = new HashMap<String, Object>();
		List<Object> listCustomer = new ArrayList<>();
		JSONObject json = null;

		ApiFuture<QuerySnapshot> future = db.collection("Clients").get();
		List<QueryDocumentSnapshot> documents = future.get().getDocuments();

		String serviceId = null;
		String name = null;
		for (QueryDocumentSnapshot document : documents) {
			Client client = document.toObject(Client.class);
			int ttMoney = 0;
			serviceId = client.getServiceId();
			name = client.getName();

			// get transaction thành công
			ApiFuture<QuerySnapshot> futureTrans = db.collection("Transactions").whereEqualTo("status", "Thành công")
					.whereEqualTo("serviceId", serviceId).whereGreaterThanOrEqualTo("datetime", fromDate)
					.whereLessThanOrEqualTo("datetime", toDate).get();
			List<QueryDocumentSnapshot> documentsTrans = futureTrans.get().getDocuments();
			for (DocumentSnapshot documentTran : documentsTrans) {
				Transaction transaction = documentTran.toObject(Transaction.class);
				ttMoney = transaction.getMoney() + ttMoney;
			}
			logger.info("serviceId: " + serviceId);
			logger.info("name: " + name);
			logger.info("ttMoney: " + ttMoney);
			if (ttMoney == 0) {
				name = "";
			} else {
				mapData.put(name, ttMoney);
				json = new JSONObject(mapData);

			}
		}
		listCustomer.add(json);
		return listCustomer.toString();
	}
}
