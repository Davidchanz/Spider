package com.spider;

import java.util.Stack;

public class Action {
    private final Stack<ActionItem> actions;

    public Action(){
        this.actions = new Stack<>();
    }

    public void add(ActionItem actionItem){
        this.actions.push(actionItem);
    }

    public ActionItem pop(){
        return this.actions.pop();
    }

    public Stack<ActionItem> get(){
        return this.actions;
    }

    public void clean(){
        this.actions.clear();
    }

}
