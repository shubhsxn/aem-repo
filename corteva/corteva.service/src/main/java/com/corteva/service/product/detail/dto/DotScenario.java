package com.corteva.service.product.detail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class DotScenario.
 */
public class DotScenario {
	
	/** The placard text. */
	@JsonProperty("placard_text")
	private String placardText;
	
	/** The placard code. */
	@JsonProperty("placard_code")
	private String placardCode;
	
	/** The quantity multiplier. */
	@JsonProperty("quantity_multiplier")
	private String quantityMultiplier;
	
	/** The unna. */
	private String unna;
	
	/** The class 9. */
	private String class9;
	
	/** The bulk. */
	private String bulk;
	
	/** The size. */
	private String size;
	
	/** The unit. */
	private String unit;
	
	/** The agrian size id. */
	@JsonProperty("agrian_size_id")
	private String agrianSizeId;
	
	/** The weight multiplier. */
	@JsonProperty("weight_multiplier")
	private String weightMultiplier;
	
	/** The containers per case. */
	@JsonProperty("containers_per_case")
	private String containersPerCase;
	
	/** The packaged. */
	private String packaged;
	
	/** The prop 65. */
	private String prop65;
	
	/** The package type. */
	@JsonProperty("package_type")
	private String packageType;
	
	/** The description. */
	private String description;
	
	/** The potentially hazardous. */
	@JsonProperty("potentially_hazardous")
	private String potentiallyHazardous;
	
	/** The combustible liquid. */
	@JsonProperty("combustible_liquid")
	private String combustibleLiquid;
	
	/** The shipping name. */
	@JsonProperty("shipping_name")
	private String shippingName;
	
	/** The hazardous. */
	private String hazardous;
	
	/** The size description. */
	@JsonProperty("size_description")
	private String sizeDescription;
	
	/** The note. */
	private String note;
	
	/**
	 * Gets the placard text.
	 * @return the placardText
	 */
	public String getPlacardText() {
		return placardText;
	}
	
	/**
	 * Sets the placard text.
	 * @param placardText the placardText to set
	 */
	public void setPlacardText(String placardText) {
		this.placardText = placardText;
	}
	
	/**
	 * Gets the placard code.
	 * @return the placardCode
	 */
	public String getPlacardCode() {
		return placardCode;
	}
	
	/**
	 * Sets the placard code.
	 * @param placardCode the placardCode to set
	 */
	public void setPlacardCode(String placardCode) {
		this.placardCode = placardCode;
	}
	
	/**
	 * Gets the quantity multiplier.
	 * @return the quantityMultiplier
	 */
	public String getQuantityMultiplier() {
		return quantityMultiplier;
	}
	
	/**
	 * Sets the quantity multiplier.
	 * @param quantityMultiplier the quantityMultiplier to set
	 */
	public void setQuantityMultiplier(String quantityMultiplier) {
		this.quantityMultiplier = quantityMultiplier;
	}
	
	/**
	 * Gets the unna.
	 * @return the unna
	 */
	public String getUnna() {
		return unna;
	}
	
	/**
	 * Sets the unna.
	 * @param unna the unna to set
	 */
	public void setUnna(String unna) {
		this.unna = unna;
	}
	
	/**
	 * Gets the class 9.
	 * @return the class9
	 */
	public String getClass9() {
		return class9;
	}
	
	/**
	 * Sets the class 9.
	 * @param class9 the class9 to set
	 */
	public void setClass9(String class9) {
		this.class9 = class9;
	}
	
	/**
	 * Gets the bulk.
	 * @return the bulk
	 */
	public String getBulk() {
		return bulk;
	}
	
	/**
	 * Sets the bulk.
	 * @param bulk the bulk to set
	 */
	public void setBulk(String bulk) {
		this.bulk = bulk;
	}
	
	/**
	 * Gets the size.
	 * @return the size
	 */
	public String getSize() {
		return size;
	}
	
	/**
	 * Sets the size.
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}
	
	/**
	 * Gets the unit.
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	
	/**
	 * Sets the unit.
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	/**
	 * Gets the agrian size id.
	 * @return the agrianSizeId
	 */
	public String getAgrianSizeId() {
		return agrianSizeId;
	}
	
	/**
	 * Sets the agrian size id.
	 * @param agrianSizeId the agrianSizeId to set
	 */
	public void setAgrianSizeId(String agrianSizeId) {
		this.agrianSizeId = agrianSizeId;
	}
	
