package com.example.ljs.account;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by ljs on 2017/7/7.
 */

public class SerializableMap implements Serializable{
    private HashMap<String,Object>map;

    public HashMap<String,Object> getMap(){
        return map;
    }

    public void setMap(HashMap<String,Object> map){
        this.map=map;
    }
}
