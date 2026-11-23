# 🚪 DOORS: Terminal Edition

> **50 Türen. Ein Hotel. Und du bist nicht allein.**

Ein textbasiertes Survival-Horrorspiel in Java, inspiriert von Roblox DOORS und entwickelt als Schulprojekt auf Basis des Zork-Templates.

Durchsuche Räume, sammle Gegenstände und achte auf Warnsignale. Überlebe Rush, reagiere auf Screech und entkomme Seek. Das endgültige Ziel: Figures Library bei Tür 50 erreichen und das Hotel verlassen.

---

## Projektstatus

Der aktuelle Entwicklungsstand umfasst die Räume **1–49**, zufällige Raumlayouts und Funde, Jeffs Shop sowie die Begegnungen mit Rush, Screech und Seek.

**Tür 50 mit Figure ist noch nicht spielbar.** In Raum 49 endet vorerst die Strecke, ohne dass ein Sieg ausgelöst wird.

### Umgesetzt

- [x] Spieler mit HP, Coins und Inventar
- [x] Räume 1–49 mit Vorwärts- und Rückwärtsverbindungen
- [x] Fester Einführungsraum mit Key und verschlossener Tür
- [x] Zufällige normale Raumlayouts und Rewards
- [x] ASCII-Anzeige beim Betreten und mit `look`
- [x] Dunkelheit und erschwerte Orientierung
- [x] Flashlight mit Ein-/Aus-Zustand und Batterieverbrauch
- [x] Verbrauchbare Batterien und Bandagen
- [x] Jeffs Shop in Raum 25
- [x] Lockpicks und einmaliger Crucifix-Schutz
- [x] Rush mit Warnung, Timer und Verstecken
- [x] Screech mit Reaktionsfrist und Schaden
- [x] Seek mit Cutscene, Guiding Light und Zeitlimit
- [x] Deathscreens für die drei Monster
- [x] Monster-Timer laufen auch ohne Eingabe weiter
- [x] Erste JUnit-Tests für Räume

### Noch geplant

- [ ] Figures Library bei Tür 50
- [ ] Bücher- und Symbolrätsel
- [ ] PIN-Eingabe und erfolgreicher Spielabschluss
- [ ] Weitere Tests und Balancing
- [ ] Ältere Diagramme und Detaildokumentationen aktualisieren

## Spielablauf

| Abschnitt | Inhalt |
| :--- | :--- |
| Raum 1 | Fester Einstieg: Key finden und die erste verschlossene Tür öffnen |
| Räume 2–4 | Zufällige Räume und Funde; Screech kann in dunklen Räumen auftreten |
| Ab Raum 5 | Zusätzlich kann Rush erscheinen |
| Raum 25 | Jeffs Shop: sicherer Raum zum Einkaufen |
| Räume 26–34 | Weitere Erkundung vor der Verfolgung |
| Räume 35–45 | Seek-Verfolgung mit Links-/Rechts-Entscheidungen |
| Raum 46 | Ziel der Seek-Flucht |
| Räume 46–49 | Normale Erkundung nach der Verfolgung |
| Raum 50 | Geplantes Figure-Finale |

Nach der Flucht vor Seek ist der Rückweg in die Verfolgungsstrecke versperrt. Innerhalb der normalen Abschnitte kann der Spieler mit `return` zurückgehen.

## Räume und ASCII-Maps

Die normalen Räume werden beim Spielstart aus acht Layouts ausgewählt.

| Layout | Umgebung |
| :--- | :--- |
| `BEDROOM` | Hotelzimmer mit Bett, Schrank und Schreibtisch |
| `LIBRARY` | Kleine Bibliothek mit Regalen und Lesetisch |
| `HALLWAY` | Langer Flur mit Seitentüren |
| `KITCHEN` | Küche mit Esstisch und Vorratsschränken |
| `STORAGE_ROOM` | Lagerraum mit Kisten, Fässern und Regalen |
| `BATHROOM` | Badezimmer mit Badewanne, Dusche und Schrank |
| `LOUNGE` | Aufenthaltsraum mit Sofa, Kamin und Klavier |
| `OFFICE` | Büro mit Schreibtisch, Aktenschränken und Safe |

Zusätzlich gibt es besondere Layouts:

