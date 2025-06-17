package com.eduflow.modules.geo;

import com.axelor.app.AppSettings;
import com.axelor.app.AxelorModule;
import com.axelor.meta.service.MetaService;
import com.axelor.meta.service.ViewProcessor;

public class GeoModule extends AxelorModule {

    @Override
    protected void configure() {
        bind(ViewProcessor.class).to(ViewProcessorImpl.class);
        bind(MetaService.class).to(MetaServiceImplEduFlow.class);
    }
}

