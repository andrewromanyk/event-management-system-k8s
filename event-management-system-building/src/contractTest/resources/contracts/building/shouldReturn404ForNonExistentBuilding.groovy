package contracts.building

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return 404 for non-existent building"

    request {
        method GET()
        url("/api/building/999999")
        headers {
            contentType(applicationJson())
        }
    }

    response {
        status 404
    }
}