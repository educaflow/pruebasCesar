package com.educaflow.apps.expedientes.tiposexpedientes.firmar_actas;

import com.axelor.inject.Beans;
import com.educaflow.apps.expedientes.common.EventContext;
import com.educaflow.apps.expedientes.common.annotations.OnEnterState;
import com.educaflow.apps.expedientes.common.annotations.WhenEvent;
import com.educaflow.apps.expedientes.db.FirmarActas;
import com.educaflow.apps.expedientes.db.repo.FirmarActasRepository;
import com.educaflow.common.validation.messages.BusinessException;

import com.google.inject.Inject;



public class EventManagerImpl extends com.educaflow.apps.expedientes.common.EventManager<FirmarActas, FirmarActas.State, FirmarActas.Event,FirmarActas.Profile> {

    private final FirmarActasRepository repository;

    @Inject
    public EventManagerImpl(FirmarActasRepository repository) {
        super(FirmarActas.class, FirmarActas.State.class, FirmarActas.Event.class,FirmarActas.Profile.class);
        this.repository = repository;
    }

    @Override
    public void triggerInitialEvent(FirmarActas firmarActas, EventContext<FirmarActas.Profile> eventContext) throws BusinessException {


    }


    @WhenEvent
    public void triggerDelete(FirmarActas firmarActas, FirmarActas original, EventContext<FirmarActas.Profile> eventContext) throws BusinessException {
        //firmarActas.updateState(FirmarActas.State.);
    }
    @WhenEvent
    public void triggerPresentar(FirmarActas firmarActas, FirmarActas original, EventContext<FirmarActas.Profile> eventContext) throws BusinessException {
        firmarActas.updateState(FirmarActas.State.FIRMA_POR_USUARIOS);
    }
    @WhenEvent
    public void triggerFirmar(FirmarActas firmarActas, FirmarActas original, EventContext<FirmarActas.Profile> eventContext) throws BusinessException {
        //firmarActas.updateState(FirmarActas.State.);
    }



/***************************************************************************************/
/*************************************** Estados ***************************************/
/***************************************************************************************/

    @OnEnterState
    public void onEnterEntradaDatos(FirmarActas firmarActas, EventContext<FirmarActas.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterFirmaPorUsuarios(FirmarActas firmarActas, EventContext<FirmarActas.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterAceptado(FirmarActas firmarActas, EventContext<FirmarActas.Profile> eventContext) {

    }









}