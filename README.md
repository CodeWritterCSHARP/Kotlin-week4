4
Selitä lyhyesti:
Mitä tarkoittaa navigointi Jetpack Composessa:
- Navigointi tarkoittaa sitä, miten käyttäjä siirtyy eri näkymien (screenien) välillä sovelluksessa.
  Jetpack Compose Navigation -kirjastoa käytetään single-activity-sovelluksissa,
  joissa koko navigaatio hoidetaan composable-funktioiden avulla
  
Mitä ovat NavHost ja NavController:
- NavHost on konttori, joka näyttää oikean composable-funktion riippuen siitä, mikä reitti (route) on aktiivinen.
  NavHost saa NavControllerin ja startDestinationin ja sisältää määritellyt reitit (composable(route = "home") { ... }).
  NavController on navigoinnin ”ohjain”. Se muistaa, missä näkymässä ollaan, hoitaa siirtymät (navigate), back-napin toiminnan.
  
Miten sovelluksesi navigaatiorakenne on toteutettu (Home ↔ Calendar):
- Navigointi tapahtuu TopAppBarin ikoneilla, kun painetaan tabia,
  kutsutaan navController.navigate("calendar") tai navigate("home") singleTop-logiikalla, jotta ei luoda turhia pinoja.

Kuvaa arkkitehtuuri:
Miten MVVM ja navigointi yhdistyvät (yksi ViewModel kahdelle screenille):
- Käytetään yhtä ja samaa TaskViewModel-instanssia molemmille näytöille.
  ViewModel luodaan MainActivityn komennolla: val viewModel: TaskViewModel = viewModel().

Miten ViewModelin tila jaetaan kummankin ruudun välillä:
- Molemmat näytöt lukevat saman tilan: val tasks by viewModel.tasks.collectAsState()
  Kun yhdessä näytössä kutsutaan viewModel.toggleDone() tai viewModel.addTask(),
  StateFlow päivittää tilan: molemmat näytöt reagoivat automaattisesti, koska ne seuraavat samaa flow’ta.

Selitä lyhyesti:
Miten CalendarScreen on toteutettu (miten tehtävät ryhmitellään / esitetään kalenterimaisesti):
- Tehtävät ryhmitellään päivämäärän mukaan. LazyColumnissa käydään läpi lajiteltu map:
  Jokaisesta päivästä tulostetaan ensin päivämäärä otsikkona, sen alle listataan kyseisen päivän tehtävät.

Miten AlertDialog hoitaa addTask ja editTask:
- Yksi sama TaskDetailDialog-komponentti käytetään sekä lisäämiseen että muokkaamiseen
  Kun task == null, kyseessä on addTask; Kun task != null, kyseessä on updateTask
  Dialogissa on samat kentät: otsikko, kuvaus, prioriteetti, eräpäivä
  Tallennuspainike kutsuu viewModel.addTask() tai viewModel.updateTask() riippuen siitä, oliko task null vai ei
  Poistopainike näkyy vain muokkaustilassa

3
MVVM erottaa UI:n (View) logiikasta (ViewModel) ja datasta (Model). ViewModel hallitsee tilaa ja liiketoimintalogiikkaa,
jolloin Compose UI pysyy yksinkertaisena ja reaktiivisena.
   
StateFlow on Coroutine-pohjainen tilavirta, joka säilyttää viimeisimmän arvon ja emittoi muutokset.
Compose voi kuunnella sitä collectAsState() avulla, jolloin UI päivittyy automaattisesti.

2
Miten Compose-tilanhallinta toimii:
- Compose-tilanhallinta perustuu reaktiivisuuteen: UI päivittyy automaattisesti aina, kun siihen liittyvä tila muuttuu.
- Tila voidaan säilyttää paikallisesti remember-funktiolla tai pidempään ViewModelin avulla.

Miksi ViewModel on parempi kuin pelkkä remember:
- ViewModel säilyttää datan sovelluksen elinkaaren yli, esim. ruudun kierron yhteydessä.
  Se pitää logiikan erillään käyttöliittymästä, mikä tekee koodista selkeämpää ja helpommin ylläpidettävää.
  Composables voivat seurata ViewModelin tilaa reaktiivisesti, jolloin UI päivittyy automaattisesti muutosten yhteydessä.

1
Sovelluksessa käytetään Task-data classia, joka sisältää seuraavat kentät:
- id: yksilöllinen tunniste
- 
- title: tehtävän nimi
- description: tehtävän kuvaus
- priority: tehtävän tärkeys
- dueDate: eräpäivä merkkijonona
- done: boolean, onko tehtävä valmis

Funktiot:
- addTask(list, task): List<Task> - lisää uuden tehtävän listan loppuun ja palauttaa uuden listan
- toggleDone(list, id): List<Task> - vaihtaa tehtävän done-tilan TRUE FALSE
- filterByDone(list, done): List<Task> - palauttaa listan, jossa näkyvät vain done-tilassa olevat tehtävät
- sortByDueDate(list): List<Task> - palauttaa listan, joka on järjestetty eräpäivän mukaan
