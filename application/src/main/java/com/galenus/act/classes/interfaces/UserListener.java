package com.galenus.act.classes.interfaces;

import com.galenus.act.classes.User;

public interface UserListener {
    void onUserSelected(User user);
    boolean onPasswordEntered(String password);
}
