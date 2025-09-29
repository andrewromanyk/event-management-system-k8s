package ua.edu.ukma.event_management_micro.user;

import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.NamedInterface;

@ApplicationModule(
        allowedDependencies = "core"
)
@NamedInterface("api")
public class UserModule {
}
