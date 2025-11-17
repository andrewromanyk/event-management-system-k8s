package contracts.building

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return building by ID"

    request {
        method GET()
        url("/api/building/1")
        headers {
            contentType(applicationJson())
        }
    }

    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body(
                id: 1,
                address: "123 Main St, Cityville",
                hourlyRate: 50,
                areaM2: 500,
                capacity: 100,
                description: "A spacious event hall suitable for conferences and weddings."
        )
    }
}