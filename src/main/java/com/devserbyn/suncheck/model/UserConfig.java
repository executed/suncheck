package com.devserbyn.suncheck.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "user_config")
@Getter
@Setter
@NoArgsConstructor
public class UserConfig {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private User user;

    private boolean setupFinished;

    private String latitude;

    private String longitude;

    private String timezone;
    
    private String nextNotificationTime;

    private String nextNotificationType;
}