| Layout | Einsatz |
| :--- | :--- |
| Jeffs Shop | Fester Verkaufsraum bei Tür 25, definiert in `JeffShop` |
| `SEEK_RUN` | Wiederverwendetes Layout für die Entscheidungen während Seek |
| `GRAND_LIBRARY` | Vorbereitete Map für das spätere Figure-Finale |

Die übrigen Layouts sind in [`RoomLayouts.java`](src/main/java/ch/bbw/zork/RoomLayouts.java) definiert.

**Ein Layout kann mehrfach vorkommen, jeder Raum besitzt aber seinen eigenen Zustand.** Coins, Items, Schlösser und Dunkelheit werden nicht zwischen Räumen geteilt.

Die ASCII-Map erscheint beim Betreten eines Raums – sowohl mit `continue` als auch mit `return`. Mit `look` kann sie erneut angezeigt werden.

## Zufällige Räume und Rewards

Raum 1 ist immer gleich:

- Schlafzimmer-Layout
- 10 Coins
- Ein Key
- Verschlossene Vorwärtstür
- Keine Dunkelheit

In normalen Räumen ab Raum 2 gelten folgende Wahrscheinlichkeiten:

| Eigenschaft oder Fund | Wahrscheinlichkeit |
| :--- | :--- |
| Dunkler Raum | 25 % |
| Verschlossene Vorwärtstür | 20 % |
| Coins vor Raum 25 | 70 % |
| Batterie | 20 % |
| Bandage | 20 % |

Die Würfe sind unabhängig. Ein Raum kann mehrere Funde enthalten oder leer sein.

Bei einem Coin-Fund erhält der Spieler **5–25 Coins**. Eine Flashlight liegt garantiert in einem zufälligen Raum zwischen **2 und 8**.

Eine verschlossene Vorwärtstür hat immer einen zugehörigen Key im aktuellen Raum. Der letzte vorläufige Raum erhält kein zusätzliches Schloss. Jeffs Shop und die Seek-Strecke enthalten keine zufälligen Rewards.

Layouts und Funde werden **einmal beim Spielstart** erzeugt. Wiederholtes Suchen oder Zurückgehen würfelt keine neuen Gegenstände aus.

## Spieler und Items

Der Spieler startet mit **100 HP und 0 Coins**. Bei 0 HP endet der Versuch.

| Item | Wirkung | Verwendung |
| :--- | :--- | :--- |
| Key | Öffnet eine verschlossene Vorwärtstür | Automatisch bei `continue` |
| Lockpick | Öffnet eine verschlossene Vorwärtstür ohne Key | Automatisch bei `continue` |
| Flashlight | Verhindert Fehlschläge durch Dunkelheit, wenn eingeschaltet und geladen | `flashlight on` / `flashlight off` |
| Batterie | Lädt bis zu 30 Prozentpunkte nach | `use battery` |
| Bandage | Heilt bis zu 15 HP | `use bandage` |
| Crucifix | Aktiviert Schutz gegen den nächsten Schaden | `use crucifix` |

### Verbrauchbare Gegenstände

- Key und Lockpick werden beim Öffnen einer verschlossenen Tür verbraucht.
- Ein vorhandener Lockpick hat Vorrang vor einem Key.
- Geöffnete Türen bleiben offen und verbrauchen später kein weiteres Item.
- Eine Bandage heilt maximal auf 100 HP. Bei 90 HP heilt sie beispielsweise 10 HP und wird vollständig verbraucht.
- Bei vollen HP bleibt eine Bandage im Inventar.
- Eine Batterie lädt maximal auf 100 %. Bei voller Batterie wird sie nicht verbraucht.
- Batterien können erst verwendet werden, wenn der Spieler eine Flashlight besitzt.

## Dunkelheit und Flashlight

Dunkle Räume werden durch eine Textmeldung angekündigt.

| Aktion | Ohne eingeschaltete, geladene Flashlight | Mit eingeschalteter, geladener Flashlight |
| :--- | :--- | :--- |
| `continue` | 20 % Chance, die Tür nicht zu finden | Kein Fehlschlag durch Dunkelheit |
| `search` | 25 % Chance, nichts zu finden | Kein Fehlschlag durch Dunkelheit |
| `return` | Keine zusätzliche Dunkelheitsprüfung | Keine zusätzliche Dunkelheitsprüfung |

