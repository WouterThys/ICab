package com.galenus.act.classes.managers;

import com.galenus.act.classes.User;
import com.galenus.act.classes.interfaces.TimerListener;
import com.galenus.act.classes.interfaces.UserListener;

import javax.swing.*;
import java.util.*;
import java.util.Timer;

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
    private TimerListener timerListener;

    public List<User> getUserList() {
        return userList;
    }

    public void init(int userLogonTime, UserListener userListener) {
        this.userLogonTime = userLogonTime;
        this.userListener = userListener;
    }

//    public void addUser(User user) {
//        if (!userList.contains(user)) {
//            userList.add(user);
//        }
//    }

    public void updateUser(User user) {
        if (user != null) {
            int ndx = userList.indexOf(user);
            if (ndx >= 0) {
                userList.get(ndx).copyFrom(user);
            } else {
                userList.add(user);
            }
        }
    }

    public void updateUsers(List<User> newUserList) {
        if (newUserList != null) {
            List<User> tempUsers = new ArrayList<>(userList);

            for (User user : newUserList) {
                updateUser(user);
                tempUsers.remove(user);
            }

            // What remains in tempUsers can be removed
            userList.removeAll(tempUsers);
            sortUsers();
        }
    }

    public int getUserCount() {
        return userList.size();
    }

    public void clearUsers() {
        userList.clear();
    }

    public void sortUsers() {
        userList.sort(Comparator.comparing(User::getFirstName));
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

    public boolean isTimerRunning() {
        return timer != null;
    }

    public void startTimer(final TimerListener timerListener) {
        this.timerListener = timerListener;
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
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

    public void restartTimer() {
        startTimer(timerListener);
    }

    public void stopUser() {
        selectedUser.setLoggedInTime(userLogonTime);
        timerElapsed(timerListener);
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
