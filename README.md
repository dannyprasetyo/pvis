# Instalasi Sikka Woojin
1. Clone project dari git. Download ([Git Client]([www.github.com](https://git-scm.com/downloads)))
   ```bash
   $ git clone https://github.com/desianggraenis/sikka-woojin.git
   ```
   
2. Import Database menggunakan file `sikka-wojin/DB/db_woojin.sql`
   ```bash
   Localhost/phpmyadmin > New > db_woojin > Import > Choosen File > db_woojin.sql
   ```

3. Import project `sikka-woojin` di Netbeans
   ```bash
   File > Java Project with Existing Source > Project Name "Woojin" > Project Folder "sikka-woojin\APP" > Open > Source Package Folders > Add Folder "sikka-woojin\APP\src" > Next > Finish 
   ```

4. Import Library
   Klik kanan project `Woojin` > Properties > Libraries > Add Jar/Folder > Ctrl + A semua Library > open > OK

5. Running Aplikasi `sikka-woojin`
   Project `Woojin` > Source Packages > Main > Klik kanan Run File Main.java
   