package com.devserbyn.suncheck.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BotErrorHandler {

    void handleSendingMessageException(long chatId, String message, TelegramApiException e);
}
