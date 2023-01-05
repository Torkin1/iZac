package entity;

public class Perk {
    private PerkType perkType;
    private String value;

    public PerkType getPerkType() {
        return perkType;
    }

    public void setPerkType(PerkType perkType) {
        this.perkType = perkType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