Eine fehlgeschlagene Suche entfernt keine Coins oder Items. Ein Fehlschlag beim Weitergehen verbraucht keinen Key oder Lockpick.

Die Flashlight verhindert diese Orientierungsprobleme. Sie verhindert nicht automatisch das Erscheinen von Screech.

### Batterieverbrauch

- Eine neue Flashlight startet mit 100 %.
- Je 30 Sekunden eingeschalteter Zeit werden 10 Prozentpunkte verbraucht.
- Ausschalten pausiert den Verbrauch.
- Angebrochene Zeitintervalle bleiben gespeichert.
- Bei 0 % schaltet die Lampe aus.
- Nach dem Nachladen muss eine ausgeschaltete Lampe wieder eingeschaltet werden.
- `status` und `inventory` zeigen Ladestand und Ein-/Aus-Zustand.

Auch Wartezeit ohne Eingabe zählt. Der Verbrauch wird beim Abfragen oder Verwenden der Flashlight nachgerechnet.

## Jeffs Shop – Raum 25

Jeffs Shop ist immer hell und unverschlossen. Hier erscheinen keine Monster.

Mit `shop` wird das Angebot angezeigt. Einkäufe sind nur möglich, solange sich der Spieler in Raum 25 befindet.

| Produkt | Preis | Kaufbefehl |
| :--- | ---: | :--- |
| Crucifix | 250 Coins | `buy crucifix` |
| Lockpick | 50 Coins | `buy lockpick` |
| Flashlight | 100 Coins | `buy flashlight` |
| Bandage | 75 Coins | `buy bandage` |

Eine gekaufte Flashlight ersetzt die bisherige. Sie besitzt 100 % Batterie und ist zunächst ausgeschaltet.

Reichen die Coins nicht aus, wird weder Geld abgezogen noch ein Item vergeben.

**Ab dem ersten Erreichen von Raum 25 können keine weiteren Coins gesammelt werden.** Das gilt auch beim Zurückgehen in frühere Räume. Bereits gesammelte Coins bleiben erhalten und können bei Jeff ausgegeben werden.

Der Shop kann später erneut besucht werden, solange er noch erreichbar ist.

## Monster

Monsterchancen werden beim Betreten eines Raums geprüft, auch beim Zurückgehen. Wiederholtes `search` oder `look` löst keinen neuen Erscheinungswurf aus.

Pro Raumbetreten beginnt höchstens eine Begegnung. Ab Raum 5 wird zuerst Rush geprüft. Erscheint Rush nicht, kann in einem dunklen Raum Screech auftreten.

Während einer laufenden Rush- oder Screech-Begegnung ist der Raumwechsel gesperrt.

### Rush

**Ab Raum 5: 25 % Erscheinungswahrscheinlichkeit pro geeignetem Raumbetreten.**

Die Warnung steht unter der Raumausgabe:

```text
flicker flicker
```

Ab diesem Moment hat der Spieler **6 Sekunden**, um `hide` einzugeben.

- Ohne rechtzeitiges Verstecken verursacht Rush tödlichen Schaden.
- Im Versteck ist nur `unhide` erlaubt.
- Ein zu frühes `unhide` lässt den Spieler im Versteck bleiben.
- Nach Ablauf des Timers erscheint:

```text
A dark force just rushed through. I should probably move
```

Danach muss der Spieler selbst `unhide` eingeben, um weiterzuspielen.

Deathscreen:

```text
Oh, looks like rush got you.
Next time, try to hide quick enough..
```

### Screech

**In dunklen Räumen: 50 % Erscheinungswahrscheinlichkeit, wenn keine andere Begegnung beginnt.**

Die Warnung lautet:

```text
psst psst
```

Der Spieler muss innerhalb von **3 Sekunden** `peek` eingeben.

Bei rechtzeitiger Reaktion:

```text
What the hell was that? Whatever.
```

Bei einer zu späten Reaktion verliert er **40 HP**. Überlebt er den Biss, erscheint:

```text
OUCH. Stupid little crap. I better get out of here
```

Deathscreen:

```text
Oh, you died to screech.
Just peek at him and this scaredy cat will leave you alone..
```

### Seek

Beim Betreten von Raum 35 startet eine kurze, dreisekündige Text-Cutscene. **Erst danach beginnt das Zeitlimit von 30 Sekunden.**

