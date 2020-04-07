package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.model.User;
import com.devserbyn.suncheck.service.UserRepositoryService;
import com.devserbyn.suncheck.service.UserService;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryService userRepoService;

    @Override
    public void register(Update update) {
        User newUser = new User();
        newUser.setChatId(update.getMessage().getChatId());
        newUser.setUsername(update.getMessage().getFrom().getUserName());
        try {
            userRepoService.save(newUser).orElseThrow(RuntimeException::new);
        } catch (Exception e) {
            System.out.println("User registration failed");
        }
    }

    @Override
    public void remove(Update update) {
        try {
            userRepoService.deleteByChatId(update.getMessage().getChatId());
        } catch (Exception e) {
            System.out.println("User removing failed");
        }
    }

    @Override
    public boolean checkIfUserRegistered(long chatId) {
        return userRepoService.findByChatId(chatId).isPresent();
    }
}
