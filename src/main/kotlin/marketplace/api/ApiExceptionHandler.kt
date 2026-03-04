package marketplace.api

import marketplace.model.Error
import marketplace.model.ErrorCode
import marketplace.product.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(ex: ProductNotFoundException): ResponseEntity<Error> {
        val error = Error(
            errorCode = ErrorCode.PRODUCT_NOT_FOUND,
            message = ex.message ?: "Товар не найден по ID",
            details = null
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }
}
