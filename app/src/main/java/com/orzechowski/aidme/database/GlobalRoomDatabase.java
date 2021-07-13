package com.orzechowski.aidme.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.orzechowski.aidme.browser.database.Category;
import com.orzechowski.aidme.browser.database.CategoryDAO;
import com.orzechowski.aidme.database.helper.Helper;
import com.orzechowski.aidme.database.helper.HelperDAO;
import com.orzechowski.aidme.database.tag.CategoryTag;
import com.orzechowski.aidme.database.tag.CategoryTagDAO;
import com.orzechowski.aidme.database.tag.HelperTag;
import com.orzechowski.aidme.database.tag.HelperTagDAO;
import com.orzechowski.aidme.database.tag.Tag;
import com.orzechowski.aidme.database.tag.TagDAO;
import com.orzechowski.aidme.database.tag.TutorialTag;
import com.orzechowski.aidme.database.tag.TutorialTagDAO;
import com.orzechowski.aidme.tutorial.database.Tutorial;
import com.orzechowski.aidme.tutorial.database.TutorialDAO;
import com.orzechowski.aidme.tutorial.instructionsrecycler.database.InstructionSet;
import com.orzechowski.aidme.tutorial.instructionsrecycler.database.InstructionSetDAO;
import com.orzechowski.aidme.tutorial.mediaplayer.database.Multimedia;
import com.orzechowski.aidme.tutorial.mediaplayer.database.MultimediaDAO;
import com.orzechowski.aidme.tutorial.mediaplayer.database.MultimediaInVersion;
import com.orzechowski.aidme.tutorial.mediaplayer.database.MultimediaInVersionDAO;
import com.orzechowski.aidme.tutorial.mediaplayer.sound.TutorialSound;
import com.orzechowski.aidme.tutorial.mediaplayer.sound.TutorialSoundDAO;
import com.orzechowski.aidme.tutorial.version.database.Version;
import com.orzechowski.aidme.tutorial.version.database.VersionDAO;
import com.orzechowski.aidme.tutorial.version.database.VersionInstruction;
import com.orzechowski.aidme.tutorial.version.database.VersionInstructionDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Version.class, InstructionSet.class, Tutorial.class, VersionInstruction.class,
        TutorialSound.class, Helper.class, Multimedia.class, Category.class, MultimediaInVersion.class,
        Tag.class, HelperTag.class, TutorialTag.class, CategoryTag.class},
        version = 1, exportSchema = false)
public abstract class GlobalRoomDatabase extends RoomDatabase
{
    public abstract VersionDAO versionDao();
    public abstract InstructionSetDAO instructionDao();
    public abstract TutorialDAO tutorialDao();
    public abstract TutorialSoundDAO tutorialSoundDAO();
    public abstract HelperDAO helperDao();
    public abstract MultimediaDAO multimediaDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract VersionInstructionDAO versionInstructionDAO();
    public abstract MultimediaInVersionDAO multimediaInVersionDAO();
    public abstract TagDAO tagDAO();
    public abstract HelperTagDAO helperTagDAO();
    public abstract TutorialTagDAO tutorialTagDAO();
    public abstract CategoryTagDAO categoryTagDAO();

    private static volatile GlobalRoomDatabase INSTANCE;

    public static GlobalRoomDatabase getDatabase(final Context context)
    {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    GlobalRoomDatabase.class, "AidMe")
                    .addCallback(sRoomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);

