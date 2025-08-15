package com.educaflow.apps.seguridad;

import com.axelor.auth.db.Permission;
import com.axelor.auth.db.User;
import com.axelor.db.JpaRepository;
import com.educaflow.apps.expedientes.db.AmbitoTipoExpediente;
import com.educaflow.apps.seguridad.db.Permiso;
import com.educaflow.apps.sistemaeducativo.db.Cargo;
import com.educaflow.common.util.AxelorDBUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class ExpedientePermiso {

    private final User usuario;
    private final String OBJECT = "com.educaflow.apps.expedientes.db.Expediente";
    private final String PARAM_USUARIO = "__user__";             // o "__user__.code" si comparas código
    private final String PARAM_CENTRO = "__user__.centroActivo";
    private final String CARGO_ADMIN = "ADMIN";

    public ExpedientePermiso(User usuario) {
        this.usuario = usuario;
    }

    public User getUsuario() {
        return usuario;
    }

    public Set<Permission> getPermisos() {
        Set<Permission> permisos = new HashSet<>();

        // --- Construir condición del permiso sobre Expediente ---
        StringBuilder condition = new StringBuilder("(");
        StringJoiner joiner = new StringJoiner(" OR ");

        // params específicos para permisoExpediente (mantener orden exacto de los ?)
        StringJoiner expedienteParams = new StringJoiner(", ");

        for (AmbitoTipoExpediente ambito : AmbitoTipoExpediente.values()) {
            joiner.add(buildAmbitoCondition(ambito, "ambitoCreador", expedienteParams));
        }

        for (AmbitoTipoExpediente ambito : AmbitoTipoExpediente.values()) {
            joiner.add(buildAmbitoCondition(ambito, "ambitoResponsable", expedienteParams));
        }

        condition.append(joiner.toString()).append(")");

        Permission permisoExpediente = new Permission();
        permisoExpediente.setObject(OBJECT);
        permisoExpediente.setCanRead(true);
        permisoExpediente.setCanWrite(false);
        permisoExpediente.setCanCreate(false);
        permisoExpediente.setCanRemove(false);
        permisoExpediente.setCanImport(false);
        permisoExpediente.setCanExport(false);
        permisoExpediente.setCondition(condition.toString());
        permisoExpediente.setConditionParams(expedienteParams.toString());

        // --- Permiso admin (su propia lista de params, en orden) ---
        Permission permisoAdmin = new Permission();
        permisoAdmin.setObject(OBJECT);
        permisoAdmin.setCanRead(true);
        permisoAdmin.setCanWrite(false);
        permisoAdmin.setCanCreate(false);
        permisoAdmin.setCanRemove(false);
        permisoAdmin.setCanImport(false);
        permisoAdmin.setCanExport(false);

        String adminCondition =
                "EXISTS (SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroUsuario cu " +
                        " WHERE cu.usuario = ? AND cu.centro = ? " +
                        " AND EXISTS(SELECT 1 FROM cu.cargos c WHERE c.code= '"+ CARGO_ADMIN + "'))";
        permisoAdmin.setCondition(adminCondition);

        StringJoiner adminParams = new StringJoiner(", ");
        adminParams.add(PARAM_USUARIO);
        adminParams.add(PARAM_CENTRO);
        permisoAdmin.setConditionParams(adminParams.toString());

        permisos.add(permisoExpediente);
        permisos.add(permisoAdmin);

        return permisos;
    }

    /**
     * Construye la sub-condición para un ámbito y añade al StringJoiner "params" los parámetros
     * en el mismo orden en que aparecen los signos de interrogación '?' en la condición devuelta.
     */
    private String buildAmbitoCondition(AmbitoTipoExpediente ambito, String tipoAmbito, StringJoiner params) {
        String base = "SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroUsuario cu WHERE cu.usuario = ? AND cu.centro = ?";
        String valoresAmbito = tipoAmbito.equals("ambitoCreador")
                ? "valoresAmbitoCreador"
                : "valoresAmbitoResponsable";

        StringBuilder sb = new StringBuilder();
        sb.append("(")
                .append("self.tipoExpediente.").append(tipoAmbito).append(" = '").append(ambito.name()).append("'")
                .append(" AND EXISTS (").append(base);

        // cada base introduce: cu.usuario = ? , cu.centro = ?
        params.add(PARAM_USUARIO);
        params.add(PARAM_CENTRO);

        switch (ambito) {
            case INDIVIDUAL:
                sb.append(" AND self.").append(valoresAmbito).append(".usuario = cu.usuario");
                break;
            case CENTRO:
                sb.append(" AND self.").append(valoresAmbito).append(".centro = cu.centro");
                break;
            case DEPARTAMENTO:
                sb.append(" AND self.").append(valoresAmbito).append(".centro = cu.centro");
                sb.append(" AND EXISTS (SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroDepartamento cd");
                sb.append(" WHERE cd.centro = cu.centro");
                sb.append(" AND cd.departamento = self.").append(valoresAmbito).append(".departamento");
                sb.append(" AND self.").append(valoresAmbito).append(".departamento MEMBER OF cu.departamentos)");
                break;

/********** MAL **********************************/
            /*case CICLO:
                sb.append(" AND self.").append(valoresAmbito).append(".centro = cu.centro");
                sb.append(" AND EXISTS (SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroCiclo cc");
                sb.append(" WHERE cc.centro = cu.centro AND cc.ciclo = self.")
                        .append(valoresAmbito).append(".ciclo)");
                break;

            case CURSO:
                sb.append(" AND self.").append(valoresAmbito).append(".centro = cu.centro");
                sb.append(" AND EXISTS (SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroCiclo cc");
                sb.append(" WHERE cc.centro = cu.centro AND cc.curso = self.")
                        .append(valoresAmbito).append(".curso)");
                break;*/
/********************************************/


            case GRUPO:
                sb.append(" AND EXISTS (SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.GrupoModulo gm");
                sb.append(" WHERE gm.grupo = self.").append(valoresAmbito).append(".grupo");
                sb.append(" AND ? MEMBER OF gm.profesores)");
                params.add(PARAM_USUARIO);
                break;

            /*case MODULO:
                sb.append(" AND self.").append(valoresAmbito).append(".centro = cu.centro");
                sb.append(" AND EXISTS (SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroGrupo cg");
                sb.append(" WHERE cg.centro = cu.centro");
                sb.append(" AND cg.grupo = self.").append(valoresAmbito).append(".grupo");
                sb.append(" AND cg.modulo = self.").append(valoresAmbito).append(".modulo");
                sb.append(" AND ? MEMBER OF cg.profesores)");
                params.add(PARAM_USUARIO);
                break;*/
            default:
                sb.append(" AND 1 = 0");
                break;
        }

        sb.append("))");
        return sb.toString();
    }


}
