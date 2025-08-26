package com.educaflow.apps.configuracioncentro.db.repo;

import com.axelor.db.Query;
import com.educaflow.apps.configuracioncentro.db.Centro;
import com.educaflow.apps.configuracioncentro.db.CursoAcademico;
import com.educaflow.apps.configuracioncentro.db.Grupo;

import java.util.List;

public class GrupoRepository
        extends AbstractGrupoRepository
        implements ConfiguracionCentroRepository<Grupo> {

    @Override
    public List<Grupo> findByCentro(Centro centro) {
        return Query.of(Grupo.class)
                .filter("self.centro = :centro")
                .bind("centro", centro)
                .fetch();
    }

    @Override
    public List<Grupo> findByCursoAcademico(CursoAcademico cursoAcademico) {
        return Query.of(Grupo.class)
                .filter("self IN (" +
                    "SELECT cag.grupo " +
                    "FROM com.educaflow.apps.configuracioncentro.db.CursoAcademicoGrupo cag " +
                    "WHERE cag.cursoAcademico = :cursoAcademico" +
                    ")"
                )
                .bind("cursoAcademico", cursoAcademico)
                .fetch();
    }
}
