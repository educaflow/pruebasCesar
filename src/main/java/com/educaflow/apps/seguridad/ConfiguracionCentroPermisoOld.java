package com.educaflow.apps.seguridad;

import com.axelor.auth.db.Permission;
import com.axelor.auth.db.User;
import com.educaflow.apps.configuracioncentro.db.Departamento;
import com.educaflow.apps.configuracioncentro.db.repo.DepartamentoRepository;
import com.educaflow.common.util.AxelorDBUtil;

import java.util.*;

public class ConfiguracionCentroPermisoOld {

    private final User currentUser;
    private final String PARAM_USUARIO = "__user__";             // o "__user__.code" si comparas código
    private final String PARAM_CENTRO = "__user__.centroActivo";
    private final String PARAM_CURSO_ACADEMICO = "__user__.centroActivo.cursoAcademicoActual";
    private final String PACKAGE = "com.educaflow.apps.configuracioncentro.db";
    private Map<String, String> joinTables = Map.of(
            "departamento", PACKAGE + ".CursoAcademicoDepartamento",
            "cargo", PACKAGE + ".CursoAcademicoCargo",
            "grupo", PACKAGE + ".CursoAcademicoGrupo",
            "centroUsuario", PACKAGE + ".CursoAcademicoCentroUsuario"
    );
    private static final Map<String, Map<String, Set<Permission>>> cachePermisos = new HashMap<>();

    ConfiguracionCentroPermisoOld(User user) {
        this.currentUser = user;
    }

    public void clearCache() {
        cachePermisos.clear();
    }

    public Set<Permission> getPermissionForEntity(String entityName) {
        String userCode = currentUser.getCode();
        cachePermisos.putIfAbsent(userCode, new HashMap<>());
        Map<String, Set<Permission>> userCache = cachePermisos.get(userCode);

        Set<Permission> permissions = new HashSet<Permission>();
        StringJoiner params = new StringJoiner(", ");


        // Si ya hemos calculado los permisos para esta entidad, los devolvemos
        if (userCache.containsKey(entityName)) {
            return userCache.get(entityName);
        }


        // Los permisos de los centros y usuarios no dependen del administrador del centro

        // Permisos para centros
        if (entityName.equals("centro")) {
            Permission centroPermission = createBasePermission(entityName, true, false, false, false, false, false);
            String centroFilter = getFilterForCentros(params);
            centroPermission.setCondition(centroFilter);
            centroPermission.setConditionParams(params.toString());
            permissions.add(centroPermission);
            userCache.put(entityName, permissions);
            return permissions;
        }

        // Permisos para usuarios
        if (entityName.equals("user")) {
            Permission selfPermission = createBasePermission(entityName, true, true, false, false, false, false);
            String selfFilter = "self = ?";
            selfPermission.setCondition(selfFilter);
            selfPermission.setConditionParams(PARAM_USUARIO);
            permissions.add(selfPermission);

            Permission usuarioPermission = createBasePermission(entityName,true, false, false, false, false, false);
            String usuarioFilter = getFilterForUsuarios(params);
            usuarioPermission.setCondition(usuarioFilter);
            usuarioPermission.setConditionParams(params.toString());
            permissions.add(usuarioPermission);
            userCache.put(entityName, permissions);
            return permissions;
        }

        // Permisos para administradores de centro
        Permission adminPermission = createBasePermission(entityName, true, false, false, false, false, false);
        String adminFilter = getFilterForAdminCentro(params);
        adminPermission.setCondition(adminFilter);
        adminPermission.setConditionParams(params.toString());
        permissions.add(adminPermission);

        // Resetear params para el siguiente uso
        params = new StringJoiner(", ");

        // Permisos para centroUsuario
        if (entityName.equals("centroUsuario")) {
            Permission centroUsuarioPermission = createBasePermission(entityName, true, false, false, false, false, false);
            String centroUsuarioFilter = getFilterForCentroUsuario(params);
            centroUsuarioPermission.setCondition(centroUsuarioFilter);
            centroUsuarioPermission.setConditionParams(params.toString());
            permissions.add(centroUsuarioPermission);
            userCache.put(entityName, permissions);
            return permissions;
        }

        // Permisos para cursoAcademico
        if (entityName.equals("cursoAcademico")) {
            Permission cursoAcademicoPermission = createBasePermission(entityName, true, false, false, false, false, false);
            String cursoAcademicoFilter = getFilterForCursoAcademico(params);
            cursoAcademicoPermission.setCondition(cursoAcademicoFilter);
            cursoAcademicoPermission.setConditionParams(params.toString());
            permissions.add(cursoAcademicoPermission);
            userCache.put(entityName, permissions);
            return permissions;
        }

        // Si la entidad no está en el mapa, devolvemos sin condiciones (para tablas join)
        if (!joinTables.containsKey(entityName)) {
            Permission permission = createBasePermission(entityName, true, false, false, false, false, false);
            permissions.add(permission);
            userCache.put(entityName, permissions);
            return permissions;
        }

        // Permisos para usuarios normales (con curso académico y centro activo)
        Permission permission = createBasePermission(entityName, true, false, false, false, false, false);
        String filter = getFilterForCursoAcademicoCentroUsuario(params) + " AND "+ getFilterForEntity(entityName, params);
        permission.setCondition(filter);
        permission.setConditionParams(params.toString());
        permissions.add(permission);
        userCache.put(entityName, permissions);

        return permissions;
    }


