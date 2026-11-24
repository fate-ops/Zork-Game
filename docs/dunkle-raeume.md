# Dunkle Räume und Taschenlampe

Dunkelheit kann die Suche nach Gegenständen oder den Weg durch die nächste Tür erschweren. Eine **eingeschaltete, geladene Taschenlampe** verhindert diese Fehlschläge.

## Regeln

| Aktion im dunklen Raum | Ohne eingeschaltete, geladene Lampe | Mit eingeschalteter, geladener Lampe |
| --- | --- | --- |
| `search` | 25 % Chance auf einen Fehlschlag durch Dunkelheit. Münzen und Gegenstände bleiben im Raum. | Die Suche scheitert nicht an der Dunkelheit. Das Tragelimit gilt weiterhin. |
| `continue` | 20 % Chance, die nächste Tür im Dunkeln nicht zu finden. Du bleibst im Raum. | Du findest die Tür. Ein verschlossener Ausgang braucht weiterhin einen Schlüssel oder Lockpick. |
| `back` / `return` | Keine Dunkelheitsprüfung. | Keine Dunkelheitsprüfung. |

In hellen Räumen gibt es keine Fehlschläge durch Dunkelheit. Aktive Monster oder gesperrte Rückwege können die Bewegung weiterhin blockieren.

## Wo Dunkelheit und Taschenlampe vorkommen

- Normale Räume zwischen Tür 2 und 49 haben eine **25-%-Chance**, dunkel zu sein. Jeffs Shop und die Seek-Strecke sind davon ausgenommen.
- Die Taschenlampe liegt in **einem zufällig ausgewählten Raum zwischen Tür 2 und 8**. Dieser Raum kann hell oder dunkel sein.
- Beim Betreten eines dunklen Raums erscheint «It is very dark in here. Use a charged flashlight.»
- In dunklen Räumen kann Screech auftauchen. Bei `psst psst` hast du drei Sekunden für `peek`, bevor er 40 Schaden verursacht. Eine eingeschaltete Lampe verhindert Fehlschläge durch Dunkelheit, während Screech weiterhin auftreten kann.

## Taschenlampe bedienen

Mit `flashlight on` schaltest du die Lampe ein und mit `flashlight off` wieder aus. Pro 30 Sekunden eingeschalteter Zeit verliert sie 10 Prozentpunkte Ladung. Bei 0 % geht sie aus. `use battery` lädt sie um bis zu 30 Prozentpunkte auf.

Mit `drop flashlight` legst du die Lampe ab und sie schaltet sich aus. Mit `search` kannst du sie später mit demselben Ladestand wieder aufnehmen.

## Umsetzung im Code

- `Room` speichert den Zustand in `dark`. `makeDark()` setzt ihn, `isDark()` liest ihn und `longDescription()` ergänzt den Hinweis.
- `Game.failsInDarkness(...)` prüft, ob der Raum dunkel ist und keine eingeschaltete, geladene Taschenlampe zur Verfügung steht. Dann entscheidet der Zufall mit `SEARCH_FAIL_CHANCE` oder `CONTINUE_FAIL_CHANCE`.
- `searchRoom()` prüft die Dunkelheit vor `collectCoins()` und `collectItems()`. So bleiben die Funde erhalten, wenn die Suche im Dunkeln scheitert.
- Beim Weitergehen erfolgt die Dunkelheitsprüfung vor dem Entriegeln. So wird bei einem Fehlschlag weder ein Schlüssel noch ein Lockpick verbraucht.
- `Flashlight` erbt von `Item` und verwaltet den Ladestand und den Ein/Aus-Zustand pro Lampe. `Player` greift auf die Lampe im Inventar zu.

## Tests

Die JUnit-Tests in `RoomTests.java` prüfen unter anderem den anfänglich hellen Zustand, den Hinweis in der Raumbeschreibung sowie die Unabhängigkeit von Dunkelheit, Gegenständen und Schloss. `GameTests.java` prüft Spielbefehle über simulierte Konsoleneingaben. Die zufälligen Fehlschläge durch Dunkelheit werden in diesen Tests nicht gezielt ausgelöst.

Das Verhalten lässt sich im Spiel prüfen, indem du einen dunklen Raum zuerst ohne eingeschaltete Lampe und danach mit einer geladenen, eingeschalteten Lampe betrittst und `search` oder `continue` ausprobierst. Ein Versuch ohne Licht kann auch erfolgreich sein.