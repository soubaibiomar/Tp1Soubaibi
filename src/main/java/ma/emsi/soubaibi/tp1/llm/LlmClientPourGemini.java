
package ma.emsi.soubaibi.tp1.llm;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;

/**
 * Gère l'interface avec l'API de Gemini.
 * Squelette du professeur complété.
 */
@Dependent
public class LlmClientPourGemini implements Serializable {

    private final String key;

    private Client clientRest;

    private final WebTarget target;

    public LlmClientPourGemini() {

        // Clé stockée dans les variables d'environnement
        this.key = System.getenv("GEMINI_KEY");

        // Client REST
        this.clientRest = ClientBuilder.newClient();

        // Endpoint Gemini
        this.target = clientRest.target(
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent"
        );
    }

    /**
     * Envoie une requête REST à Gemini.
     */
    public Response envoyerRequete(Entity requestEntity) {

        Invocation.Builder request = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("x-goog-api-key", key);

        return request.post(requestEntity);
    }

    @PreDestroy
    public void closeClient() {
        if (this.clientRest != null) {
            this.clientRest.close();
        }
    }
}
