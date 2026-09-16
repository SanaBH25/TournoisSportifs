package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NOM = "TournoisSportifs.sdb";
    public static final int    VERSION_BD = 1;

    public static final String TABLE_USAGERS =     "Usagers";
    public static final String COL_ID_USAGER =     "_id";
    public static final String COL_PRENOM_USAGER = "Prenom";
    public static final String COL_NOM_USAGER =    "Nom";
    public static final String COL_EMAIL_USAGER =  "Email";
    public static final String COL_MOT_DE_PASSE_USAGER = "MotDePasse";
    private static final String USAGER_DDL =        "CREATE TABLE " + TABLE_USAGERS +
            "("
            + COL_ID_USAGER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_PRENOM_USAGER + " TEXT, "
            + COL_NOM_USAGER + " TEXT, "
            + COL_EMAIL_USAGER + " TEXT "
            + COL_MOT_DE_PASSE_USAGER + " TEXT NOT NULL"
            + ")";
    public static final String TABLE_EQUIPES =  "Equipes";
    public static final String COL_ID_EQUIPE =  "_id";
    public static final String COL_NOM_EQUIPE = "Nom";
    public static final String COL_SPORT =      "Sport";
    private static final String EQUIPE_DDL =     "CREATE TABLE " + TABLE_EQUIPES +
            "("
            + COL_ID_EQUIPE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NOM_EQUIPE + " TEXT, "
            + COL_SPORT + " TEXT "
            + ")";
    public static final String TABLE_MEMBRES_EQUIPE = "Membres_Equipe";
    public static final String COL_ID =               "_id";
    public static final String COL_ID_USER =          "_idUsager";
    public static final String COL_ID_TEAM =          "_idEquipe";
    public static final String COL_ROLE =             "Role";
    private static final String MEMBRES_EQUIPE_DDL =   "CREATE TABLE " + TABLE_MEMBRES_EQUIPE +
            "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + COL_ID_USER + " INTEGER, "
            + COL_ID_TEAM + " INTEGER , "
            + COL_ROLE + " TEXT , "
            + "FOREIGN KEY (" + COL_ID_USER+ ") REFERENCES " + TABLE_USAGERS + " ( " + COL_ID_USAGER +" ),"
            + "FOREIGN KEY (" +COL_ID_TEAM + ") REFERENCES " + TABLE_EQUIPES + " ( " + COL_ID_EQUIPE +" )"
            + ")";

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USAGER_DDL);
        db.execSQL(EQUIPE_DDL);
        db.execSQL(MEMBRES_EQUIPE_DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBRES_EQUIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USAGERS);
        onCreate(db);
    }
}
