package com.educaflow.apps.configuracioncentro.db.repo;

import com.axelor.db.Query;
import com.educaflow.apps.configuracioncentro.db.Centro;
import com.educaflow.apps.configuracioncentro.db.CursoAcademico;
import com.educaflow.apps.configuracioncentro.db.Departamento;

import java.util.List;

public class DepartamentoRepository
        extends AbstractDepartamentoRepository
        implements ConfiguracionCentroRepository<Departamento> {

    @Override
    public List<Departamento> findByCentro(Centro centro) {
        return Query.of(Departamento.class)
                .filter("self.centro = :centro")
                .bind("centro", centro)
                .fetch();
    }

    @Override
    public List<Departamento> findByCursoAcademico(CursoAcademico cursoAcademico) {
        return Query.of(Departamento.class)
                .filter("self IN (" +
                        "SELECT cad.departamento " +
                        "FROM com.educaflow.apps.configuracioncentro.db.CursoAcademicoDepartamento cad " +
                        "WHERE cad.cursoAcademico = :cursoAcademico" +
                        ")"
                )
                .bind("cursoAcademico", cursoAcademico)
                .fetch();
    }
}
