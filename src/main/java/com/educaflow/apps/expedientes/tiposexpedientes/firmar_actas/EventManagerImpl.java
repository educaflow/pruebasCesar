package com.educaflow.apps.expedientes.tiposexpedientes.firmar_actas;

import com.axelor.auth.AuthUtils;
import com.axelor.auth.db.User;
import com.axelor.db.JpaRepository;
import com.axelor.inject.Beans;
import com.educaflow.apps.expedientes.common.EventContext;
import com.educaflow.apps.expedientes.common.annotations.OnEnterState;
import com.educaflow.apps.expedientes.common.annotations.WhenEvent;
import com.educaflow.apps.expedientes.db.FirmarActas;
import com.educaflow.apps.expedientes.db.ValoresAmbito;
import com.educaflow.apps.expedientes.db.repo.FirmarActasRepository;
import com.educaflow.apps.sistemaeducativo.db.CentroUsuario;
import com.educaflow.common.util.AxelorDBUtil;
import com.educaflow.common.validation.messages.BusinessException;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EventManagerImpl extends com.educaflow.apps.expedientes.common.EventManager<FirmarActas, FirmarActas.State, FirmarActas.Event,FirmarActas.Profile> {

    private final FirmarActasRepository repository;
    private final JpaRepository<CentroUsuario> centroUsuarioRepository;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    public EventManagerImpl(FirmarActasRepository repository) {
        super(FirmarActas.class, FirmarActas.State.class, FirmarActas.Event.class,FirmarActas.Profile.class);
        this.repository = repository;
        this.centroUsuarioRepository = AxelorDBUtil.getRepository(CentroUsuario.class);
    }

    @Override
    public void triggerInitialEvent(FirmarActas firmarActas, EventContext<FirmarActas.Profile> eventContext) throws BusinessException {

        User currentUser = AuthUtils.getUser();
        CentroUsuario centroUsuario = centroUsuarioRepository.all()
                .filter("self.usuario = ?1", currentUser)
                .fetchOne();


        ValoresAmbito valoresAmbitoCreador = new ValoresAmbito();
        valoresAmbitoCreador.setUsuario(currentUser);
        valoresAmbitoCreador.setCentro(centroUsuario.getCentro());
        valoresAmbitoCreador.setDepartamento(centroUsuario.getDepartamentos().stream().findFirst().orElse(null));

        firmarActas.setValoresAmbitoCreador(valoresAmbitoCreador);
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