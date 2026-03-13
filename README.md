# Soccmel - Social Network per Uscite

Un'applicazione Android nativa scritta in Kotlin e Jetpack Compose per organizzare uscite con gli amici tramite sondaggi.

## Funzionalità Implementate

1.  **Login/Registrazione (Mock):** Inserisci un username per accedere.
2.  **Home Feed:** Visualizza i sondaggi attivi creati dai tuoi amici.
3.  **Crea Sondaggio:** Proponi un'attività ("Cosa facciamo?", "Dove andiamo?") e aggiungi opzioni.
4.  **Vota:** Entra in un sondaggio e vota le opzioni preferite.

## Struttura del Progetto

-   **`model/`**: Definizioni dei dati (`User`, `Poll`, `PollOption`).
-   **`data/`**: `MockRepository` che simula un backend locale (dati in memoria).
-   **`ui/screens/`**: Schermate dell'app (`Login`, `Home`, `CreatePoll`, `PollDetail`).
-   **`MainActivity.kt`**: Gestione della navigazione.

## Come Eseguire

1.  Apri il progetto in Android Studio.
2.  Attendi la sincronizzazione di Gradle.
3.  Premi "Run" su un emulatore o dispositivo fisico.

## Note Tecniche

-   L'app utilizza `androidx.navigation.compose` per la navigazione.
-   I dati sono volatili (reset all'avvio dell'app) poiché salvati in memoria nel `MockRepository`.
