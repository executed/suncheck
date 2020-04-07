package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.constant.INTEGER_CONSTANT;
import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.model.User;
import com.devserbyn.suncheck.model.UserConfig;
import com.devserbyn.suncheck.repository.UserConfigRepository;
import com.devserbyn.suncheck.service.SunEventService;
import com.devserbyn.suncheck.utility.JsonReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class SunEventServiceImpl implements SunEventService {

    private final UserConfigRepository userConfigRepository;


    @SneakyThrows
    @Override
    public String getSunEventTimeByUser(User user, String event) {
        UserConfig userConfig = null;
        for (UserConfig config : userConfigRepository.findAll()) {
            if (config.getUser().getChatId() == user.getChatId()) {
                userConfig = config;
            }
        }
        if (userConfig == null) {
            throw new RuntimeException();
        }
        // Requesting API UTC sunrise time
        StringBuilder apiURL = new StringBuilder("https://api.sunrise-sunset.org/json?");
        apiURL.append("lat=").append(userConfig.getLatitude())
              .append("&lng=").append(userConfig.getLongitude());

        JSONObject json = JsonReader.readJsonFromUrl(apiURL.toString());
        JsonNode root = new ObjectMapper().readTree(json.toString());
        String utcTimeString = root.get("results").get(event).asText();

        String todayDateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateTimeString = todayDateString + " " + utcTimeString;
        LocalDateTime utcTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd h:m:s a"));
        Instant instant = utcTime.toInstant(ZoneOffset.UTC);
        LocalTime localTime = instant.atZone(ZoneId.of(userConfig.getTimezone())).toLocalTime();

        return localTime.format(DateTimeFormatter.ofPattern(STR_CONSTANT.MESSAGE_TIME_FORMAT));
    }

    /*
     * This function should be right after sun event notification was sent
     */
    @Override
    public void updateUserNextNotificationTime(User curUser, String previousSunEventType) {
        UserConfig curConfig = userConfigRepository.findAll().stream()
                .filter(x -> curUser.getChatId() == x.getUser().getChatId())
                .findFirst().orElseThrow(RuntimeException::new);

        String nextSunEventType = (previousSunEventType.equals("sunrise")) ? "sunset" : "sunrise";
        LocalTime nextSunEventTime = LocalTime.parse(this.getSunEventTimeByUser(curUser, nextSunEventType), DateTimeFormatter.ofPattern(STR_CONSTANT.MESSAGE_TIME_FORMAT));
        LocalDateTime nextSunEventDateTime = LocalDateTime.now()
                .withHour(nextSunEventTime.getHour())
                .withMinute(nextSunEventTime.getMinute());
        if (LocalDateTime.now().isAfter(nextSunEventDateTime)) {
            nextSunEventDateTime = nextSunEventDateTime.plusDays(1);
        }
        LocalDateTime nextNotificationTime = nextSunEventDateTime.minusMinutes(INTEGER_CONSTANT.MIN_BEFORE_SUNEVENT_NOTIF);

        curConfig.setNextNotificationType(nextSunEventType);
        curConfig.setNextNotificationTime(nextNotificationTime.format(DateTimeFormatter.ofPattern(STR_CONSTANT.MESSAGE_TIME_FORMAT)));
        userConfigRepository.save(curConfig);
    }

    @Override
    public String getNearestSunEventType(User curUser) {
        List<LocalTime> timeline = new ArrayList<>();
        LocalTime now = LocalTime.now();
        timeline.add(now);
        timeline.add(LocalTime.parse(this.getSunEventTimeByUser(curUser, "sunrise"), DateTimeFormatter.ofPattern(STR_CONSTANT.MESSAGE_TIME_FORMAT)));
        timeline.add(LocalTime.parse(this.getSunEventTimeByUser(curUser, "sunset"), DateTimeFormatter.ofPattern(STR_CONSTANT.MESSAGE_TIME_FORMAT)));

        timeline.sort(LocalTime::compareTo);

        return (timeline.indexOf(now) == 0 || timeline.indexOf(now) == 2) ? "sunrise" : "sunset";
    }
}
