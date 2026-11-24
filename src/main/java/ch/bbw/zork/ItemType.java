package ch.bbw.zork;

/**
 * Die verschiedenen Arten von Gegenständen im Spiel. Jeder {@link Item}
 * besitzt genau einen dieser Typen.
 */
public enum ItemType {
    /** Öffnet eine verschlossene Vorwärtstür und wird dabei verbraucht. */
    KEY,
    /** Öffnet eine verschlossene Vorwärtstür ohne Key und wird verbraucht. */
    LOCKPICK,
    /** Bandage, die bis zu 15 HP heilt. */
    BANDAID,
    /** Taschenlampe, die Fehlschläge in dunklen Räumen verhindert. */
    FLASHLIGHT,
    /** Schützt einmalig vor dem nächsten schädigenden Treffer. */
    CRUCIFIX,
    /** Lädt die Batterie der Flashlight nach. */
    BATTERY
}