package ua.edu.ukma.event_management_micro;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithDocumentationTest {

    @Test
    void writeModuleDocumentation() {
        ApplicationModules modules = ApplicationModules.of(EventManagementMicroApplication.class).verify();
        new Documenter(modules).writeDocumentation();
    }
}