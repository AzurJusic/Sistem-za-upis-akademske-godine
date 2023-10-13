# Sistem-za-upis-akademske-godine
Projekat se odnosi na sistem za upis akademske godine kreiran u Java programskom jeziku.  Korištena je opcija JPA Project u Eclipsu za kreiranje ovog projekta. Korištene su biblioteke za EclipseLink i embedded derbi bazu(10.2) koje su priložene.  Biblioteke je potrebno dodati u classpath naseg JPA projekta. Pri kreiranju JPA projekta u Eclipsu potrebno je konfigurisati bazu i korsititi derbi jar biblioteku koja je priložena.  Nakon što je projekat kreiran potrebno je u src ubaciti pakete models,GUI,runners,services koji su prilozeni te  podesiti persistance.xml fajl. 

Kada je baza povezana i kada su svi fajlovi ubaceni, sinhronizirati class list na persistence.xml fajlu te ici na opciju generate tables from entities.



Koristeno:
Java 11
EclipseLink 2.5 
JPA version 2.1

Napomene: 
U runners paketu pokrenuti ApplicationClient.java kako bi imali pocetne podatke u bazi. 
U GUI paketu je glavni fajl za pokretanje programa.
Kako bi se pristupilo profilu prodekana za nastavu potrebno je u administrator GUI dodati novog profesora i oznaciti ga kao prodekana za nastavu.
Pri kreiranju nove akademske godine za profil prodekana za nastavu, unos nove akademske godine treba biti u formatu ( 13.10.2023 16:00). 
Student koji prvi puta upisuje prvu godinu studija je vec upisan na predmete i nema pravo da bira predmete. To moze da radi tek onda kada ne upisuje prvu godinu prvi puta. Predmete koje ne polozi automatski mu se dodaju pri upisu nove akademske godine. 
