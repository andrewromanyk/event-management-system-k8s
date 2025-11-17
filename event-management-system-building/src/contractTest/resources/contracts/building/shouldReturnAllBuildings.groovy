package contracts.building

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return all buildings"

    request {
        method GET()
        url("/api/building")
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
                        capacity: $(anyPositiveInt()),
                        description: $(anyNonEmptyString())
                ]
        ])
    }
}