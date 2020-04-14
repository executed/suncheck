package com.devserbyn.suncheck.model;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class UserSetupStepsBO {

    private List<UserSetupSteps> userSetupStepsList = new ArrayList<>();

    public void removeByChatId(long chatId) {
        for (int i = 0; i < userSetupStepsList.size(); i++) {
            if (userSetupStepsList.get(i).getChatId() == chatId) {
                userSetupStepsList.remove(i);
                break;
            }
        }
    }

    @Getter
    @Setter
    public static class UserSetupSteps {

        public UserSetupSteps() { }

        public UserSetupSteps(long chatId) {
            this.chatId = chatId;
        }

        private long chatId;
        private boolean waitingForLocation;
        private LocalDateTime removemeSentTime = LocalDateTime.of(1999, 1, 1, 1, 1);
    }
}
