package com.miuhouse.zxcommunity.bean;

import java.util.List;

/**
 * Created by kings on 12/28/2015.
 */
public class Folder {
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    @Override
    public boolean equals(Object o) {
        try {


        Folder other = (Folder)o;
        return this.path.equalsIgnoreCase(other.path);
    }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
