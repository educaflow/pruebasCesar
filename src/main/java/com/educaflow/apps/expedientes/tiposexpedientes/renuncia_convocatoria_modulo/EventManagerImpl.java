package com.educaflow.apps.expedientes.tiposexpedientes.renuncia_convocatoria_modulo;

import com.axelor.inject.Beans;
import com.educaflow.apps.expedientes.common.EventContext;
import com.educaflow.apps.expedientes.common.annotations.OnEnterState;
import com.educaflow.apps.expedientes.common.annotations.WhenEvent;
import com.educaflow.apps.expedientes.db.RenunciaConvocatoriaModulo;
import com.educaflow.apps.expedientes.db.repo.RenunciaConvocatoriaModuloRepository;
import com.educaflow.common.validation.messages.BusinessException;

import com.google.inject.Inject;



public class EventManagerImpl extends com.educaflow.apps.expedientes.common.EventManager<RenunciaConvocatoriaModulo, RenunciaConvocatoriaModulo.State, RenunciaConvocatoriaModulo.Event,RenunciaConvocatoriaModulo.Profile> {

    private final RenunciaConvocatoriaModuloRepository repository;

    @Inject
    public EventManagerImpl(RenunciaConvocatoriaModuloRepository repository) {
        super(RenunciaConvocatoriaModulo.class, RenunciaConvocatoriaModulo.State.class, RenunciaConvocatoriaModulo.Event.class,RenunciaConvocatoriaModulo.Profile.class);
        this.repository = repository;
    }

    @Override
    public void triggerInitialEvent(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) throws BusinessException {


    }


    @WhenEvent
    public void triggerDelete(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, RenunciaConvocatoriaModulo original, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) throws BusinessException {
        //renunciaConvocatoriaModulo.updateState(RenunciaConvocatoriaModulo.State.);
    }
    @WhenEvent
    public void triggerPresentar(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, RenunciaConvocatoriaModulo original, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) throws BusinessException {
        //renunciaConvocatoriaModulo.updateState(RenunciaConvocatoriaModulo.State.);
    }
    @WhenEvent
    public void triggerBack(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, RenunciaConvocatoriaModulo original, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) throws BusinessException {
        //renunciaConvocatoriaModulo.updateState(RenunciaConvocatoriaModulo.State.);
    }
    @WhenEvent
    public void triggerPresentarDocumentosFirmados(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, RenunciaConvocatoriaModulo original, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) throws BusinessException {
        //renunciaConvocatoriaModulo.updateState(RenunciaConvocatoriaModulo.State.);
    }
    @WhenEvent
    public void triggerResolver(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, RenunciaConvocatoriaModulo original, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) throws BusinessException {
        //renunciaConvocatoriaModulo.updateState(RenunciaConvocatoriaModulo.State.);
    }



/***************************************************************************************/
/*************************************** Estados ***************************************/
/***************************************************************************************/

    @OnEnterState
    public void onEnterEntradaDatos(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterFirmaPorUsuario(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterResolverPermitirComision(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterEntregaTickets(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterAceptado(RenunciaConvocatoriaModulo renunciaConvocatoriaModulo, EventContext<RenunciaConvocatoriaModulo.Profile> eventContext) {

    }









}