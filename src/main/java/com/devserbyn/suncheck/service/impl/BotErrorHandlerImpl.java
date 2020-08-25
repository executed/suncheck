package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.service.BotErrorHandler;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BotErrorHandlerImpl implements BotErrorHandler {

    @Override
    public void handleSendingMessageException(long chatId, String message, TelegramApiException e) {
        String logTxt = String.format("Error sending the message; Chat id: %d, Message: %s", chatId, message);
        log.error(logTxt, e);
    }
}
