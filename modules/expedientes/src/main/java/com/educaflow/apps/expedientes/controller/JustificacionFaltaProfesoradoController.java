package com.educaflow.apps.expedientes.controller;

import com.axelor.meta.CallMethod;

import java.util.Map;

public class JustificacionFaltaProfesoradoController {

    @CallMethod
    public Map<String, Object> firmarJustificante(String sourceField, String targetField, String sufijo, Long tipoExpediente) {
        Map<String, Object> payload = Map.of(
                "sourceField", sourceField,
                "targetField", targetField,
                "sufijo", sufijo,
                "tipoExpediente", tipoExpediente
        );
        Map<String, Object> data = Map.of(
                "executeJs", true,
                "methodJs", "signDocument",
                "payload", payload
        );
        return data;
    }

}
