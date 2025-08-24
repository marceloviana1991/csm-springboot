package br.org.na.csm.repository;

import br.org.na.csm.model.ItemPedido;
import br.org.na.csm.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepositoy extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedido(Pedido pedido);
}
