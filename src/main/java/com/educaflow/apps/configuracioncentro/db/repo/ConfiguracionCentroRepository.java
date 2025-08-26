package com.educaflow.apps.configuracioncentro.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Model;
import com.educaflow.apps.configuracioncentro.db.Centro;
import com.educaflow.apps.configuracioncentro.db.CursoAcademico;

import java.util.List;

public interface ConfiguracionCentroRepository<T extends Model>  {

    List<T> findByCentro(Centro centro);
    List<T> findByCursoAcademico(CursoAcademico cursoAcademico);

}