    private Permission createBasePermission(String entityName, boolean canRead, boolean canWrite, boolean canCreate, boolean canRemove, boolean canImport, boolean canExport) {
        Permission permission = new Permission();
        String capitalizedEntityName = entityName.substring(0, 1).toUpperCase() + entityName.substring(1);
        permission.setObject(PACKAGE + "." + capitalizedEntityName);
        permission.setCanRead(canRead);
        permission.setCanWrite(canWrite);
        permission.setCanCreate(canCreate);
        permission.setCanRemove(canRemove);
        permission.setCanImport(canImport);
        permission.setCanExport(canExport);
        return permission;
    }

    private String getFilterForCentroUsuario(StringJoiner params) {
        String filter = "self.centro = ?";
        params.add(PARAM_CENTRO);
        return filter;
    }

    private String getFilterForCursoAcademico(StringJoiner params) {
        String filter = "self = ?";
        params.add(PARAM_CURSO_ACADEMICO);
        return filter;
    }

    private String getFilterForCursoAcademicoCentroUsuario(StringJoiner params) {
        String filter = "EXISTS (" +
                "SELECT 1 " +
                "FROM " + PACKAGE +  ".CursoAcademicoCentroUsuario cacu " +
                "WHERE cacu.centroUsuario.usuario = ? AND cacu.centroUsuario.centro = self.centro " +
                "AND cacu.cursoAcademico = ?)";
        params.add(PARAM_USUARIO);
        params.add(PARAM_CURSO_ACADEMICO);
        return filter;
    }

    private String getFilterForEntity(String entityName, StringJoiner params) {
        String filter = "self IN " +
                "(SELECT jt." + entityName + " " +
                "FROM " + joinTables.get(entityName) + " jt " +
                "WHERE jt.cursoAcademico = ?)";
        params.add(PARAM_CURSO_ACADEMICO);
        return filter;
    }

    private String getFilterForAdminCentro(StringJoiner params) {
        String filter = "EXISTS (" +
                "SELECT 1 " +
                "FROM " + PACKAGE + ".CursoAcademicoCentroUsuarioCursoAcademicoCargo cacucc " +
                "WHERE cacucc.cursoAcademicoCentroUsuario.centroUsuario.usuario = ? " +
                "AND cacucc.cursoAcademicoCentroUsuario.centroUsuario.centro = self.centro " +
                "AND cacucc.cursoAcademicoCargo.cargo.adminCentro = true)";
        params.add(PARAM_USUARIO);
        return filter;
    }

    private String getFilterForCentros(StringJoiner params) {
        String filter = "self IN (" +
                "SELECT cu.centro " +
                "FROM " + PACKAGE + ".CentroUsuario cu " +
                "WHERE cu.usuario = ?" +
                ")";
        params.add(PARAM_USUARIO);
        return filter;
    }

    private String getFilterForUsuarios(StringJoiner params){
        String filter = "self IN (" +
                "SELECT cacu.centroUsuario.usuario " +
                "FROM " + PACKAGE +  ".CursoAcademicoCentroUsuario cacu " +
                "WHERE cacu.centroUsuario.centro = ? AND cacu.cursoAcademico = ?)";
        params.add(PARAM_CENTRO);
        params.add(PARAM_CURSO_ACADEMICO);
        return filter;
    }


}
