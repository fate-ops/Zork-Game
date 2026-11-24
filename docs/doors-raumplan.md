# DOORS: Terminal Edition - Raumplan

Die 50 Raeume sind eine lineare Kette (`continue` = vorwaerts, `back` = zurueck).

```mermaid
flowchart LR
    R1["Raum 1<br/>Schlafzimmer<br/>verschlossen, Key im Raum"]
    R2["Raeume 2-24<br/>zufaelliges Layout<br/>Muenzen, Dunkelheit,<br/>Schloss, Items"]
    R25["Raum 25<br/>Jeffs Shop<br/>keine Muenzen mehr ab hier"]
    R26["Raeume 26-34<br/>zufaelliges Layout<br/>Dunkelheit, Schloss, Items"]
    R35["Raeume 35-45<br/>Seek-Verfolgung<br/>left / right, Zeitlimit"]
    R46["Raeume 46-49<br/>zufaelliges Layout<br/>Dunkelheit, Schloss, Items"]
    R50["Raum 50<br/>Grand Library<br/>6-stelliger PIN"]

    R1 --> R2 --> R25 --> R26 --> R35 --> R46 --> R50
```

## Regeln pro Bereich

| Raeume | Besonderheit |
|--------|--------------|
| 1 | Schlafzimmer, Tuer verschlossen, Key liegt im Raum |
| 2-8 | In einem dieser Raeume liegt die Taschenlampe |
| 2-24 | Muenzen (70 %), danach keine mehr |
| 25 | Jeffs Shop (`shop`, `buy`) |
| 2-49 (ohne Shop und Seek) | 25 % dunkel, 20 % verschlossen (Key im selben Raum), je 20 % Batterie / Pflaster |
| 35-45 | Seek-Verfolgung, keine Dunkelheit, Schloesser oder Items |
| ab Seek-Ende | Weg zurueck in die Seek-Raeume ist eingestuerzt |
| 50 | Grand Library mit `source`, `findbook`, `notes`, `code`; kein Rush / Screech |

## Items und Gewicht

Maximales Tragegewicht: 15. Taschenlampe 3, Batterie 2, Kruzifix 2, alle anderen 1.
`drop <item>` legt Gegenstaende im aktuellen Raum ab, `map` listet alle Raeume mit ihren Items.
