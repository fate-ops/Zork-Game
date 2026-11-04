# 🚪 DOORS: Terminal Edition

> **50 Türen. Ein Hotel. Hinter jeder Tür eine neue Entscheidung.**

Ein textbasiertes Survival-Horrorspiel in Java, inspiriert von Roblox DOORS und entwickelt als Schulprojekt auf Basis des Zork-Templates.

Erkunde Räume, sammle Gegenstände und achte auf Warnsignale. Das Ziel: Tür 50 erreichen, Figures Bibliotheksrätsel lösen und lebend entkommen.

---

## Projektstatus

Das Spiel befindet sich in Entwicklung. Die Grundmechaniken sind spielbar; Monster, besondere Begegnungen und der vollständige Durchlauf werden schrittweise ergänzt.

### Bereits umgesetzt

- [x] Spieler mit 100 HP, Coins und Inventar
- [x] Vorwärts- und Rückwärtsbewegung mit `continue` und `return`
- [x] Räume nach Coins und Items durchsuchen
- [x] Verschlossene Türen und verbrauchbare Schlüssel
- [x] Dunkle Räume mit erschwerter Suche und Orientierung
- [x] Flashlight ein- und ausschalten
- [x] Zeitabhängiger Batterieverbrauch
- [x] Status- und Inventaranzeige
- [x] Zehn ASCII-Raumlayouts in `RoomLayouts.java`
- [x] Erste JUnit-Tests für Räume

### Als Nächstes geplant

- [ ] ASCII-Layouts mit den spielbaren Räumen verbinden
- [ ] Vollständigen Ablauf mit 50 Türen aufbauen
- [ ] Rush und Verstecken
- [ ] Screech und schnelle Reaktionen
- [ ] Seek-Verfolgung mit Links-/Rechts-Entscheidungen
- [ ] Figures Library als Finale

> Der aktuelle Prototyp enthält fünf Testräume. Die 50 Türen und die Monsterbegegnungen beschreiben das Entwicklungsziel.

## Spielidee

Der Spieler betritt ein verlassen wirkendes Hotel und arbeitet sich durch 50 Türen. Normale Erkundung wechselt sich mit Verfolgungen, Gefahren und einem abschliessenden Rätsel ab.

Die Steuerung erfolgt über Textbefehle. Raumbeschreibungen, Warnmeldungen und ASCII-Grundrisse vermitteln die Umgebung.

Aufmerksamkeit ist entscheidend: Flackerndes Licht kündigt eine Gefahr an, Dunkelheit erschwert die Orientierung und ein Geräusch kann Figure auf den Spieler aufmerksam machen.

## Räume und Maps

Es gibt zehn unterschiedliche Raumlayouts: acht normale Räume und zwei besondere Bereiche.

| Layout | Beschreibung | Geplanter Einsatz |
| :--- | :--- | :--- |
| `BEDROOM` | Hotelzimmer mit Bett, Schrank und Schreibtisch | Normale Erkundung |
| `LIBRARY` | Kleine Bibliothek mit Regalen und Lesetisch | Normale Erkundung |
| `HALLWAY` | Langer Flur mit Seitentüren | Normale Erkundung |
| `KITCHEN` | Küche mit Esstisch und Vorratsschränken | Normale Erkundung |
| `STORAGE_ROOM` | Lagerraum mit Kisten, Fässern und Regalen | Normale Erkundung |
| `BATHROOM` | Badezimmer mit Badewanne, Dusche und Schrank | Normale Erkundung |
| `LOUNGE` | Aufenthaltsraum mit Sofa, Kamin und Klavier | Normale Erkundung |
| `OFFICE` | Büro mit Schreibtisch, Aktenschränken und Safe | Normale Erkundung |
| `SEEK_RUN` | Verzweigung mit einer linken und rechten Tür | Seek-Verfolgung |
| `GRAND_LIBRARY` | Grosse Bibliothek mit verschlossenem Ausgang | Figure-Finale bei Tür 50 |

Die Layouts sind in [`RoomLayouts.java`](src/main/java/ch/bbw/zork/RoomLayouts.java) definiert.

**Zehn Layouts bedeuten nicht zehn spielbare Räume.** Normale Layouts werden im späteren Durchlauf wiederverwendet. Jeder erzeugte Raum erhält seinen eigenen Zustand für Coins, Items, Dunkelheit und Schloss.

Die kleine Bibliothek `LIBRARY` ist ein normaler Raum. Das Finale findet ausschliesslich in der `GRAND_LIBRARY` statt.

## Geplanter Spielablauf

