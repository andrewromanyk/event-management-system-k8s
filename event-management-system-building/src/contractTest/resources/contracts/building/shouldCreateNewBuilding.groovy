package contracts.building

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should create a new building"

    request {
        method POST()
        url("/api/building")
        headers {
            contentType(applicationJson())
        }
        body([
                address: "New Building Address",
                hourlyRate: 75,
                areaM2: 600,
                capacity: 150,
                description: "A brand new building for events"
        ])
    }

    response {
        status 201
        headers {
            contentType(applicationJson())
        }
        body([
                id: $(anyNumber()),
                address: "New Building Address",
                hourlyRate: 75,
                areaM2: 600,
                capacity: 150,
                description: "A brand new building for events"
        ])
    }
}