package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class LocationDTO {
    //private Long memberId;
    //private List<String> selectedLocations;
    private double longitude;
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    //    public Long getMemberId() {
//        return memberId;
//    }
//
//    public void setMemberId(Long memberId) {
//        this.memberId = memberId;
//    }
//
//    public List<String> getSelectedLocations() {
//        return selectedLocations;
//    }
//
//    public void setSelectedLocations(List<String> selectedLocations) {
//        this.selectedLocations = selectedLocations;
//    }
}
