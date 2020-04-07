package com.devserbyn.suncheck.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {

    void register(Update update);

    void remove(Update update);

    boolean checkIfUserRegistered(long chatId);
}
