package com.moraes_dev.sistema_estoque.DTO;

import java.math.BigDecimal;

public record CreateProductDTO(String name,
                               String brand,
                               String category,
                               String subcategory,
                               int amount,
                               BigDecimal price,
                               String imageUrl,
                               String codeBar) {
}
