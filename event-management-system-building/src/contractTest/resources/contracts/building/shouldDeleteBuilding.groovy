package contracts.building

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should delete a building"

    request {
        method DELETE()
        url("/api/building/1")
    }

    response {
        status 200
    }
}