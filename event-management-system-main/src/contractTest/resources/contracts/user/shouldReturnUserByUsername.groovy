package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return the pre-created 'user' from PostConstruct"

    request {
        method GET()
        // This username exists in your UserService.setupData()
        url("/api/user/user")
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
                // IDs might vary depending on insertion order, but usually 'user' is 2 (after admin)
                id: $(anyNumber()),
                username: "user",
                userRole: "USER",
                firstName: "Normal",
                lastName: "User",
                email: "cr.and321@gmail.com",
                phoneNumber: "+1234567891",
                dateOfBirth: "1995-05-15"
        ])
    }
}