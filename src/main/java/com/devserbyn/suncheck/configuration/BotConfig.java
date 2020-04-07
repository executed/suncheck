package com.devserbyn.suncheck.configuration;

import com.devserbyn.suncheck.model.SuncheckBot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Configuration
@RequiredArgsConstructor
public class BotConfig {

    private final SuncheckBot bot;

    @SneakyThrows
    @Bean
    public TelegramBotsApi getTelegramBotsApi() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(bot);
        return telegramBotsApi;
    }
}
