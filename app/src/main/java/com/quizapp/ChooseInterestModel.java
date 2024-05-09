package com.quizapp;

public class ChooseInterestModel {
    private boolean isSelected;
    private String interestName;

    public ChooseInterestModel(boolean isSelected, String interestName) {
        this.isSelected = isSelected;
        this.interestName = interestName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }
}
