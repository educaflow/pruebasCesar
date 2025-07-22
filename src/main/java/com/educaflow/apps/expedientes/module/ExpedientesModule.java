package com.educaflow.apps.expedientes.module;

import com.axelor.app.AxelorModule;
import com.axelor.auth.EduFlowAuthResolverRegistry;
import com.educaflow.apps.expedientes.auth.ExpedientesAuthResolver;

public class ExpedientesModule extends AxelorModule {

    @Override
    protected void configure() {
        System.out.println("Configuring ExpedientesModule...");
        System.out.println("Registering ExpedientesAuthResolver...");
        EduFlowAuthResolverRegistry.register(new ExpedientesAuthResolver());
        System.out.println("ExpedientesAuthResolver registered...");
    }


}
