package com.ecommerce.DTOs.responses;

import java.util.List;

public record CategoryType(Long id, String name, List<ProductType> products) {
}
