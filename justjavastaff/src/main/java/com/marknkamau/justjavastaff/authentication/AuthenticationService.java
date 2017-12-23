package com.marknkamau.justjavastaff.authentication;


import android.support.annotation.Nullable;

import com.marknkamau.justjavastaff.models.Employee;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

public interface AuthenticationService {
    void signIn(String email, String password, AuthListener listener);

    @Nullable
    Employee currentEmployee();

    interface AuthListener {
        void onSuccess(Employee employee);

        void onError(String reason);
    }
}
