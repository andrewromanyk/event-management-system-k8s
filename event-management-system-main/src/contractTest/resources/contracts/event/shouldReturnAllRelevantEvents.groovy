package contracts.event

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return all relevant events"

    request {
        method GET()
        url("/api/event/relevant")
    }

    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
                [
                        id: 1,
                        eventTitle: "Sample Event",
                        description: "This is a sample event for testing purposes.",
                        buildingId: 1,
                        price: 50.0
                ]
        ])
    }
}