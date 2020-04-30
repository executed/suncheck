package com.devserbyn.suncheck.model;

import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.controller.ApiController;
import com.devserbyn.suncheck.service.BotErrorHandler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuncheckBot extends TelegramLongPollingBot {

    private final ApiController apiController;
    private final BotErrorHandler botErrorHandler;

    private boolean silentMessage = false;

    @Override
    public void onUpdateReceived(Update update) {
        String response = apiController.handle(update);
        this.sendResponse(update.getMessage().getChatId(), response);
    }

    @Override
    public String getBotUsername() {
        return System.getenv().get(STR_CONSTANT.BOT_USERNAME_ENV_VAR);
    }

    @Override
    public String getBotToken() {
        return System.getenv().get(STR_CONSTANT.BOT_TOKEN_ENV_VAR);
    }

    public void sendResponse(long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage(chatId, message);
            sendMessage.enableMarkdown(true);
            sendMessage.enableNotification();
            if (this.silentMessage) {
                sendMessage.disableNotification();
            }
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            botErrorHandler.handleSendingMessageException(chatId, message, e);
        }
    }

    public void setSilentMessage(boolean silentMessage) {
        this.silentMessage = silentMessage;
    }
}
