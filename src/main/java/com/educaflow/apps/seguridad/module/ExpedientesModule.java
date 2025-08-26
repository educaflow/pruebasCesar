package com.educaflow.apps.seguridad.module;

import com.axelor.app.AxelorModule;
import com.axelor.auth.EduFlowAuthResolverRegistry;
import com.educaflow.apps.seguridad.EducaFlowAuthResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpedientesModule extends AxelorModule {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected void configure() {
        log.info("Registrando ExpedientesAuthResolver...");
        EduFlowAuthResolverRegistry.register(new EducaFlowAuthResolver());
        log.info("ExpedientesAuthResolver registrado correctamente.");
    }



}
