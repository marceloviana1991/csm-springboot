package br.org.na.csm.repository;

import br.org.na.csm.model.Grupo;
import br.org.na.csm.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByGrupo(Grupo grupo);
}
