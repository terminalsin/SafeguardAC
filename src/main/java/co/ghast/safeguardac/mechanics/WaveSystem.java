package co.ghast.safeguardac.mechanics;

import co.ghast.safeguardac.SafeGuard;
import lombok.Getter;
import lombok.Setter;

public class WaveSystem {
    private SafeGuard sg;
    public WaveSystem(SafeGuard sg){
        this.sg = sg;

    }


    @Getter @Setter public boolean WaveActive;

    public void activateWave(){
        this.WaveActive = true;
    }
    public void disableWave(){
        this.WaveActive = false;
    }
}
