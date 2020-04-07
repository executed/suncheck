package com.devserbyn.suncheck.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ApiController {

    String handle(Update update);
}
