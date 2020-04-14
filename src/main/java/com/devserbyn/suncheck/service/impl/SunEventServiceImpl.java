package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.constant.INTEGER_CONSTANT;
import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.model.User;
import com.devserbyn.suncheck.model.UserConfig;
import com.devserbyn.suncheck.repository.UserConfigRepository;
import com.devserbyn.suncheck.service.SunEventService;
import com.devserbyn.suncheck.utility.DateUtility;
import com.devserbyn.suncheck.utility.JsonReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static com.devserbyn.suncheck.constant.STR_CONSTANT.SUNRISE_EVENT_NAME;
import static com.devserbyn.suncheck.constant.STR_CONSTANT.SUNSET_EVENT_NAME;
import static com.devserbyn.suncheck.constant.STR_CONSTANT.SUN_EVENT_API_URL_FORMAT;

@Service
@Slf4j
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
        String urlWithParams = String.format(SUN_EVENT_API_URL_FORMAT, userConfig.getLatitude(), userConfig.getLongitude());
        JSONObject json = JsonReader.readJsonFromUrl(urlWithParams);
        if (json == null) {
            throw new RuntimeException("Sun event API request returned no data");
        }
        JsonNode root = new ObjectMapper().readTree(json.toString());
        String utcTimeString = root.get("results").get(event).asText();

        String todayDateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String dateTimeString = todayDateString + " " + utcTimeString;
        LocalDateTime utcTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd h:m:s a"));
        Instant instant = utcTime.toInstant(ZoneOffset.UTC);
        LocalTime localTime = instant.atZone(ZoneId.of(userConfig.getTimezone())).toLocalTime();

        Date resultDate = new Date();
        resultDate.setHours(localTime.getHour());
        resultDate.setMinutes(localTime.getMinute());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STR_CONSTANT.MESSAGE_TIME_FORMAT, Locale.ENGLISH);

        return simpleDateFormat.format(resultDate);
    }

    /*
     * This function should be right after sun event notification was sent
     */
    @Override
    public void updateUserNextNotificationTime(User curUser, String previousSunEventType) {
        UserConfig curConfig = userConfigRepository.findAll().stream()
                .filter(x -> curUser.getChatId() == x.getUser().getChatId())
                .findFirst().orElseThrow(RuntimeException::new);

        String nextSunEventType = (previousSunEventType.equals(SUNRISE_EVENT_NAME)) ? SUNSET_EVENT_NAME : SUNRISE_EVENT_NAME;
        Date nextSunEventDate = DateUtility.convertHoursAndMinToDate(this.getSunEventTimeByUser(curUser, nextSunEventType));
        LocalDateTime nextSunEventDateTime = LocalDateTime.now()
                .withHour(nextSunEventDate.getHours())
                .withMinute(nextSunEventDate.getMinutes());
        if (LocalDateTime.now().isAfter(nextSunEventDateTime)) {
            nextSunEventDateTime = nextSunEventDateTime.plusDays(1);
        }
        LocalDateTime nextNotificationTime = nextSunEventDateTime.minusMinutes(INTEGER_CONSTANT.MIN_BEFORE_SUNEVENT_NOTIF);
        Date nextNotificationDate = Date.from(nextNotificationTime.atZone(ZoneId.systemDefault()).toInstant());

        curConfig.setNextNotificationType(nextSunEventType);
        curConfig.setNextNotificationTime(String.format("%tk:%tM", nextNotificationDate, nextNotificationDate));
        userConfigRepository.save(curConfig);
    }

    @Override
    public String getNearestSunEventType(User curUser) {
        List<Date> timeline = new ArrayList<>();
        Date now = new Date();
        timeline.add(now);
        String sunriseTime = this.getSunEventTimeByUser(curUser, SUNRISE_EVENT_NAME);
        String sunsetTime = this.getSunEventTimeByUser(curUser, SUNSET_EVENT_NAME);

        DateFormat readFormat = new SimpleDateFormat(STR_CONSTANT.MESSAGE_TIME_FORMAT);
        Date sunriseDate = null;
        Date sunsetDate = null;
        try {
            sunriseDate = readFormat.parse(sunriseTime);
            sunsetDate = readFormat.parse(sunsetTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeline.add(sunriseDate);
        timeline.add(sunsetDate);

        timeline.sort(Date::compareTo);

        return (timeline.indexOf(now) == 0 || timeline.indexOf(now) == 2) ? SUNRISE_EVENT_NAME : SUNSET_EVENT_NAME;
    }
}
