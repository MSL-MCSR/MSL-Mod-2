package com.mcsrleague.mslmod.widget;

import java.util.ArrayList;
import java.util.List;

public class PickableWidgetSet {
    List<PickableWidget> pickableWidgets;
    int picked = 0;
    public PickableWidgetSet(){
        pickableWidgets = new ArrayList<>();
    }

    public void pick(PickableWidget pickedWidget){
        picked = pickableWidgets.indexOf(pickedWidget);
        for(PickableWidget pickableWidget: pickableWidgets){
            pickableWidget.setPicked(pickableWidget.equals(pickedWidget));
        }
    }
    public void pick(int index){
        pick(pickableWidgets.get(index));
    }

    public int getPicked() {
        return picked;
    }

    public void add(PickableWidget pickableWidget){
        pickableWidgets.add(pickableWidget);
    }
}
