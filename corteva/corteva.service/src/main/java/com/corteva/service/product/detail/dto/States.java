package com.corteva.service.product.detail.dto;

import java.util.List;

/**
 * The Class States.
 */
public class States {
	
	/** The state. */
	private List<String> state;
	
	/**
	 * Gets the state.
	 * @return the state
	 */
	public List<String> getState() {
		return state;
	}
	
	/**
	 * Sets the state.
	 * @param state the state to set
	 */
	public void setState(List<String> state) {
		this.state = state;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "States [state=" + state + "]";
	}
}
