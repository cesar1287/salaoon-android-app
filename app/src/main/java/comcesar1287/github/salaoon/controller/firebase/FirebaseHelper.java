package comcesar1287.github.salaoon.controller.firebase;

import com.google.firebase.database.DatabaseReference;

import comcesar1287.github.salaoon.controller.domain.User;

public class FirebaseHelper {

    public static final String FIREBASE_DATABASE_USERS = "users";
    public static final String FIREBASE_DATABASE_CLIENTS = "clients";
    public static final String FIREBASE_DATABASE_PROFESSINALS = "professionals";

    public static void writeNewUser(DatabaseReference mDatabase, String userId, String name,
                                    String email, String profile_pic) {

        User user = new User(name, email, profile_pic);

        mDatabase.child(FIREBASE_DATABASE_USERS).child(userId).setValue(user);
    }
}
