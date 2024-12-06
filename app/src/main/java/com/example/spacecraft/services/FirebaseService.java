package com.example.spacecraft.services;

import com.example.spacecraft.models.app.Profile;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FirebaseService {
    private FirebaseDatabase firebaseDatabase;

    public FirebaseService() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void storeGoogleAuthUser(FirebaseUser user, int highestScore) {
        firebaseDatabase.getReference("users").child(user.getUid()).child("username").setValue(user.getDisplayName());
        firebaseDatabase.getReference("users").child(user.getUid()).child("highestScore").setValue(highestScore);
    }

    public Query getRankingOnFirebase() {
        return firebaseDatabase.getReference("users").orderByChild("highestScore");
    }
    public Query getProfileOnFirebase() {
        return firebaseDatabase.getReference("profiles");
    }



    public com.google.android.gms.tasks.Task<Void> storeProfile(Profile profile) {
        return firebaseDatabase.getReference("profiles").child(profile.getIdLocal() + "").setValue(profile);
    }
}
