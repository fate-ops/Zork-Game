# Bewertungsraster – Zork 2 Game Projekt

> Jede Aufgabe wird in 3 Stufen bewertet:
> - **Stufe 1** – rudimentär umgesetzt, Ziel nicht erreicht → Note **3.5**
> - **Stufe 2** – gut gemacht, einiges fehlt noch → Note **4.8**
> - **Stufe 3** – perfekt, Ziele übertroffen → Note **6.0**
>
> Aufgaben mit (*) werden bei der **Präsentation** bewertet.

---

## Automatisch geprüft (via `repo_check.py`)

### 1. Git-Nutzung & Commit-Aktivität

| Stufe | Beschreibung |
|---|---|
| 1 | Repo vorhanden, aber sehr wenige Commits (< 5); kaum alle Mitglieder haben commitet |
| 2 | Regelmässige Commits, die meisten Mitglieder sind im Git-Verlauf sichtbar |
| 3 | Kontinuierlicher Verlauf über den ganzen Zeitraum, alle Mitglieder gleichmässig beteiligt, sinnvolle Commit-Nachrichten |

---

### 2. JavaDoc – Klassen

| Stufe | Beschreibung |
|---|---|
| 1 | Weniger als die Hälfte der Klassen haben einen JavaDoc-Kommentar |
| 2 | Die meisten Klassen sind dokumentiert, einzelne fehlen noch |
| 3 | Alle Klassen vollständig mit JavaDoc dokumentiert |

---

### 3. JavaDoc – Methoden

| Stufe | Beschreibung |
|---|---|
| 1 | Weniger als 50 % der Methoden sind dokumentiert |
| 2 | Mehr als 50 % der Methoden sind dokumentiert |
| 3 | Mehr als 90 % der Methoden sind mit JavaDoc versehen |

---

### 4. JUnit-Tests (5–8 Tests pro Fachklasse)

| Stufe | Beschreibung |
|---|---|
| 1 | Tests vorhanden, aber weniger als 5 pro Fachklasse; viele Tests ohne `assert` |
| 2 | Die meisten Fachklassen haben >= 5 Tests; Tests prüfen sinnvolle Dinge |
| 3 | Alle Fachklassen haben >= 5 sinnvolle Tests mit `assert`, Build und Tests grün |

---

### 5. Build & Tests grün (Maven)

| Stufe | Beschreibung |
|---|---|
| 1 | Projekt kompiliert nicht oder Tests schlagen alle fehl |
| 2 | Projekt kompiliert, einzelne Tests schlagen fehl |
| 3 | `mvn clean test` läuft durch, alle Tests grün |

---

### 6. Dokumentation – README.md

| Stufe | Beschreibung |
|---|---|
| 1 | README vorhanden, aber sehr kurz (< 30 Wörter) oder fehlt |
| 2 | README mit sinnvollem Inhalt (>= 30 Wörter), beschreibt das Spiel |
| 3 | Ausführliches README (>= 150 Wörter) mit Spielbeschreibung, Anleitung und Hinweisen |

---

### 7. Weitere .md-Dokumentation

| Stufe | Beschreibung |
|---|---|
| 1 | Keine weiteren .md-Dateien ausser README |
| 2 | Mindestens eine weitere .md-Datei vorhanden |
| 3 | Mehrere .md-Dateien mit strukturierter Dokumentation (z. B. Spielidee, Architektur) |

---

### 8. Klassendiagramm

| Stufe | Beschreibung |
|---|---|
| 1 | Kein Klassendiagramm vorhanden |
| 2 | Klassendiagramm vorhanden, zeigt die wichtigsten Klassen |
| 3 | Vollständiges, aktuelles Klassendiagramm mit Attributen, Methoden und Beziehungen |

---

### 9. Raumskizze / Raumplan

| Stufe | Beschreibung |
|---|---|
| 1 | Keine Raumskizze vorhanden |
| 2 | Raumskizze vorhanden, zeigt die Räume und Verbindungen |
| 3 | Klarer, vollständiger Raumplan mit Beschriftungen; stimmt mit Implementierung überein |

---

### 10. Räume (new Room) – Gewonnen-Logik

| Stufe | Beschreibung |
|---|---|
| 1 | Weniger als 3 eigene Räume; keine Gewonnen-Logik erkennbar |
| 2 | 3–9 eigene Räume; Gewonnen-Logik im Code vorhanden |
| 3 | 5–10 gut gestaltete Räume mit eigenem Thema; Gewonnen-Bedingung klar implementiert und dokumentiert |