| Abschnitt | Inhalt |
| :--- | :--- |
| Türen 1–9 | Einstieg: Befehle kennenlernen, Räume durchsuchen und Schlüssel verwenden |
| Ab Tür 10 | Rush kann in normalen Räumen auftreten |
| Tür 35 bis 45 | Seek-Verfolgung; Ziel ist das Erreichen von Tür 46 |
| Türen 46–49 | Letzte Erkundungsstrecke vor dem Finale |
| Tür 50 | Figures Library: Bücher finden, Code entschlüsseln und entkommen |

Das Spiel endet nach dem erfolgreichen Library-Rätsel. Der frühere Plan mit 100 Türen, einem zweiten Seek-Run und einem Aufzugfinale wurde auf diesen Umfang reduziert.

## Spieler und Gegenstände

| Ressource | Bedeutung |
| :--- | :--- |
| HP | Der Spieler startet mit 100 Lebenspunkten. Für Monsterbegegnungen ist ein Spielende bei 0 HP vorgesehen. |
| Coins | Werden in Räumen gesammelt. Weitere Verwendungsmöglichkeiten sind geplant. |
| Inventar | Enthält gefundene Gegenstände. |
| Key | Öffnet eine verschlossene Vorwärtstür und wird dabei verbraucht. |
| Flashlight | Verhindert Dunkelheits-Fehlschläge, solange sie eingeschaltet und geladen ist. |

Eine entriegelte Tür bleibt offen. Beim erneuten Durchqueren wird kein weiterer Schlüssel benötigt. Bereits eingesammelte Coins und Items erscheinen im selben Raum nicht erneut.

## Dunkelheit und Flashlight

Dunkle Räume werden durch eine Textmeldung angekündigt.

| Aktion | Ohne eingeschaltete, geladene Flashlight | Mit eingeschalteter, geladener Flashlight |
| :--- | :--- | :--- |
| `continue` | 20 % Chance, die Tür nicht zu finden | Kein Fehlschlag durch Dunkelheit |
| `search` | 25 % Chance, nichts zu finden | Kein Fehlschlag durch Dunkelheit |
| `return` | Rückweg funktioniert normal | Rückweg funktioniert normal |

Bei einer fehlgeschlagenen Suche bleiben Coins und Items im Raum. Ein Fehlschlag beim Weitergehen verbraucht keinen Schlüssel.

### Batterie

- Start bei 100 %.
- Alle 30 Sekunden eingeschalteter Zeit werden 10 Prozentpunkte verbraucht.
- Ausschalten pausiert den Verbrauch.
- Angebrochene Zeitintervalle bleiben beim Ausschalten gespeichert.
- Bei 0 % schaltet die Lampe aus und kann nicht erneut eingeschaltet werden.
- `status` und `inventory` zeigen den Batteriestand.

Die verstrichene Zeit wird bei einer Abfrage oder Nutzung nachgerechnet. Auch Wartezeit ohne Eingabe zählt, solange die Lampe eingeschaltet ist.

## Geplante Monster

### Rush

Ab Tür 10 kann Rush in normalen Räumen erscheinen. Flackerndes Licht warnt den Spieler. Mit `hide` muss er sich rechtzeitig verstecken.

### Screech

In dunklen Räumen ist eine Erscheinungswahrscheinlichkeit von 5 % pro Sekunde vorgesehen. Eine Warnmeldung fordert zur schnellen Reaktion mit `spin` auf. Bei einer zu späten Reaktion verliert der Spieler 40 HP.

### Seek

Bei Tür 35 beginnt die Verfolgung. In jedem Abschnitt entscheidet der Spieler zwischen links und rechts.

Das Guiding Light markiert die richtige Richtung mit einem Stern `*` in der ASCII-Darstellung.

- Ziel: Tür 46 erreichen.
- Zeitlimit: 45 Sekunden für die gesamte Verfolgung.
- Falsche Richtung: 5 Sekunden Zeitabzug.
- Zeit abgelaufen: Der Versuch endet.

Das Seek-Layout ist bereits vorhanden. Richtungsmarkierung, Eingaben und Timer werden noch ergänzt.

### Figure – Finale bei Tür 50

Figure kann nichts sehen, hört dafür besonders gut. Der Spieler muss in der grossen Bibliothek Hinweise sammeln und den sechsstelligen PIN des Great Gate entschlüsseln.

Der geplante Ablauf:

