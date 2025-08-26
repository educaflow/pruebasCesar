package com.educaflow.apps.configuracioncentro.db.repo;

import com.axelor.db.Query;
import com.educaflow.apps.configuracioncentro.db.Centro;
import com.educaflow.apps.configuracioncentro.db.CursoAcademico;


import java.util.List;

public class CursoAcademicoRepository
        extends  AbstractCursoAcademicoRepository
        implements ConfiguracionCentroRepository<CursoAcademico> {

    @Override
    public List<CursoAcademico> findByCentro(Centro centro) {
        return Query.of(CursoAcademico.class)
                .filter("self.centro = :centro")
                .bind("centro", centro)
                .fetch();
    }

    @Override
    public List<CursoAcademico> findByCursoAcademico(CursoAcademico cursoAcademico) {
        return Query.of(CursoAcademico.class)
                .filter("self = :cursoAcademico")
                .bind("cursoAcademico", cursoAcademico)
                .fetch();
    }
}
