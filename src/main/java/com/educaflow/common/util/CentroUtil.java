package com.educaflow.common.util;

import com.axelor.auth.AuthUtils;
import com.axelor.auth.db.User;
import com.axelor.meta.CallMethod;
import com.google.inject.Singleton;

@Singleton
public class CentroUtil {

    @CallMethod
    public String getCentroActivoName() {
        User userActivo = AuthUtils.getUser();
        if (userActivo == null) {
            return "";
        }
        return userActivo.getCentroActivo().getName();
    }
}
