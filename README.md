# 🚪 DOORS: Terminal Edition

**50 Türen. Ein Hotel. Und du bist nicht allein.**

Ein textbasiertes Survival-Horrorspiel in Java, inspiriert von Roblox DOORS und entwickelt als Schulprojekt auf Basis des Zork-Templates.

Durchsuche Räume, sammle Schlüssel und Ausrüstung und achte auf die Warnsignale. Bei Tür 50 wartet Figure in der Grand Library. Wer den sechsstelligen PIN des Great Gate entschlüsselt und Figure ausweicht, entkommt dem Hotel.

## Projektstatus

Der Spielablauf von **Tür 1 bis Tür 50** ist implementiert. Dazu gehören zufällige Räume und Funde, Jeffs Shop, die Begegnungen mit Rush und Screech, die Seek-Verfolgung und das Figure-Finale mit einem eigenen Siegabschluss.

**Als Nächstes kommt der Feinschliff.** Wir spielen einen vollständigen Durchlauf, vergleichen das Projekt mit dem Bewertungsraster und verbessern die Stellen, die dabei auffallen. Dazu gehören das Balancing der Reaktionszeiten, klare Meldungen im Terminal und die Aktualisierung von Tests und Dokumentation, wo nötig.

## Spielablauf

| Abschnitt | Was passiert? |
| :--- | :--- |
| Tür 1 | Fester Einstieg. Finde mit `search` den Schlüssel zur ersten verschlossenen Tür. |
| Türen 2–24 | Erkunde zufällig zusammengestellte Räume und sammle Coins und Items. Ab Raum 5 kann Rush auftauchen. |
| Tür 25 | Jeffs Shop. Gib deine Coins für Ausrüstung aus. Ab dem ersten Erreichen dieses Raums kannst du keine weiteren Coins sammeln. |
| Türen 26–34 | Weitere Erkundung vor der Verfolgung. |
| Türen 35–45 | Seek verfolgt dich. Folge dem `*` in der ASCII-Map und erreiche Tür 46 innerhalb von 30 Sekunden. |
| Türen 46–49 | Erkunde die letzten normalen Räume. Der Rückweg in die Seek-Strecke ist versperrt. |
| Tür 50 | Entschlüssle das Great Gate in der Grand Library und entkomme Figure. |

Die acht normalen ASCII-Layouts werden wiederverwendet. Jeder Raum hat eigene Funde und einen eigenen Zustand für Dunkelheit und Schloss. Beim Betreten erscheint die Map. Mit `look` kannst du sie erneut anzeigen.

## Begegnungen

| Gegner | Warnsignal und Reaktion |
| :--- | :--- |
| **Rush** | `flicker flicker`. Du hast 6 Sekunden für `hide`. Nach der Entwarnung verlässt du dein Versteck mit `unhide`. |
| **Screech** | `psst psst`. Tippe innerhalb von 3 Sekunden `peek`, sonst verlierst du 40 HP. |
| **Seek** | Nach einer kurzen Cutscene startet der 30-Sekunden-Timer. Wähle `left` oder `right` auf der mit `*` markierten Seite. Eine falsche Wahl kostet 5 Sekunden und lässt dich im selben Raum. |
| **Figure** | Wenn er dich in der Grand Library hört, hast du 4 Sekunden für `hide`. Warte auf die Entwarnung und tippe dann `unhide`. |

Die Timer laufen auch ohne Eingabe weiter. Ein Befehl zählt, sobald du ihn mit **Enter** abschickst.

## Tür 50 · Grand Library

1. `source` zeigt die **Reihenfolge der sechs Symbole** für den PIN.
2. `findbook` findet ein Buch mit einer Zuordnung, etwa `STAR = 7`. Es gibt acht unterschiedliche Bücher. Zwei Symbole gehören nicht zum PIN.
3. `notes` zeigt alle bisher gefundenen Zuordnungen.
4. Setze die sechs Ziffern in der Reihenfolge aus `source` zusammen und gib sie mit `code` ein, zum Beispiel `code 072931`. Der PIN wird pro Durchlauf erzeugt und kann mit einer Null beginnen.

