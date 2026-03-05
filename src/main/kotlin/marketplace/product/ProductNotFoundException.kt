package marketplace.product

class ProductNotFoundException(id: Long) : RuntimeException("Товар с id $id не найден")
