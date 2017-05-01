package com.marknkamau.justjava.utils;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthUtils {
    private static FirebaseAuth firebaseAuth;

    static {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

    public static void logOut(){
        firebaseAuth.signOut();
    }

    public static void signIn(String email, String password, final AuthActionListener listener){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                listener.actionSuccessful("Sign in successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.actionFailed(e.getMessage());
            }
        });
    }

    public static void sendPasswordResetEmail(String email, final AuthActionListener listener){
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.actionSuccessful("Password reset email sent");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.actionFailed(e.getMessage());
            }
        });
    }

    public interface AuthActionListener{
        void actionSuccessful(String response);
        void actionFailed(String response);
    }

}
