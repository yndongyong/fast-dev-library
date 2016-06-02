package org.yndongyong.fastandroid.viewmodel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dong on 2016/6/2.
 */
public class SerializableList<T> implements Serializable {
    
    private List<T> lis;

    public List<T> getLis() {
        return this.lis;
    }

    public void setLis(List<T> lis) {
        this.lis = lis;
    }

}
