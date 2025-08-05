package com.educaflow.apps.expedientes.tiposexpedientes.certificado_titulo_ciclo;

import com.axelor.inject.Beans;
import com.educaflow.apps.expedientes.common.EventContext;
import com.educaflow.apps.expedientes.common.annotations.OnEnterState;
import com.educaflow.apps.expedientes.common.annotations.WhenEvent;
import com.educaflow.apps.expedientes.db.CertificadoTituloCiclo;
import com.educaflow.apps.expedientes.db.repo.CertificadoTituloCicloRepository;
import com.educaflow.common.validation.messages.BusinessException;

import com.google.inject.Inject;



public class EventManagerImpl extends com.educaflow.apps.expedientes.common.EventManager<CertificadoTituloCiclo, CertificadoTituloCiclo.State, CertificadoTituloCiclo.Event,CertificadoTituloCiclo.Profile> {

    private final CertificadoTituloCicloRepository repository;

    @Inject
    public EventManagerImpl(CertificadoTituloCicloRepository repository) {
        super(CertificadoTituloCiclo.class, CertificadoTituloCiclo.State.class, CertificadoTituloCiclo.Event.class,CertificadoTituloCiclo.Profile.class);
        this.repository = repository;
    }

    @Override
    public void triggerInitialEvent(CertificadoTituloCiclo certificadoTituloCiclo, EventContext<CertificadoTituloCiclo.Profile> eventContext) throws BusinessException {


    }


    @WhenEvent
    public void triggerDelete(CertificadoTituloCiclo certificadoTituloCiclo, CertificadoTituloCiclo original, EventContext<CertificadoTituloCiclo.Profile> eventContext) throws BusinessException {
        //certificadoTituloCiclo.updateState(CertificadoTituloCiclo.State.);
    }
    @WhenEvent
    public void triggerPresentar(CertificadoTituloCiclo certificadoTituloCiclo, CertificadoTituloCiclo original, EventContext<CertificadoTituloCiclo.Profile> eventContext) throws BusinessException {
        //certificadoTituloCiclo.updateState(CertificadoTituloCiclo.State.);
    }
    @WhenEvent
    public void triggerSubsanar(CertificadoTituloCiclo certificadoTituloCiclo, CertificadoTituloCiclo original, EventContext<CertificadoTituloCiclo.Profile> eventContext) throws BusinessException {
        //certificadoTituloCiclo.updateState(CertificadoTituloCiclo.State.);
    }
    @WhenEvent
    public void triggerAceptar(CertificadoTituloCiclo certificadoTituloCiclo, CertificadoTituloCiclo original, EventContext<CertificadoTituloCiclo.Profile> eventContext) throws BusinessException {
        //certificadoTituloCiclo.updateState(CertificadoTituloCiclo.State.);
    }
    @WhenEvent
    public void triggerRechazar(CertificadoTituloCiclo certificadoTituloCiclo, CertificadoTituloCiclo original, EventContext<CertificadoTituloCiclo.Profile> eventContext) throws BusinessException {
        //certificadoTituloCiclo.updateState(CertificadoTituloCiclo.State.);
    }



/***************************************************************************************/
/*************************************** Estados ***************************************/
/***************************************************************************************/

    @OnEnterState
    public void onEnterEntradaDatos(CertificadoTituloCiclo certificadoTituloCiclo, EventContext<CertificadoTituloCiclo.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterRevision(CertificadoTituloCiclo certificadoTituloCiclo, EventContext<CertificadoTituloCiclo.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterAceptado(CertificadoTituloCiclo certificadoTituloCiclo, EventContext<CertificadoTituloCiclo.Profile> eventContext) {

    }
    @OnEnterState
    public void onEnterRechazado(CertificadoTituloCiclo certificadoTituloCiclo, EventContext<CertificadoTituloCiclo.Profile> eventContext) {

    }









}