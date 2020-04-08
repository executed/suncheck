package com.devserbyn.suncheck.service.scheduled;

import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.model.SuncheckBot;
import com.devserbyn.suncheck.model.User;
import com.devserbyn.suncheck.model.UserConfig;
import com.devserbyn.suncheck.repository.UserConfigRepository;
import com.devserbyn.suncheck.service.SunEventService;

import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:cron_schedule.properties")
public class SunEventNotificationService {

    private final SuncheckBot suncheckBot;
    private final UserConfigRepository userConfigRepository;
    private final SunEventService sunEventService;

    @Scheduled(cron = "${sunEventNotificationCheck.cron}")
    public void sendSunEventNotificationToUsers() {
        for (UserConfig userConfig : userConfigRepository.findAll()) {
            String userZDTFormatted = LocalDateTime.now(ZoneId.of(userConfig.getTimezone()).getRules().getOffset(Instant.now()))
                    .format(DateTimeFormatter.ofPattern(STR_CONSTANT.MESSAGE_TIME_FORMAT));
            if (userConfig.getNextNotificationTime().equals(userZDTFormatted)) {
                StringBuilder msgText = new StringBuilder();
                msgText.append((userConfig.getNextNotificationType().equals("sunrise")) ? STR_CONSTANT.SUNRISE_UNICODE : STR_CONSTANT.SUNSET_UNICODE)
                       .append(sunEventService.getSunEventTimeByUser(userConfig.getUser(), userConfig.getNextNotificationType()));
                suncheckBot.sendResponse(userConfig.getUser().getChatId(), msgText.toString());

                sunEventService.updateUserNextNotificationTime(userConfig.getUser(), userConfig.getNextNotificationType());
            }
        }
    }
}
