# 🚪 DOORS: Terminal Edition

### 100 Türen. Ein Ausweg. Keine zweite Chance.

**Solo-Storygame · Survival-Horror · Textbefehle · Rätsel**

Ein Schulprojekt, inspiriert von Roblox DOORS.


> Das Licht flackert. Hinter dir wird es laut. Vor dir liegt eine verschlossene Tür.
> Du hast nur wenige Sekunden, um den richtigen Befehl einzugeben.
> **Was tust du?**

## 🎮 Unsere Projektidee

Wir entwickeln ein textbasiertes Survival-Horrorspiel für das Terminal. Der Spieler muss 100 Türen überleben, Räume erkunden, Gegenstände sammeln und verschiedenen Monstern entkommen. Die Steuerung erfolgt über Textbefehle. Hinweise im Terminal und ASCII-Darstellungen helfen dabei, Gefahren zu erkennen und Rätsel zu lösen.

Unser Ziel ist es, die Spannung von DOORS mit einfachen Mitteln ins Terminal zu bringen. Aufmerksamkeit, schnelle Reaktionen und kluger Umgang mit Items entscheiden darüber, ob der Spieler den Aufzug erreicht.

**Projektstatus:** Konzeptphase. Diese README beschreibt die geplanten Funktionen und Spielregeln.

## ❤️ Spieler und Ressourcen

| Ressource | Bedeutung |
| :--- | :--- |
| HP | Lebenspunkte des Spielers. Bei 0 HP endet der Versuch. |
| Coins | Können in Räumen gefunden und bei Jeff ausgegeben werden. |
| Items | Helfen bei der Erkundung, heilen oder schützen vor Schaden. |
| Schlüssel | Werden benötigt, wenn eine Tür verschlossen ist. |

Der normale Spielablauf besteht darin, einen Raum zu untersuchen, auf Warnsignale zu achten und anschliessend die nächste Tür zu erreichen. Manche Begegnungen unterbrechen diesen Ablauf mit Rätseln oder Aufgaben unter Zeitdruck.

## 🗺️ Der Weg durch die 100 Türen

| Abschnitt | Geplantes Ereignis |
| :--- | :--- |
| Einstieg bis Tür 10 | Einfache Räume zum Kennenlernen der Befehle und Sammeln von Coins. |
| Ab Tür 10 | Rush kann erscheinen. Flackerndes Licht kündigt ihn an. |
| Tür 35 | Die erste Verfolgung durch Seek beginnt. Ziel ist Tür 46. |
| Tür 50 | Figure bewacht ein Rätsel mit Büchern und einem fünfstelligen Code. |
| Direkt nach Tür 50 | Jeff bietet in seinem Shop hilfreiche Items an. |
| Tür 75 | Seek kehrt zurück. Diesmal führt die Flucht durch zwölf Türen. |
| Tür 100 | Zehn Batterien finden, Figure ausweichen und mit dem Aufzug entkommen. |

## 🔦 Räume und Dunkelheit

Mit `search` untersucht der Spieler einen Raum nach Coins und gegebenenfalls einem Schlüssel. Mit `continue` versucht er, zur nächsten Tür zu gelangen. Eine verschlossene Tür erfordert zuerst den passenden Schlüssel.

Dunkle Räume werden durch einen Texthinweis angekündigt. Eine funktionierende Flashlight erleichtert die Orientierung und die Suche.

Ohne funktionierende Flashlight gelten in dunklen Räumen folgende Regeln:

- Bei `continue` besteht eine **20 % Chance**, die Tür nicht zu finden und im aktuellen Raum zu bleiben.
- Auch mit `search` wird es schwieriger, Coins oder einen Schlüssel zu finden. Die genaue Wahrscheinlichkeit ist noch offen.

Solange sich der Spieler in einem dunklen Raum befindet, besteht **jede Sekunde eine 5 % Chance**, dass Screech erscheint.

## 👁️ Monster und Begegnungen

### Rush: Achte auf das Licht

