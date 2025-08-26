package com.educaflow.apps.seguridad;

import com.axelor.auth.EduFlowAuthResolver;
import com.axelor.auth.db.Permission;
import com.axelor.auth.db.User;
import com.axelor.db.JpaSecurity.AccessType;
import com.axelor.db.Model;
import com.educaflow.apps.expedientes.db.Expediente;
import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Optional;
import java.util.Set;

public class EducaFlowAuthResolver implements EduFlowAuthResolver {


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
            ExpedientePermiso expedientePermiso = new ExpedientePermiso(user);
            Set<Permission> all = expedientePermiso.getPermisos();
            return Optional.of(all);

        }

        if (modelClass.getPackageName().equals("com.educaflow.apps.configuracioncentro.db")) {
            String simpleName = modelClass.getSimpleName();
            ConfiguracionCentroPermiso configuracionCentroPermiso = new ConfiguracionCentroPermiso(user);
            String objectName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
            return configuracionCentroPermiso.getPermissionForEntity(objectName);
        }

        if (modelClass.getPackageName().equals("com.educaflow.apps.sistemaeducativo.db")) {
            Permission permission = new Permission();
            permission.setObject(object);
            permission.setCanRead(true);
            permission.setCanWrite(false);
            permission.setCanCreate(false);
            permission.setCanRemove(false);
            permission.setCanImport(false);
            permission.setCanExport(false);
            return Optional.of(Set.of(permission));
        }
        return Optional.empty();
    }


}
