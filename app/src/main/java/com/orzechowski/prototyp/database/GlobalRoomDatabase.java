package com.orzechowski.prototyp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.orzechowski.prototyp.database.tutorial.Tutorial;
import com.orzechowski.prototyp.database.tutorial.TutorialDAO;
import com.orzechowski.prototyp.instructionsrecycler.database.InstructionSet;
import com.orzechowski.prototyp.instructionsrecycler.database.InstructionSetDAO;
import com.orzechowski.prototyp.versionrecycler.database.Version;
import com.orzechowski.prototyp.versionrecycler.database.VersionDAO;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Version.class, InstructionSet.class, Tutorial.class},
        version = 1, exportSchema = false)
public abstract class GlobalRoomDatabase extends RoomDatabase {

    public abstract VersionDAO versionDao();
    public abstract InstructionSetDAO instructionDao();
    public abstract TutorialDAO tutorialDao();

    private static volatile GlobalRoomDatabase INSTANCE;

    public static GlobalRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    GlobalRoomDatabase.class, "AidMeDatabase")
                    .addCallback(sRoomDatabaseCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);

            databaseWriteExecutor.execute(()->{
                TutorialDAO tutorialDAO = INSTANCE.tutorialDao();
                InstructionSetDAO instructionDAO = INSTANCE.instructionDao();
                VersionDAO versionDAO = INSTANCE.versionDao();

                tutorialDAO.insert(new Tutorial(0L, "Masaż serca", 0L));

                instructionDAO.insert(new InstructionSet(0L, "Wstęp", "Jeżeli ofiara nie jest w stanie samodzielnie oddychać…", 5000, 0L));
                instructionDAO.insert(new InstructionSet(1L, "Ułożenie ofiary", "Upewnij się, że ofiara leży na plecach, jej głowa jest ułożona prosto oraz że jej drogi oddechowe są udrożnione.", 8000, 0L));
                instructionDAO.insert(new InstructionSet(2L, "Pozycja do udzielania pomocy", "Uklęknij wygodnie nad ofiarą tak, by twoje dłonie mogły być wyprostowane prostopadle do jej klatki piersiowej.", 8000, 0L));
                instructionDAO.insert(new InstructionSet(3L, "Ułożenie dłoni", "Umieść dłoń na środku klatki piersiowej, drugą dłoń umieść nad pierwszą tak, aby palce się przeplatały.", 9000, 0L));
                instructionDAO.insert(new InstructionSet(4L, "Palce", "Nie wywieraj nacisku na klatkę piersiową palcami, utrzymaj je lekko uniesione i splecione.", 8000, 0L));
                instructionDAO.insert(new InstructionSet(5L, "Głębokość uciśnięć", "Staraj się wywierać nacisk o 5 centymetrów prosto w dół ciężarem swojego ciała.", 7000, 0L));
                instructionDAO.insert(new InstructionSet(6L, "Uciśnięcia", "Kontynuuj uciśnięcia do momentu przybycia pomocy zgodnie z tempem dźwięku który słyszysz w tle.", 14000, 0L));
                instructionDAO.insert(new InstructionSet(7L, "W razie zwymiotowania ofiary", "Jeśli ofiara zwymiotuje w trakcie, przekręć ją na bok tak by głowa była skierowana w dół i poczekaj aż jej usta się opróżnią, przetrzyj je, po czym wróć do procedury.", 8000, 0L));

                versionDAO.insert(new Version(0L, "Wiem co robię, potrzebne mi jest tylko tempo!", Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7), 0L));
                versionDAO.insert(new Version(1L, "Przeprowadź mnie przez wszystkie podstawowe kroki!", Arrays.asList(4, 6, 7), 0L));
            });
        }
    };
}