Bei jedem neuen Buchfund besteht eine **15-%-Chance**, dass Figure dich hört. Ein falscher sechsstelliger PIN löst die Warnung immer aus. Der richtige PIN öffnet das Great Gate und beendet den Run erfolgreich.

## Ausrüstung und Jeffs Shop

| Item | Preis bei Jeff | Wirkung |
| :--- | ---: | :--- |
| Crucifix | 250 Coins | `use crucifix` aktiviert Schutz für einen Treffer. |
| Lockpick | 50 Coins | Öffnet automatisch eine verschlossene Tür. Wird dabei vor einem vorhandenen Schlüssel verbraucht. |
| Flashlight | 100 Coins | Verhindert Fehlschläge durch Dunkelheit, solange sie eingeschaltet und geladen ist. |
| Bandage | 75 Coins | `use bandage` heilt bis zu 15 HP. Bei vollen HP bleibt sie im Inventar. |

Batterien findest du in Räumen. Mit `use battery` lädst du die Flashlight um bis zu 30 Prozentpunkte auf. Beim Einschalten beginnt der Verbrauch von 10 Prozentpunkten pro 30 Sekunden.

Ein aktives Crucifix kann auch einen tödlichen Treffer abfangen. **Figure kann danach erneut erscheinen.** Bei Seek blockiert es einen Fangversuch und gibt dir 10 zusätzliche Sekunden.

## Befehle

| Befehl                                         | Funktion |
|:-----------------------------------------------| :--- |
| `continue` / `return/back`                        | Vorwärtsgehen oder in den vorherigen erreichbaren Raum zurückkehren |
| `look` / `search`                              | Raumkarte anzeigen oder nach Coins und Items suchen |
| `map` | Zeigt alle Räume und ihre noch vorhandenen Gegenstände. `>>` markiert deinen aktuellen Raum. |
| `status` / `inventory`                         | Spielerstatus oder Inventar anzeigen |
| `flashlight on` / `flashlight off`             | Flashlight einschalten oder ausschalten |
| `shop` / `buy <item>`                          | Angebot ansehen oder in Raum 25 einkaufen, etwa mit `buy lockpick` |
| `use bandage` / `use battery` / `use crucifix` | Ausrüstung verwenden |
| `drop <item>` | Legt ein Item im aktuellen Raum ab und macht Platz im Inventar. Mit `search` kannst du es wieder aufnehmen. Beispiel: `drop bandage`. |
| `hide` / `unhide` / `peek`                     | Auf Monster reagieren |
| `left` / `right`                               | Während Seek die Richtung wählen |
| `source` / `findbook` / `notes` / `code <PIN>` | Das Rätsel in der Grand Library lösen |
| `help` / `quit`                                | Befehlsübersicht anzeigen oder das Spiel beenden |

## Starten

Voraussetzungen sind ein JDK, beispielsweise **JDK 21**, und Maven für den Build im Terminal. In IntelliJ IDEA kannst du das Projekt über die `pom.xml` öffnen und die Main-Methode in `Zork2.java` starten.

Alternativ im Projektordner:

```bash
mvn clean package
java -cp target/classes ch.bbw.zork.Zork2
```

## Dokumentation

- [Räumlichkeiten und ursprüngliche Skizze](docs/doors-raeumlichkeiten-doc.md)
- [Dokumentation zu dunklen Räumen](docs/dunkle-raeume.md)
- [Räumlichkeiten-UML](diagrams/doors-raeumlichkeiten-uml.png)
- [Klassendiagramm](diagrams/doors-klassendiagramm.png)

Die ergänzenden Dokumente und Diagramme zeigen teilweise frühere Entwicklungsstände.

Das Projekt nutzt das bereitgestellte Zork-Template von Michael Kölling, überarbeitet von Rinaldo Lanza. Spielkonzept und Entscheidungen stammen von uns. KI hat uns bei der Dokumentation, bei Erklärungen und bei Codevorschlägen unterstützt.

> Wenn das Licht flackert, lies nicht erst die README.
