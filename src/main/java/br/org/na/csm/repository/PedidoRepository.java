package br.org.na.csm.repository;

import br.org.na.csm.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE YEAR(p.data) = :ano AND MONTH(p.data) = :mes")
    List<Pedido> findByAnoEMes(@Param("mes") int mes, @Param("ano") int ano);
}