	/**
	 * Gets the weight multiplier.
	 * @return the weightMultiplier
	 */
	public String getWeightMultiplier() {
		return weightMultiplier;
	}
	
	/**
	 * Sets the weight multiplier.
	 * @param weightMultiplier the weightMultiplier to set
	 */
	public void setWeightMultiplier(String weightMultiplier) {
		this.weightMultiplier = weightMultiplier;
	}
	
	/**
	 * Gets the containers per case.
	 * @return the containersPerCase
	 */
	public String getContainersPerCase() {
		return containersPerCase;
	}
	
	/**
	 * Sets the containers per case.
	 * @param containersPerCase the containersPerCase to set
	 */
	public void setContainersPerCase(String containersPerCase) {
		this.containersPerCase = containersPerCase;
	}
	
	/**
	 * Gets the packaged.
	 * @return the packaged
	 */
	public String getPackaged() {
		return packaged;
	}
	
	/**
	 * Sets the packaged.
	 * @param packaged the packaged to set
	 */
	public void setPackaged(String packaged) {
		this.packaged = packaged;
	}
	
	/**
	 * Gets the prop 65.
	 * @return the prop65
	 */
	public String getProp65() {
		return prop65;
	}
	
	/**
	 * Sets the prop 65.
	 * @param prop65 the prop65 to set
	 */
	public void setProp65(String prop65) {
		this.prop65 = prop65;
	}
	
	/**
	 * Gets the package type.
	 * @return the packageType
	 */
	public String getPackageType() {
		return packageType;
	}
	
	/**
	 * Sets the package type.
	 * @param packageType the packageType to set
	 */
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	
	/**
	 * Gets the description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the potentially hazardous.
	 * @return the potentiallyHazardous
	 */
	public String getPotentiallyHazardous() {
		return potentiallyHazardous;
	}
	
	/**
	 * Sets the potentially hazardous.
	 * @param potentiallyHazardous the potentiallyHazardous to set
	 */
	public void setPotentiallyHazardous(String potentiallyHazardous) {
		this.potentiallyHazardous = potentiallyHazardous;
	}
	
	/**
	 * Gets the combustible liquid.
	 * @return the combustibleLiquid
	 */
	public String getCombustibleLiquid() {
		return combustibleLiquid;
	}
	
	/**
	 * Sets the combustible liquid.
	 * @param combustibleLiquid the combustibleLiquid to set
	 */
	public void setCombustibleLiquid(String combustibleLiquid) {
		this.combustibleLiquid = combustibleLiquid;
	}
	
	/**
	 * Gets the shipping name.
	 * @return the shippingName
	 */
	public String getShippingName() {
		return shippingName;
	}
	
	/**
	 * Sets the shipping name.
	 * @param shippingName the shippingName to set
	 */
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	
	/**
	 * Gets the hazardous.
	 * @return the hazardous
	 */
	public String getHazardous() {
		return hazardous;
	}
	
	/**
	 * Sets the hazardous.
	 * @param hazardous the hazardous to set
	 */
	public void setHazardous(String hazardous) {
		this.hazardous = hazardous;
	}
	
	/**
	 * Gets the size description.
	 * @return the sizeDescription
	 */
	public String getSizeDescription() {
		return sizeDescription;
	}
	
	/**
	 * Sets the size description.
	 * @param sizeDescription the sizeDescription to set
	 */
	public void setSizeDescription(String sizeDescription) {
		this.sizeDescription = sizeDescription;
	}
	
	/**
	 * Gets the note.
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * Sets the note.
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DotScenario [placardText=" + placardText + ", placardCode=" + placardCode + ", quantityMultiplier="
				+ quantityMultiplier + ", unna=" + unna + ", class9=" + class9 + ", bulk=" + bulk + ", size=" + size
				+ ", unit=" + unit + ", agrianSizeId=" + agrianSizeId + ", weightMultiplier=" + weightMultiplier
				+ ", containersPerCase=" + containersPerCase + ", packaged=" + packaged + ", prop65=" + prop65
				+ ", packageType=" + packageType + ", description=" + description + ", potentiallyHazardous="
				+ potentiallyHazardous + ", combustibleLiquid=" + combustibleLiquid + ", shippingName=" + shippingName
				+ ", hazardous=" + hazardous + ", sizeDescription=" + sizeDescription + ", note=" + note + "]";
	}
}
