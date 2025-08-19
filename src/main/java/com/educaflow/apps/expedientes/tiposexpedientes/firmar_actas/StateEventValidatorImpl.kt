package com.educaflow.apps.expedientes.tiposexpedientes.firmar_actas;

import com.educaflow.apps.expedientes.common.StateEventValidator
import com.educaflow.apps.expedientes.common.annotations.BeanValidationRulesForStateAndEvent


import com.educaflow.common.validation.dsl.ifValueIn
import com.educaflow.common.validation.dsl.rules
import com.educaflow.common.validation.engine.BeanValidationRules
import com.educaflow.common.validation.rules.*
import java.time.LocalDate
import com.educaflow.apps.expedientes.db.FirmarActas as model

class StateEventValidatorImpl: StateEventValidator {

    @BeanValidationRulesForStateAndEvent
    public fun getForStateEntradaDatosInEventDelete(): BeanValidationRules {
        return rules {

        }
    }

    @BeanValidationRulesForStateAndEvent
    public fun getForStateEntradaDatosInEventPresentar(): BeanValidationRules {
        return rules {
            /*field(model::getCiclo) {
                +Required()
            }
            field(model::getCurso) {
                +Required()
            }
            field(model::getGrupo) {
                +Required()
            }*/
        }
    }

    @BeanValidationRulesForStateAndEvent
    public fun getForStateFirmaPorUsuariosInEventFirmar(): BeanValidationRules {
        return rules {
        }
    }
;

}