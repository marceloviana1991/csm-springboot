package br.org.na.csm.service;

import br.org.na.csm.dto.material.MaterialCadastroRequestDto;
import br.org.na.csm.dto.material.MaterialCadastroResponseDto;
import br.org.na.csm.dto.material.MaterialEdicaoDto;
import br.org.na.csm.dto.material.MaterialListagemPorGrupoDto;
import br.org.na.csm.model.Grupo;
import br.org.na.csm.model.Material;
import br.org.na.csm.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private GrupoService grupoService;

    @Autowired
    private ImagemService imagemService;

    @Transactional
    public MaterialCadastroResponseDto salvar(MaterialCadastroRequestDto requestDto, MultipartFile imagem) {
        Grupo grupo = grupoService.buscarPorId(requestDto.grupoId());
        String codigoDaImagem = imagemService.salvarImagem(imagem);
        Material material = new Material();
        material.setNome(requestDto.nome());
        material.setPreco(requestDto.preco());
        material.setCodigoDaImagem(codigoDaImagem);
        material.setGrupo(grupo);
        material.setQuantidadeEmEstoque(requestDto.quantidadeEmEstoque());
        materialRepository.save(material);
        return new MaterialCadastroResponseDto(material);
    }

    public List<MaterialListagemPorGrupoDto> buscarPorGrupoParaCompra(Long id) {
        Grupo grupo = grupoService.buscarPorId(id);
        return materialRepository.findByGrupo(grupo)
                .stream()
                .map(MaterialListagemPorGrupoDto::new)
                .toList();
    }

    public List<MaterialListagemPorGrupoDto> buscarPorGrupoParaVenda(Long id) {
        Grupo grupo = grupoService.buscarPorId(id);
        return materialRepository.findByGrupo(grupo)
                .stream()
                .map(material -> {
                    material.setPreco((float) (material.getPreco()*1.1));
                    return new MaterialListagemPorGrupoDto(material);
                })
                .toList();
    }

    public Resource buscarImagem(Long id) {
        Material material = materialRepository.getReferenceById(id);
        return imagemService.buscarImagem(material.getCodigoDaImagem());
    }

    @Transactional
    public void trocarImagem(Long id, MultipartFile imagem) {
        Material material = materialRepository.getReferenceById(id);
        String codigoDaImagem = imagemService.trocarImagem(imagem, material.getCodigoDaImagem());
        material.setCodigoDaImagem(codigoDaImagem);
    }

    @Transactional
    public void alterar(Long id, MaterialEdicaoDto requestDto) {
        Material material = materialRepository.getReferenceById(id);
        if (requestDto.nome() != null) {
            material.setNome(requestDto.nome());
        }
        if (requestDto.preco() != null) {
            material.setPreco(requestDto.preco());
        }
        if (requestDto.quantidadeEmEstoque() != null) {
            material.setQuantidadeEmEstoque(requestDto.quantidadeEmEstoque());
        }
    }
}
