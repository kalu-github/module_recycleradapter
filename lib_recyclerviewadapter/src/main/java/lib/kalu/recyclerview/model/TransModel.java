package lib.kalu.recyclerview.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 多级菜单
 * created by kalu on 2017/5/26 15:11
 */
public abstract class TransModel<T> implements Serializable {

    // 默认合并
    protected boolean isExpand = false;
    // 数据集合
    protected List<T> modelList;

    /*****************************************************************************************/

    public abstract int getLevel();

    public boolean isExpanded() {
        return isExpand;
    }

    public void setExpanded(boolean expanded) {
        isExpand = expanded;
    }

    public List<T> getModelList() {
        return modelList;
    }

    public void setModelList(List<T> modelList) {
        this.modelList = modelList;
    }

    /*****************************************************************************************/

    public boolean hasModel() {
        return modelList != null && modelList.size() > 0;
    }

    public T getModel(int position) {
        if (hasModel() && position < modelList.size()) {
            return modelList.get(position);
        } else {
            return null;
        }
    }

    public int getModelPosition(T subItem) {
        return modelList != null ? modelList.indexOf(subItem) : -1;
    }

    public void addModel(T subItem) {
        if (modelList == null) {
            modelList = new ArrayList<>();
        }
        modelList.add(subItem);
    }

    public void addModel(int position, T subItem) {
        if (modelList != null && position >= 0 && position < modelList.size()) {
            modelList.add(position, subItem);
        } else {
            addModel(subItem);
        }
    }

    public boolean contains(T subItem) {
        return modelList != null && modelList.contains(subItem);
    }

    public boolean removeModel(T subItem) {
        return modelList != null && modelList.remove(subItem);
    }

    public boolean removeModel(int position) {
        if (modelList != null && position >= 0 && position < modelList.size()) {
            modelList.remove(position);
            return true;
        }
        return false;
    }
}
