package com.corteva.service.product.detail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class Restriction.
 */
public class Restriction {
	
	/** The rate. */
	private String rate;
	
	/** The condition. */
	private String condition;
	
	/** The state. */
	private String state;
	
	/** The rate unit. */
	@JsonProperty("rate_unit")
	private String rateUnit;
	
	/** The commodity id. */
	@JsonProperty("commodity_id")
	private String commodityId;
	
	/** The commodity name. */
	@JsonProperty("commodity_name")
	private String commodityName;
	
	/**
	 * Gets the rate.
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}
	
	/**
	 * Sets the rate.
	 * @param rate the rate to set
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}
	
	/**
	 * Gets the condition.
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}
	
	/**
	 * Sets the condition.
	 * @param condition the condition to set
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	/**
	 * Gets the state.
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Sets the state.
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * Gets the rate unit.
	 * @return the rateUnit
	 */
	public String getRateUnit() {
		return rateUnit;
	}
	
	/**
	 * Sets the rate unit.
	 * @param rateUnit the rateUnit to set
	 */
	public void setRateUnit(String rateUnit) {
		this.rateUnit = rateUnit;
	}
	
	/**
	 * Gets the commodity id.
	 * @return the commodityId
	 */
	public String getCommodityId() {
		return commodityId;
	}
	
	/**
	 * Sets the commodity id.
	 * @param commodityId the commodityId to set
	 */
	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}
	
	/**
	 * Gets the commodity name.
	 * @return the commodityName
	 */
	public String getCommodityName() {
		return commodityName;
	}
	
	/**
	 * Sets the commodity name.
	 * @param commodityName the commodityName to set
	 */
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Restriction [rate=" + rate + ", condition=" + condition + ", state=" + state + ", rateUnit=" + rateUnit
				+ ", commodityId=" + commodityId + ", commodityName=" + commodityName + "]";
	}
}
