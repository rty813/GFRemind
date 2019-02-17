package xyz.rty813.gfremind;

public class SettingModel {
    private boolean enable;
    private String keywords;
    private boolean hideMain;
    private boolean hideSub;
    private String exclude;

    public SettingModel(boolean enable, String keywords, boolean hideMain, boolean hideSub, String exclude) {
        this.enable = enable;
        this.keywords = keywords;
        this.hideMain = hideMain;
        this.hideSub = hideSub;
        this.exclude = exclude;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean isHideMain() {
        return hideMain;
    }

    public void setHideMain(boolean hideMain) {
        this.hideMain = hideMain;
    }

    public boolean isHideSub() {
        return hideSub;
    }

    public void setHideSub(boolean hideSub) {
        this.hideSub = hideSub;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }
}
