package com.eduflow.modules.geo;

import com.axelor.meta.service.MetaService;
import com.axelor.rpc.Response;

public class MetaServiceImplEduFlow extends MetaService {


    @Override
    public Response findView(String model, String name, String type) {


        System.out.println(name);
        if (name.startsWith("_")) {
            name=name.substring(1);
        }


        return super.findView(model, name, type);
    }
}
