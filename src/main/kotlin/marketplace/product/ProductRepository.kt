package marketplace.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProductRepository : JpaRepository<ProductEntity, Long> {

    @Query(
        """
        SELECT p FROM ProductEntity p
        WHERE (:status IS NULL OR p.status = :status)
        AND (:category IS NULL OR p.category = :category)
        """
    )
    fun findAllFiltered(
        @Param("status") status: String?,
        @Param("category") category: String?,
        pageable: Pageable
    ): Page<ProductEntity>
}
