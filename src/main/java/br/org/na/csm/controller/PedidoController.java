package br.org.na.csm.controller;

import br.org.na.csm.dto.pedido.PedidoListagemDto;
import br.org.na.csm.dto.pedido.PedidoVendaDto;
import br.org.na.csm.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> efetuarPedidoDeVenda(
            @RequestBody PedidoVendaDto requestDto
    ) {
        pedidoService.cadastrarPedidoDeVenda(requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PedidoListagemDto>> listarPedidoPorMes(
            @RequestParam("mes") Integer mes,
            @RequestParam("ano") Integer ano
    ) {
        return ResponseEntity.ok(pedidoService.buscarPorMes(mes, ano));
    }

    @PatchMapping("/confirmar/{id}")
    @Transactional
    public ResponseEntity<Void> confirmarPagamento (
            @PathVariable Long id
    ) {
        pedidoService.mudarStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancelar/{id}")
    @Transactional
    public ResponseEntity<Void> cancelarPedido (
            @PathVariable Long id
    ) {
        pedidoService.excluirPedido(id);
        return ResponseEntity.ok().build();
    }
}
