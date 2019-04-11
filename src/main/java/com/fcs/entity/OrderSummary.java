package com.fcs.entity;

public class OrderSummary {

	public OrderSummary(String day) {
		this.day = day;
		this.loadedMoneyVtt = (long) 0;
		this.neededMoneyVtt = (long) 0;
		this.waitMoneyVtt = (long) 0;
		this.loadedMoneyVms = (long) 0;
		this.neededMoneyVms = (long) 0;
		this.waitMoneyVms = (long) 0;
		this.loadedMoneyVnp = (long) 0;
		this.neededMoneyVnp = (long) 0;
		this.waitMoneyVnp = (long) 0;
	}

	public void setOrderSummaryVtt(Order order) {
		this.loadedMoneyVtt = (long) (this.loadedMoneyVtt + order.getLoadedMoney());
		this.neededMoneyVtt = (long) (this.neededMoneyVtt + order.getNeededMoney());
		this.waitMoneyVtt = this.neededMoneyVtt - this.loadedMoneyVtt;
		this.loadedMoneyTotal = this.loadedMoneyVtt + this.loadedMoneyVms + this.loadedMoneyVnp;
		this.neededMoneyTotal = this.neededMoneyVtt + this.neededMoneyVms + this.neededMoneyVnp;
		this.waitMoneyTotal = this.neededMoneyTotal - this.loadedMoneyTotal;
	}

	public void setOrderSummaryVms(Order order) {
		this.loadedMoneyVms = (long) (this.loadedMoneyVms + order.getLoadedMoney());
		this.neededMoneyVms = (long) (this.neededMoneyVms + order.getNeededMoney());
		this.waitMoneyVms = this.neededMoneyVms - this.loadedMoneyVms;
		this.loadedMoneyTotal = this.loadedMoneyVtt + this.loadedMoneyVms + this.loadedMoneyVnp;
		this.neededMoneyTotal = this.neededMoneyVtt + this.neededMoneyVms + this.neededMoneyVnp;
		this.waitMoneyTotal = this.neededMoneyTotal - this.loadedMoneyTotal;
	}

	public void setOrderSummaryVnp(Order order) {
		this.loadedMoneyVnp = (long) (this.loadedMoneyVnp + order.getLoadedMoney());
		this.neededMoneyVnp = (long) (this.neededMoneyVnp + order.getNeededMoney());
		this.waitMoneyVnp = this.neededMoneyVnp - this.loadedMoneyVnp;
		this.loadedMoneyTotal = this.loadedMoneyVtt + this.loadedMoneyVms + this.loadedMoneyVnp;
		this.neededMoneyTotal = this.neededMoneyVtt + this.neededMoneyVms + this.neededMoneyVnp;
		this.waitMoneyTotal = this.neededMoneyTotal - this.loadedMoneyTotal;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Long getLoadedMoneyVtt() {
		return loadedMoneyVtt;
	}

	public void setLoadedMoneyVtt(Long loadedMoneyVtt) {
		this.loadedMoneyVtt = loadedMoneyVtt;
	}

	public Long getNeededMoneyVtt() {
		return neededMoneyVtt;
	}

	public void setNeededMoneyVtt(Long neededMoneyVtt) {
		this.neededMoneyVtt = neededMoneyVtt;
	}

	public Long getWaitMoneyVtt() {
		return waitMoneyVtt;
	}

	public void setWaitMoneyVtt(Long waitMoneyVtt) {
		this.waitMoneyVtt = waitMoneyVtt;
	}

	public Long getLoadedMoneyVms() {
		return loadedMoneyVms;
	}

	public void setLoadedMoneyVms(Long loadedMoneyVms) {
		this.loadedMoneyVms = loadedMoneyVms;
	}

	public Long getNeededMoneyVms() {
		return neededMoneyVms;
	}

	public void setNeededMoneyVms(Long neededMoneyVms) {
		this.neededMoneyVms = neededMoneyVms;
	}

	public Long getWaitMoneyVms() {
		return waitMoneyVms;
	}

	public void setWaitMoneyVms(Long waitMoneyVms) {
		this.waitMoneyVms = waitMoneyVms;
	}

	public Long getLoadedMoneyVnp() {
		return loadedMoneyVnp;
	}

	public void setLoadedMoneyVnp(Long loadedMoneyVnp) {
		this.loadedMoneyVnp = loadedMoneyVnp;
	}

	public Long getNeededMoneyVnp() {
		return neededMoneyVnp;
	}

	public void setNeededMoneyVnp(Long neededMoneyVnp) {
		this.neededMoneyVnp = neededMoneyVnp;
	}

	public Long getWaitMoneyVnp() {
		return waitMoneyVnp;
	}

	public void setWaitMoneyVnp(Long waitMoneyVnp) {
		this.waitMoneyVnp = waitMoneyVnp;
	}

	public Long getLoadedMoneyTotal() {
		return loadedMoneyTotal;
	}

	public void setLoadedMoneyTotal(Long loadedMoneyTotal) {
		this.loadedMoneyTotal = loadedMoneyTotal;
	}

	public Long getNeededMoneyTotal() {
		return neededMoneyTotal;
	}

	public void setNeededMoneyTotal(Long neededMoneyTotal) {
		this.neededMoneyTotal = neededMoneyTotal;
	}

	public Long getWaitMoneyTotal() {
		return waitMoneyTotal;
	}

	public void setWaitMoneyTotal(Long waitMoneyTotal) {
		this.waitMoneyTotal = waitMoneyTotal;
	}

	public String day;
	public Long loadedMoneyVtt;
	public Long neededMoneyVtt;
	public Long waitMoneyVtt;
	public Long loadedMoneyVms;
	public Long neededMoneyVms;
	public Long waitMoneyVms;
	public Long loadedMoneyVnp;
	public Long neededMoneyVnp;
	public Long waitMoneyVnp;
	public Long loadedMoneyTotal;
	public Long neededMoneyTotal;
	public Long waitMoneyTotal;
}
