# Cyberpunk Notes App 🌃

A secure, cyberpunk-themed note-taking application for Android, built with modern Android technologies and a distinct visual style.

Developed live as part of live stream series:
https://www.youtube.com/playlist?list=PL0zZBw8Dq429NOr4MPDZHolp8UoFVuNPI

![Notes App Screenshot](images/notes.png)

## 🚀 Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material3)
- **Architecture**: MVP (Model-View-Presenter)
- **Database**: Room (SQLite abstraction)
- **Security**: AndroidX Security Crypto (EncryptedSharedPreferences)
- **Asynchronous**: Kotlin Coroutines & Flow
- **Serialization**: GSON
- **PDF Generation**: iText7

## 🏗 Architecture & Components

The application follows the **MVP (Model-View-Presenter)** pattern to separate UI logic from business logic.

### 📱 Key Activities & Screens

1.  **`NotesActivity` (Main Entry)**
    -   **Role**: Displays and manages the primary note list.
    -   **Logic**:
        -   Validates session and passcode state with `SessionManager`/`PreferencesManager`.
        -   Loads notes via `NotesPresenter`/`NotesRepository` and keeps trash-bin visibility in sync.
    -   **UI**: `NotesListScreen` (Compose) with **search**, **grid/list toggle**, **JSON import/export**, and quick access to settings, trash, pinned notes, and URL-filtered notes.

2.  **`NoteDetailActivity`**
    -   **Role**: Create, view, and edit individual notes.
    -   **Features**:
        -   Auto-save functionality.
        -   Export to PDF (`FileUtils`).
        -   Share note content.
    -   **UI**: `NoteDetailScreen` (Compose).

3.  **`LoginActivity`**
    -   **Role**: Handles secure access to the application.
    -   **Modes**:
        -   `SETUP`: Create a new passcode.
        -   `CONFIRM`: Verify the new passcode.
        -   `VALIDATE`: Unlock the app.
    -   **UI**: `LoginScreen` (Compose) with custom 4-digit PIN input and cyberpunk styling.

4.  **`SettingsActivity`**
    -   **Role**: Manage application settings.
    -   **Features**:
        -   Toggle "Require passcode on opening".
        -   Initiates the passcode setup flow.

5.  **`TrashBinActivity`**
    -   **Role**: Shows soft-deleted notes with options to restore individually, restore all, or empty the bin permanently.
    -   **UI**: `TrashBinScreen` (Compose) driven by `TrashBinPresenter`/`NotesRepository`.

6.  **`PinnedNotesActivity`**
    -   **Role**: Presents notes that have been marked as pinned from the main list.
    -   **UI**: `PinnedNotesScreen` (Compose) with unpin support and navigation back to details.

7.  **`UrlNotesActivity`**
    -   **Role**: Filters notes containing URLs for quick link discovery.
    -   **UI**: `UrlNotesScreen` (Compose) powered by `UrlNotesPresenter` querying URL-bearing notes.

### 🔐 Security & Session Management

The app implements a robust security model to protect user notes.

-   **Storage**: Passcodes are stored using **`EncryptedSharedPreferences`** (AES256-GCM), ensuring they are encrypted at rest and cannot be read even on rooted devices without the Keystore key.
-   **Session State**:
    -   Managed by **`SessionManager`** (Singleton).
    -   `isLoggedIn` flag tracks the current session.
    -   Session is cleared on process death or when the passcode is disabled.
    -   Prevents infinite login loops by checking both the enabled flag and session state.

### 💾 Data Layer

-   **`AppDatabase`**: Room database instance.
-   **`Note`**: Entity representing a note (id, title, content, timestamp) with flags for soft deletion and pinning.
-   **`NoteDao`**: Data Access Object for database operations including search, trash management, pin toggling, and URL filtering.
-   **`NotesRepository`**: Abstraction layer between Presenters and the DAO.

### 🎨 UI & Theming

-   **Theme**: Custom Cyberpunk theme (`NotesAppTheme`).
-   **Colors**: Neon palette (Cyan, Magenta, Yellow) on dark backgrounds.
-   **Components**: Custom `CyberpunkCard`, `NeonButton`, and glitch-effect text styles.

## 📂 Project Structure

```
com.dmitryy.notesapp
├── data/           # Room entities, DAO, Database, Repository
├── ui/
│   ├── base/       # Base MVP interfaces
│   ├── list/       # Notes list screen (Activity, Presenter, Contract)
│   ├── detail/     # Note detail screen (Activity, Presenter, Contract)
│   ├── login/      # Login screen (Activity, Presenter, Contract)
│   ├── settings/   # Settings screen (Activity, Presenter, Contract)
│   ├── trash/      # Trash bin screen for soft-deleted notes
│   ├── pinned/     # Pinned notes screen
│   ├── url/        # Notes filtered to links/URLs
│   └── theme/      # Compose theme and color definitions
└── utils/          # Helpers (Logger, FileUtils, PreferencesManager, SessionManager, NavigationUtils)
```
