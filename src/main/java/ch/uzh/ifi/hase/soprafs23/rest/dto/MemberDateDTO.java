package ch.uzh.ifi.hase.soprafs23.rest.dto;

import java.util.List;

public class MemberDateDTO {
    private Long memberId;
    private List<String> selectedDates;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<String> getSelectedDates() {
        return selectedDates;
    }

    public void setSelectedDates(List<String> selectedDates) {
        this.selectedDates = selectedDates;
    }
}
