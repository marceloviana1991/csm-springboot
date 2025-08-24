package br.org.na.csm.controller;

import br.org.na.csm.dto.material.MaterialCadastroRequestDto;
import br.org.na.csm.dto.material.MaterialCadastroResponseDto;
import br.org.na.csm.dto.material.MaterialEdicaoDto;
import br.org.na.csm.dto.material.MaterialListagemPorGrupoDto;
import br.org.na.csm.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/materiais")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping
    @Transactional
    public ResponseEntity<MaterialCadastroResponseDto> cadastrarMaterial(
            @RequestPart("requestDto") MaterialCadastroRequestDto requestDto,
            @RequestParam("imagem") MultipartFile imagem,
            UriComponentsBuilder uriBuilder
    ) {
        MaterialCadastroResponseDto responseDto = materialService.salvar(requestDto, imagem);
        URI uri = uriBuilder.path("/materiais/{id}").buildAndExpand(responseDto.id()).toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> editarMaterial(
            @PathVariable Long id,
            @RequestBody MaterialEdicaoDto requestDto
            ) {
        materialService.alterar(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/grupos/compra/{id}")
    public ResponseEntity<List<MaterialListagemPorGrupoDto>> listarMateriaisPorGrupoParaCompra(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(materialService.buscarPorGrupoParaCompra(id));
    }

    @GetMapping("/grupos/venda/{id}")
    public ResponseEntity<List<MaterialListagemPorGrupoDto>> listarMateriaisPorGrupoParaVenda(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(materialService.buscarPorGrupoParaVenda(id));
    }

    @GetMapping("/imagem/{id}")
    public ResponseEntity<Resource> carregarImagemDoMaterial(
            @PathVariable Long id
    ) throws IOException {
        Resource imagem = materialService.buscarImagem(id);
        String contentType = Files.probeContentType(imagem.getFile().toPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(imagem);
    }

    @PutMapping("/imagem/{id}")
    @Transactional
    public ResponseEntity<Void> alterarImagem(
            @PathVariable Long id,
            @RequestParam("imagem") MultipartFile imagem
    ) {
        materialService.trocarImagem(id, imagem);
        return ResponseEntity.ok().build();
    }
}
