package com.galenus.act.classes.managers;

import com.galenus.act.classes.User;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class UserManager {

    private static final UserManager Instance = new UserManager();
    public static UserManager usrMgr() {
        return Instance;
    }
    private UserManager() {}

    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        if (!userList.contains(user)) {
            userList.add(user);
        }
    }

    public void updateUserList(Vector<SoapObject> soapObjectVector) {

    }

}
