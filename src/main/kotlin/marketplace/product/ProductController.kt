package marketplace.product

import marketplace.api.ProductsApi
import marketplace.model.ProductCreate
import marketplace.model.ProductListResponse
import marketplace.model.ProductResponse
import marketplace.model.ProductStatus
import marketplace.model.ProductUpdate
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.OffsetDateTime

@RestController
class ProductController(
    private val productRepository: ProductRepository
) : ProductsApi {

    @Transactional
    override fun createProduct(productCreate: ProductCreate): ResponseEntity<ProductResponse> {
        val entity = ProductEntity()
        applyToEntity(entity, productCreate.name, productCreate.description, productCreate.price, productCreate.stock, productCreate.category, productCreate.status)
        val saved = productRepository.save(entity)
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved))
    }

    @Transactional(readOnly = true)
    override fun getProductById(id: Long): ResponseEntity<ProductResponse> {
        val entity = productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }
        return ResponseEntity.ok(toResponse(entity))
    }

    @Transactional(readOnly = true)
    override fun listProducts(
        page: Int,
        size: Int,
        status: ProductStatus?,
        category: String?
    ): ResponseEntity<ProductListResponse> {
        val result = productRepository.findAllFiltered(
            status?.value,
            category,
            PageRequest.of(page, size)
        )
        val response = ProductListResponse(
            content = result.content.map { toResponse(it) },
            totalElements = result.totalElements,
            page = page,
            propertySize = size
        )
        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun updateProduct(id: Long, productUpdate: ProductUpdate): ResponseEntity<ProductResponse> {
        val entity = productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }
        applyToEntity(entity, productUpdate.name, productUpdate.description, productUpdate.price, productUpdate.stock, productUpdate.category, productUpdate.status)
        entity.updatedAt = OffsetDateTime.now()
        return ResponseEntity.ok(toResponse(productRepository.save(entity)))
    }

    @Transactional
    override fun deleteProduct(id: Long): ResponseEntity<Unit> {
        val entity = productRepository.findById(id).orElseThrow { ProductNotFoundException(id) }
        entity.status = ProductStatus.ARCHIVED.value
        productRepository.save(entity)
        return ResponseEntity.noContent().build()
    }

    private fun applyToEntity(
        entity: ProductEntity,
        name: String,
        description: String?,
        price: Float,
        stock: Int,
        category: String,
        status: ProductStatus
    ) {
        entity.name = name
        entity.description = description
        entity.price = BigDecimal.valueOf(price.toDouble()).setScale(2, RoundingMode.HALF_UP)
        entity.stock = stock
        entity.category = category
        entity.status = status.value
    }

    private fun toResponse(entity: ProductEntity): ProductResponse {
        val createdAt = entity.createdAt ?: throw IllegalStateException("createdAt is null")
        val updatedAt = entity.updatedAt ?: throw IllegalStateException("updatedAt is null")
        val status = ProductStatus.entries.first { it.value == entity.status }
        return ProductResponse(
            id = entity.id ?: 0L,
            name = entity.name,
            price = entity.price.toFloat(),
            stock = entity.stock,
            category = entity.category,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt,
            description = entity.description
        )
    }
}
