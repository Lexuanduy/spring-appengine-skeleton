package com.fcs.entity;

public class OrderLog {

	public OrderLog(String orderCode) {
		this.orderCode = orderCode;
		this.loadedMoney = (long) 0.0;
		this.neededMoney = (long) 0.0;
		this.waitMoney = (long) 0.0;
		this.total = 0;
		this.confirm = 0;
		this.waitOrder = 0;
		this.successOrder = 0;
		this.nextOrder = 0;
		this.lockOrder = 0;
		this.errorOrder = 0;
		this.finishOrder = 0;
		this.stt = 0;
	}

	public void setOrderLog(Long long1, Long long2, String status, String confirm, String updatedAt) {
		this.total = this.total + 1;
		if (confirm.equalsIgnoreCase("Chốt")) {
			this.confirm = this.confirm + 1;
		}
		if (status.equalsIgnoreCase("Chờ")) {
			this.waitOrder = this.waitOrder + 1;
		}
		if (status.equalsIgnoreCase("Hoàn thành")) {
			this.successOrder = this.successOrder + 1;
		}
		if (status.equalsIgnoreCase("Bỏ qua")) {
			this.nextOrder = this.nextOrder + 1;
		}
		if (status.equalsIgnoreCase("Bị khóa")) {
			this.lockOrder = this.lockOrder + 1;
		}
		if (status.equalsIgnoreCase("Lỗi")) {
			this.errorOrder = this.errorOrder + 1;
		}
		this.finishOrder = this.errorOrder + this.lockOrder + this.nextOrder + this.successOrder + this.waitOrder;
		this.loadedMoney = this.loadedMoney + long1;
		this.neededMoney = this.neededMoney + long2;
		this.waitMoney = this.neededMoney - this.loadedMoney;
		this.updatedAt = updatedAt;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getLoadedMoney() {
		return loadedMoney;
	}

	public void setLoadedMoney(Long loadedMoney) {
		this.loadedMoney = loadedMoney;
	}

	public Long getNeededMoney() {
		return neededMoney;
	}

	public void setNeededMoney(Long neededMoney) {
		this.neededMoney = neededMoney;
	}

	public Long getWaitMoney() {
		return waitMoney;
	}

	public void setWaitMoney(Long waitMoney) {
		this.waitMoney = waitMoney;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getConfirm() {
		return confirm;
	}

	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}

	public int getWaitOrder() {
		return waitOrder;
	}

	public void setWaitOrder(int waitOrder) {
		this.waitOrder = waitOrder;
	}

	public int getSuccessOrder() {
		return successOrder;
	}

	public void setSuccessOrder(int successOrder) {
		this.successOrder = successOrder;
	}

	public int getNextOrder() {
		return nextOrder;
	}

	public void setNextOrder(int nextOrder) {
		this.nextOrder = nextOrder;
	}

	public int getLockOrder() {
		return lockOrder;
	}

	public void setLockOrder(int lockOrder) {
		this.lockOrder = lockOrder;
	}

	public int getErrorOrder() {
		return errorOrder;
	}

	public void setErrorOrder(int errorOrder) {
		this.errorOrder = errorOrder;
	}

	public int getStt() {
		return stt;
	}

	public void setStt(int stt) {
		this.stt = stt;
	}

	public int getFinishOrder() {
		return finishOrder;
	}

	public void setFinishOrder(int finishOrder) {
		this.finishOrder = finishOrder;
	}

	public String orderCode;
	public String updatedAt;
	public Long loadedMoney;
	public Long neededMoney;
	public Long waitMoney;
	public int total;
	public int confirm;
	public int waitOrder;
	public int successOrder;
	public int nextOrder;
	public int lockOrder;
	public int errorOrder;
	public int finishOrder;
	public int stt;
}
