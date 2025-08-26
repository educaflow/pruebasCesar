package com.educaflow.apps.configuracioncentro.db.repo;

import com.axelor.db.Query;
import com.educaflow.apps.configuracioncentro.db.Centro;
import com.educaflow.apps.configuracioncentro.db.CursoAcademico;
import com.educaflow.apps.configuracioncentro.db.Cargo;

import java.util.List;

public class CargoRepository
        extends AbstractCargoRepository
        implements ConfiguracionCentroRepository<Cargo> {


    @Override
    public List<Cargo> findByCentro(Centro centro) {
        return Query.of(Cargo.class)
                .filter("self.centro = :centro")
                .bind("centro", centro)
                .fetch();
    }

    @Override
    public List<Cargo> findByCursoAcademico(CursoAcademico cursoAcademico) {
        return Query.of(Cargo.class)
                .filter("self IN (" +
                        "SELECT cac.cargo " +
                        "FROM com.educaflow.apps.configuracioncentro.db.CursoAcademicoCargo cac " +
                        "WHERE cac.cursoAcademico = :cursoAcademico" +
                        ")"
                )
                .bind("cursoAcademico", cursoAcademico)
                .fetch();
    }
}
