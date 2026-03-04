package marketplace.api

import marketplace.model.Error
import marketplace.model.ErrorCode
import marketplace.product.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import jakarta.validation.ConstraintViolationException

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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<Error> {
        val violations = ex.bindingResult.fieldErrors.map { fieldError ->
            mapOf(
                "field" to fieldError.field,
                "message" to (fieldError.defaultMessage ?: "Ошибка валидации")
            )
        }
        val error = Error(
            errorCode = ErrorCode.VALIDATION_ERROR,
            message = "Ошибка валидации входных данных",
            details = mapOf("violations" to violations)
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Error> {
        val violations = ex.constraintViolations.map { v ->
            val field = v.propertyPath?.toString()?.split(".")?.lastOrNull() ?: "Ошибка валидации"
            mapOf(
                "field" to field,
                "message" to (v.message ?: "Ошибка валидации")
            )
        }
        val error = Error(
            errorCode = ErrorCode.VALIDATION_ERROR,
            message = "Ошибка валидации входных данных",
            details = mapOf("violations" to violations)
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<Error> {
        val causeMessage = ex.mostSpecificCause?.message ?: ex.message ?: "Ошибка валидации"
        val error = Error(
            errorCode = ErrorCode.VALIDATION_ERROR,
            message = "Ошибка валидации входных данных",
            details = mapOf("violations" to listOf(mapOf("field" to "body", "message" to causeMessage)))
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }
}
