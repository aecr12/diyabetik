package com.ae.Models;

import java.util.HashMap;
import java.util.Map;

public class TreatmentChoice {
    private String id;
    private boolean insulin;
    private boolean pump;
    private boolean oralAntidiabetic;
    private boolean insulinAntidiabetic;

    public TreatmentChoice() {
    }

    public TreatmentChoice(String id, boolean insulin, boolean pump, boolean oralAntidiabetic, boolean insulinAntidiabetic) {
        this.id = id;
        this.insulin = insulin;
        this.pump = pump;
        this.oralAntidiabetic = oralAntidiabetic;
        this.insulinAntidiabetic = insulinAntidiabetic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isInsulin() {
        return insulin;
    }

    public void setInsulin(boolean insulin) {
        this.insulin = insulin;
    }

    public boolean isPump() {
        return pump;
    }

    public void setPump(boolean pump) {
        this.pump = pump;
    }

    public boolean isOralAntidiabetic() {
        return oralAntidiabetic;
    }

    public void setOralAntidiabetic(boolean oralAntidiabetic) {
        this.oralAntidiabetic = oralAntidiabetic;
    }

    public boolean isInsulinAntidiabetic() {
        return insulinAntidiabetic;
    }

    public void setInsulinAntidiabetic(boolean insulinAntidiabetic) {
        this.insulinAntidiabetic = insulinAntidiabetic;
    }


}
