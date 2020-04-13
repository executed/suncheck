package com.devserbyn.suncheck.service;

import com.devserbyn.suncheck.model.enums.TextResourceKeys;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TextResourceServiceTest {

    @Autowired
    private TextResourceService textResourceService;

    @Test
    public void testGet() {
        String foundTextRes = textResourceService.get(TextResourceKeys.FIRST_TXT_RES);
        Assert.assertEquals("First text resource", foundTextRes);
    }

    @Test
    public void testGetCachedTR() {
        textResourceService.clearCaches();
        textResourceService.cacheAllTextResources();
        String foundTextRes = textResourceService.get(TextResourceKeys.SECOND_TXT_RES);
        Assert.assertEquals("Second text resource", foundTextRes);
    }
}
