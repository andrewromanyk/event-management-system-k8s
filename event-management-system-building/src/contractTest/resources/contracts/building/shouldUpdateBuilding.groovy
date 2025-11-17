package contracts.building

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should update an existing building"

    request {
        method PUT()
        url("/api/building/1")
        headers {
            contentType(applicationJson())
        }
        body([
                address: "Updated Address",
                hourlyRate: 80,
                areaM2: 550,
                capacity: 120,
                description: "Updated description"
        ])
    }

    response {
        status 200
    }
}