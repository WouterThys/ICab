package com.galenus.act.classes;

public class Door {

    private long id;
    private boolean isOpen;
    private boolean isLocked;

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
}
