
# TP1 - API Gemini avec Jakarta EE et JSF

## Auteur
Omar Soubaibi - EMSI

## Objectif
Créer une application Web JSF permettant
de communiquer directement avec l'API Gemini.

## Technologies
- Java 25
- Jakarta EE 11
- Jakarta Faces (JSF)
- PrimeFaces
- Maven
- Payara Server 7
- API REST Gemini

## Fonctionnalités
- Envoi de questions à Gemini
- Gestion des rôles système
- Conservation de l'historique
- Mode debug JSON
- Nouveau chat
- Rôle bonus : Auditeur Sécurité IA

## Configuration
Créer une variable d'environnement
nommée GEMINI_KEY contenant la clé API Gemini.

Ne jamais publier la clé secrète.

## Compilation
mvn clean package

## Déploiement
Démarrer Payara 7 puis déployer :
target/tp1Soubaibi.war

## Utilisation
Ouvrir :
http://localhost:8080/tp1Soubaibi/

## Tests
- Conversation avec Gemini
- Historique de deux questions
- Clé API invalide
- Rôle JSON invalide
- Restauration des modifications avec Git
