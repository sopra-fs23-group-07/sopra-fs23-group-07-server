package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class MemberSportDTO {
    private Long memberId;
    private String selectedSports;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getSelectedSports() {
        return selectedSports;
    }

    public void setSelectedSports(String selectedSports) {
        this.selectedSports = selectedSports;
    }
}
