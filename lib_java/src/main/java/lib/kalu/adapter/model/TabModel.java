package lib.kalu.adapter.model;

import java.io.Serializable;

/**
 * description: 分组
 * created by kalu on 2017/5/26 15:10
 */
public abstract class TabModel<T> implements Serializable {

    private boolean isTab = false;
    private String tabName = "";
    private T t;

    public TabModel(boolean isTab, String tabName) {
        this.isTab = isTab;
        this.tabName = tabName;
        this.t = null;
    }

    public TabModel(T t) {
        this.t = t;
    }

    public boolean isTab() {
        return isTab;
    }

    public void setTab(boolean tab) {
        isTab = tab;
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