Ab Tür 10 besteht die Möglichkeit, dass Rush sich nähert. Das Licht beginnt zu flackern. Der Spieler muss den Hinweis erkennen und sich rechtzeitig mit `hide` verstecken.

### Screech: Dreh dich um

Screech kann in dunklen Räumen erscheinen. Sobald das Terminal seine Anwesenheit ankündigt, muss der Spieler schnell `spin` eingeben. Reagiert er zu spät, verliert er **40 HP**.

### Seek: Folge dem Guiding Light

Bei Tür 35 beginnt eine automatische Flucht vor Seek. Der Spieler entscheidet in jedem Raum, ob er nach links oder rechts läuft. Die Räume werden als ASCII-Modell mit zwei Türen dargestellt. Ein Stern `*` markiert die richtige Tür und stellt das Guiding Light dar.

| Regel | Erste Verfolgung | Zweite Verfolgung |
| :--- | :--- | :--- |
| Start | Tür 35 | Tür 75 |
| Strecke | Etwa zehn Räume, Ziel Tür 46 | Zwölf Türen |
| Zeitlimit | 45 Sekunden insgesamt | 45 Sekunden insgesamt |
| Falsche Richtung | 5 Sekunden Zeitabzug | 5 Sekunden Zeitabzug |
| Zeit abgelaufen | Seek erwischt den Spieler. Der Versuch endet. | Seek erwischt den Spieler. Der Versuch endet. |

Die Herausforderung besteht darin, die Markierung schnell zu erkennen und die passende Richtung einzugeben.

### Figure: Ein Geräusch kann reichen

Figure sieht nichts, hört dafür aber besonders gut. Wenn er den Spieler hört, muss dieser sofort `hide` eingeben. Gelingt das nicht rechtzeitig, endet der Versuch sofort.

#### Tür 50: Das Bücher-Rätsel

Der Spieler muss einen **fünfstelligen Zahlencode** entschlüsseln, um die Ausgangstür zu öffnen.

1. `source` zeigt eine Vorlage mit fünf Symbolen in einer festen Reihenfolge. Diese Reihenfolge bestimmt die Stellen des Codes.
2. `findbook` zeigt eines von insgesamt acht Büchern. Jedes Buch ordnet einem Symbol eine Ziffer zu.
3. Der Spieler sammelt die benötigten Zuordnungen und setzt die Ziffern in der Reihenfolge der Vorlage zusammen.
4. Mit `code` startet er die Eingabe des fünfstelligen Codes.
5. Ist der Code richtig, wird die Ausgangstür freigeschaltet.

**Das Risiko:** Jeder Aufruf von `findbook` hat eine **15 % Chance**, Figures Aufmerksamkeit zu wecken. Dann muss sich der Spieler schnell verstecken.

Beispiel für die Entschlüsselung, mit frei gewählten Symbolen und Werten:

| Position in der Vorlage | Symbol | Ziffer aus dem passenden Buch |
| :---: | :---: | :---: |
| 1 | ★ | 7 |
| 2 | ◆ | 2 |
| 3 | ● | 9 |
| 4 | ▲ | 4 |
| 5 | ■ | 1 |

Aus dieser Vorlage ergibt sich der Code **72941**. Die übrigen Bücher müssen für diesen Beispielcode nicht verwendet werden.

## 🛒 Jeffs Shop und Items

Nach Tür 50 erreicht der Spieler Jeffs Shop. Hier kann er seine gesammelten Coins für verschiedene Hilfsmittel ausgeben.

| Item | Geplante Wirkung |
| :--- | :--- |
| Bandaids | Stellen Lebenspunkte wieder her. |
| Flashlight | Hilft in dunklen Räumen. Bei leerer Batterie kann bei Jeff eine neue gekauft werden. |
| Crucifix | Wird mit `crucifix` aktiviert und schützt einmalig vor dem nächsten Schaden. Danach ist der Schutz verbraucht. |

Preise, Heilwerte und die Batterielaufzeit der Flashlight werden während der Entwicklung festgelegt.

