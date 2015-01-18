package net.david_bauer.tcpclient;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by David on 17.01.2015.
 */
public class ActionElementList implements Serializable {
    private ActionElement[] ae = new ActionElement[0];
    public boolean ready = true;
    public Integer addElement(String name, String task) {
        ActionElement[] aeTemp = ae.clone();
        Integer oldArrayLength = aeTemp.length;
        Integer newArrayLength = oldArrayLength + 1;
        ae = new ActionElement[newArrayLength];
        if (newArrayLength != 1) {
            for(Integer i=0; i < aeTemp.length; i++) {
                ae[i] = aeTemp[i];
            }
        }
        ae[oldArrayLength] = new ActionElement(name, task);
        return oldArrayLength;
    }
    public ActionElement getElement(Integer id) {
        return ae[id];
    }
    public String getName(Integer id) {
        return ae[id].getName();
    }
    public String getTask(Integer id) {
        return ae[id].getTask();
    }
    public Integer getLength() {
        return ae.length;
    }
    public ActionElement[] getArray() {
        return ae.clone();
    }
    public void removeElement(Integer id) {
        ActionElement[] aeTemp = ae.clone();
        Integer oldArrayLength = aeTemp.length;
        Integer newArrayLength = oldArrayLength - 1;
        ae = new ActionElement[newArrayLength];
        if (newArrayLength != 0) {
            Boolean found = false;
            for(Integer i=0; i < aeTemp.length;) {
                if (i==id) {
                    found=true;
                }
                else {
                    if (found==true) {
                        ae[i-1] = aeTemp[i];
                    }
                    else {
                        ae[i] = aeTemp[i];
                    }
                }
                i++;
            }
        }
    }
}
