# 🚪 DOORS: Terminal Edition

**50 Türen. Ein Hotel. Und du bist nicht allein.**

DOORS: Terminal Edition ist ein textbasiertes Survival-Horrorspiel in Java. Es wurde als Schulprojekt auf Basis des Zork-Templates entwickelt und ist von Roblox DOORS inspiriert.

Durchsuche Räume, sammle Schlüssel und Ausrüstung und achte auf die Warnsignale. Hinter Tür 50 wartet Figure in der Grand Library. Entschlüssle den PIN des Great Gate, um aus dem Hotel zu entkommen.

## Spielablauf

| Abschnitt | Was passiert? |
| --- | --- |
| Tür 1 | Du startest im Schlafzimmer. Finde mit `search` den Schlüssel für die erste verschlossene Tür. |
| Türen 2–24 | Erkunde zufällig zusammengestellte Räume und sammle Münzen und Gegenstände. In einem der Räume 2–8 liegt die Taschenlampe. Ab Tür 5 kann Rush erscheinen. |
| Tür 25 | Kaufe Ausrüstung bei Jeff. Ab dem ersten Erreichen des Shops kannst du keine weiteren Münzen sammeln. |
| Türen 26–34 | Durchsuche die Räume vor der Verfolgung. Dunkle Räume und verschlossene Ausgänge sind möglich. |
| Türen 35–45 | Seek verfolgt dich. Folge der mit `*` markierten Richtung und erreiche Tür 46 innerhalb von 30 Sekunden. |
| Türen 46–49 | Erkunde die letzten normalen Räume. Der Rückweg in die Seek-Strecke ist versperrt. |
| Tür 50 | Entschlüssle den PIN in der Grand Library und öffne das Great Gate. |

Die normalen Räume verwenden acht wiederkehrende ASCII-Layouts. Funde, Dunkelheit und Schlösser werden pro Raum festgelegt. Mit `look` kannst du das Layout des aktuellen Raums erneut anzeigen.

## Begegnungen

| Gegner | Warnsignal und Reaktion |
| --- | --- |
| **Rush** | Bei `flicker flicker` hast du sechs Sekunden für `hide`. Nach der Entwarnung verlässt du dein Versteck mit `unhide`. |
| **Screech** | Bei `psst psst` hast du drei Sekunden für `peek`. Ohne rechtzeitige Reaktion verursacht Screech 40 Schaden. |
| **Seek** | Nach der Cutscene startet der 30-Sekunden-Timer. Wähle `left` oder `right` entsprechend dem `*`. Eine falsche Wahl kostet fünf Sekunden und lässt dich im selben Raum. |
| **Figure** | Wenn Figure dich in der Grand Library hört, hast du vier Sekunden für `hide`. Warte auf die Entwarnung und tippe dann `unhide`. |

Die Timer laufen auch ohne Eingabe weiter. Schicke deine Befehle mit **Enter** ab.

## Tür 50 · Grand Library

1. `source` zeigt die Reihenfolge der sechs Symbole für den PIN.
2. `findbook` findet ein Buch mit einer Zuordnung wie `STAR = 7`. Es gibt acht verschiedene Bücher. Sechs der acht Symbole werden für den PIN gebraucht.
3. `notes` zeigt alle bisher gefundenen Zuordnungen.
4. Setze die sechs Ziffern in der Reihenfolge aus `source` zusammen und gib sie mit `code` ein, etwa `code 072931`. Der PIN wird pro Durchlauf erzeugt und kann mit einer Null beginnen.

Bei jedem neuen Buchfund besteht eine **15-%-Chance**, dass Figure dich hört. Ein falscher sechsstelliger PIN löst die Warnung immer aus. Der richtige PIN öffnet das Great Gate und beendet den Durchlauf erfolgreich.

## Ausrüstung und Jeffs Shop

| Gegenstand | Preis bei Jeff | Wirkung |
| --- | ---: | --- |
| Crucifix | 250 Münzen | `use crucifix` aktiviert den Schutz vor einem schädigenden Treffer. |
| Lockpick | 50 Münzen | Öffnet eine verschlossene Tür und wird dabei vor einem vorhandenen Schlüssel verbraucht. |
| Flashlight | 100 Münzen | Verhindert Fehlschläge durch Dunkelheit, solange sie geladen und eingeschaltet ist. |
| Bandage | 75 Münzen | `use bandage` heilt bis zu 15 HP. Bei vollen HP bleibt die Bandage im Inventar. |

