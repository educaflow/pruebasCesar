package com.educaflow.apps.expedientes.tiposexpedientes.firmar_actas;

import com.axelor.auth.AuthUtils;
import com.axelor.auth.db.User;
import com.axelor.db.JpaRepository;
import com.educaflow.apps.configuracioncentro.db.Centro;
import com.educaflow.apps.configuracioncentro.db.Departamento;
import com.educaflow.apps.configuracioncentro.db.repo.DepartamentoRepository;
import com.educaflow.apps.expedientes.common.EventContext;
import com.educaflow.apps.expedientes.common.annotations.OnEnterState;
import com.educaflow.apps.expedientes.common.annotations.WhenEvent;
import com.educaflow.apps.expedientes.db.FirmarActas;
import com.educaflow.apps.expedientes.db.ValoresAmbito;
import com.educaflow.common.util.AxelorDBUtil;
import com.educaflow.common.validation.messages.BusinessException;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EventManagerImpl extends com.educaflow.apps.expedientes.common.EventManager<FirmarActas, FirmarActas.State, FirmarActas.Event,FirmarActas.Profile> {

    protected final JpaRepository<Departamento> departamentoRepository;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    public EventManagerImpl(DepartamentoRepository departamentoRepository) {
        super(FirmarActas.class, FirmarActas.State.class, FirmarActas.Event.class,FirmarActas.Profile.class);
        this.departamentoRepository = AxelorDBUtil.getRepository(Departamento.class);
    }

    @Override
    public void triggerInitialEvent(FirmarActas firmarActas, EventContext<FirmarActas.Profile> eventContext) throws BusinessException {

        User currentUser = AuthUtils.getUser();
        Centro centroActivo = currentUser.getCentroActivo();


        ValoresAmbito valoresAmbitoCreador = new ValoresAmbito();
        valoresAmbitoCreador.setUsuario(currentUser);
        valoresAmbitoCreador.setCentro(centroActivo);
        Departamento departamento = departamentoRepository
                .all()
                .filter("self.code = :code")
                .bind("code", "ADMINISTRACION")
                .fetchOne();
        valoresAmbitoCreador.setDepartamento(departamento);
        firmarActas.setValoresAmbitoCreador(valoresAmbitoCreador);

        /*ValoresAmbito valoresAmbitoResponsable = new ValoresAmbito();
        valoresAmbitoResponsable.setUsuario(currentUser);
        valoresAmbitoResponsable.setCentro(centroActivo);
        firmarActas.setValoresAmbitoResponsable(valoresAmbitoResponsable);*/
    }


    @WhenEvent
    public void triggerDelete(FirmarActas firmarActas, FirmarActas original, EventContext<FirmarActas.Profile> eventContext) throws BusinessException {
        /*ValoresAmbito valoresAmbitoResponsable = new ValoresAmbito();
        valoresAmbitoResponsable.setUsuario(AuthUtils.getUser());
        valoresAmbitoResponsable.setCentro(AuthUtils.getUser().getCentroActivo());
        valoresAmbitoResponsable.setCiclo(firmarActas.getCiclo());
        valoresAmbitoResponsable.setCurso(firmarActas.getCurso());
        valoresAmbitoResponsable.setGrupo(firmarActas.getGrupo());
        firmarActas.setValoresAmbitoResponsable(valoresAmbitoResponsable);
        firmarActas.updateState(FirmarActas.State.FIRMA_POR_USUARIOS);*/
    }
    @WhenEvent
    public void triggerPresentar(FirmarActas firmarActas, FirmarActas original, EventContext<FirmarActas.Profile> eventContext) throws BusinessException {
        /*ValoresAmbito valoresAmbitoResponsable = new ValoresAmbito();
        valoresAmbitoResponsable.setUsuario(AuthUtils.getUser());
        valoresAmbitoResponsable.setCentro(AuthUtils.getUser().getCentroActivo());
        System.out.println("Código Ciclo: " + firmarActas.getCiclo().getCode());
        System.out.println("Id Ciclo: " + firmarActas.getCiclo().getId());
        valoresAmbitoResponsable.setCiclo(firmarActas.getCiclo());
        valoresAmbitoResponsable.setCurso(firmarActas.getCurso());
        System.out.println("Grupo: " + firmarActas.getGrupo());
        valoresAmbitoResponsable.setGrupo(firmarActas.getGrupo());
        firmarActas.setValoresAmbitoResponsable(valoresAmbitoResponsable);
        firmarActas.updateState(FirmarActas.State.FIRMA_POR_USUARIOS);*/
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