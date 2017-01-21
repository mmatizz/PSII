##Podstawy sztucznej inteligencji 

##Projekt: Rozpoznawanie liter

##Wejścia/Wyjścia: 
•	Plik graficzny o wymiarach 5x7 – 35 pikseli
•	Każdy plik graficzny będzie przedstawiał daną literę przez wektory, w ktorych beda -5 i -5 w zaleznosci od tego czy piksel jest biały (-5) czy czarny (5).
•	Wyjście: wektor wag neurona zwycięzcy z podaną literą

##OPIS:
•	Program służy do rozpoznywania liter

•	Po uruchomieniu programu ukazuje się nam interfejs graficzny, na którym jest:

    • pole do rysowania
    • lista, na której wyświetlane są dodane literi
    • przyciski
        - DODAJ - dodawanie litery na listę
        - WYCZYŚĆ - czyszczenie pola do rysowania
        - ROZPOZNAJ - rozponawanie narysowanej litery
        - USUŃ - usuwanie litery z listy
        - TRENUJ - rozpoczęcie uczenia sieci dodanych liter do listy
        

•	Po narysowaniu danej litery w programie dodaje się ją do listy za pomocą przycisku DODAJ

•	Każda litera po dodaniu do listy jest przekształcana do mniejszego rozmiaru pikseli (downsampling) o rozmiarze 5*7, gdzie  

•	Po downsamplingu do rozmiaru 35 pikseli, przekształca się je na wektor, który następnie wysyłany jest do metody z nauką sieci

•	W sieci każda litera odpowiada jednemu neuronowi, który na starcie ma wylosowane wagi

•	Sieć wykorzystuje regułe Kohonena, zgodnie z którą neuron, który ma największą wartość wag dla danego wektora, wejścia zostaje zwycięzcą (WTA)

•	Dodatkowo przy każdej iteracji nauki sieci obliczany jest błąd, na którego podstawie program dalej uczy sieć lub zaprzestaje nauki, gdy sieć już jest nauczona

•	Po nauczeniu sieci można sprawdzić jej działanie za pomocą narysowania litery testującej i wciśnięcia przycisku Rozpoznaj

•	Po prawidłowym nauczeniu sieci program powinien zwrócić komunikat o id neuronu, który został zwycięzcą oraz literę do niego przypisaną.



