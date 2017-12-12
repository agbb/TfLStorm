package org.apache.storm.starter;

import java.io.Serializable;

public class ArrivalBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // add more properties as necessary
    String StopPointName;
	String StopCode2;
    String Towards;
    Double Latitude;
    Double Longitude;
    String LineID;
    String DestinationName;
    int VehicleID;
    String RegistrationNumber;
    int EstimatedTime;
    int ExpireTime;
    Boolean hasExpired;

    public ArrivalBean(String stopPointName, String stopCode2, String towards, Double latitude, Double longitude,
			String lineID, String destinationName, int vehicleID, String registrationNumber, int estimatedTime,
			int expireTime, Boolean hasExpired) {
		StopPointName = stopPointName;
		StopCode2 = stopCode2;
		Towards = towards;
		Latitude = latitude;
		Longitude = longitude;
		LineID = lineID;
		DestinationName = destinationName;
		VehicleID = vehicleID;
		RegistrationNumber = registrationNumber;
		EstimatedTime = estimatedTime;
		ExpireTime = expireTime;
		this.hasExpired = hasExpired;
	}
    
    public ArrivalBean(){
        
    }

	public String getStopPointName() {
		return StopPointName;
	}

	public String getStopCode2() {
		return StopCode2;
	}

	public String getTowards() {
		return Towards;
	}

	public Double getLatitude() {
		return Latitude;
	}

	public Double getLongitude() {
		return Longitude;
	}

	public String getLineID() {
		return LineID;
	}

	public String getDestinationName() {
		return DestinationName;
	}

	public int getVehicleID() {
		return VehicleID;
	}

	public String getRegistrationNumber() {
		return RegistrationNumber;
	}

	public int getEstimatedTime() {
		return EstimatedTime;
	}

	public int getExpireTime() {
		return ExpireTime;
	}

	public Boolean getHasExpired() {
		return hasExpired;
	}

	public void setStopPointName(String stopPointName) {
		StopPointName = stopPointName;
	}

	public void setStopCode2(String stopCode2) {
		StopCode2 = stopCode2;
	}

	public void setTowards(String towards) {
		Towards = towards;
	}

	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}

	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}

	public void setLineID(String lineID) {
		LineID = lineID;
	}

	public void setDestinationName(String destinationName) {
		DestinationName = destinationName;
	}

	public void setVehicleID(int vehicleID) {
		VehicleID = vehicleID;
	}

	public void setRegistrationNumber(String registrationNumber) {
		RegistrationNumber = registrationNumber;
	}

	public void setEstimatedTime(int estimatedTime) {
		EstimatedTime = estimatedTime;
	}

	public void setExpireTime(int expireTime) {
		ExpireTime = expireTime;
	}

	public void setHasExpired(Boolean hasExpired) {
		this.hasExpired = hasExpired;
	}
    
}