package co.ghast.safeguardac.mechanics;

import co.ghast.safeguardac.SafeGuard;
import lombok.Getter;
import lombok.Setter;

public class ModeSystem {
    private SafeGuard sg;
    @Getter @Setter private String mode;
    public ModeSystem(SafeGuard sg){
        this.sg = sg;
        this.mode = sg.getSettingsConfig().getDetectionMode();
        setupMode(mode);
    }



    public void setupMode(String mode){

        if (mode == ""){
            return;
        }
        if (mode.equalsIgnoreCase("rotation")){
            this.mode = TypesEnum.ROTATION.getName();
            return;
        }
        if (mode.equalsIgnoreCase("report")){
            this.mode = TypesEnum.REPORT.getName();
            return;
        }
        if (mode.equalsIgnoreCase("wave")){
            this.mode = TypesEnum.WAVE.getName();
            return;
        }
        if (mode.equalsIgnoreCase("regular")){
            this.mode = TypesEnum.REGULAR.getName();
        }
    }

}
