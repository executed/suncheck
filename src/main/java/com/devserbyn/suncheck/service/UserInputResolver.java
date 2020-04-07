package com.devserbyn.suncheck.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserInputResolver {

    String resolveCommand(Update update);

    String resolvePlainText(Update update);

    String resolveLocation(Update update);
}
