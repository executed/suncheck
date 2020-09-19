package com.devserbyn.suncheck.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IsAliveResponseEntity {

    private UUID uuid;

    private boolean status;

    private String info;

    private LocalDateTime dateCreated;
}
