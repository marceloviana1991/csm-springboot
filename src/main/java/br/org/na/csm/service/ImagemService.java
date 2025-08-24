package br.org.na.csm.service;

import br.org.na.csm.model.Material;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImagemService {

    private final Path diretorio;

    public ImagemService(@Value("${imagem.upload-dir}") String uploadDir) {
        this.diretorio = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.diretorio);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório para upload de arquivos.", ex);
        }
    }

    public String salvarImagem(MultipartFile imagem) {
        try {
            UUID uuid = UUID.randomUUID();
            String extensaoDaImagem = getFileExtension(imagem.getOriginalFilename());
            String codigoDaImagem = uuid + "." + extensaoDaImagem;
            Path destinoDaImagem = this.diretorio.resolve(codigoDaImagem);
            Files.copy(imagem.getInputStream(), destinoDaImagem, StandardCopyOption.REPLACE_EXISTING);
            return codigoDaImagem;
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível armazenar o arquivo.", e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public Resource buscarImagem(String codigoDaImagem) {
        try {
            Path filePath = this.diretorio.resolve(codigoDaImagem).normalize();
            Resource imagem = new UrlResource(filePath.toUri());
            if (imagem.exists()) {
                return imagem;
            } else {
                throw new RuntimeException("Arquivo não encontrado: " + codigoDaImagem);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Arquivo não encontrado: " + codigoDaImagem, ex);
        }
    }

    public String trocarImagem(MultipartFile imagem, String codigoDaImagem) {
        try {
            Path filePath = this.diretorio.resolve(codigoDaImagem).normalize();
            Files.delete(filePath);
            return salvarImagem(imagem);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao tentar trocar a imagem. Não foi possível deletar o arquivo antigo.", e);
        }
    }
}
