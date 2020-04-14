package com.devserbyn.suncheck.service.impl;

import com.devserbyn.suncheck.constant.STR_CONSTANT;
import com.devserbyn.suncheck.model.enums.TextResourceKeys;
import com.devserbyn.suncheck.service.TextResourceService;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:textresource.properties")
public class TextResourceServiceImpl implements TextResourceService {

    private final Environment environment;

    private Properties cachedTextResources = new Properties();

    @Override
    public String get(TextResourceKeys textResourceKey) {
        String textResourceCode = Optional.ofNullable(String.valueOf(textResourceKey.getTextResourceCode()))
                .orElseThrow(() -> new RuntimeException(String.format("Text resource hasn't got a code: %s", textResourceKey.toString())));
        String textResource = Optional.ofNullable(cachedTextResources.getProperty(textResourceCode))
                                      .orElse(environment.getProperty(textResourceCode));
        if (textResource == null) {
            throw new RuntimeException(String.format("Text resource %s wasn't found", textResourceCode));
        }
        textResource = textResource.replaceAll(STR_CONSTANT.BOT_ANSWER_NEW_LINE_CHAR, System.lineSeparator());

        return textResource;
    }

    @Override
    public void cacheAllTextResources() {
        MutablePropertySources propSources = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propSources.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .forEach(propName -> cachedTextResources.setProperty(propName, environment.getProperty(propName)));
    }

    @Override
    public void clearCaches() {
        cachedTextResources = new Properties();
    }
}
