package com.educaflow.apps.configuracioncentro.db.repo;

import com.axelor.db.Query;
import com.educaflow.apps.configuracioncentro.db.Centro;
import com.educaflow.apps.configuracioncentro.db.CentroUsuario;
import com.educaflow.apps.configuracioncentro.db.CursoAcademico;

import java.util.List;

public class CentroUsuarioRepository
        extends AbstractCentroUsuarioRepository
        implements ConfiguracionCentroRepository<CentroUsuario> {

    @Override
    public List<CentroUsuario> findByCentro(Centro centro) {
        return Query.of(CentroUsuario.class)
                .filter("self.centro = :centro")
                .bind("centro", centro)
                .fetch();
    }

    @Override
    public List<CentroUsuario> findByCursoAcademico(CursoAcademico cursoAcademico) {
        return Query.of(CentroUsuario.class)
                .filter("self IN (" +
                        "SELECT cacu.centroUsuario " +
                        "FROM com.educaflow.apps.configuracioncentro.db.CursoAcademicoCentroUsuario cacu " +
                        "WHERE cacu.cursoAcademico = :cursoAcademico" +
                        ")"
                )
                .bind("cursoAcademico", cursoAcademico)
                .fetch();
    }
}
