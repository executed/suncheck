package com.devserbyn.suncheck.model;

import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.controller.ApiController;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SuncheckBot extends TelegramLongPollingBot {

    private final ApiController apiController;

    @Override
    public void onUpdateReceived(Update update) {
        String response = apiController.handle(update);
        this.sendResponse(update, response);
    }

    @Override
    public String getBotUsername() {
        return System.getenv().get(STR_CONSTANT.BOT_USERNAME_ENV_VAR);
    }

    @Override
    public String getBotToken() {
        return System.getenv().get(STR_CONSTANT.BOT_TOKEN_ENV_VAR);
    }

    public void sendResponse(Update update, String message) {
        try {
            SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), message);
            sendMessage.enableMarkdown(true);
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Something went wrong", e);
        }
    }

    public void sendResponse(long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage(chatId, message);
            sendMessage.enableMarkdown(true);
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Something went wrong", e);
        }
    }
}
