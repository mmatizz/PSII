% LICZBA AKTYWNOSCI PLAM SLONECZNYCH W ZALEZNOSCI OD ROKU
load sunspots.txt
figure(1)
plot(sunspots(:,1), sunspots(:,2), 'k-')
xlabel('Kolejne lata')
ylabel('Aktywnoœæ plam slonecznych')

N = length(sunspots);
P = [sunspots(1:N-2,2)'; sunspots(2:N-1,2)'];
T = sunspots(3:N,2)';
%TWORZY PLASZCZYZNE z 2 POPRZEDNICH LAT
figure(2)
plot3(P(1,:), P(2,:), T, 'bo');
hold on
title('zal. liczby plam w bie¿¹cym roku od dwóch lat poprzednich');
grid on;
xlabel('rok-1');
ylabel('rok-2');
zlabel('teraz');

Pu = P(:,1:200);
Tu = T(:,1:200);
%Utworzono neuron i obliczono optymalne wartosci wspolczynnikow wagowych
%neuronu
neuron = newlind(Pu,Tu);

disp('Wspolczynniki wagowe neuronu')
disp( neuron.IW{1} )
disp( neuron.b{1} )
%Wspolczynniki wagowe neurony oraz zmienna progowa
w1 = neuron.IW{1}(1);
w2 = neuron.IW{1}(2);
b = neuron.b{1};

%sim - utworzenie wektoru odpowiedzi neuronu
%prognozowane wartosci aktywnosci na przestrzeni 200 lat
%nalozenie wykresow 
Tsu = sim(neuron,Pu);
figure(3);
plot(sunspots(1:200,1),sunspots(1:200,2),'r-*')
hold on
plot(sunspots(1:200,1),Tsu,'b-o')
hold on
xlabel('lata');
ylabel('liczba plam');
legend('rzeczywiste', 'prognozowane');


%Tworzymy i symulujemy now¹ sieæ, tym razem ju¿ na wszystkich danych
net = newlind(P,T);
Pw = P(:,1:N-2);
Tw = T(:,1:N-2);
Ts = newlind(Pw,Tw);
disp('wspó³czynniki wagowe neuronu:');
disp(Ts.IW{1});
disp(Ts.b{1});
w1 = Ts.IW{1}(1);
w2 =Ts.IW{1}(2);
b = Ts.b{1};
Tsw = sim(Ts,Pw);
%Wyswietlamy wykres weryfikuj¹cy predykcjê w latach 1300-2000 (rysunek4)
figure(4);
plot(sunspots(1:N-2,1),sunspots(1:N-2,2),'r-*')
hold on
plot(sunspots(1:N-2,1),Tsw,'b-o')
hold on
xlabel('lata');
ylabel('liczba plam');
legend('rzeczywiste', 'prognozowane');

e = Tsw - T;
figure(5);
plot(sunspots(3:N,1), e,'r-o');
title('wykres bledu predykcji');
xlabel('lata')
ylabel('blad predykcji')
