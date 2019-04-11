package com.fcs.spring;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.fcs.entity.Transaction;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(
				"D:\\ProjectsFCS\\new-yinyang\\yinyangFcs\\yinyangFcs\\src\\main\\java\\com\\fcs\\yinyangFcs\\entity\\config\\yinyangpaygate-firebase-adminsdk-0lxlm-36e7e77dfa.json"));
		FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials)
				.setProjectId("yinyangpaygate").build();
		FirebaseApp.initializeApp(options);
		Firestore db = FirestoreClient.getFirestore();
		db.collection("Transactions").whereEqualTo("messageCode", 1).whereEqualTo("loaded", false)
				.addSnapshotListener(new EventListener<QuerySnapshot>() {
					@Override
					public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirestoreException e) {
						if (e != null) {
							System.err.println("Listen failed: " + e);
							return;
						}

						for (DocumentChange dc : snapshots.getDocumentChanges()) {
							switch (dc.getType()) {
							case ADDED:
								System.out.println("re send");
								Transaction transaction = dc.getDocument().toObject(Transaction.class);
								String url = transaction.getUrl();
								System.out.println("url: " + url);
								int code = transaction.getMessageCode();
								System.out.println("message code : " + code);
								Long transId = transaction.getTransId();
								System.out.println("transId : " + transId);
								int amount = transaction.getMoney();
								System.out.println("money: " + amount);
								String serviceTransId = transaction.getServiceTransId();
								System.out.println("serviceTransId: " + serviceTransId);
								String hash = transaction.getHash();
								System.out.println("hash: " + hash);
								String serial = transaction.getSerial();
								System.out.println("serial: " + serial);
								System.out.println("callback URL: " + url + "?code=" + String.valueOf(code)
										+ "&transId=" + String.valueOf(transId) + "&amount=" + String.valueOf(amount)
										+ "&serviceTransId=" + serviceTransId + "&hash=" + hash + "&serial=" + serial);
								// call back url
								try {
									Response content = Jsoup.connect(url).ignoreContentType(true).timeout(60 * 1000)
											.data("code", String.valueOf(code)).data("transId", String.valueOf(transId))
											.data("amount", String.valueOf(amount))
											.data("serviceTransId", serviceTransId).data("hash", hash)
											.data("serial", serial).method(Method.GET).followRedirects(true)
											.ignoreHttpErrors(true).execute();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								// Update an existing document
								DocumentReference docRef = db.collection("Transactions").document(transId.toString());
								// (async) Update one field
								ApiFuture<WriteResult> future = docRef.update("loaded", true);
								// endcall
								break;
							default:
								break;
							}
						}
					}
				});
	}
}
