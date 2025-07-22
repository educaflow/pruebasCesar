package com.educaflow.apps.expedientes.auth;

import com.axelor.auth.EduFlowAuthResolver;
import com.axelor.auth.db.Permission;
import com.axelor.auth.db.User;
import com.axelor.db.JpaSecurity.AccessType;

import java.util.Optional;
import java.util.Set;

public class ExpedientesAuthResolver implements EduFlowAuthResolver {

    /*@Override
    public Optional<Boolean> hasAccess(AccessType accessType, User user, Class<? extends Model> model) {
        System.out.printf("Interceptando acceso: model = %s, tipo = %s, user = %s%n", model.getName(), accessType, user.getName());


        if (!Expediente.class.isAssignableFrom(model)) {
            System.out.println("ExpedientesAuthResolver: Model " + model.getName() + " is not handled by ExpedientesAuthResolver.");
            return Optional.empty();
        }

        return Optional.of(false);

    }*/

/*    @Override
    public Optional<Boolean> hasAccess(AccessType accessType, User user, Model instance) {
        System.out.printf("Interceptando acceso (por instancia): %s, tipo = %s, user = %s%n",
                instance.getClass().getName(), accessType, user.getName());

        if (user.getRoles().stream().anyMatch(role -> "admin".equalsIgnoreCase(role.getName()))) {
            return Optional.of(true);
        }
        if (instance instanceof TipoExpediente) {
            TipoExpediente tipo = (TipoExpediente) instance;
            if ("JustificacionFaltaProfesorado".equals(tipo.getCode())) {
                System.out.println("Bloqueando acceso a tipo: " + tipo.getName());
                return Optional.of(false);
            }
        }
        return Optional.empty();
    }*/

    @Override
    public Optional<Set<Permission>> resolve(final User user, final String object, final AccessType type, Long... ids) {
        return Optional.empty();
    }
}
