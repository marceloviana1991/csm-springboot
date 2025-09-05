package br.org.na.csm.service;

import br.org.na.csm.dto.pedido.PedidoListagemDto;
import br.org.na.csm.dto.pedido.PedidoVendaDto;
import br.org.na.csm.model.ItemPedido;
import br.org.na.csm.model.Material;
import br.org.na.csm.model.Pedido;
import br.org.na.csm.repository.ItemPedidoRepositoy;
import br.org.na.csm.repository.MaterialRepository;
import br.org.na.csm.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private ItemPedidoRepositoy itemPedidoRepositoy;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public void cadastrarPedidoDeVenda(PedidoVendaDto requestDto) {
        Pedido pedido = new Pedido();
        pedido.setCliente(requestDto.cliente());
        pedido.setTelefone(requestDto.telefone());
        pedido.setConfirmado(false);
        pedidoRepository.save(pedido);
        System.out.println(requestDto.itens());
        requestDto.itens().forEach( itemPedidoDto -> {
            ItemPedido itemPedido = new ItemPedido();
            Material material = materialRepository.getReferenceById(itemPedidoDto.materialId());
            material.setQuantidadeEmEstoque(material.getQuantidadeEmEstoque() - itemPedidoDto.quantidade());
            itemPedido.setMaterial(material);
            itemPedido.setQuantidade(itemPedidoDto.quantidade());
            itemPedido.setValorTotal((itemPedidoDto.quantidade()*material.getPreco()*1.1F));
            itemPedido.setPedido(pedido);
            itemPedidoRepositoy.save(itemPedido);
        });
    }

    public List<PedidoListagemDto> buscarPorMes(Integer mes, Integer ano) {
        List<PedidoListagemDto> pedidosListagemDto = new ArrayList<>();
        List<Pedido> pedidos = pedidoRepository.findByAnoEMes(mes, ano);
        pedidos.forEach( pedido -> {
            List<ItemPedido> itensPedido = itemPedidoRepositoy.findByPedido(pedido);
            PedidoListagemDto pedidoListagemDto = new PedidoListagemDto(pedido, itensPedido);
            pedidosListagemDto.add(pedidoListagemDto);
        });
        return pedidosListagemDto;
    }

    @Transactional
    public void mudarStatus(Long id) {
        Pedido pedido = pedidoRepository.getReferenceById(id);
        pedido.setConfirmado(true);
    }

    @Transactional
    public void excluirPedido(Long id) {
        Pedido pedido = pedidoRepository.getReferenceById(id);
        List<ItemPedido> itens = itemPedidoRepositoy.findByPedido(pedido);
        itens.forEach( item -> {
            Material material = item.getMaterial();
            material.setQuantidadeEmEstoque(material.getQuantidadeEmEstoque() + item.getQuantidade());
            itemPedidoRepositoy.delete(item);
        });
        pedidoRepository.delete(pedido);
    }
}
