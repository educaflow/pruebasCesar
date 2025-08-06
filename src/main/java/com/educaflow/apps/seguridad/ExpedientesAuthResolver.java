package com.educaflow.apps.seguridad;

import com.axelor.auth.EduFlowAuthResolver;
import com.axelor.auth.db.Permission;
import com.axelor.auth.db.Role;
import com.axelor.auth.db.User;
import com.axelor.db.JpaSecurity.AccessType;
import com.educaflow.apps.expedientes.db.Expediente;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Optional;
import java.util.Set;

public class ExpedientesAuthResolver implements EduFlowAuthResolver {


    protected final Logger log = LoggerFactory.getLogger(getClass());

    private Set<Permission> filterPermissions(
            final Set<Permission> permissions, final String object, final AccessType type) {
        final Set<Permission> all = Sets.newLinkedHashSet();
        if (permissions == null || permissions.isEmpty()) {
            return all;
        }

        // add object permissions
        for (final Permission permission : permissions) {
            if (Objects.equal(object, permission.getObject()) && hasAccess(permission, type)) {
                all.add(permission);
            }
        }

        // add wild card permissions
        final String pkg = object.substring(0, object.lastIndexOf('.')) + ".*";
        for (final Permission permission : permissions) {
            if (Objects.equal(pkg, permission.getObject()) && hasAccess(permission, type)) {
                all.add(permission);
            }
        }

        return all;
    }


    @Override
    public Optional<Set<Permission>> resolve(final User user, final String object, final AccessType type, Long... ids) {
        Class<?> modelClass = null;
        try {
            modelClass = Class.forName(object);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (Expediente.class.isAssignableFrom(modelClass)) {
            String expedienteObject = "com.educaflow.apps.expedientes.db.Expediente";
            // user permissions
            Set<Permission> all = filterPermissions(user.getPermissions(), expedienteObject, type);

            // user's role permissions
            if (user.getRoles() != null) {
                for (final Role role : user.getRoles()) {
                    all.addAll(filterPermissions(role.getPermissions(), expedienteObject, type));
                }
            }

            // group permissions
            if (user.getGroup() != null) {
                all.addAll(filterPermissions(user.getGroup().getPermissions(), expedienteObject, type));
            }

            // group's role permissions
            if (user.getGroup() != null && user.getGroup().getRoles() != null) {
                for (final Role role : user.getGroup().getRoles()) {
                    all.addAll(filterPermissions(role.getPermissions(), expedienteObject, type));
                }
            }
            return Optional.of(all);

        }
        return Optional.empty();
    }

    private Set<Permission> buildPermissions() {
        Set<Permission> permissions = Sets.newLinkedHashSet();
        Permission permission = new Permission();
        permission.setObject("com.educaflow.apps.expedientes.db.Expediente");
        permission.setCanRead(true);
        permission.setCanWrite(false);
        permission.setCanCreate(false);
        permission.setCanExport(false);
        permission.setCanImport(false);
        permission.setCondition(this.getCondition("ambitoCreador", "INDIVIDUAL"));
        permission.setConditionParams("__user__");
        permissions.add(permission);
        permission.setCondition(this.getCondition("ambitoCreador", "CENTRO"));
        permissions.add(permission);
        return  permissions;
    }

    private String getCondition(String tipoAmbito, String ambito) {
        return "self.tipoExpediente." + tipoAmbito + " = '" + ambito + "'\n" +
                "AND EXISTS (\n" +
                "  SELECT 1\n" +
                "  FROM com.educaflow.apps.sistemaeducativo.db.CentroUsuario cu\n" +
                "  WHERE cu.usuario = ?\n" +
                "    AND cu.centro.code = '46019660'\n" +
                "    AND self.valoresAmbitoCreador.usuario = cu.usuario\n" +
                ")";
    }

    private String getCentroCondition() {
        return  "AND EXISTS (\n" +
                "  SELECT 1\n" +
                "  FROM com.educaflow.apps.sistemaeducativo.db.CentroUsuario cu\n" +
                "  WHERE cu.usuario = ?\n" +
                "    AND cu.centro.code = '46019660'\n" +
                ")";
    }


}
