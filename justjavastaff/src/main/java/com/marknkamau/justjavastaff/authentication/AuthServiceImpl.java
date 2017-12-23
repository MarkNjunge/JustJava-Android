package com.marknkamau.justjavastaff.authentication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marknkamau.justjavastaff.models.Employee;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

public class AuthServiceImpl implements AuthenticationService {
    private FirebaseAuth auth;

    public AuthServiceImpl() {
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(String email, String password, final AuthListener listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        listener.onSuccess(getEmployeeFromUser(authResult.getUser()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getMessage());
                    }
                });
    }

    @Override
    @Nullable
    public Employee currentEmployee() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null ){
            return null;
        }else {
            return getEmployeeFromUser(currentUser);
        }
    }

    private Employee getEmployeeFromUser(FirebaseUser user){
        //TODO Admin interface should create users with a display name.
        String name = user.getEmail().split("@")[0];

        return new Employee(user.getUid(), name, user.getEmail());
    }
}
