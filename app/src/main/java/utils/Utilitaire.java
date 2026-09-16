package utils;

import org.mindrot.jbcrypt.BCrypt;

public class Utilitaire {
    private Utilitaire() {
    }

    public static String hacherMotDePasse (String motDePasseEnClair){
        String hash;
        hash = BCrypt.hashpw(motDePasseEnClair, BCrypt.gensalt());
        return hash;
    }

    public static boolean verifierMotDePasse(String motDePasseEnClair, String hashStocke) {
        return BCrypt.checkpw(motDePasseEnClair, hashStocke);
    }


}
