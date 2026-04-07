package com.moraes_dev.sistema_estoque.DTO;


public record ProductsCsvDTO (String name,
                              String brand,
                              String category,
                              String subcategory,
                              String amount,
                              String price,
                              String imageUrl,
                              String barCode){
}
