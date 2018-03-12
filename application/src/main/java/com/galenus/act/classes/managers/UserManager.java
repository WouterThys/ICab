package com.galenus.act.classes.managers;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.TimerListener;
import com.galenus.act.classes.interfaces.UserListener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserManager {

    private static final UserManager Instance = new UserManager();
    public static UserManager usrMgr() {
        return Instance;
    }
    private UserManager() {}

    private List<User> userList = new ArrayList<>();
    private User selectedUser;
    private Timer timer;
    private int userLogonTime;
    private UserListener userListener;

    public List<User> getUserList() {
        return userList;
    }

    public void init(int userLogonTime, UserListener userListener) {
        this.userLogonTime = userLogonTime;
        this.userListener = userListener;
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
                result = true;
            }
        }
        return result;
    }

    public void logOffUser() {
        if (selectedUser != null) {
            stopTimer();
            selectedUser.logOut();
        }
    }

    public void startTimer(final TimerListener timerListener) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timerElapsed(timerListener);
                });
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    private void timerElapsed(TimerListener timerListener) {
        if (selectedUser != null) {
            if (timerListener != null) {
                selectedUser.setLoggedInTime(selectedUser.getLoggedInTime() + 1);
                timerListener.onTimerElapse(selectedUser.getLoggedInTimeString(userLogonTime));
                if (selectedUser.getLoggedInTime() > userLogonTime) {
                    selectedUser.setOverTime(true);
                    if (userListener != null) {
                        userListener.onUserShouldLogOff(selectedUser);
                    }
                }
            }
        }
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
