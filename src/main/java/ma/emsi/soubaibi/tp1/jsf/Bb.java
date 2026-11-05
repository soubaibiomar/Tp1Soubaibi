
package ma.emsi.soubaibi.tp1.jsf;

import ma.emsi.soubaibi.tp1.llm.JsonAdapterPourGemini;
import ma.emsi.soubaibi.tp1.llm.LlmInteraction;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Backing bean pour la page JSF index.xhtml.
 * Conserve l'etat de la conversation pendant la vue.
 */
@Named
@ViewScoped
public class Bb implements Serializable {

    private String roleSysteme;
    private boolean roleSystemeChangeable = true;
    private List<SelectItem> listeRolesSysteme;

    private String question;
    private String reponse;

    private StringBuilder conversation = new StringBuilder();

    // Proprietes du mode debug demandees dans le TP1
    private boolean debug;
    private String texteRequeteJson;
    private String texteReponseJson;

    // Adaptateur JSON Gemini
    @Inject
    private JsonAdapterPourGemini jsonAdapter;

    // Contexte JSF pour afficher les erreurs
    @Inject
    private FacesContext facesContext;

    public Bb() {
    }

    public String getRoleSysteme() {
        return roleSysteme;
    }

    public void setRoleSysteme(String roleSysteme) {
        this.roleSysteme = roleSysteme;
    }

    public boolean isRoleSystemeChangeable() {
        return roleSystemeChangeable;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getConversation() {
        return conversation.toString();
    }

    public void setConversation(String conversation) {
        this.conversation = new StringBuilder(conversation);
    }

    // Getters et setters du mode debug

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getTexteRequeteJson() {
        return texteRequeteJson;
    }

    public void setTexteRequeteJson(String texteRequeteJson) {
        this.texteRequeteJson = texteRequeteJson;
    }

    public String getTexteReponseJson() {
        return texteReponseJson;
    }

    public void setTexteReponseJson(String texteReponseJson) {
        this.texteReponseJson = texteReponseJson;
    }

    /**
     * Envoie la question a Gemini par l'intermediaire
     * de JsonAdapterPourGemini.
     *
     * @return null pour rester sur la meme page.
     */
    public String envoyer() {

        if (question == null || question.isBlank()) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Texte question vide",
                    "Il manque le texte de la question"
            );

            facesContext.addMessage(null, message);
            return null;
        }

        // Le role systeme est transmis au debut du chat
        if (this.conversation.isEmpty()) {
            jsonAdapter.setSystemRole(this.roleSysteme);
        }

        try {
            // Appel de l'adaptateur : code du support TP1
            LlmInteraction interaction =
                    jsonAdapter.envoyerRequete(question);

            this.reponse = interaction.reponseExtraite();
            this.texteRequeteJson = interaction.questionJson();
            this.texteReponseJson = interaction.reponseJson();

            // On bloque le changement de role apres un succes
            this.roleSystemeChangeable = false;

            // Afficher la question et la reponse
            afficherConversation();

        } catch (Exception e) {

            this.reponse = "Erreur : " + e.getMessage();

            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Probleme de connexion avec l'API du LLM",
                    "Probleme de connexion avec l'API du LLM "
                            + e.getMessage()
                            + "\n"
                            + Arrays.toString(e.getStackTrace())
            );

            facesContext.addMessage(null, message);
        }

        return null;
    }

    /**
     * Commence une nouvelle conversation.
     */
    public String nouveauChat() {
        return "index";
    }

    /**
     * Ajoute la question et la reponse a l'affichage.
     */
    private void afficherConversation() {

        this.conversation
                .append("== User:\n")
                .append(question)
                .append("\n== Serveur:\n")
                .append(reponse)
                .append("\n");
    }

    /**
     * Liste des roles systeme du TP0.
     */
    public List<SelectItem> getRolesSysteme() {

        if (this.listeRolesSysteme == null) {

            this.listeRolesSysteme = new ArrayList<>();

            String role = """
                    You are a helpful assistant.
                    You help the user to find the information they need.
                    If the user type a question, you answer it.
                    """;

            this.listeRolesSysteme.add(
                    new SelectItem(role, "Assistant")
            );

            role = """
                    You are an interpreter.
                    You translate from English to French
                    and from French to English.
                    If the user type a French text,
                    you translate it into English.
                    If the user type an English text,
                    you translate it into French.
                    If the text contains only one to three words,
                    give some examples of usage of these words
                    in English.
                    """;

            this.listeRolesSysteme.add(
                    new SelectItem(
                            role,
                            "Traducteur Anglais-Français")
            );

            role = """
                    Your are a travel guide.
                    If the user type the name of a country
                    or of a town,
                    you tell them what are the main places
                    to visit in the country or the town
                    are you tell them the average price
                    of a meal.
                    """;

            this.listeRolesSysteme.add(
                    new SelectItem(role, "Guide touristique")
            );
        }

        return this.listeRolesSysteme;
    }
}
