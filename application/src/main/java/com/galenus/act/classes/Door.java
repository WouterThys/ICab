package com.galenus.act.classes;

import java.util.ArrayList;
import java.util.List;

public class Door {

    private final long id;
    private boolean isOpen;
    private boolean isLocked;

    private final List<Item> itemList = new ArrayList<>();

    public Door(long id) {
        this.id = id;
        this.isOpen = false;
        this.isLocked = true;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Door) && (((Door) obj).getId() == getId());
    }

    @Override
    public String toString() {
        String result = "Door " + String.valueOf(getId());
        if (isLocked) {
            result += " locked, ";
        } else {
            result += " unlocked, ";
        }
        if (isOpen) {
            result += "open";
        } else {
            result += "closed";
        }
        return result;
    }

    public void addItem(Item item) {
        if (!itemList.contains(item)) {
            itemList.add(item);
        }
    }

    public void clearItemList() {
        itemList.clear();
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public long getId() {
        return id;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }
}
