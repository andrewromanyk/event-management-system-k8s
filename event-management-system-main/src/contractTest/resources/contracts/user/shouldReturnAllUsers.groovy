package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return all users"

    request {
        method GET()
        url("/api/user")
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
                        id: 1,
                        username: "admin",
                        userRole: "ADMIN",
                        firstName: "Admin",
                        lastName: "User",
                        email: "test@gmail.com",
                        phoneNumber: "+1234567890",
                        dateOfBirth: "2000-01-01"
                ],
        ])
    }
}