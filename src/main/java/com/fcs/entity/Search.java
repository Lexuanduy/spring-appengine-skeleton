package com.fcs.entity;

public class Search {
	public static class Request {

		public String getOrderCode() {
			return orderCode;
		}

		public void setOrderCode(String orderCode) {
			this.orderCode = orderCode;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getTypeService() {
			return typeService;
		}

		public void setTypeService(String typeService) {
			this.typeService = typeService;
		}

		public String getHouseNetwork() {
			return houseNetwork;
		}

		public void setHouseNetwork(String houseNetwork) {
			this.houseNetwork = houseNetwork;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getFromDate() {
			return fromDate;
		}

		public void setFromDate(String fromDate) {
			this.fromDate = fromDate;
		}

		public String getToDate() {
			return toDate;
		}

		public void setToDate(String toDate) {
			this.toDate = toDate;
		}

		public String getConfirm() {
			return confirm;
		}

		public void setConfirm(String confirm) {
			this.confirm = confirm;
		}

		public int getLoadedMoney() {
			return loadedMoney;
		}

		public void setLoadedMoney(int loadedMoney) {
			this.loadedMoney = loadedMoney;
		}

		private String orderCode;
		private String phone;
		private String typeService;
		private String houseNetwork;
		private String status;
		private String fromDate;
		private String toDate;
		private String confirm;
		private int loadedMoney;
	};
}
