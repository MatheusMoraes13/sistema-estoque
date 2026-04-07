package com.moraes_dev.sistema_estoque.service;

import com.moraes_dev.sistema_estoque.DTO.CreateProductDTO;
import com.moraes_dev.sistema_estoque.DTO.ProductsCsvDTO;
import com.moraes_dev.sistema_estoque.entity.ProductEntity;
import com.moraes_dev.sistema_estoque.repository.ProductsRepository;
import com.opencsv.exceptions.CsvException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Service
@AllArgsConstructor
@Slf4j
public class ProductsService {

    private final ProductsRepository productsRepository;

    public Page<ProductEntity> getAllProducts(Pageable pageable) {
        log.info("Retornando todos os produtos cadastrados na base de dados.");
        return productsRepository.findAll(pageable);
    }

    @Transactional
    public void createProductsByCsv(MultipartFile productsCsv) throws IOException, CsvException {
        log.info("Realizando o cadastro dos produtos enviados via CSV.");
        Reader reader = new InputStreamReader(productsCsv.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1)
                .build();

        List<String[]> rows = csvReader.readAll();

        for (String[] row : rows) {
            if (productsRepository.findByBarCode(row[7]) != null) {
                log.info("Produto já cadastrado na base de dados com o código de barras {}", row[7]);
                continue;
            }

            ProductsCsvDTO currentProduct = new ProductsCsvDTO(row[0],
                    row[1], row[2], row[3],
                    row[4], row[5], row[6],
                    row[7]);
            ProductEntity currentProductEntity = new ProductEntity(currentProduct);
            productsRepository.save(currentProductEntity);
            log.info("Produto {} cadastrado com sucesso na base de dados.", currentProductEntity.getName());
        }

        log.info("Produtos recebidos via CSV e cadastrados com sucesso.");
    }

    @Transactional
    public ResponseEntity<?> createProduct(CreateProductDTO product){
        log.info("Realizando o cadastro do product {}.", product.name());

        try {
            if (productsRepository.findByBarCode(product.codeBar()) != null){
                log.info("Produto já cadastrado na base de dados com o código de barras {}", product.codeBar());
                return ResponseEntity.badRequest().body("Produto já cadastrado para o código de barras " + product.codeBar());
            } else {
                ProductEntity productToSave = new ProductEntity(product);
                productsRepository.save(productToSave);

                log.info("Produto {} cadastrado com sucesso no banco de dados.", product.name());
                return ResponseEntity.ok(product);
            }
        } catch (Exception e){
            log.error("Erro ao realizar o cadastro do product {} na base de dados.", product.name(),e);
            return ResponseEntity.internalServerError().body("Erro interno ao realizar o cadastro do product" + product.name() + "na base de dados.");
        }
    }

    @Transactional
    public ProductEntity updateProduct(long id, ProductEntity product){
        log.info("Atualizando os dados do produto: {}.", product.getName());
        ProductEntity foundProduct = productsRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Nenhum produto encontrado para o ID: ", id);
                });
        if (productsRepository.findById(product.getId()).isPresent()) {
            productsRepository.save(product);

            log.info("Produto {} atualizado com sucesso na base de dados.", product.getName());
            return product;
        } else {
            log.error("Nenhum produto com o id \"{}\" encontrado na base de dados.", product.getId());
            return ResponseEntity.badRequest().body("Nenhum produto com o id \"" + product.getId() + "\" encontrado na base de dados");
        }
    }

    @Transactional
    public ResponseEntity<?> deleteProduct(long id){
        log.info("Apagando o produto {}", id);
        try {
            if (productsRepository.findById(id).isPresent()) {
                productsRepository.deleteById(id);
                log.info("Produto {} apagado com sucesso da base de dados.", id);
                return ResponseEntity.ok("Produto " + id + " apagado com sucesso da base de dados.");
            } else {
                log.info("Nenhum produto cadastrado com o ID informado.");
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            log.error("Erro interno ao tentar apagar o produto {}", id, e);
            return ResponseEntity.internalServerError().body("Erro interno ao tentar apagar o produto " + id);
        }
    }
}
