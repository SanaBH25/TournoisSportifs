package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import modele.Usager;

public class UsagerAdapter {
    private DbHelper helper;
    private SQLiteDatabase db;
    public UsagerAdapter(Context context) {
        helper = new DbHelper(context, DbHelper.DB_NOM, null, DbHelper.VERSION_BD);
    }
    public void openBD() {
        db = helper.getWritableDatabase();
    }
    public void closeBD() { db.close();}

    public void inserer(Usager usager) {
        openBD();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_PRENOM_USAGER, usager.getPrenom());
        cv.put(DbHelper.COL_NOM_USAGER,  usager.getNom());
        cv.put(DbHelper.COL_EMAIL_USAGER,   usager.getEmail());
        cv.put(DbHelper.COL_MOT_DE_PASSE_USAGER, usager.getMotDePasse());

        db.insert(DbHelper.TABLE_USAGERS, null, cv);
        closeBD();
    }

    public ArrayList<Usager> lister() {
        openBD();
        String[] cols = {
                DbHelper.COL_ID_USAGER, DbHelper.COL_PRENOM_USAGER, DbHelper.COL_NOM_USAGER,
                DbHelper.COL_EMAIL_USAGER, DbHelper.COL_MOT_DE_PASSE_USAGER
        };
        ArrayList<Usager> resultats = new ArrayList<>();

        Cursor curseur = db.query(DbHelper.TABLE_USAGERS, cols, null, null,
                null, null, null);
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            Usager d = new Usager(null,
                    curseur.getString(3),
                    curseur.getString(0),
                    null,
                    curseur.getString(4),
                    curseur.getString(2),
                    curseur.getString(1));
            resultats.add(d);
            curseur.moveToNext();
        }
        curseur.close();
        closeBD();
        return resultats;
    }
}
