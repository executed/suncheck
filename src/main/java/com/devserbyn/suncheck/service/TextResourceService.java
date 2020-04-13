package com.devserbyn.suncheck.service;

import com.devserbyn.suncheck.model.enums.TextResourceKeys;

public interface TextResourceService {

    public String get(TextResourceKeys textResourceKey);

    public void cacheAllTextResources();

    public void clearCaches();
}
