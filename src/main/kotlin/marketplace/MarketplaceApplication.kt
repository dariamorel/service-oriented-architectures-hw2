package marketplace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MarketplaceApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<MarketplaceApplication>(*args)
        }
    }
}
