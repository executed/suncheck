package com.devserbyn.suncheck.service;

import com.devserbyn.suncheck.model.User;

public interface SunEventService {

    String getSunEventTimeByUser(User user, String event);

    void updateUserNextNotificationTime(User curUser, String previousSunEventType);

    String getNearestSunEventType(User curUser);
}
