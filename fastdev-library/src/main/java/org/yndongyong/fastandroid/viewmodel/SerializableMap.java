package org.yndongyong.fastandroid.viewmodel;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Dong on 2016/6/2.
 */
public class SerializableMap<T> implements Serializable {

    private Map<T, Integer> map;

    public Map<T, Integer> getMap() {
        return this.map;
    }

    public void setMap(Map<T, Integer> map) {
        this.map = map;
    }

}
