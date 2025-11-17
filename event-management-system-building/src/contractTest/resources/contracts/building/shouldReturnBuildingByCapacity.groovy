package contracts.building

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return buildings filtered by capacity"

    request {
        method GET()
        urlPath("/api/building") {
            queryParameters {
                parameter("capacity", 100)
            }
        }
        headers {
            contentType(applicationJson())
        }
    }

    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
                [
                        id: $(anyNumber()),
                        address: $(anyNonEmptyString()),
                        hourlyRate: $(anyPositiveInt()),
                        areaM2: $(anyPositiveInt()),
                        capacity: 100,
                        description: $(anyNonEmptyString())
                ]
        ])
    }
}