package com.galenus.act.classes.managers;

import com.galenus.act.classes.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static final UserManager Instance = new UserManager();
    public static UserManager usrMgr() {
        return Instance;
    }
    private UserManager() {}

    private List<User> userList = new ArrayList<>();
    private User selectedUser;

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        if (!userList.contains(user)) {
            userList.add(user);
        }
    }

    public void setSelectedUser(User user) {
        if (selectedUser != null) {
            selectedUser.logOut();
        }
        selectedUser = user;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public boolean logInUser(String pin) {
        boolean result = false;
        if (selectedUser != null) {
            if (selectedUser.isPinCorrect(pin)) {
                selectedUser.logIn();
                return true;
            }
        }
        return result;
    }

    // Very much illegal!!
    public void printAllUserPins() {
        for (User user : userList) {
            int n = 0;
            while (n < 10000) {
                try {
                    String num = String.format("%04d", n);

                    String test = User.getEncryptedString(num);
                    if (test.equals(user.getEncodedPin())) {
                        System.out.println(user.getFirstName() + ": Pin = " + num);
                    }
                    n++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
