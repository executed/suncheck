package com.devserbyn.suncheck.service.scheduled;

import com.devserbyn.suncheck.constant.STR_CONSTANT;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeploymentService {

    @Scheduled(cron = "${deployment.preventScheduling.cronExp}")
    public void postponeSnoozeOnServer() throws IOException {
        URL obj = new URL(System.getenv().get(STR_CONSTANT.SERVER_URL_ENV_VAR));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.getResponseCode();
    }
}
