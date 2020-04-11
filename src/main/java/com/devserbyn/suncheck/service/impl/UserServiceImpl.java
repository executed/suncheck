package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.model.User;
import com.devserbyn.suncheck.model.UserConfig;
import com.devserbyn.suncheck.repository.UserConfigRepository;
import com.devserbyn.suncheck.service.UserRepositoryService;
import com.devserbyn.suncheck.service.UserService;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryService userRepoService;
    private final UserConfigRepository userConfigRepository;

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
        Long userChatId = update.getMessage().getChatId();
        UserConfig userConfig = userConfigRepository.findAll().stream()
                .filter(c -> c.getUser().getChatId() == userChatId)
                .findFirst().orElseThrow(() -> new RuntimeException("User config wasn't found"));
        try {
            userConfigRepository.delete(userConfig);
            userRepoService.deleteByChatId(userChatId);
        } catch (Exception e) {
            userConfigRepository.save(userConfig);
            log.error("User removing failed", e);
        }
    }

    @Override
    public boolean checkIfUserRegistered(long chatId) {
        return userRepoService.findByChatId(chatId).isPresent();
    }
}
