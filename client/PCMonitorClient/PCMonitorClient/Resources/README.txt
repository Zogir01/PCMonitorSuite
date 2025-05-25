PCMonitor � Monitor sprz�tu dla Windows
---------------------------------------

OPIS:
-------
PCMonitor to lekki program do monitorowania komponent�w komputera (CPU, GPU, RAM itd.).
Dzia�a w tle i umo�liwia podgl�d danych z czujnik�w oraz ich wysy�anie do serwera HTTP w formacie JSON.

URUCHAMIANIE:
-------------
1. Uruchom PCMonitor.exe
2. Ikona programu pojawi si� w zasobniku systemowym (tray)
3. Kliknij PPM na ikonie, aby otworzy� menu

FUNKCJE:
--------
- Start monitoring � rozpoczyna zbieranie danych co 10 sekund
- Stop monitoring � zatrzymuje zbieranie danych
- Poka� logi � otwiera plik PCMonitor.log
- Wyczy�� logi � czy�ci zawarto�� log�w
- Poka� dane diagnostyczne � otwiera okno z aktualnymi danymi
- Zamknij � zamyka program

PODGL�D DANYCH:
---------------
Po klikni�ciu "Poka� dane diagnostyczne", pojawi si� okno z tabel� zawieraj�c�:
� Nazw� sprz�tu
� Nazw� czujnika
� Typ warto�ci (temperatura, u�ycie, taktowanie)
� Aktualn� warto��

WYMAGANIA:
----------
- System Windows 10 lub nowszy
- .NET Framework lub .NET 6+
- Po��czenie z Internetem (je�li wysy�asz dane na serwer)

DODATKI:
--------
� Plik `PCMonitor.ico` musi znajdowa� si� w katalogu z programem, aby ikona tray dzia�a�a poprawnie
� Logi zapisywane s� w `PCMonitor.log`

LICENCJA:
---------
