package com.corteva.service.product.detail.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class DotDescription.
 */
public class DotDescription {
	
	/** The dot scenario. */
	@JsonProperty("dot_scenario")
	private List<DotScenario> dotScenario;
	
	/**
	 * Gets the dot scenario.
	 * @return the dotScenario
	 */
	public List<DotScenario> getDotScenario() {
		return dotScenario;
	}
	
	/**
	 * Sets the dot scenario.
	 * @param dotScenario the dotScenario to set
	 */
	public void setDotScenario(List<DotScenario> dotScenario) {
		this.dotScenario = dotScenario;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DotDescription [dotScenario=" + dotScenario + "]";
	}
	
}
