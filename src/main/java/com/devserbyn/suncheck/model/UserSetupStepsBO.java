package com.devserbyn.suncheck.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class UserSetupStepsBO {

    private List<UserSetupSteps> userSetupStepsList = new ArrayList<>();

    @Getter
    @Setter
    public static class UserSetupSteps {

        private long chatId;
        private boolean waitingForLocation;
    }
}