Der Spieler muss die Entscheidungen in den Räumen **35–45** überwinden und Raum **46** erreichen.

| Regel | Verhalten |
| :--- | :--- |
| Steuerung | `left` oder `right` |
| Guiding Light | Ein `*` markiert die richtige Seite |
| Richtige Wahl | Nächster Raum; die richtige Seite wird neu gewürfelt |
| Falsche Wahl | 5 Sekunden Abzug; gleicher Raum und gleiche Markierung |
| Zeitlimit | 30 Sekunden insgesamt, ohne Cutscene |
| Ziel | Raum 46 erreichen |

Während der Verfolgung erscheinen weder Rush noch Screech. Normale Bewegung mit `continue` oder `return` ist währenddessen nicht möglich.

`look` zeigt die aktuelle Seek-Map erneut. Status, Inventar und Item-Verwendung sind weiterhin möglich, aber der Timer läuft dabei weiter.

Nach Erreichen von Raum 46 endet die Verfolgung. Der Rückweg in die Seek-Strecke ist versperrt.

Deathscreen:

```text
Seek caught you
The guiding light shows you where to go with a "*", trust it.
```

## Crucifix-Schutz

Mit `use crucifix` wird ein Crucifix aus dem Inventar verbraucht und der Schutz aktiviert.

- Der Schutz bleibt bis zum nächsten schädigenden Treffer aktiv.
- Er verhindert genau einen Treffer, auch wenn dieser sonst tödlich wäre.
- Danach ist der Schutz aufgebraucht.
- Eine erneute Aktivierung bei bereits aktivem Schutz verbraucht kein weiteres Crucifix.
- `status` zeigt an, ob der Schutz aktiv ist.

### Gegen Rush

```text
OH MAN. CLOSE ONE
Rush has been crucified.
```

### Gegen Screech

```text
GO AWAY!
Screech has been crucified
```

**Rush und Screech werden dadurch nicht dauerhaft entfernt.** In späteren Räumen können sie erneut erscheinen.

### Gegen Seek

Läuft der Seek-Timer bei aktivem Schutz ab, blockiert das Crucifix den Fangversuch:

```text
GOT HIM. But why is he not going down?
Seek has been slowed down for 10 Seconds. KEEP RUNNING!
```

Ab diesem Moment erhält der Spieler **10 weitere Sekunden** für die verbleibende Strecke. Seek verfolgt ihn weiter. Der Crucifix-Schutz ist danach verbraucht.

## Commands

| Befehl | Funktion |
| :--- | :--- |
| `continue` | Zur nächsten Tür weitergehen |
| `return` | In den vorherigen erreichbaren Raum zurückgehen |
| `look` | Aktuelle ASCII-Map erneut anzeigen |
| `search` | Coins und Items aus dem Raum sammeln |
| `status` | Raum, HP, Coins, Flashlight und Schutzstatus anzeigen |
| `inventory` | Inventar anzeigen |
| `flashlight on` | Flashlight einschalten |
| `flashlight off` | Flashlight ausschalten |
| `shop` | Jeffs Angebot in Raum 25 anzeigen |
| `buy crucifix` | Crucifix für 250 Coins kaufen |
| `buy lockpick` | Lockpick für 50 Coins kaufen |
| `buy flashlight` | Flashlight für 100 Coins kaufen |
| `buy bandage` | Bandage für 75 Coins kaufen |
| `use crucifix` | Einmaligen Schutz aktivieren |
| `use bandage` | Bis zu 15 HP heilen |
| `use battery` | Flashlight um bis zu 30 Prozentpunkte laden |
| `hide` | Während einer Rush-Warnung verstecken |
| `unhide` | Versteck nach Rushs Durchlauf verlassen |
| `peek` | Screech verscheuchen |
| `left` | Während Seek die linke Tür wählen |
| `right` | Während Seek die rechte Tür wählen |
| `help` | Befehlsübersicht anzeigen |
| `quit` | Spiel beenden; im Versteck gesperrt |

Die bisherigen Kurzbefehle `bandage` und `battery` funktionieren ebenfalls.

**Ein Command zählt erst nach dem Abschicken mit Enter.**

## Timer und Spielende

Die Befehlseingabe wird in einem eigenen Thread gelesen und über eine Queue an die Spielschleife übergeben.

