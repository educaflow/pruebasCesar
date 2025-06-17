package com.eduflow.modules.geo;

import com.axelor.i18n.I18n;
import com.axelor.inject.Beans;
import com.axelor.meta.loader.XMLViews;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.meta.schema.views.AbstractView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.eduflow.common.geo.db.Provincia;
import com.axelor.meta.service.MetaService;
import com.axelor.rpc.Response;

public class FlowAction {

    public void abrirFormularioDinamico(ActionRequest request, ActionResponse response) {
        Provincia registroActual = request.getContext().asType(Provincia.class);

        if (registroActual == null) {
            response.setInfo("No se pudo obtener el registro.");
            return;
        }

        System.out.println("Registro actual: " + registroActual.getDescripcion());

        String vistaFormularioADetalle;
        String tipoRegistro = registroActual.getEstado();

        if ("A".equals(tipoRegistro)) {
            vistaFormularioADetalle = "form-provincia-A";
        } else if ("B".equals(tipoRegistro)) {
            vistaFormularioADetalle = "form-provincia-B";
        } else {
            vistaFormularioADetalle = "form-provincia";
        }

        response.setView(
                ActionView.define("Hola")
                        .model("com.eduflow.common.geo.db.Provincia")
                        .add("form", vistaFormularioADetalle)
                        .param("forceEdit","true")
                        .context("_showRecord", registroActual.getId())
                        .map());

    }

    public void printA(ActionRequest request, ActionResponse response) {
        Provincia provincia = request.getContext().asType(Provincia.class);
        System.out.println("A");
        System.out.println(request.getAction());
        System.out.println(provincia.getIdProvincia());
        System.out.println(provincia.getDescripcion());

        response.setView(
                ActionView.define("Hola")
                        .model("com.eduflow.common.geo.db.Provincia")
                        .add("grid","grid-provincias" )
                        .map());
    }
    public void printB(ActionRequest request, ActionResponse response) {
        System.out.println("B");
    }
    public void printC(ActionRequest request, ActionResponse response) {
        System.out.println("C");
    }
    public void printD(ActionRequest request, ActionResponse response) {
        System.out.println("D");
    }
}