---

### 11. Neue Klassen (über Vorlage hinaus)

| Stufe | Beschreibung |
|---|---|
| 1 | Keine neuen Klassen; nur Vorlage verwendet |
| 2 | Mindestens eine neue Klasse (z. B. Item) hinzugefügt |
| 3 | Mehrere sinnvolle neue Klassen mit klarer Verantwortung (Single Responsibility) |

---

### 12. Repo-Hygiene (.gitignore, keine Junk-Dateien)

| Stufe | Beschreibung |
|---|---|
| 1 | Kein .gitignore; kompilierte Dateien (target/, .class) oder IDE-Ordner (.idea/) im Repo |
| 2 | .gitignore vorhanden, aber nicht vollständig; einzelne unnötige Dateien noch im Repo |
| 3 | .gitignore vollständig; keine unnötigen Dateien im Repo |

---

## Bei der Präsentation bewertet (*)

### 13. `back`-Befehl (*)

| Stufe | Beschreibung |
|---|---|
| 1 | Befehl existiert, aber funktioniert nicht zuverlässig |
| 2 | Befehl funktioniert; Realisierung ist erklärt |
| 3 | Befehl funktioniert einwandfrei; Realisierung ist gut dokumentiert und im Code sauber umgesetzt |

---

### 14. Item-Klasse – Gegenstand im Raum (*)

| Stufe | Beschreibung |
|---|---|
| 1 | Item-Klasse ansatzweise vorhanden, aber nicht im Raum nutzbar |
| 2 | Item-Klasse mit Eigenschaften, Raum kann einen Gegenstand enthalten |
| 3 | Item vollständig mit allen Attributen, Raum kann nach Gegenstand gefragt werden, sauber dokumentiert |

---

### 15. Mehrere Gegenstände (ArrayList in Room) (*)

| Stufe | Beschreibung |
|---|---|
| 1 | Nur ein Gegenstand pro Raum möglich |
| 2 | ArrayList in Room vorhanden, Ausgabe angepasst |
| 3 | Mehrere Gegenstände pro Raum, vollständige und lesbare Ausgabe, sauber implementiert |

---

### 16. Gewicht (weight auf Item) (*)

| Stufe | Beschreibung |
|---|---|
| 1 | Gewicht-Attribut deklariert, aber nicht genutzt |
| 2 | Gewicht vorhanden und wird bei der Ausgabe angezeigt |
| 3 | Gewicht vollständig integriert, beeinflusst das Spielgeschehen (z. B. Maximalgewicht) |

---

### 17. `map`-Befehl (*)

| Stufe | Beschreibung |
|---|---|
| 1 | Befehl existiert, aber Ausgabe unvollständig |
| 2 | Alle Räume mit Gegenständen werden ausgegeben; aktueller Raum ist erkennbar |
| 3 | Übersichtliche, vollständige Ausgabe; ArrayList in Game sauber genutzt |

---

### 18. Spieler trägt Gegenstände (ein/mehrere, Maximalgewicht, ablegen) (*)

| Stufe | Beschreibung |
|---|---|
| 1 | Spieler kann einen Gegenstand aufnehmen, aber nicht ablegen oder mehrere tragen |
| 2 | Spieler kann mehrere Gegenstände tragen und ablegen; Maximalgewicht vorhanden |
| 3 | Vollständige Implementierung mit sinnvollem Maximalgewicht, Fehlermeldungen und klarer Dokumentation |

---

### 19. Codestruktur / Single Responsibility (*)

| Stufe | Beschreibung |
|---|---|
| 1 | Eine Klasse macht zu viel (> 300 Zeilen, gemischte Verantwortlichkeiten) |
| 2 | Klassen haben weitgehend eine klare Verantwortung, kleinere Ausnahmen |
| 3 | Jede Klasse hat genau eine Verantwortung; Architektur ist logisch und erklärbar |

---

### 20. Vererbung (optional, Bonus) (*)

| Stufe | Beschreibung |
|---|---|
| 1 | Vererbung angedacht, aber nicht korrekt umgesetzt |
| 2 | Vererbung korrekt implementiert (z. B. Item -> DiamantItem) |
| 3 | Vererbung sinnvoll eingesetzt, dokumentiert und im Klassendiagramm dargestellt |

---

## Notenschlüssel

| Stufe | Note |
|---|---|
| 1 | 3.5 |
| 2 | 4.8 |
| 3 | 6.0 |

> Zwischenwerte sind möglich, wenn die Arbeit zwischen zwei Stufen liegt.
