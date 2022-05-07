package com.devserbyn.suncheck.model;

import com.devserbyn.suncheck.constant.PATH_CONSTANT;
import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.controller.ApiController;
import com.devserbyn.suncheck.service.BotErrorHandler;
import com.devserbyn.suncheck.utility.ResourceUtil;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SuncheckBot extends TelegramLongPollingBot {

    private final ApiController apiController;
    private final BotErrorHandler botErrorHandler;
    private final ResourceUtil resourceUtil;

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
            SendMessage sendMessage = new SendMessage(Long.toString(chatId), message);
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

    public void setMyCommands() {
        final String[] fileLines = resourceUtil.readResourceFileLines(PATH_CONSTANT.BOT_COMMANDS).split("\n");
        List<BotCommand> botCommandList = new ArrayList<>();
        Arrays.stream(fileLines).forEach(l -> {
            final String[] commandAndDesc = l.split(" - ");
            botCommandList.add(new BotCommand(commandAndDesc[0], commandAndDesc[1]));
        });
        try {
            this.execute(new SetMyCommands(botCommandList, null, null));
            log.info("Bot commands were successfully set");
        } catch (TelegramApiException e) {
            botErrorHandler.handleSendingMessageException(1L, "setCommands", e);
        }
    }
}
