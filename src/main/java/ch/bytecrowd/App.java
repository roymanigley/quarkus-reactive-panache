package ch.bytecrowd;

import io.quarkus.runtime.QuarkusApplication;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title="Example API",
                version = "1.0.1",
                contact = @Contact(
                        name = "Roy Manigley",
                        url = "https://bytecrowd.ch",
                        email = "roy@bytecrowd.ch"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class App extends Application {
}
