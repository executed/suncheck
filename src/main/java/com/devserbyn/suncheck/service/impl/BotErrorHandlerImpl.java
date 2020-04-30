package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.service.BotErrorHandler;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BotErrorHandlerImpl implements BotErrorHandler {

    @Override
    public void handleSendingMessageException(long chatId, String message, Exception e) {
        String logTxt = String.format("Error sending the message; Chat id: %d, Message: %s", chatId, message);
        log.error(logTxt, e);
    }
}
