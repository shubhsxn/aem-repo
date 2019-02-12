package com.corteva.service.product.detail.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class AgrianProduct.
 */
public class AgrianProduct {
	
	/** The company id. */
	@JsonProperty("company_id")
	private String companyId;
	
	/** The rei hours high. */
	@JsonProperty("rei_hours_high")
	@JsonIgnore
	private String reiHoursHigh;
	
	/** The product id. */
	@JsonProperty("product_id")
	private String productId;
	
	/** The density. */
	@JsonIgnore
	private String density;
	
	/** The federally restricted. */
	@JsonProperty("federally_restricted")
	@JsonIgnore
	private String federallyRestricted;
	
	/** The plant back restrictions. */
	@JsonProperty("plant_back_restrictions")
	@JsonIgnore
	private PlantBackRestrictions plantBackRestrictions;
	
	/** The unna. */
	@JsonIgnore
	private String unna;
	
	/** The avoid grazing. */
	@JsonProperty("avoid_grazing")
	@JsonIgnore
	private String avoidGrazing;
	
	/** The dot description. */
	@JsonProperty("dot_description")
	@JsonIgnore
	private DotDescription dotDescription;
	
	/** The purity. */
	@JsonIgnore
	private String purity;
	
	/** The product name. */
	@JsonProperty("product_name")
	private String productName;
	
	/** The state. */
	@JsonIgnore
	private String state;
	
	/** The placards. */
	@JsonIgnore
	private Placards placards;
	
	/** The hazard codes. */
	@JsonProperty("hazard_codes")
	@JsonIgnore
	private HazardCodes hazardCodes;
	
	/** The unlicensed areas. */
	@JsonProperty("unlicensed_areas")
	private UnlicensedAreas unlicensedAreas;
	
	/** The licensed state. */
	private Map<String, String> licensedState;
	
	/** The epa. */
	@JsonIgnore
	private String epa;
	
	/** The chemicals. */
	@JsonIgnore
	private Chemicals chemicals;
	
	/** The unid. */
	@JsonIgnore
	private String unid;
	
	/** The registrations. */
	@JsonIgnore
	private Registrations registrations;
	
	/** The hazard classes. */
	@JsonProperty("hazard_classes")
	@JsonIgnore
	private HazardClasses hazardClasses;
	
	/** The signal word. */
	@JsonProperty("signal_word")
	@JsonIgnore
	private String signalWord;
	
	/** The documents. */
	private Documents documents;
	
	/** The osha hazard classes. */
	@JsonProperty("osha_hazard_classes")
	@JsonIgnore
	private OshaHazardClasses oshaHazardClasses;
	
	/** The rei hours low. */
	@JsonProperty("rei_hours_low")
	@JsonIgnore
	private String reiHoursLow;
	
	/** The moa code. */
	@JsonProperty("moa_code")
	@JsonIgnore
	private String moaCode;
	
	/** The dot medical number. */
	@JsonProperty("dot_medical_number")
	@JsonIgnore
	private String dotMedicalNumber;
	
	/** The country code. */
	@JsonProperty("country_code")
	@JsonIgnore
	private String countryCode;
	
	/** The manufacturer name. */
	@JsonProperty("manufacturer_name")
	@JsonIgnore
	private String manufacturerName;
	
	/** The special instructions. */
	@JsonProperty("special_instructions")
	@JsonIgnore
	private String specialInstructions;
	
	/** The last changed. */
	@JsonProperty("last_changed")
	private String lastChanged;
	
	/** The ais. */
	@JsonIgnore
	private Ais ais;
	
	/** The dot response number. */
	@JsonProperty("dot_response_number")
	@JsonIgnore
	private String dotResponseNumber;
	
	/** The ppe. */
	@JsonIgnore
	private String ppe;
	
	/** The ppe reentry. */
	@JsonProperty("ppe_reentry")
	@JsonIgnore
	private String ppeReentry;
	
	/**
	 * Gets the company id.
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}
	
	/**
	 * Sets the company id.
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	/**
	 * Gets the rei hours high.
	 * @return the reiHoursHigh
	 */
	public String getReiHoursHigh() {
		return reiHoursHigh;
	}
	
