package com.devserbyn.suncheck.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserCommandResolver {

    String resolveStart(Update update);

    String resolveSunEvent(Long chatId, String eventName);

    String resolveRemoveme(Update update);

    String resolveHelp(Update update);
}
