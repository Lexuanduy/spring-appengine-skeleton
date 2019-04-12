package com.fcs.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fcs.entity.Card;
import com.fcs.entity.Client;
import com.fcs.entity.Order;
import com.fcs.entity.Transaction;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@RestController(value = "CardController")
public class CardController {
	Logger logger = Logger.getLogger(CardController.class.getName());

	@RequestMapping(value = "/api/card/post", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createCard(@RequestParam("serviceId") String serviceId, @RequestParam("type") String type,
			@RequestParam("serial") String serial, @RequestParam("code") String code, @RequestParam("money") int money,
			@RequestParam("serviceTransId") String serviceTransId, @RequestParam("hash") String hash)
			throws InterruptedException, ExecutionException, IOException {
		Firestore db = FirestoreClient.getFirestore();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> mapData = new HashMap<String, Object>();
		ApiFuture<QuerySnapshot> queryOrder2 = db.collection("Orders").whereEqualTo("status", "Đang nạp").get();
		List<Order> order2s = queryOrder2.get().toObjects(Order.class);
		if (order2s.isEmpty()) {
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -103);
			map.put("Message", "Không có order nào đang thực hiện giao dịch!");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		// check service id
		DocumentReference docRefClient = db.collection("Clients").document(serviceId);
		ApiFuture<DocumentSnapshot> futureClient = docRefClient.get();
		DocumentSnapshot documentClient = futureClient.get();
		if (!documentClient.exists()) {
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -51);
			map.put("Message", "serviceId không tồn tại.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}

		DocumentReference docRefCard = db.collection("Cards").document(serial);
		ApiFuture<DocumentSnapshot> futureCard = docRefCard.get();
		DocumentSnapshot documentCard = futureCard.get();
		// check serial
		if (documentCard.exists()) {
			mapData.put("TransId", documentCard.getData().get("transId"));
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -120);
			map.put("Message", "Thẻ đã được sử dụng.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		// check code
		ApiFuture<QuerySnapshot> queryCardCode = db.collection("Cards").whereEqualTo("code", code).get();
		List<Card> listCardCode = queryCardCode.get().toObjects(Card.class);
		if (!listCardCode.isEmpty()) {
//			mapData.put("TransId", );
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -120);
			map.put("Message", "Mã thẻ đã được sử dụng.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		// check serial
		if (documentCard.exists()) {
			mapData.put("TransId", documentCard.getData().get("transId"));
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -120);
			map.put("Message", "Thẻ đã được sử dụng.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		ApiFuture<QuerySnapshot> queryCard = db.collection("Cards").whereEqualTo("serviceTransId", serviceTransId)
				.get();
		List<QueryDocumentSnapshot> cards = queryCard.get().getDocuments();
		DocumentReference docRefCard2 = db.collection("Cards").document(serviceTransId);
		ApiFuture<DocumentSnapshot> futureCard2 = docRefCard2.get();
		DocumentSnapshot documentCard2 = futureCard2.get();
		if (!cards.isEmpty()) {
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -63);
			map.put("Message", "Mã giao dịch của đối tác đã tồn tại.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}

		// Create a Map to store the data we want to set card
		Long _transId = System.currentTimeMillis();
		Long datetimeCreate = System.currentTimeMillis() / 1000;
		Map<String, Object> docData = new HashMap<>();
		docData.put("serviceId", serviceId);
		docData.put("type", type);
		docData.put("serial", serial);
		docData.put("code", code);
		docData.put("money", money);
		docData.put("serviceTransId", serviceTransId);
		docData.put("hash", hash);
		docData.put("transId", _transId);
		docData.put("datetime", datetimeCreate);
		String url = null;
		DocumentReference docRefClients = db.collection("Clients").document(serviceId);
		ApiFuture<DocumentSnapshot> futClient = docRefClients.get();
		DocumentSnapshot document2Client = futClient.get();
		Client client = document2Client.toObject(Client.class);
		logger.info("url: " + client.getCallbackURL());
		docData.put("url", client.getCallbackURL());
		docData.put("status", false);
		ApiFuture<WriteResult> future = db.collection("Cards").document(serial).set(docData);

		// kiem tra transaction
		try {
			Thread.sleep(80 * 1000);
		} catch (InterruptedException ex) {
			// Stop immediately and go home
		}
		Transaction transaction = null;
		DocumentReference docRef = db.collection("Transactions").document(Long.toString(_transId));
		ApiFuture<DocumentSnapshot> fut = docRef.get();
		DocumentSnapshot document2 = fut.get();
		transaction = document2.toObject(Transaction.class);
		logger.info("document transId: " + Long.toString(_transId));
		logger.info("transaction: " + transaction);
		if (transaction == null) {
			mapData.put("Amount", money);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -200);
			map.put("Message", "Giao dịch đang chờ xử lý.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		logger.info("messageCode: " + transaction.getMessageCode());

		if (transaction.getMessageCode() == 1) {
			mapData.put("TransId", transaction.getTransId());
			mapData.put("Amount", money);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", 1);
			map.put("Message", "Nạp thẻ thành công!");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		if (transaction.getMessageCode() == -61) {
			mapData.put("TransId", transaction.getTransId());
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -61);
			map.put("Message", "Số serial hoặc mã thẻ không đúng định dạng.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		if (transaction.getMessageCode() == -120) {
			mapData.put("TransId", transaction.getTransId());
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -120);
			map.put("Message", "Thẻ đã được sử dụng.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		if (transaction.getMessageCode() == -57) {
			mapData.put("TransId", transaction.getTransId());
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -57);
			map.put("Message", "Số serial hoặc mã thẻ không đúng định dạng.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		if (transaction.getMessageCode() == -53) {
			mapData.put("TransId", transaction.getTransId());
			mapData.put("Amount", 0);
			mapData.put("ServiceTransId", serviceTransId);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -53);
			map.put("Message", "Quý khách đã nhập sai quá 5 lần.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
		mapData.put("TransId", transaction.getTransId());
		mapData.put("Amount", money);
		mapData.put("ServiceTransId", serviceTransId);
		JSONObject jsonData = new JSONObject(mapData);
		map.put("Data", jsonData);
		map.put("Code", -9999);
		map.put("Message", "Lỗi hệ thống.");
		JSONObject json = new JSONObject(map);
		return json.toString();
	}

	@RequestMapping(value = "/api/card/info", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String cardInfo(@RequestParam("serviceId") String serviceId, @RequestParam("transId") Long transId,
			@RequestParam("hash") String hash) throws IOException, InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> mapData = new HashMap<String, Object>();

		DocumentReference docRefTransaction = db.collection("Transactions").document(String.valueOf(transId));
		ApiFuture<DocumentSnapshot> futureTransaction = docRefTransaction.get();
		DocumentSnapshot documentTransaction = futureTransaction.get();
		Transaction transaction = documentTransaction.toObject(Transaction.class);

		if (!documentTransaction.exists()) {
			mapData.put("TransId", transId);
			mapData.put("Amount", 0);
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", -60);
			map.put("Message", "Mã giao dịch của hệ thống không tồn tại.");
			JSONObject json = new JSONObject(map);
			return json.toString();
		} else {
			logger.info("transaction exist!");
			if (!transaction.getServiceId().equals(serviceId)) {
				mapData.put("TransId", transId);
				mapData.put("Amount", 0);
				JSONObject jsonData = new JSONObject(mapData);
				map.put("Data", jsonData);
				map.put("Code", -51);
				map.put("Message", "serviceId không tồn tại.");
				JSONObject json = new JSONObject(map);
				return json.toString();
			}
			if (!transaction.getHash().equals(hash)) {
				mapData.put("TransId", transId);
				mapData.put("Amount", 0);
				mapData.put("serviceTransId", documentTransaction.getData().get("serviceTransId"));
				JSONObject jsonData = new JSONObject(mapData);
				map.put("Data", jsonData);
				map.put("Code", -54);
				map.put("Message", "Hash không đúng.");
				JSONObject json = new JSONObject(map);
				return json.toString();
			}
			mapData.put("TransId", transId);
			mapData.put("Amount", transaction.getMoney());
			mapData.put("serviceTransId", transaction.getServiceTransId());
			JSONObject jsonData = new JSONObject(mapData);
			map.put("Data", jsonData);
			map.put("Code", transaction.getMessageCode());
			map.put("Message", transaction.getMessage());
			JSONObject json = new JSONObject(map);
			return json.toString();
		}
	}
}
