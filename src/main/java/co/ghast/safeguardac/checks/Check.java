package co.ghast.safeguardac.checks;

import co.ghast.safeguardac.SafeGuard;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class Check implements Listener{
    private String identifier;
    private String name;
    private CheckCategory checkCategory;
    private SafeGuard sg;

    private boolean enabled = true;
    private boolean banTimer = false;
    private boolean bannable = true;
    private boolean judgementDay = false;
    private boolean atlas = false;

    private Integer maxViolations = 5;
    private Integer violationsToNotify = 1;
    private Long violationResetTime = 600000L;

    public Check(String identifier, String name, CheckCategory checkCategory, SafeGuard sg) {
        this.name = name;
        this.sg = sg;
        this.checkCategory = checkCategory;
        this.identifier = identifier;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isBannable() {
        return this.bannable;
    }

    public boolean hasBanTimer() {
        return this.banTimer;
    }

    public boolean isJudgmentDay() {
        return this.judgementDay;
    }

    public boolean isAtlas() { return  this.atlas; }

    public SafeGuard getSafeGuard() {
        return this.sg;
    }

    public Integer getMaxViolations() {
        return this.maxViolations;
    }

    public Integer getViolationsToNotify() {
        return this.violationsToNotify;
    }

    public Long getViolationResetTime() {
        return this.violationResetTime;
    }

    public void setEnabled(boolean Enabled) {
        if (Enabled) {
            if (!this.isEnabled()) {
                this.sg.registerListener(this);
            }
        } else {
            if (this.isEnabled()) {
                HandlerList.unregisterAll(this);
            }
        }

        this.enabled = Enabled;
    }

    public void setBannable(boolean bannable) {
        this.bannable = bannable;
    }

    public void setAutobanTimer(boolean banTimer) {
        this.banTimer = banTimer;
    }

    public void setMaxViolations(int MaxViolations) {
        this.maxViolations = MaxViolations;
    }

    public void setViolationsToNotify(int violationsToNotify) {
        this.violationsToNotify = violationsToNotify;
    }

    public void setViolationResetTime(long violationResetTime) {
        this.violationResetTime = violationResetTime;
    }

    public void setJudgementDay(boolean JudgementDay) {
        this.judgementDay = JudgementDay;
    }

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public CheckCategory getCheckCategory() {
        return checkCategory;
    }
}
