package pl.artur.shopsystem.product.service.impl;

import kafka.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.dto.ProductTypeNumberOutputDto;
import pl.artur.shopsystem.product.repository.ProductRepository;
import pl.artur.shopsystem.product.service.ProductService;
import product.model.Genre;
import supplier.TimeSupplier;

import java.util.NoSuchElementException;
import java.util.UUID;

import static exception.ProductExceptionMessages.NO_PRODUCT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    ProductServiceImpl(ProductRepository productRepository,
                       TimeSupplier timeSupplier) {
        this.productRepository = productRepository;
        this.productMapper = new ProductMapper(timeSupplier);
    }

    @Override
    public Page<ProductOutputDto> fetchAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::mapToProductOutputDto);
    }

    @Override
    public ProductOutputDto fetchProductById(String id) {
        UUID productId = UUID.fromString(id);
        return productRepository.findById(productId)
                .map(productMapper::mapToProductOutputDto)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));
    }

    @Override
    public ProductTypeNumberOutputDto getNumberOfProductsByGenre(Genre type) {
        return ProductTypeNumberOutputDto.builder()
                .type(type)
                .productsNumber(productRepository.countByType(type))
                .build();
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        productRepository.save(
                productMapper.mapToProduct(productDto));
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        productRepository.save(
                productMapper.mapToProduct(productDto));
    }

    @Override
    public void removeProduct(ProductDto productDto) {
        productRepository.save(
                productMapper.mapToProduct(productDto));
    }
}
