package com.devserbyn.suncheck.service;

import com.devserbyn.suncheck.model.User;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SunEventServiceTest {

    @Autowired
    private UserRepositoryService userRepositoryService;
    @Autowired
    private SunEventService sunEventService;

    @Test
    public void getSunriseEventTimeByUser() {
        User user = userRepositoryService.findByUsername("orbalts").orElseThrow(RuntimeException::new);
        String sunrise = sunEventService.getSunEventTimeByUser(user, "sunrise");
        String sunset = sunEventService.getSunEventTimeByUser(user, "sunset");
        Assert.assertNotNull(sunrise, sunset);

        System.out.println("Sunrise: " + sunrise);
        System.out.println("Sunset: " + sunset);
    }

}
