package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import modele.MembreEquipe;

public class MembreEquipeAdapter {
    private DbHelper helper;
    private SQLiteDatabase db;

    public MembreEquipeAdapter(Context context) {
        helper = new DbHelper(context, DbHelper.DB_NOM, null, DbHelper.VERSION_BD);
    }

    public void openBD() {
        db = helper.getWritableDatabase();
    }

    public void closeBD() {
        db.close();
    }

    public void inserer(long idUsager, long idEquipe, String role) {
        openBD();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_ID_USER, idUsager);
        cv.put(DbHelper.COL_ID_TEAM, idEquipe);
        cv.put(DbHelper.COL_ROLE, role);

        db.insert(DbHelper.TABLE_MEMBRES_EQUIPE, null, cv);
        closeBD();
    }

    public boolean estMembre(long idUsager, long idEquipe) {
        openBD();
        Cursor curseur = db.query(DbHelper.TABLE_MEMBRES_EQUIPE,
                new String[]{DbHelper.COL_ID},
                DbHelper.COL_ID_USER + " = ? AND " + DbHelper.COL_ID_TEAM + " = ?",
                new String[]{String.valueOf(idUsager), String.valueOf(idEquipe)},
                null, null, null);
        boolean existe = curseur.getCount() > 0;
        curseur.close();
        closeBD();
        return existe;
    }

    public ArrayList<MembreEquipe> listerParEquipe(long idEquipe) {
        openBD();
        String sql = "SELECT u." + DbHelper.COL_NOM_USAGER + ", m." + DbHelper.COL_ROLE
                + ", m." + DbHelper.COL_ID_TEAM
                + " FROM " + DbHelper.TABLE_MEMBRES_EQUIPE + " m"
                + " JOIN " + DbHelper.TABLE_USAGERS + " u ON m." + DbHelper.COL_ID_USER
                + " = u." + DbHelper.COL_ID_USAGER
                + " WHERE m." + DbHelper.COL_ID_TEAM + " = ?";

        ArrayList<MembreEquipe> resultats = new ArrayList<>();
        Cursor curseur = db.rawQuery(sql, new String[]{String.valueOf(idEquipe)});
        curseur.moveToFirst();
        while (!curseur.isAfterLast()) {
            MembreEquipe m = new MembreEquipe(
                    curseur.getString(0),
                    curseur.getString(1),
                    null,
                    curseur.getString(2));
            resultats.add(m);
            curseur.moveToNext();
        }
        curseur.close();
        closeBD();
        return resultats;
    }
}
