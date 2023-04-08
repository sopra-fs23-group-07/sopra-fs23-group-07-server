package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class MemberLocationDTO {
    private Long memberId;
    private List<String> selectedLocations;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<String> getSelectedLocations() {
        return selectedLocations;
    }

    public void setSelectedLocations(List<String> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }
}