## 🔋 Tür 100: Die letzte Flucht

Im letzten Bereich trifft der Spieler erneut auf Figure. Um zu entkommen, muss er **alle zehn versteckten Batterien** finden und zum Aufzug bringen.

Die verfügbaren Bereiche sind `west`, `east`, `north`, `south` und `elevator`. In den Räumen können Batterien versteckt sein. Mit `search` wird der aktuelle Raum durchsucht.

Jede Suche hat eine **20 % Chance**, dass Figure den Spieler hört. In diesem Fall ist erneut eine schnelle Reaktion mit `hide` erforderlich.

Sobald der Spieler alle zehn Batterien besitzt, kann er zum Bereich `elevator` gehen und entkommen. Damit ist das Spiel gewonnen.

## ⌨️ Geplante Befehle

| Befehl | Funktion | Einsatz |
| :--- | :--- | :--- |
| `search` | Den aktuellen Raum durchsuchen. | Coins und Schlüssel finden, im Finale Batterien suchen. |
| `continue` | Versuchen, zur nächsten Tür zu gelangen. | Normale Erkundung. |
| `hide` | Sich vor einer Gefahr verstecken. | Bei Rush oder wenn Figure aufmerksam wird. |
| `spin` | Sich schnell umdrehen. | Auf Screech reagieren. |
| `source` | Die Vorlage mit fünf geordneten Symbolen anzeigen. | Figurenrätsel bei Tür 50. |
| `findbook` | Eines der acht Bücher mit einer Symbol-Ziffer-Zuordnung anzeigen. | Figurenrätsel bei Tür 50. |
| `code` | Die Eingabe des fünfstelligen Zahlencodes starten. | Ausgang bei Tür 50 freischalten. |
| `crucifix` | Ein vorhandenes Crucifix aktivieren. | Einmaligen Schutz vorbereiten. |

Für Links/Rechts-Entscheidungen, Raumwechsel im Finale, Einkäufe und die Nutzung weiterer Items wird die genaue Befehlssyntax noch festgelegt.

## 🛠️ Geplante Umsetzung

Die Umsetzung verbindet eine normale Befehlseingabe mit zeitabhängigen Ereignissen. Besonders wichtig ist, dass Timer und Monsterereignisse auch dann weiterlaufen, wenn der Spieler gerade keinen Befehl eingibt.

Unsere zentralen Entwicklungsaufgaben sind:

- Spielerstatus mit HP, Coins und Inventar verwalten.
- Räume, verschlossene Türen und zufällige Funde umsetzen.
- Monster mit Warnsignalen und passenden Reaktionen einbauen.
- Zeitlimits und Zufallsereignisse mit der Terminaleingabe verbinden.
- Seek-Räume mit ASCII darstellen.
- Das Bücher-Rätsel, Jeffs Shop und das Aufzugfinale umsetzen.

Programmiersprache, Projektstruktur und Startanleitung werden ergänzt, sobald die technische Grundlage feststeht.

## 📌 Noch offene Detailregeln

Die Kernidee steht. Folgende Punkte werden bei der Umsetzung konkretisiert:

- Start-HP, Itempreise, Heilwerte und Flashlight-Laufzeit.
- Häufigkeit von Rush und die genauen Reaktionszeiten für `hide` und `spin`.
- Ob Tür 10 noch zum sicheren Einstieg gehört oder bereits Rush auslösen kann.
- Die genaue Zählung der ersten Seek-Strecke zwischen Tür 35 und Tür 46.
- Verhalten bei wiederholtem `findbook` und bei einer falschen Codeeingabe.
- Wirkung des Crucifix bei sofort tödlichen Begegnungen und abgelaufenen Seek-Timern.
- Ob andere Zufallsmonster während besonderer Begegnungen pausieren.

**Öffne die nächste Tür. Aber achte darauf, was hinter dir passiert.**

*Ja, dieses Text ist KI generiert, die Ideen und Commands habe aber ich selber entdeckt. KI dient hier, um meine Ideen formulieren zu können in einem tollen MD Format!*
