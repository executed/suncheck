package com.devserbyn.suncheck.listener;

import com.devserbyn.suncheck.constant.PATH_CONSTANT;
import com.devserbyn.suncheck.model.SuncheckBot;
import com.devserbyn.suncheck.service.ApplicationService;
import com.devserbyn.suncheck.service.TextResourceService;
import com.devserbyn.suncheck.utility.ResourceUtil;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartupApplicationListener implements ApplicationListener<ApplicationStartedEvent> {

    private final ResourceUtil resourceUtil;
    private final TextResourceService textResourceService;
    private final ApplicationService applicationService;
    private final SuncheckBot bot;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        applicationService.checkEnvVarsPresence();
        textResourceService.cacheAllTextResources();
        bot.setMyCommands();
        // Print banner saying bot was initialized
        System.out.println(resourceUtil.readResourceFileLines(PATH_CONSTANT.BANNER_BOT_INIT));
    }
}
