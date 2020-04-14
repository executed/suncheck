package com.devserbyn.suncheck.service.scheduled;

import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.model.SuncheckBot;
import com.devserbyn.suncheck.model.UserConfig;
import com.devserbyn.suncheck.repository.UserConfigRepository;
import com.devserbyn.suncheck.service.SunEventService;
import com.devserbyn.suncheck.service.UserCommandResolver;

import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:cron_schedule.properties")
public class SunEventNotificationService {

    private final SuncheckBot suncheckBot;
    private final UserConfigRepository userConfigRepository;
    private final SunEventService sunEventService;
    private final UserCommandResolver userCommandResolver;

    @Scheduled(cron = "${sunEventNotificationCheck.cron}")
    public void sendSunEventNotificationToUsers() {
        for (UserConfig userConfig : userConfigRepository.findAll()) {
            LocalDateTime userZDT = LocalDateTime.now(ZoneId.of(userConfig.getTimezone()).getRules().getOffset(Instant.now()));
            Date userZonedDate = new Date();
            userZonedDate.setHours(userZDT.getHour());
            userZonedDate.setMinutes(userZDT.getMinute());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STR_CONSTANT.MESSAGE_TIME_FORMAT, Locale.ENGLISH);

            if (userConfig.getNextNotificationTime().equals(simpleDateFormat.format(userZonedDate))) {
                Long chatId = userConfig.getUser().getChatId();
                String message = userCommandResolver.resolveSunEvent(chatId, userConfig.getNextNotificationType());
                // disabling notification sound if it is sunrise
                if (STR_CONSTANT.SUNRISE_EVENT_NAME.equals(userConfig.getNextNotificationType())) {
                    suncheckBot.setSilentMessage(true);
                }
                suncheckBot.sendResponse(userConfig.getUser().getChatId(), message);
                suncheckBot.setSilentMessage(false);

                sunEventService.updateUserNextNotificationTime(userConfig.getUser(), userConfig.getNextNotificationType());
            }
        }
    }
}
