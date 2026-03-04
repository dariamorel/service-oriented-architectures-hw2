package marketplace.api

import marketplace.model.Error
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.NoSuchElementException

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<Error> {
        val error = Error(code = "NOT_FOUND", message = ex.message ?: "Not found")
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }
}