Die Spielschleife prüft die Begegnungen regelmässig. Dadurch laufen Warnfristen, Cutscene und Seek-Timer auch dann weiter, wenn der Spieler nichts eingibt.

Nach einem tödlichen Treffer erscheint der passende Deathscreen und das Programm endet normal mit Exit-Code 0. Ein neuer Versuch wird durch erneutes Starten von `Zork2` begonnen.

## Tür 50 – Figure und die Grand Library

**Dieser Abschnitt ist geplant und noch nicht spielbar.**

Figure bewacht die grosse Bibliothek. Er kann nichts sehen, hört dafür besonders gut. Der Spieler soll Bücher finden und den sechsstelligen PIN des Great Gate entschlüsseln.

Geplanter Ablauf:

1. `source` zeigt sechs Symbole in der benötigten Reihenfolge.
2. `findbook` liefert Hinweise aus insgesamt acht Büchern.
3. Die Bücher ordnen Symbolen einzelne Ziffern zu.
4. Der Spieler setzt daraus den sechsstelligen PIN zusammen.
5. Mit `code` wird die Lösung eingegeben.
6. Der richtige PIN öffnet den Ausgang und beendet das Spiel erfolgreich.

Bei `findbook` ist eine 15-%-Chance vorgesehen, Figures Aufmerksamkeit zu wecken. Dann muss der Spieler rechtzeitig `hide` eingeben.

Reaktionsfristen und die Folgen falscher Codeeingaben werden bei der Umsetzung festgelegt.

## Projekt starten

Benötigt werden ein JDK, beispielsweise JDK 21, und für den Terminal-Build Maven.

### IntelliJ IDEA

1. Repository klonen und den Projektordner mit der `pom.xml` öffnen.
2. JDK auswählen und das Maven-Projekt laden.
3. Die Main-Methode in `src/main/java/ch/bbw/zork/Zork2.java` starten.

### Terminal

Im Hauptordner des Projekts:

```bash
mvn clean package
java -cp target/classes ch.bbw.zork.Zork2
```

## Aufbau des Codes

| Klasse | Aufgabe |
| :--- | :--- |
| `Zork2` | Einstiegspunkt des Programms |
| `Game` | Raumerzeugung, Spielschleife, Navigation und Befehlsverarbeitung |
| `Player` | HP, Coins, Inventar, Verbrauchsitems und Crucifix-Schutz |
| `Room` | Raumnummer, Layout, Ausgänge, Funde, Dunkelheit und Schloss |
| `RoomLayouts` | ASCII-Layouts für normale Räume, Seek und die Grand Library |
| `JeffShop` | Shop-Layout, Angebot, Preise und Käufe |
| `MonsterController` | Rush, Screech, Verstecken und Reaktionsfristen |
| `SeekController` | Cutscene, Guiding Light, Richtungswahl und Chase-Timer |
| `Item` | Gegenstand mit Name und Typ |
| `ItemType` | Verfügbare Gegenstandstypen |
| `Parser` | Texteingaben lesen und in Commands umwandeln |
| `Command` | Befehl mit optionalem zweitem Wort |
| `CommandWords` | Gültige Befehle |

## Dokumentation

- [Räumlichkeiten und ursprüngliche Skizze](docs/doors-raeumlichkeiten-doc.md)
- [Dokumentation dunkler Räume](docs/dunkle-raeume.md)
- [Räumlichkeiten-UML](diagrams/doors-raeumlichkeiten-uml.png)
- [Klassendiagramm](diagrams/doors-klassendiagramm.png)

Die ergänzenden Dokumente und Diagramme enthalten teilweise frühere Planungsstände. Die neuen Klassen und Mechaniken müssen dort noch nachgeführt werden.

## Herkunft und KI-Unterstützung

Dieses Schulprojekt basiert auf dem bereitgestellten Zork-Template von Michael Kölling, überarbeitet von Rinaldo Lanza.

Die Spielgestaltung ist von Roblox DOORS inspiriert. Es handelt sich um ein unabhängiges Schulprojekt.

Unser Konzept, die gewünschten Mechaniken und die Entscheidungen zur Gestaltung stammen von uns. KI unterstützt bei der Dokumentation, bei Erklärungen sowie beim Entwerfen und Überarbeiten von Code. Wir integrieren die Vorschläge und testen das Spiel schrittweise.

---

> **Wenn das Licht flackert, lies nicht erst die README.**
