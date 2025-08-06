package com.educaflow.common.util;

import com.axelor.meta.CallMethod;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class UserUtil {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @CallMethod
    public String getCentroActivo() {
        log.debug("getting active center for user");
        return "CIPFP Mislata";
    }
}