	/**
	 * Sets the rei hours high.
	 * @param reiHoursHigh the reiHoursHigh to set
	 */
	public void setReiHoursHigh(String reiHoursHigh) {
		this.reiHoursHigh = reiHoursHigh;
	}
	
	/**
	 * Gets the product id.
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	
	/**
	 * Sets the product id.
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/**
	 * Gets the density.
	 * @return the density
	 */
	public String getDensity() {
		return density;
	}
	
	/**
	 * Sets the density.
	 * @param density the density to set
	 */
	public void setDensity(String density) {
		this.density = density;
	}
	
	/**
	 * Gets the federally restricted.
	 * @return the federallyRestricted
	 */
	public String getFederallyRestricted() {
		return federallyRestricted;
	}
	
	/**
	 * Sets the federally restricted.
	 * @param federallyRestricted the federallyRestricted to set
	 */
	public void setFederallyRestricted(String federallyRestricted) {
		this.federallyRestricted = federallyRestricted;
	}
	
	/**
	 * Gets the plant back restrictions.
	 * @return the plantBackRestrictions
	 */
	public PlantBackRestrictions getPlantBackRestrictions() {
		return plantBackRestrictions;
	}
	
	/**
	 * Sets the plant back restrictions.
	 * @param plantBackRestrictions the plantBackRestrictions to set
	 */
	public void setPlantBackRestrictions(PlantBackRestrictions plantBackRestrictions) {
		this.plantBackRestrictions = plantBackRestrictions;
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
	 * Gets the avoid grazing.
	 * @return the avoidGrazing
	 */
	public String getAvoidGrazing() {
		return avoidGrazing;
	}
	
	/**
	 * Sets the avoid grazing.
	 * @param avoidGrazing the avoidGrazing to set
	 */
	public void setAvoidGrazing(String avoidGrazing) {
		this.avoidGrazing = avoidGrazing;
	}
	
	/**
	 * Gets the dot description.
	 * @return the dotDescription
	 */
	public DotDescription getDotDescription() {
		return dotDescription;
	}
	
	/**
	 * Sets the dot description.
	 * @param dotDescription the dotDescription to set
	 */
	public void setDotDescription(DotDescription dotDescription) {
		this.dotDescription = dotDescription;
	}
	
	/**
	 * Gets the purity.
	 * @return the purity
	 */
	public String getPurity() {
		return purity;
	}
	
	/**
	 * Sets the purity.
	 * @param purity the purity to set
	 */
	public void setPurity(String purity) {
		this.purity = purity;
	}
	
	/**
	 * Gets the product name.
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	
	/**
	 * Sets the product name.
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
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
	 * Gets the placards.
	 * @return the placards
	 */
	public Placards getPlacards() {
		return placards;
	}
	
	/**
	 * Sets the placards.
	 * @param placards the placards to set
	 */
	public void setPlacards(Placards placards) {
		this.placards = placards;
	}
	
	/**
	 * Gets the hazard codes.
	 * @return the hazardCodes
	 */
	public HazardCodes getHazardCodes() {
		return hazardCodes;
	}
	
	/**
	 * Sets the hazard codes.
	 * @param hazardCodes the hazardCodes to set
	 */
	public void setHazardCodes(HazardCodes hazardCodes) {
		this.hazardCodes = hazardCodes;
	}
	
	/**
	 * Gets the unlicensed areas.
	 * @return the unlicensedAreas
	 */
	public UnlicensedAreas getUnlicensedAreas() {
		return unlicensedAreas;
	}
	
	/**
	 * Sets the unlicensed areas.
	 * @param unlicensedAreas the unlicensedAreas to set
	 */
	public void setUnlicensedAreas(UnlicensedAreas unlicensedAreas) {
		this.unlicensedAreas = unlicensedAreas;
	}
	
	/**
	 * Gets the epa.
	 * @return the epa
	 */
	public String getEpa() {
		return epa;
	}
	
	/**
	 * Sets the epa.
	 * @param epa the epa to set
	 */
	public void setEpa(String epa) {
		this.epa = epa;
	}
	
	/**
	 * Gets the chemicals.
	 * @return the chemicals
	 */
	public Chemicals getChemicals() {
		return chemicals;
	}
	
	/**
	 * Sets the chemicals.
	 * @param chemicals the chemicals to set
	 */
	public void setChemicals(Chemicals chemicals) {
		this.chemicals = chemicals;
	}
	
	/**
	 * Gets the unid.
	 * @return the unid
	 */
	public String getUnid() {
		return unid;
	}
	
	/**
	 * Sets the unid.
	 * @param unid the unid to set
	 */
	public void setUnid(String unid) {
		this.unid = unid;
	}
	
	/**
	 * Gets the registrations.
	 * @return the registrations
	 */
	public Registrations getRegistrations() {
		return registrations;
	}
	
	/**
	 * Sets the registrations.
	 * @param registrations the registrations to set
	 */
	public void setRegistrations(Registrations registrations) {
		this.registrations = registrations;
	}
	
	/**
	 * Gets the hazard classes.
	 * @return the hazardClasses
	 */
	public HazardClasses getHazardClasses() {
		return hazardClasses;
	}
	
	/**
	 * Sets the hazard classes.
	 * @param hazardClasses the hazardClasses to set
	 */
	public void setHazardClasses(HazardClasses hazardClasses) {
		this.hazardClasses = hazardClasses;
	}
	
	/**
	 * Gets the signal word.
	 * @return the signalWord
	 */
	public String getSignalWord() {
		return signalWord;
	}
	
	/**
	 * Sets the signal word.
	 * @param signalWord the signalWord to set
	 */
	public void setSignalWord(String signalWord) {
		this.signalWord = signalWord;
	}
	
	/**
	 * Gets the documents.
	 * @return the documents
	 */
	public Documents getDocuments() {
		return documents;
	}
	
	/**
	 * Sets the documents.
	 * @param documents the documents to set
	 */
	public void setDocuments(Documents documents) {
		this.documents = documents;
	}
	
	/**
	 * Gets the osha hazard classes.
	 * @return the oshaHazardClasses
	 */
	public OshaHazardClasses getOshaHazardClasses() {
		return oshaHazardClasses;
	}
	
	/**
	 * Sets the osha hazard classes.
	 * @param oshaHazardClasses the oshaHazardClasses to set
	 */
	public void setOshaHazardClasses(OshaHazardClasses oshaHazardClasses) {
		this.oshaHazardClasses = oshaHazardClasses;
	}
	
	/**
	 * Gets the rei hours low.
	 * @return the reiHoursLow
	 */
	public String getReiHoursLow() {
		return reiHoursLow;
	}
	
	/**
	 * Sets the rei hours low.
	 * @param reiHoursLow the reiHoursLow to set
	 */
	public void setReiHoursLow(String reiHoursLow) {
		this.reiHoursLow = reiHoursLow;
	}
	
	/**
	 * Gets the moa code.
	 * @return the moaCode
	 */
	public String getMoaCode() {
		return moaCode;
	}
	
	/**
	 * Sets the moa code.
	 * @param moaCode the moaCode to set
	 */
	public void setMoaCode(String moaCode) {
		this.moaCode = moaCode;
	}
	
	/**
	 * Gets the dot medical number.
	 * @return the dotMedicalNumber
	 */
	public String getDotMedicalNumber() {
		return dotMedicalNumber;
	}
	
	/**
	 * Sets the dot medical number.
	 * @param dotMedicalNumber the dotMedicalNumber to set
	 */
	public void setDotMedicalNumber(String dotMedicalNumber) {
		this.dotMedicalNumber = dotMedicalNumber;
	}
	
	/**
	 * Gets the country code.
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}
	
	/**
	 * Sets the country code.
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	/**
	 * Gets the manufacturer name.
	 * @return the manufacturerName
	 */
	public String getManufacturerName() {
		return manufacturerName;
	}
	
	/**
	 * Sets the manufacturer name.
	 * @param manufacturerName the manufacturerName to set
	 */
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	
	/**
	 * Gets the last changed.
	 * @return the lastChanged
	 */
	public String getLastChanged() {
		return lastChanged;
	}
	
	/**
	 * Sets the last changed.
	 * @param lastChanged the lastChanged to set
	 */
	public void setLastChanged(String lastChanged) {
		this.lastChanged = lastChanged;
	}
	
	/**
	 * Gets the ais.
	 * @return the ais
	 */
	public Ais getAis() {
		return ais;
	}
	
	/**
	 * Sets the ais.
	 * @param ais the ais to set
	 */
	public void setAis(Ais ais) {
		this.ais = ais;
	}
	
	/**
	 * Gets the dot response number.
	 * @return the dotResponseNumber
	 */
	public String getDotResponseNumber() {
		return dotResponseNumber;
	}
	
	/**
	 * Sets the dot response number.
	 * @param dotResponseNumber the dotResponseNumber to set
	 */
	public void setDotResponseNumber(String dotResponseNumber) {
		this.dotResponseNumber = dotResponseNumber;
	}
	
	/**
	 * Gets the ppe.
	 * @return the ppe
	 */
	public String getPpe() {
		return ppe;
	}
	
	/**
	 * Sets the ppe.
	 * @param ppe the ppe to set
	 */
	public void setPpe(String ppe) {
		this.ppe = ppe;
	}
	
	/**
	 * Gets the ppe reentry.
	 * @return the ppeReentry
	 */
	public String getPpeReentry() {
		return ppeReentry;
	}
	
	/**
	 * Sets the ppe reentry.
	 * @param ppeReentry the ppeReentry to set
	 */
	public void setPpeReentry(String ppeReentry) {
		this.ppeReentry = ppeReentry;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AgrianProduct [companyId=" + companyId + ", reiHoursHigh=" + reiHoursHigh + ", productId=" + productId
				+ ", density=" + density + ", federallyRestricted=" + federallyRestricted + ", plantBackRestrictions="
				+ plantBackRestrictions + ", unna=" + unna + ", avoidGrazing=" + avoidGrazing + ", dotDescription="
				+ dotDescription + ", purity=" + purity + ", productName=" + productName + ", state=" + state
				+ ", placards=" + placards + ", hazardCodes=" + hazardCodes + ", unlicensedAreas=" + unlicensedAreas
				+ ", epa=" + epa + ", chemicals=" + chemicals + ", unid=" + unid + ", registrations=" + registrations
				+ ", hazardClasses=" + hazardClasses + ", signalWord=" + signalWord + ", documents=" + documents
				+ ", oshaHazardClasses=" + oshaHazardClasses + ", reiHoursLow=" + reiHoursLow + ", moaCode=" + moaCode
				+ ", dotMedicalNumber=" + dotMedicalNumber + ", countryCode=" + countryCode + ", manufacturerName="
				+ manufacturerName + ", specialInstructions=" + specialInstructions + ", lastChanged=" + lastChanged
				+ ", ais=" + ais + ", dotResponseNumber=" + dotResponseNumber + ", ppe=" + ppe + ", ppeReentry="
				+ ppeReentry + "]";
	}
	
	/**
	 * Gets the special instructions.
	 * @return the specialInstructions
	 */
	public String getSpecialInstructions() {
		return specialInstructions;
	}
	
	/**
	 * Sets the special instructions.
	 * @param specialInstructions the specialInstructions to set
	 */
	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	
	/**
	 * Gets the licensed state.
	 * @return the licensedState
	 */
	public Map<String, String> getLicensedState() {
		return licensedState;
	}
	
	/**
	 * Sets the licensed state.
	 * @param licensedState the licensedState to set
	 */
	public void setLicensedState(Map<String, String> licensedState) {
		this.licensedState = licensedState;
	}
	
	

	/** The licensed state for Map View. */
	private String licensedStateMap;
	
	/**
	 * Gets the licensedStateMap state.
	 * @return the licensedStateMap
	 */
	public String getLicensedStateMap() {
		return licensedStateMap;
	}
	
	/**
	 * Sets the licensedStateMap.
	 * @param licensedStateMap the licensedState to set
	 */
	public void setLicensedStateMap(String licensedStateMap) {
		this.licensedStateMap = licensedStateMap;
	}
}
