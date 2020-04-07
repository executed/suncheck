package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.model.User;
import com.devserbyn.suncheck.model.UserConfig;
import com.devserbyn.suncheck.model.UserSetupStepsBO;
import com.devserbyn.suncheck.model.UserSetupStepsBO.UserSetupSteps;
import com.devserbyn.suncheck.repository.UserConfigRepository;
import com.devserbyn.suncheck.service.SunEventService;
import com.devserbyn.suncheck.service.UserInputResolver;
import com.devserbyn.suncheck.service.UserRepositoryService;
import com.devserbyn.suncheck.service.UserService;
import com.devserbyn.suncheck.utility.TimezoneMapper;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInputResolverImpl implements UserInputResolver {

    private final UserService userService;
    private final UserRepositoryService userRepositoryService;
    private final UserSetupStepsBO userSetupStepsBO;
    private final UserConfigRepository userConfigRepository;
    private final SunEventService sunEventService;

    @Override
    public String resolveCommand(Update update) {
        String command = update.getMessage().getText();
        switch (command) {
            case "/start": {
                if (userService.checkIfUserRegistered(update.getMessage().getChatId())) {
                    return "You've already joined SunCheck";
                }
                userService.register(update);
                List<UserSetupSteps> userSetupStepsList = userSetupStepsBO.getUserSetupStepsList();
                boolean isPresent = false;
                for (UserSetupSteps setupSteps : userSetupStepsList) {
                    if (setupSteps.getChatId() == (update.getMessage().getChatId())) {
                        isPresent = true;
                        setupSteps.setWaitingForLocation(true);
                    }
                }
                if (!isPresent) {
                    UserSetupSteps userSetupSteps = new UserSetupSteps();
                    userSetupSteps.setChatId(update.getMessage().getChatId());
                    userSetupSteps.setWaitingForLocation(true);
                    userSetupStepsList.add(userSetupSteps);
                }
                return "Please send me your Location so I can recognize the timezone";
            }
            case "/sunrise": {
                if (!userService.checkIfUserRegistered(update.getMessage().getChatId())) {
                    return "You haven't configured SunCheck yet";
                }
                UserConfig userConfig = userConfigRepository.findAll().stream()
                        .filter(x -> x.getUser().getChatId() == update.getMessage().getChatId())
                        .findFirst().orElse(null);
                if (userConfig == null || !userConfig.isSetupFinished()) {
                    return "You haven't configured SunCheck yet";
                }
                return STR_CONSTANT.SUNRISE_UNICODE + " " + sunEventService.getSunEventTimeByUser(userConfig.getUser(), "sunrise");
            }
            case "/sunset": {
                if (!userService.checkIfUserRegistered(update.getMessage().getChatId())) {
                    return "You haven't configured SunCheck yet";
                }
                UserConfig userConfig = userConfigRepository.findAll().stream()
                        .filter(x -> x.getUser().getChatId() == update.getMessage().getChatId())
                        .findFirst().orElse(null);
                if (userConfig == null || !userConfig.isSetupFinished()) {
                    return "You haven't configured SunCheck yet";
                }
                return STR_CONSTANT.SUNSET_UNICODE + " " + sunEventService.getSunEventTimeByUser(userConfig.getUser(), "sunset");
            }
            default: return "Command wasn't recognized";
        }
    }

    @Override
    public String resolvePlainText(Update update) {
        return null;
    }

    @Override
    public String resolveLocation(Update update) {
        long curChatId = update.getMessage().getChatId();
        Optional<User> user = userRepositoryService.findByChatId(curChatId);
        if (user.isEmpty()) {
            return "Something went wrong";
        }
        for (UserConfig userConfig : userConfigRepository.findAll()) {
            if (userConfig.getUser().getChatId() == curChatId && userConfig.isSetupFinished()) {
                return "You've already finished your location setup";
            }
        }
        UserConfig userConfig = new UserConfig();

        userConfig.setUser(user.orElseThrow(RuntimeException::new));
        userConfig.setLatitude(update.getMessage().getLocation().getLatitude().toString());
        userConfig.setLongitude(update.getMessage().getLocation().getLongitude().toString());
        userConfig.setTimezone(TimezoneMapper.latLngToTimezoneString(Double.parseDouble(userConfig.getLatitude()), Double.parseDouble(userConfig.getLongitude())));
        userConfig.setSetupFinished(true);
        userConfigRepository.save(userConfig);

        User curUser = user.orElseThrow(RuntimeException::new);

        String nextSunEventType = (sunEventService.getNearestSunEventType(curUser).equals("sunrise")) ? "sunset" : "sunrise";
        sunEventService.updateUserNextNotificationTime(curUser, nextSunEventType);

        return "Everything is ready to go. Wait for your notifications. Typically we'll send it to you 60 min before the sunset/sunrise";
    }
}
