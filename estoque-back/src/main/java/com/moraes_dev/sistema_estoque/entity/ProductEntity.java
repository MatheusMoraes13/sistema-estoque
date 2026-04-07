package com.moraes_dev.sistema_estoque.entity;

import com.moraes_dev.sistema_estoque.DTO.CreateProductDTO;
import com.moraes_dev.sistema_estoque.DTO.ProductsCsvDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "produtos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name")
    String name;
    @Column(name = "Marca")
    String brand;
    @Column(name = "Categoria")
    String category;
    @Column(name = "Subcategoria")
    String subCategory;
    @Column(name = "Qtd")
    int amount;
    @Column(name = "Preço")
    BigDecimal price;
    @Column(name = "Imagem URL")
    String imageUrl;
    @Column(name = "Código de Barras")
     String barCode;

    public ProductEntity(ProductsCsvDTO produto){
        this.name = produto.name();
        this.brand = produto.brand();
        this.category = produto.category();
        this.subCategory = produto.subcategory();
        this.amount = Integer.parseInt(produto.amount());
        this.price = new BigDecimal(produto.price());
        this.imageUrl = produto.imageUrl();
        this.barCode = produto.barCode();
    }

    public ProductEntity(CreateProductDTO produto){
        this.name = produto.name();
        this.brand = produto.brand();
        this.category = produto.category();
        this.subCategory = produto.subcategory();
        this.amount = produto.amount();
        this.price = produto.price();
        this.imageUrl = produto.imageUrl();
        this.barCode = produto.codeBar();
    }
}
