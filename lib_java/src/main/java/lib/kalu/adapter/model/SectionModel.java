package lib.kalu.adapter.model;

import java.io.Serializable;

/**
 * description: 分组
 * created by kalu on 2017/5/26 15:10
 */
public abstract class SectionModel<T> implements Serializable {

    private boolean isSection = false;
    private String tabName = "";
    private T t;

    public SectionModel(boolean isSection, String tabName) {
        this.isSection = isSection;
        this.tabName = tabName;
        this.t = null;
    }

    public SectionModel(T t) {
        this.t = t;
    }

    public boolean isSection() {
        return isSection;
    }

    public void setSection(boolean tab) {
        isSection = tab;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
