package contracts.user

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return the pre-created 'user' from PostConstruct"

    request {
        method GET()
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