1. `source` zeigt die Reihenfolge der sechs benötigten Symbole.
2. `findbook` liefert Hinweise aus insgesamt acht Büchern.
3. Jedes Buch ordnet einem Symbol eine Ziffer zu.
4. Die Ziffern werden entsprechend der Vorlage zusammengesetzt.
5. Mit `code` wird der PIN eingegeben.
6. Der richtige PIN öffnet den Ausgang und beendet das Spiel erfolgreich.

Jede Verwendung von `findbook` soll eine 15 % Chance haben, Figures Aufmerksamkeit zu wecken. In diesem Fall muss der Spieler rechtzeitig `hide` eingeben. Ohne rechtzeitiges Verstecken endet der Versuch.

Reaktionsfristen, wiederholte Buchfunde und die Folgen einer falschen Codeeingabe werden bei der Umsetzung festgelegt.

## Befehle

### Aktuell verfügbar

| Befehl | Funktion |
| :--- | :--- |
| `continue` | Zur nächsten Tür weitergehen; bei Bedarf einen vorhandenen Key verwenden |
| `return` | In den vorherigen Raum zurückgehen |
| `search` | Den Raum nach Coins und Items durchsuchen |
| `status` | HP, Coins sowie Flashlight-Batterie und Zustand anzeigen |
| `inventory` | Gefundene Gegenstände anzeigen |
| `flashlight on` | Vorhandene Flashlight einschalten |
| `flashlight off` | Flashlight ausschalten |
| `help` | Verfügbare Befehle anzeigen |
| `quit` | Spiel beenden |

### Für spätere Begegnungen vorgesehen

| Befehl | Funktion |
| :--- | :--- |
| `hide` | Vor Rush oder Figure verstecken |
| `spin` | Auf Screech reagieren |
| `left` / `right` | Während der Seek-Verfolgung eine Richtung wählen |
| `source` | Symbolreihenfolge des Library-Rätsels anzeigen |
| `findbook` | Einen Buchhinweis finden |
| `code` | Den PIN für den Ausgang eingeben |

Die Befehle für spätere Begegnungen sind noch nicht spielbar. Ihre genaue Eingabeform kann sich während der Entwicklung ändern.

## Projekt starten

Benötigt werden ein JDK, beispielsweise JDK 21, und für den Maven-Build Maven.

### In IntelliJ IDEA

1. Das Repository klonen.
2. Den Projektordner mit der `pom.xml` öffnen.
3. Das JDK auswählen und das Maven-Projekt laden.
4. Die Main-Methode in `src/main/java/ch/bbw/zork/Zork2.java` starten.

### Im Terminal

Im Hauptordner des Projekts:

```bash
mvn clean package
java -cp target/classes ch.bbw.zork.Zork2
```

## Aufbau des Codes

| Klasse | Aufgabe |
| :--- | :--- |
| `Zork2` | Einstiegspunkt des Programms |
| `Game` | Spielablauf, Befehlsverarbeitung und Raumwechsel |
| `Player` | HP, Coins, Inventar und Flashlight-Batterie |
| `Room` | Raumzustand, Ausgänge, Coins, Items und Schloss |
| `RoomLayouts` | ASCII-Grundrisse der zehn Raumtypen |
| `Item` | Einzelner Gegenstand mit Name und Typ |
| `ItemType` | Verfügbare Gegenstandstypen |
| `Parser` | Texteingaben in Befehle umwandeln |
| `Command` | Befehl mit optionalem zweitem Wort |
| `CommandWords` | Liste der gültigen Befehle |

## Dokumentation

- [Räumlichkeiten und ursprüngliche Skizze](docs/doors-raeumlichkeiten-doc.md)
- [Dokumentation dunkler Räume](docs/dunkle-raeume.md)
- [Räumlichkeiten-UML](diagrams/doors-raeumlichkeiten-uml.png)
- [Klassendiagramm](diagrams/doors-klassendiagramm.png)

Die ergänzenden Dokumente und Diagramme stammen teilweise aus früheren Planungsständen und werden an das 50-Türen-Konzept angepasst.

## Herkunft und KI-Unterstützung

Dieses Schulprojekt basiert auf dem bereitgestellten Zork-Template von Michael Kölling, überarbeitet von Rinaldo Lanza.

Spielidee und Gestaltung sind von Roblox DOORS inspiriert. Es handelt sich um ein unabhängiges Schulprojekt.

Unser Spielkonzept und die Entscheidungen zu den Mechaniken stammen von uns. KI unterstützt uns bei der Formulierung und Strukturierung der Dokumentation sowie beim Erklären und Besprechen von Code.

---

> **Öffne die nächste Tür. Aber achte darauf, was hinter dir passiert.**
