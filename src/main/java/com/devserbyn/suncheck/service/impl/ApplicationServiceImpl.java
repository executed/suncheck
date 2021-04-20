package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.service.ApplicationService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Arrays.asList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    @Override
    public void checkEnvVarsPresence() {
        /*final List<String> requiredVars = asList(STR_CONSTANT.BOT_USERNAME_ENV_VAR, STR_CONSTANT.BOT_TOKEN_ENV_VAR);
        final List<String> extraVars = Collections.singletonList(STR_CONSTANT.SERVER_URL_ENV_VAR);
        // Handling required env vars
        List<String> absentReqVars = requiredVars.stream().filter(reqVar -> System.getenv().get(reqVar) == null).collect(Collectors.toList());
        if (!absentReqVars.isEmpty()) {
            absentReqVars.forEach(x -> log.error(String.format("Required environment variable missing: %s", x)));
            throw new RuntimeException("System can't be initialized without missing required variables");
        }
        // Handling extra env vars
        extraVars.stream().filter(extraVar -> System.getenv().get(extraVar) == null)
                          .forEach(x -> log.warn(String.format("Extra environment variable missing: %s", x)));*/
    }
}
