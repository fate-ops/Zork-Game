# Dunkle Räume & Taschenlampe

Erweiterung von Zork/DOORS: Einige Räume sind dunkel. Ohne Taschenlampe
(Flashlight) können Suchen und Weitergehen fehlschlagen.

## Regeln

| Situation | ohne Flashlight | mit Flashlight |
|---|---|---|
| `search` im dunklen Raum | 25 % Chance: nichts gefunden, man muss nochmals suchen | findet zuverlässig |
| `continue` im dunklen Raum | 20 % Chance: Tür nicht gefunden, man bleibt im Raum | geht zuverlässig weiter |
| heller Raum | normal | normal |

## Spielablauf

- Raum 3 und Raum 4 sind dunkel.
- Die Flashlight liegt in **Raum 3**. Man muss sie also im Dunkeln suchen.
- Betritt man einen dunklen Raum, erscheint: *"It is very dark in here. A flashlight would help."*

## Umsetzung

### `Room`
- Neues Attribut `private boolean dark` (Standard `false`).
- `isDark()` gibt den Zustand zurück, `makeDark()` macht den Raum dunkel.
- Gleiches Muster wie `locked` / `lock()` / `unlock()`.
- `longDescription()` hängt bei dunklen Räumen einen Hinweis an. Der Raum ist
  selbst für seine Beschreibung verantwortlich.

### `Game`
- Neues Attribut `private Random random`, erzeugt im Konstruktor.
- Konstanten `SEARCH_FAIL_CHANCE = 25` und `CONTINUE_FAIL_CHANCE = 20`.
- Neue Methode `failsInDarkness(int failChancePercent)`:
    1. Raum hell → `false`
    2. Spieler hat `ItemType.FLASHLIGHT` → `false`
    3. sonst `random.nextInt(100) < failChancePercent`
       (`nextInt(100)` liefert 0–99, bei 25 sind das genau 25 von 100 Werten)
- Eine Methode für beide Fälle, nur die Wahrscheinlichkeit ist verschieden.
  So gibt es keinen doppelten Code.

### Wo die Prüfung steht (wichtige Entscheidungen)

- **`searchRoom()`**: ganz am Anfang, **vor** `collectCoins()` und `collectItems()`.
  Diese Methoden leeren den Raum. Stünde die Prüfung danach, wären Coins und
  Items bei einer Fehlsuche verloren.
- **`continueToNextRoom()`**: ganz am Anfang, **vor** `unlockCurrentDoor()`.
  Diese Methode verbraucht den Schlüssel. So geht der Schlüssel nicht verloren,
  wenn man die Tür im Dunkeln nicht findet.
- **`return`** (zurückgehen) ist von der Dunkelheit **nicht** betroffen.
  Den Weg zurück kennt man bereits.

## Tests

`src/test/java/ch/bbw/zork/RoomTests.java` (6 Tests, JUnit 4):

| Test | prüft |
|---|---|
| `newRoomIsNotDark` | neuer Raum ist hell |
| `makeDarkMakesRoomDark` | `makeDark()` macht den Raum dunkel |
| `darkRoomDescriptionWarnsAboutDarkness` | Beschreibung enthält den Hinweis |
| `lightRoomDescriptionHasNoDarknessWarning` | heller Raum ohne Hinweis |
| `darkRoomStillContainsItems` | Dunkelheit löscht keine Items |
| `makeDarkDoesNotLockRoom` | dunkel und verschlossen sind unabhängig |

Der Zufall in `Game` wird nicht automatisch getestet, weil `Game` von
`System.in` liest. Geprüft wurde er durch Spielen: `search` in Raum 3 schlägt
manchmal fehl, mit Flashlight nie.