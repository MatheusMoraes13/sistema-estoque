package com.moraes_dev.sistema_estoque.controller;

import com.moraes_dev.sistema_estoque.DTO.CreateProdutcDTO;
import com.moraes_dev.sistema_estoque.entity.ProductsEntity;
import com.moraes_dev.sistema_estoque.service.ProductsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/estoque")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class ProductsController {

    ProductsService productsService;

    @GetMapping({"", "/"})
    public ResponseEntity<?> getAllProducts(){
        log.info("Solicitação para listar todos os produtos.");
        return productsService.getAllProducts();
    }

    @PostMapping("/produtos/csv")
    public ResponseEntity<?> createProductsByCsv(@RequestParam("file") MultipartFile file){
        log.info("Solicitação de upload CSV recebida. Arquivo: {}", file.getOriginalFilename());
        return productsService.createProductsByCsv(file);
    }

    @PostMapping("/produtos")
    public ResponseEntity<?> createProduct(@RequestBody CreateProdutcDTO produto){
        log.info("Solicitação para criar novo produto: {}", produto);
        return productsService.createProduct(produto);
    }

    @PutMapping("/produtos/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestBody ProductsEntity product){
        log.info("Solicitação para atualizar produto ID: {}", id);
        return productsService.updateProduct(id, product);
    }

    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id){
        log.info("Solicitação para deletar produto ID: {}", id);
        return productsService.deleteProduct(id);
    }

}