            databaseWriteExecutor.execute(()->{
                TutorialDAO tutorialDAO = INSTANCE.tutorialDao();
                InstructionSetDAO instructionDAO = INSTANCE.instructionDao();
                VersionDAO versionDAO = INSTANCE.versionDao();
                TutorialSoundDAO tutorialSoundDAO = INSTANCE.tutorialSoundDAO();
                HelperDAO helperDAO = INSTANCE.helperDao();
                MultimediaDAO multimediaDAO = INSTANCE.multimediaDAO();
                CategoryDAO categoryDAO = INSTANCE.categoryDAO();
                VersionInstructionDAO versionInstructionDAO = INSTANCE.versionInstructionDAO();
                MultimediaInVersionDAO multimediaInVersionDAO = INSTANCE.multimediaInVersionDAO();
                TagDAO tagDAO = INSTANCE.tagDAO();
                HelperTagDAO helperTagDAO = INSTANCE.helperTagDAO();
                TutorialTagDAO tutorialTagDAO = INSTANCE.tutorialTagDAO();
                CategoryTagDAO categoryTagDAO = INSTANCE.categoryTagDAO();

                helperDAO.insert(new Helper(0L, "Ania", "Kozłowska", "", "Studentka"));
                helperDAO.insert(new Helper(1L, "Łukasz", "Orzechowski", "", "Twórca"));
                helperDAO.insert(new Helper(2L, "Kasia", "Kulpa", "", "Studentka"));

                //beginning of CPR tutorial
                tutorialDAO.insert(new Tutorial(0L, "Masaż serca", 1L, "heart_massage.jpg"));

                instructionDAO.insert(new InstructionSet(0L, "Wstęp", "Jeżeli ofiara nie jest w stanie samodzielnie oddychać…", 5000, 0L, 0));
                instructionDAO.insert(new InstructionSet(1L, "Ułożenie ofiary", "Upewnij się, że ofiara leży na plecach, jest ułożona prosto, a jej drogi oddechowe są udrożnione.", 8000, 0L, 1));
                instructionDAO.insert(new InstructionSet(2L, "Pozycja do udzielania pomocy", "Uklęknij wygodnie nad ofiarą tak, by twoje dłonie mogły być wyprostowane prostopadle do jej klatki piersiowej.", 8000, 0L, 2));
                instructionDAO.insert(new InstructionSet(3L, "Ułożenie dłoni", "Umieść dłoń na środku klatki piersiowej, drugą dłoń umieść nad pierwszą tak, aby palce się przeplatały.", 9000, 0L, 3));
                instructionDAO.insert(new InstructionSet(4L, "Palce", "Nie wywieraj nacisku na klatkę piersiową palcami, utrzymaj je lekko uniesione i splecione.", 8000, 0L, 4));
                instructionDAO.insert(new InstructionSet(5L, "Głębokość uciśnięć", "Staraj się wywierać nacisk o 5 centymetrów prosto w dół ciężarem swojego ciała.", 7000, 0L, 5));
                instructionDAO.insert(new InstructionSet(6L, "Uciśnięcia", "Kontynuuj uciśnięcia do momentu przybycia pomocy zgodnie z tempem dźwięku który słyszysz w tle.", 14000, 0L, 6));
                instructionDAO.insert(new InstructionSet(7L, "W razie zwymiotowania ofiary", "Jeśli ofiara zwymiotuje w trakcie, przekręć ją na bok tak by głowa była skierowana w dół i poczekaj aż jej usta się opróżnią, przetrzyj je, po czym wróć do procedury.", 8000, 0L, 7));

                versionDAO.insert(new Version(0L, "Przeprowadź mnie przez wszystkie podstawowe kroki!", 0L, true,"0", false, false,  null));
                versionDAO.insert(new Version(1L, "Wiem, co robię, potrzebne mi jest tylko tempo!", 0L, false, "0", false, false, null));

                versionInstructionDAO.insert(new VersionInstruction(0L, 0L, 0));
                versionInstructionDAO.insert(new VersionInstruction(1L, 0L, 1));
                versionInstructionDAO.insert(new VersionInstruction(2L, 0L, 2));
                versionInstructionDAO.insert(new VersionInstruction(3L, 0L, 3));
                versionInstructionDAO.insert(new VersionInstruction(4L, 0L, 4));
                versionInstructionDAO.insert(new VersionInstruction(5L, 0L, 5));
                versionInstructionDAO.insert(new VersionInstruction(6L, 0L, 6));
                versionInstructionDAO.insert(new VersionInstruction(7L, 0L, 7));
                versionInstructionDAO.insert(new VersionInstruction(8L, 0L, 8));

                versionInstructionDAO.insert(new VersionInstruction(9L, 1L, 6));

                tutorialSoundDAO.insert(new TutorialSound(0L, 45000L, true, 545L, 0L));

                multimediaDAO.insert(new Multimedia(0L, 0L, -1,false, "m0_0.mp4" ,true, 0));

                multimediaInVersionDAO.insert(new MultimediaInVersion(0L, 0L, 0L));
                multimediaInVersionDAO.insert(new MultimediaInVersion(1L, 0L, 1L));

                //beginning of broken limb general tutorial
                tutorialDAO.insert(new Tutorial(1L, "Złamana kończyna", 1L, "broken_bone.jpeg"));

                instructionDAO.insert(new InstructionSet(8L, "Wstęp", "Jeżeli istnieje podejrzenie złamania", 5000, 1L, 0));
                instructionDAO.insert(new InstructionSet(9L, "Nie udało się wezwać pomocy", "...i nie możesz dosięgnąć numeru alarmowego", 5000, 1L, 1));
                instructionDAO.insert(new InstructionSet(10L, "Upomnienie ofiary", "Ofiara powinna pozostać w bezruchu. Złamane fragmenty kostne są ostre, i każdy ruch stwarza ryzyko dalszego okaleczenia.", 5000, 1L, 2));
                instructionDAO.insert(new InstructionSet(11L, "Upomnienie użytkownika", "Nie próbuj ustawiać ani prostować kończyny, nie przemieszczaj jej w żaden sposób.", 5000, 1L, 3));
                instructionDAO.insert(new InstructionSet(12L, "Upomnienie użytkownika, złamanie otwarte", "Nie dotykaj wystającego fragmentu kostnego.", 5000, 1L, 4));
                instructionDAO.insert(new InstructionSet(13L, "Pomoc wezwana, koniec", "Nie podejmuj dalszych działań, upewnij się, że ofiara jest bezpieczna i ułożona bądź usadzona w wygodnej pozycji.", 5000, 1L, 5));
                instructionDAO.insert(new InstructionSet(14L, "Jedzenie i picie", "Nie pozwalaj ofierze niczego jeść ani pić, może to mieć negatywny wpływ na działanie znieczulenia.", 5000, 1L, 6));
                instructionDAO.insert(new InstructionSet(15L, "Unieruchamianie kości", "Unieruchomienie kończyny jest wymagane w celu przetransportowania ofiary do najbliższego szpitala.", 5000, 1L, 7));
                instructionDAO.insert(new InstructionSet(24L, "Upomnienie o stawach", "Unieruchomione muszą zostać również dwa sąsiadujące z kością stawy.", 5000, 1L, 8));
                instructionDAO.insert(new InstructionSet(16L, "Unieruchamianie dolnej kończyny", "Częściowe unieruchomienie kończyny dolnej może zostać osiągnięte poprzez przymocowanie jej do drugiej kończyny.", 5000, 1L, 9));
                instructionDAO.insert(new InstructionSet(23L, "Unieruchomienie zdrowej kończyny", "Należy w takim wypadku w miarę możliwości odpowiednio usztywnić też zdrową kończynę.", 5000, 1L, 10));
                instructionDAO.insert(new InstructionSet(17L, "Upomnienie o wiązaniu kończyn", "W przypadku gdy rozwiązanie to nie będzie wygodne dla ofiary, nie należy go stosować.", 5000, 1L, 11));
                instructionDAO.insert(new InstructionSet(18L, "Temblak", "Wykonanie prowizorycznego temblaka do usztywniania górnej kończyny zaprezentowane zostało w galerii zdjęć.", 5000, 1L, 12));
                instructionDAO.insert(new InstructionSet(19L, "Opatrywanie złamania otwartego", "Opatrunek złamania otwartego należy wykonać poprzez stopniowe owijanie czystego materiału wokół i ponad, lecz nie bezpośrednio na miejscu złamania.", 5000, 1L, 13));
                instructionDAO.insert(new InstructionSet(20L, "Upomienie o złamaniu otwartym", "Nie należy stosować łatwo rozpadających się przedmiotów takich jak waciki do podwyższenia opatrunku.", 5000, 1L, 14));
                instructionDAO.insert(new InstructionSet(21L, "Opatrywanie złamanie otwartego ciąg dalszy", "Należy wykonać dostatecznie dużo warstw, aby opatrunek bezpośrednio nad raną nie wywierał nacisku na kość.", 5000, 1L, 15));
                instructionDAO.insert(new InstructionSet(22L, "Upomnienie o nacisku na kość", "Każdy nacisk na wystający fragment kostny powoduje komplikacje.", 5000, 1L, 16));
                instructionDAO.insert(new InstructionSet(25L, "Sprawdzanie krwiobiegu - wezwana pomoc", "Do czasu przybycia pomocy sprawdzaj co kilka minut krwiobieg poniżej opatrunku…", 5000, 1L, 17));
                instructionDAO.insert(new InstructionSet(26L, "Sprawdzanie krwiobiegu - niewezwana pomoc", "Jeżeli jest z tobą osoba towarzysząca, poproś ją o sprawdzanie co kilka minut krwiobiegu poniżej opatrunku…", 5000, 1L, 18));
                instructionDAO.insert(new InstructionSet(27L, "Upomnienie o krwiobiegu", "…w celu upewnienia się, że sztywny opatrunek go nie tamuje. W przypadku gdy krwiobieg jest zatamowany, należy poluzować opatrunek.", 5000, 1L, 19));
                instructionDAO.insert(new InstructionSet(28L, "Wstrząs", "Ryzyko wstrząsu, czyli niedostatecznej ilości tlenu w organizmie, mogącej być spowodowanej utratą dużej ilości krwi, omówione jest w oddzielnym poradniku. W razie potrzeby naciśnij tutaj aby do niego przejść.", 5000, 1L, 20));

                versionDAO.insert(new Version(9L, "Złamana ręka", 1L, false, null, true, false, null));
                versionDAO.insert(new Version(10L, "Złamana noga", 1L, false, null, true, false, null));
                versionDAO.insert(new Version(3L, "Udało mi się wezwać pomoc, jestem z ofiarą do czasu jej przybycia", 1L, false, null, true, true, 9L));
                versionDAO.insert(new Version(4L, "Muszę sam/a zawieźć ofiarę do szpitala", 1L, false, null, true, true, 9L));
                versionDAO.insert(new Version(11L, "Udało mi się wezwać pomoc, jestem z ofiarą do czasu jej przybycia", 1L, false, null, true, true, 10L));
                versionDAO.insert(new Version(12L, "Muszę sam/a zawieźć ofiarę do szpitala", 1L, false, null, true, true, 10L));
                versionDAO.insert(new Version(5L, "Złamanie zamknięte", 1L, false, null, false, true, 3L));
                versionDAO.insert(new Version(6L, "Złamanie otwarte", 1L, false, null, false, true, 3L));
                versionDAO.insert(new Version(7L, "Złamanie zamknięte", 1L, false, null, false, true, 4L));
                versionDAO.insert(new Version(8L, "Złamanie otwarte", 1L, false, null, false, true, 4L));
                versionDAO.insert(new Version(13L, "Złamanie zamknięte", 1L, false, null, false, true, 11L));
                versionDAO.insert(new Version(14L, "Złamanie otwarte", 1L, false, null, false, true, 11L));
                versionDAO.insert(new Version(15L, "Złamanie zamknięte", 1L, false, null, false, true, 12L));
                versionDAO.insert(new Version(16L, "Złamanie otwarte", 1L, false, null, false, true, 12L));

                versionInstructionDAO.insert(new VersionInstruction(10L, 5L, 0));
                versionInstructionDAO.insert(new VersionInstruction(11L, 5L, 2));
                versionInstructionDAO.insert(new VersionInstruction(12L, 5L, 3));
                versionInstructionDAO.insert(new VersionInstruction(13L, 5L, 5));
                versionInstructionDAO.insert(new VersionInstruction(14L, 5L, 6));
                versionInstructionDAO.insert(new VersionInstruction(15L, 5L, 17));
                versionInstructionDAO.insert(new VersionInstruction(38L, 5L, 19));

                versionInstructionDAO.insert(new VersionInstruction(16L, 6L, 0));
                versionInstructionDAO.insert(new VersionInstruction(17L, 6L, 2));
                versionInstructionDAO.insert(new VersionInstruction(18L, 6L, 3));
                versionInstructionDAO.insert(new VersionInstruction(19L, 6L, 4));
                versionInstructionDAO.insert(new VersionInstruction(20L, 6L, 5));
                versionInstructionDAO.insert(new VersionInstruction(21L, 6L, 6));
                versionInstructionDAO.insert(new VersionInstruction(22L, 6L, 17));
                versionInstructionDAO.insert(new VersionInstruction(37L, 6L, 19));

                versionInstructionDAO.insert(new VersionInstruction(23L, 7L, 0));
                versionInstructionDAO.insert(new VersionInstruction(24L, 7L, 1));
                versionInstructionDAO.insert(new VersionInstruction(25L, 7L, 2));
                versionInstructionDAO.insert(new VersionInstruction(26L, 7L, 3));
                versionInstructionDAO.insert(new VersionInstruction(27L, 7L, 6));
                versionInstructionDAO.insert(new VersionInstruction(28L, 7L, 7));
                versionInstructionDAO.insert(new VersionInstruction(29L, 7L, 8));
                versionInstructionDAO.insert(new VersionInstruction(33L, 7L, 12));
                versionInstructionDAO.insert(new VersionInstruction(34L, 7L, 18));
                versionInstructionDAO.insert(new VersionInstruction(35L, 7L, 19));
                versionInstructionDAO.insert(new VersionInstruction(36L, 7L, 20));

                versionInstructionDAO.insert(new VersionInstruction(39L, 8L, 0));
                versionInstructionDAO.insert(new VersionInstruction(40L, 8L, 1));
                versionInstructionDAO.insert(new VersionInstruction(41L, 8L, 2));
                versionInstructionDAO.insert(new VersionInstruction(42L, 8L, 3));
                versionInstructionDAO.insert(new VersionInstruction(43L, 8L, 4));
                versionInstructionDAO.insert(new VersionInstruction(44L, 8L, 6));
                versionInstructionDAO.insert(new VersionInstruction(45L, 8L, 7));
                versionInstructionDAO.insert(new VersionInstruction(46L, 8L, 8));
                versionInstructionDAO.insert(new VersionInstruction(50L, 8L, 12));
                versionInstructionDAO.insert(new VersionInstruction(51L, 8L, 13));
                versionInstructionDAO.insert(new VersionInstruction(52L, 8L, 14));
                versionInstructionDAO.insert(new VersionInstruction(53L, 8L, 15));
                versionInstructionDAO.insert(new VersionInstruction(54L, 8L, 16));
                versionInstructionDAO.insert(new VersionInstruction(55L, 8L, 18));
                versionInstructionDAO.insert(new VersionInstruction(56L, 8L, 19));
                versionInstructionDAO.insert(new VersionInstruction(57L, 8L, 20));

                versionInstructionDAO.insert(new VersionInstruction(58L, 13L, 0));
                versionInstructionDAO.insert(new VersionInstruction(59L, 13L, 2));
                versionInstructionDAO.insert(new VersionInstruction(60L, 13L, 3));
                versionInstructionDAO.insert(new VersionInstruction(61L, 13L, 5));
                versionInstructionDAO.insert(new VersionInstruction(62L, 13L, 6));
                versionInstructionDAO.insert(new VersionInstruction(63L, 13L, 17));
                versionInstructionDAO.insert(new VersionInstruction(64L, 13L, 19));

                versionInstructionDAO.insert(new VersionInstruction(65L, 14L, 0));
                versionInstructionDAO.insert(new VersionInstruction(66L, 14L, 2));
                versionInstructionDAO.insert(new VersionInstruction(67L, 14L, 3));
                versionInstructionDAO.insert(new VersionInstruction(68L, 14L, 4));
                versionInstructionDAO.insert(new VersionInstruction(69L, 14L, 5));
                versionInstructionDAO.insert(new VersionInstruction(70L, 14L, 6));
                versionInstructionDAO.insert(new VersionInstruction(71L, 14L, 17));
                versionInstructionDAO.insert(new VersionInstruction(72L, 14L, 19));

                versionInstructionDAO.insert(new VersionInstruction(73L, 15L, 0));
                versionInstructionDAO.insert(new VersionInstruction(74L, 15L, 1));
                versionInstructionDAO.insert(new VersionInstruction(75L, 15L, 2));
                versionInstructionDAO.insert(new VersionInstruction(76L, 15L, 3));
                versionInstructionDAO.insert(new VersionInstruction(77L, 15L, 6));
                versionInstructionDAO.insert(new VersionInstruction(78L, 15L, 7));
                versionInstructionDAO.insert(new VersionInstruction(79L, 15L, 8));
                versionInstructionDAO.insert(new VersionInstruction(80L, 15L, 9));
                versionInstructionDAO.insert(new VersionInstruction(81L, 15L, 10));
                versionInstructionDAO.insert(new VersionInstruction(82L, 15L, 11));
                versionInstructionDAO.insert(new VersionInstruction(83L, 15L, 12));
                versionInstructionDAO.insert(new VersionInstruction(84L, 15L, 19));
                versionInstructionDAO.insert(new VersionInstruction(85L, 15L, 20));

                versionInstructionDAO.insert(new VersionInstruction(86L, 16L, 0));
                versionInstructionDAO.insert(new VersionInstruction(87L, 16L, 1));
                versionInstructionDAO.insert(new VersionInstruction(89L, 16L, 2));
                versionInstructionDAO.insert(new VersionInstruction(90L, 16L, 3));
                versionInstructionDAO.insert(new VersionInstruction(91L, 16L, 4));
                versionInstructionDAO.insert(new VersionInstruction(92L, 16L, 6));
                versionInstructionDAO.insert(new VersionInstruction(93L, 16L, 7));
                versionInstructionDAO.insert(new VersionInstruction(94L, 16L, 8));
                versionInstructionDAO.insert(new VersionInstruction(95L, 16L, 9));
                versionInstructionDAO.insert(new VersionInstruction(96L, 16L, 10));
                versionInstructionDAO.insert(new VersionInstruction(97L, 16L, 11));
                versionInstructionDAO.insert(new VersionInstruction(98L, 16L, 12));
                versionInstructionDAO.insert(new VersionInstruction(99L, 16L, 13));
                versionInstructionDAO.insert(new VersionInstruction(100L, 16L, 14));
                versionInstructionDAO.insert(new VersionInstruction(101L, 16L, 15));
                versionInstructionDAO.insert(new VersionInstruction(102L, 16L, 16));
                versionInstructionDAO.insert(new VersionInstruction(103L, 16L, 19));
                versionInstructionDAO.insert(new VersionInstruction(104L, 16L, 20));

                multimediaDAO.insert(new Multimedia(1L, 1L, 5000, true, "m1_0.jpg", true, 0));
                multimediaDAO.insert(new Multimedia(2L, 1L, 5000, true, "m1_1.jpg", true, 1));

                multimediaInVersionDAO.insert(new MultimediaInVersion(3L, 1L, 5L));
                multimediaInVersionDAO.insert(new MultimediaInVersion(4L, 2L, 5L));
                multimediaInVersionDAO.insert(new MultimediaInVersion(5L, 1L, 6L));
                multimediaInVersionDAO.insert(new MultimediaInVersion(6L, 2L, 6L));
                multimediaInVersionDAO.insert(new MultimediaInVersion(7L, 1L, 7L));
                multimediaInVersionDAO.insert(new MultimediaInVersion(8L, 2L, 7L));
                multimediaInVersionDAO.insert(new MultimediaInVersion(9L, 1L, 8L));
                multimediaInVersionDAO.insert(new MultimediaInVersion(10L, 2L, 8L));

                //beginning of heatstroke tutorial
                tutorialDAO.insert(new Tutorial(2L, "Udar słoneczny", 1L, "sun.jpg"));

                //beginning of categories
                categoryDAO.insert(new Category(0L, "Pierwsza pomoc", true, "first_aid.jpg", 0));
                categoryDAO.insert(new Category(1L, "Pożar", true, "fire.jpeg", 0));
                categoryDAO.insert(new Category(2L, "Żywioł", true, "natural_disaster.jpeg", 0));
                categoryDAO.insert(new Category(3L, "Atak terrorystyczny", true, "terrorist.jpeg", 0));
                categoryDAO.insert(new Category(4L, "Zwierzęta", true, "animal_danger.jpg", 0));
                categoryDAO.insert(new Category(5L, "Przetrwanie w dziczy", true, "survival.jpeg", 0));
                categoryDAO.insert(new Category(6L, "Problemy z oddychaniem", false, "breathing.jpg", 1));
                categoryDAO.insert(new Category(7L, "Urazy", true, "injury.jpeg", 1));
                categoryDAO.insert(new Category(8L, "Złamania", false, "broken_bone.jpeg", 2));
                categoryDAO.insert(new Category(9L, "Zwichnięcia", false, "sprain.jpeg", 2));
                categoryDAO.insert(new Category(10L, "Głowa", true, "head_accident.jpeg", 1));

                //beginning of tags
                tagDAO.insert(new Tag(0L, "firstaid", 0));
                tagDAO.insert(new Tag(1L, "fire", 0));
                tagDAO.insert(new Tag(2L, "natural", 0));
                tagDAO.insert(new Tag(3L, "terrorism", 0));
                tagDAO.insert(new Tag(4L, "animals", 0));
                tagDAO.insert(new Tag(5L, "survival", 0));
                tagDAO.insert(new Tag(6L, "breathing", 1));
                tagDAO.insert(new Tag(7L, "injury", 1));
                tagDAO.insert(new Tag(8L, "broken bones", 2));
                tagDAO.insert(new Tag(9L, "sprain", 2));
                tagDAO.insert(new Tag(10L, "head", 1));
                tagDAO.insert(new Tag(11L, "creator", null));
                tagDAO.insert(new Tag(12L, "immediate", null));
                tagDAO.insert(new Tag(13L, "limbs", null));
                tagDAO.insert(new Tag(14L, "heatstroke", null));

                categoryTagDAO.insert(new CategoryTag(0L, 0L, 0L));
                categoryTagDAO.insert(new CategoryTag(1L, 1L, 1L));
                categoryTagDAO.insert(new CategoryTag(2L, 2L, 2L));
                categoryTagDAO.insert(new CategoryTag(3L, 3L, 3L));
                categoryTagDAO.insert(new CategoryTag(4L, 4L, 4L));
                categoryTagDAO.insert(new CategoryTag(5L, 5L, 5L));
                categoryTagDAO.insert(new CategoryTag(6L, 6L, 0L));
                categoryTagDAO.insert(new CategoryTag(7L, 6L, 6L));
                categoryTagDAO.insert(new CategoryTag(8L, 7L, 0L));
                categoryTagDAO.insert(new CategoryTag(9L, 7L, 7L));
                categoryTagDAO.insert(new CategoryTag(10L, 8L, 0L));
                categoryTagDAO.insert(new CategoryTag(11L, 8L, 7L));
                categoryTagDAO.insert(new CategoryTag(12L, 8L, 8L));
                categoryTagDAO.insert(new CategoryTag(13L, 9L, 0L));
                categoryTagDAO.insert(new CategoryTag(14L, 9L, 7L));
                categoryTagDAO.insert(new CategoryTag(15L, 9L, 9L));
                categoryTagDAO.insert(new CategoryTag(16L, 10L, 0L));
                categoryTagDAO.insert(new CategoryTag(17L, 10L, 10L));

                helperTagDAO.insert(new HelperTag(0L, 1L, 11L));

                tutorialTagDAO.insert(new TutorialTag(0L, 0L, 0L));
                tutorialTagDAO.insert(new TutorialTag(1L, 0L, 6L));
                tutorialTagDAO.insert(new TutorialTag(2L, 0L, 12L));
                tutorialTagDAO.insert(new TutorialTag(3L, 1L, 0L));
                tutorialTagDAO.insert(new TutorialTag(4L, 1L, 7L));
                tutorialTagDAO.insert(new TutorialTag(5L, 1L, 8L));
                tutorialTagDAO.insert(new TutorialTag(6L, 1L, 13L));
                tutorialTagDAO.insert(new TutorialTag(7L, 2L, 0L));
                tutorialTagDAO.insert(new TutorialTag(8L, 2L, 10L));
                tutorialTagDAO.insert(new TutorialTag(9L, 2L, 14L));
            });
        }
    };
}
