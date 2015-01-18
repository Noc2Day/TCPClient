package net.david_bauer.tcpclient;

import java.io.Serializable;

/**
 * Created by David on 17.01.2015.
 */
public class ActionElement implements Serializable {
    private String name;
    private String task;
    public ActionElement(String name, String task) {
        this.name = name;
        this.task = task;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public String getName() {
        return this.name;
    }
    public String getTask() {
        return this.task;
    }
}
