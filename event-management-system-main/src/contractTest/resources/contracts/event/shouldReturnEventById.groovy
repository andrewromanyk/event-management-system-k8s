package contracts.event

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return the pre-created event from PostConstruct"

    request {
        method GET()
        url("/api/event/1")
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
                id: 1,
                eventTitle: "Sample Event",
                description: "This is a sample event for testing purposes.",
                buildingId: 1,
                creatorId: 3,
                numberOfTickets: 100,
                minAgeRestriction: 18,
                price: 50.0,
                dateTimeStart: $(producer(regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?"))),
                dateTimeEnd: $(producer(regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?")))
        ])
    }
}