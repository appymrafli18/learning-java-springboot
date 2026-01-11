package com.ecommerce.mappers;

import com.ecommerce.DTOs.responses.CategoryType;
import com.ecommerce.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring") // Agar bisa di-@Autowired di Controller
@Component
public interface CategoryMapper {

    // Mapping dari satu object
    CategoryType toType(Category category);

    // Mapping dari list (MapStruct otomatis buatkan loop-nya)
    List<CategoryType> toTypeList(List<Category> categories);
}
