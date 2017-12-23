package com.marknkamau.justjavastaff.ui.login;

import com.marknkamau.justjavastaff.authentication.AuthenticationService;
import com.marknkamau.justjavastaff.models.Employee;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

/**
 * Created by Mark Njung'e.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {
    @Mock
    private AuthenticationService auth;

    @Mock
    private LoginView view;

    private LoginPresenter presenter;

    @Before
    public void setUp() {
        presenter = new LoginPresenter(auth, view);
    }

    @Test
    public void should_signIn() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                AuthenticationService.AuthListener authListener = (AuthenticationService.AuthListener) invocation.getArguments()[2];
                authListener.onSuccess(new Employee("", "", ""));
                return null;
            }
        }).when(auth).signIn(Mockito.anyString(), Mockito.anyString(), Mockito.any(AuthenticationService.AuthListener.class));

        presenter.signIn("mark@justjava.com", "");

        Mockito.verify(view).onSignedIn();
    }

    @Test
    public void should_showError_onFailedSignedIn(){
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                AuthenticationService.AuthListener authListener = (AuthenticationService.AuthListener) invocation.getArguments()[2];
                authListener.onError("");
                return null;
            }
        }).when(auth).signIn(Mockito.anyString(), Mockito.anyString(), Mockito.any(AuthenticationService.AuthListener.class));

        presenter.signIn("mark@justjava.com", "");

        Mockito.verify(view).displayMessage(Mockito.anyString());
    }

    @Test
    public void should_showError_onInvalidEmail(){
        presenter.signIn("mark@mail.com", "");

        Mockito.verify(view).displayMessage(Mockito.anyString());
    }
}