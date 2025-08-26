package com.educaflow.apps.seguridad;

import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.Permission;
import com.axelor.auth.db.User;
import com.axelor.db.JpaRepository;
import com.axelor.db.Model;
import com.educaflow.apps.configuracioncentro.db.Centro;
import com.educaflow.apps.configuracioncentro.db.CursoAcademicoCentroUsuario;
import com.educaflow.apps.configuracioncentro.db.repo.ConfiguracionCentroRepository;
import com.educaflow.common.util.AxelorDBUtil;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;

public class ConfiguracionCentroPermiso {

    private final User currentUser;

    private static final Map<String, Set<Permission>> cachePermisos = new HashMap<>();
    private final String PACKAGE = "com.educaflow.apps.configuracioncentro.db";
    private Map<String, String> joinTables = Map.of(
            "departamento", PACKAGE + ".CursoAcademicoDepartamento",
            "cargo", PACKAGE + ".CursoAcademicoCargo",
            "grupo", PACKAGE + ".CursoAcademicoGrupo",
            "centroUsuario", PACKAGE + ".CursoAcademicoCentroUsuario"
    );

    ConfiguracionCentroPermiso(User user) {
        this.currentUser = user;
    }

    public void clearCache() {
        cachePermisos.clear();
    }

    @SuppressWarnings("unchecked")
    public Optional<Set<Permission>> getPermissionForEntity(String object) {
        cachePermisos.putIfAbsent(object, new HashSet<>());

        if (cachePermisos.containsKey(object) && !cachePermisos.get(object).isEmpty()) {
            return Optional.of(cachePermisos.get(object));
        }

        Set<Permission> permissions = new HashSet<>();

        if (joinTables.containsKey(object)) {
            System.out.println("Calculando permisos para: " + object);

            StringJoiner params = new StringJoiner(", ");
            String filter = getFilterForBaseEntity(params, object);
            permissions.add(buildPermission(filter, params));

            params = new StringJoiner(", ");
            String adminFilter = getFilterForBaseEntityAdminCentro(params, object);
            permissions.add(buildPermission(adminFilter, params));

            cachePermisos.put(object, permissions);
            return Optional.of(permissions);
        }

        return Optional.empty();

    }

    private Permission buildPermission(String filter, StringJoiner params) {
        Permission permission = createBasePermission(filter, true, false, false, false, false, false);
        permission.setCondition(filter);
        permission.setConditionParams(params.toString());
        return  permission;
    }


    private Permission createBasePermission(String object, boolean canRead, boolean canWrite, boolean canCreate, boolean canRemove, boolean canImport, boolean canExport) {
        Permission permission = new Permission();
        permission.setObject(object);
        permission.setCanRead(canRead);
        permission.setCanWrite(canWrite);
        permission.setCanCreate(canCreate);
        permission.setCanRemove(canRemove);
        permission.setCanImport(canImport);
        permission.setCanExport(canExport);
        return permission;
    }

    private String getFilterForBaseEntity(StringJoiner params, String entityName) {
        String filter = "? = FALSE AND self IN " +
                "(SELECT jt." + entityName + " " +
                "FROM " + joinTables.get(entityName) + " jt " +
                "WHERE jt.cursoAcademico = ?)";
        params.add("__user__.adminCentroActivo,__user__.centroActivo.cursoAcademicoActual");
        filter += " AND " + getFilterCheckUserCentro(params);
        return filter;
    }

    private String getFilterCheckUserCentro(StringJoiner params) {
        String filter = "EXISTS (" +
                "SELECT 1 " +
                "FROM " + PACKAGE +  ".CursoAcademicoCentroUsuario cacu " +
                "WHERE cacu.centroUsuario.usuario = ? AND cacu.centroUsuario.centro = ? " +
                "AND cacu.cursoAcademico = ?)";
        params.add("__user__, __user__.centroActivo,__user__.centroActivo.cursoAcademicoActual");
        return filter;
    }

    private String getFilterForBaseEntityAdminCentro(StringJoiner params, String entityName) {
        String entity = entityName.substring(0, 1).toUpperCase() + entityName.substring(1);
        String filter = "? = TRUE AND self IN " +
                "(SELECT e " +
                "FROM  " + entity + " e " +
                "WHERE e.centro = ?)";
        params.add("__user__.adminCentroActivo,__user__.centroActivo");
        return filter;
    }



}
