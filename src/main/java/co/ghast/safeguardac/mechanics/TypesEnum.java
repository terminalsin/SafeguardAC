package co.ghast.safeguardac.mechanics;

import lombok.Getter;

public enum TypesEnum {
    REGULAR("regular"),
    REPORT("report"),
    ROTATION("rotation"),
    WAVE("wave");


    @Getter private String name;

    private TypesEnum(String mode) {
        this.name = name;
    }

}
