package com.educaflow.apps.seguridad;

import com.axelor.auth.db.Permission;
import com.axelor.auth.db.User;
import com.educaflow.apps.expedientes.db.AmbitoTipoExpediente;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class ExpedientePermiso {

    private final User usuario;
    private final String OBJECT = "com.educaflow.apps.expedientes.db.Expediente";

    public ExpedientePermiso(User usuario) {
        this.usuario = usuario;
    }

    public User getUsuario() {
        return usuario;
    }

    public Set<Permission> getPermisos() {
        Set<Permission> permisos = new HashSet<>();
        String paramUsuario = "__user__";
        String paramCentro = "__user__.centroActivo";
        StringBuilder condition = new StringBuilder("(");
        StringJoiner joiner = new StringJoiner(" OR ");

        for (AmbitoTipoExpediente ambito : AmbitoTipoExpediente.values()) {
            joiner.add(buildAmbitoCondition(ambito, "ambitoCreador"));
        }

        for (AmbitoTipoExpediente ambito : AmbitoTipoExpediente.values()) {
            joiner.add(buildAmbitoCondition(ambito, "ambitoResponsable"));
        }

        condition.append(joiner.toString()).append(")");

        Permission permisoExpediente = new Permission();
        permisoExpediente.setObject(OBJECT);
        permisoExpediente.setCanRead(true);
        permisoExpediente.setCanWrite(true);
        permisoExpediente.setCanCreate(true);
        permisoExpediente.setCanRemove(true);
        permisoExpediente.setCanImport(true);
        permisoExpediente.setCanExport(true);
        permisoExpediente.setCondition(condition.toString());

        // Repetimos los parámetros 2 veces por cada ámbito
        StringJoiner params = new StringJoiner(", ");

        for (AmbitoTipoExpediente ambito : AmbitoTipoExpediente.values()) {
            for (int j = 0; j < 2; j++) { // 1 vez para ambitoCreador, 1 vez para ambitoResponsable
                params.add(paramUsuario);
                params.add(paramCentro);

                // Solo MODULO necesita un tercer parámetro (usuario) para cg.profesores MEMBER OF
                if (ambito == AmbitoTipoExpediente.MODULO) {
                    params.add(paramUsuario);
                }
            }
        }

        permisoExpediente.setConditionParams(params.toString());

        Permission permisoAdmin = new Permission();
        permisoAdmin.setObject(OBJECT);
        permisoAdmin.setCanRead(true);
        permisoAdmin.setCanWrite(true);
        permisoAdmin.setCanCreate(false);
        permisoAdmin.setCanRemove(false);
        permisoAdmin.setCanImport(false);
        permisoAdmin.setCanExport(false);
        permisoAdmin.setCondition(
                "EXISTS (" +
                        " SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroUsuario cu " +
                        " JOIN cu.cargos cargo " +
                        " WHERE cu.usuario = ? AND cu.centro = ? AND cargo.code = 'admin'" +
                        ")"
        );
        permisoAdmin.setConditionParams("__user__, __user__.centroActivo");

        permisos.add(permisoExpediente);
        permisos.add(permisoAdmin);

        return permisos;
    }

    private String buildAmbitoCondition(AmbitoTipoExpediente ambito, String tipoAmbito) {
        String base = "SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroUsuario cu WHERE cu.usuario = ? AND cu.centro = ?";
        String valoresAmbito = tipoAmbito.equals("ambitoCreador")
                ? "valoresAmbitoCreador"
                : "valoresAmbitoResponsable";

        StringBuilder sb = new StringBuilder();
        sb.append("(")
                .append("self.tipoExpediente.").append(tipoAmbito).append(" = '").append(ambito.name()).append("'")
                .append(" AND EXISTS (").append(base);

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
                sb.append(" WHERE cd.centro = cu.centro AND cd.departamento = self.")
                        .append(valoresAmbito).append(".departamento)");
                break;
            case CICLO:
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
                break;
            case GRUPO:
                sb.append(" AND self.").append(valoresAmbito).append(".centro = cu.centro");
                sb.append(" AND EXISTS (SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroCiclo cc");
                sb.append(" WHERE cc.centro = cu.centro AND self.").append(valoresAmbito).append(".grupo MEMBER OF cc.grupos)");
                break;
            case MODULO:
                sb.append(" AND self.").append(valoresAmbito).append(".centro = cu.centro");
                sb.append(" AND EXISTS (SELECT 1 FROM com.educaflow.apps.sistemaeducativo.db.CentroGrupo cg");
                sb.append(" WHERE cg.centro = cu.centro");
                sb.append(" AND cg.grupo = self.").append(valoresAmbito).append(".grupo");
                sb.append(" AND cg.modulo = self.").append(valoresAmbito).append(".modulo");
                sb.append(" AND ? MEMBER OF cg.profesores)");
                break;
            default:
                sb.append(" AND 1 = 0");
                break;
        }

        sb.append("))");
        return sb.toString();
    }


}
