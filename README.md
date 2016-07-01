# VcfCreator

Dit programma converteert route-bestanden in verschillende formaten naar Vcf-bestanden, 
zodat deze kunnen worden ingelezen in bijvoorbeeld een Volkswagen RNS510 navigatiesysteem.

This program converts trail files in various formats to Vcf files,
so that they can be read into a Volkswagen RNS510 navigation system.
See the included Installation_en.txt for further instructions.

Dieses Programm konvertiert Strecke-Dateien in verschiedenen Formaten zur Vcf Dateien,
so dass die in eine Volkswagen RNS510 Navigationssystem gelesen werden können.
Siehe die mitgelieferte Installations_de.txt für weitere Anweisungen.

Ondersteunde bestandsformaten:
- rte OziExplorer Route File Version 1.0 (en nog een ander bestand wat als extentie .rte had)
- gpx GPS Exchange Format versie 1.0
- itn TomTom itinerary files
- kml Google Earth, Google Maps Keyhole Markup Language versie 2.1
- trp TravelPilot? Data Version:1.9.1.1

Om het programma te gebruiken gaat u als volgt te werk:
- unzip het bestand (Dat heeft u waarschijnlijk net gedaan om dit te kunnen lezen)
- Voer VcfCreator.bat uit. Hiervoor moet Java geinstalleed zijn, als dat nog niet het geval is
  kunt u dit downloaden van http://www.java.com
- Selecteer een invoer bestand.
- Selecteer een uitvoer map. Als deze map nog niet bestaat zal hij worden aangemaakt.
  In deze uitvoer map zal een submap destinations worden aangemaakt.
- Voer een Route-prefix in. Deze zorgt ervoor dat u de verschillende routes uit elkaar kunt houden
  als deze zijn opgesplitst in allemaal "losse" routepunten.
- Kies Converteer
- Als het bestand geconverteerd kan worden zal worden aagegeven hoeveel VCards er aangemaakt zijn
  in de uitvoer map/destinations.

De destinations gebruiken in uw navigatiesysteem.
Volkswagen RNS510:
- Kopieer de map destinations naar een SD-kaartje. Kopieer de hele map, niet alleen de inhoud.
- Start Nav
- Kies RitModus
- Selecteer de doelen vanaf de SD-kaart
 
