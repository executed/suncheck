package com.devserbyn.suncheck.service.scheduled;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySource ("classpath:deployment.properties")
public class DeploymentService {

    private final Environment environment;

    @Scheduled(cron = "${deployment.preventScheduling.cronExp}")
    public void postponeSnoozeOnServer() throws IOException {
        URL obj = new URL(environment.getProperty("deployment.preventScheduling.accessUrl"));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.getResponseCode();
    }
}