Eine eingeschaltete Taschenlampe verliert alle 30 Sekunden 10 Prozentpunkte Ladung. Mit `use battery` lädst du sie um bis zu 30 Prozentpunkte auf. Beim Ablegen schaltet sich die Lampe aus und behält ihren Ladestand.

Das Inventar fasst **15 Gewichtspunkte**. Die Flashlight wiegt 3, Batterie und Crucifix wiegen je 2, alle anderen Gegenstände je 1. Ein zu schwerer Fund bleibt im Raum. Mit `drop <item>` schaffst du Platz und kannst abgelegte Gegenstände mit `search` wieder aufnehmen.

Ein aktives Crucifix kann auch einen tödlichen Treffer abfangen. Bei Seek blockiert es einen Fangversuch und gibt dir zehn zusätzliche Sekunden. Figure kann nach einem abgewehrten Treffer erneut erscheinen.

## Befehle

| Befehl | Funktion |
| --- | --- |
| `continue` | Durch die nächste Tür gehen. |
| `back` / `return` | In den vorherigen erreichbaren Raum zurückgehen. |
| `look` | Das ASCII-Layout des aktuellen Raums anzeigen. |
| `search` | Den aktuellen Raum nach Münzen und Gegenständen durchsuchen. |
| `map` | Alle Räume und ihre noch vorhandenen Gegenstände anzeigen. `>>` markiert deinen Standort. |
| `status` / `inventory` | Spielerstatus oder Inventar anzeigen. |
| `flashlight on` / `flashlight off` | Die Taschenlampe ein- oder ausschalten. |
| `shop` / `buy <item>` | Jeffs Angebot anzeigen oder in Raum 25 einkaufen, etwa mit `buy lockpick`. |
| `use bandage` / `use battery` / `use crucifix` | Ausrüstung verwenden. |
| `drop <item>` | Einen Gegenstand im aktuellen Raum ablegen, etwa mit `drop bandage`. |
| `hide` / `unhide` / `peek` | Auf Monster reagieren. |
| `left` / `right` | Während der Seek-Verfolgung die Richtung wählen. |
| `source` / `findbook` / `notes` / `code <PIN>` | Das Rätsel in der Grand Library lösen. |
| `help` / `quit` | Befehlsübersicht anzeigen oder das Spiel beenden. |

## Starten

Du brauchst ein JDK, beispielsweise **JDK 21**, und Maven für den Build im Terminal. In IntelliJ IDEA öffnest du das Projekt über die `pom.xml` und startest die Main-Methode in `Zork2.java`.

Im Terminal kannst du das Projekt im Repo-Ordner bauen und starten.

```bash
mvn clean package
java -cp target/classes ch.bbw.zork.Zork2
```

## Tests

Die JUnit-Tests decken `Game`, `Player`, `Item`, `Room`, `JeffShop`, `LibraryPuzzle`, `MonsterController` und `SeekController` ab. Die Game-Tests prüfen unter anderem verschlossene Türen, Schlüsselverbrauch, Rückwege, das Ablegen von Gegenständen und die Kartenanzeige.

```bash
mvn clean test
```

## Dokumentation

- [Klassendiagramm (PNG)](diagrams/doors-klassendiagramm.png)
- [Raumskizze (PNG)](diagrams/doors-raeumlichkeiten-uml.png)
- [Dokumentation der Räumlichkeiten](docs/doors-raeumlichkeiten-doc.md)
- [Raumplan mit Regeln pro Bereich](docs/doors-raumplan.md)
- [Dunkle Räume und Taschenlampe](docs/dunkle-raeume.md)

Das Projekt nutzt das bereitgestellte Zork-Template von Michael Kölling, überarbeitet von Rinaldo Lanza. Spielkonzept und Entscheidungen stammen von uns. KI hat uns bei der Dokumentation, bei Erklärungen und bei Codevorschlägen unterstützt.