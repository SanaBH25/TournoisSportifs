package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import modele.Equipe;

public class EquipeAdapter {
    private DbHelper helper;
    private SQLiteDatabase db;

    public EquipeAdapter(Context context) {
        helper = new DbHelper(context, DbHelper.DB_NOM, null, DbHelper.VERSION_BD);
    }

    public void openBD(){ db = helper.getWritableDatabase();}
    public void closeBD(){db.close();}

    public long inserer (Equipe equipe){
        openBD();
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_NOM_EQUIPE, equipe.getNom());
        cv.put(DbHelper.COL_SPORT, equipe.getSport());

        long id = db.insert(DbHelper.TABLE_EQUIPES,null,cv);
        closeBD();
        return id;
    }

    public ArrayList<Equipe> lister (){
        openBD();
        String [] cols = {
                DbHelper.COL_ID_EQUIPE, DbHelper.COL_NOM_EQUIPE, DbHelper.COL_SPORT
        };
        ArrayList<Equipe> resultats = new ArrayList<>();

        Cursor curseur = db.query(DbHelper.TABLE_EQUIPES, cols, null, null,
                null, null, null);
        curseur.moveToFirst();
        while(!curseur.isAfterLast()){
            Equipe e = new Equipe (
                    curseur.getString(0),
                    curseur.getString(1),
                    curseur.getString(2));
            resultats.add(e);
            curseur.moveToNext();
        }
        curseur.close();
        closeBD();
        return resultats;
    }

}
