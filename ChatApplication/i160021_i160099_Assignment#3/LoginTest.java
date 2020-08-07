package com.zeelawahab.i160021_i160099;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

public class LoginTest {
    @Test
    public void OnLoginButtonClick() throws InterruptedException {
        //Perform type action on the view with id as email
        onView(withId(R.id.email)).perform(typeText("email@gmail.com"), click());
        //Perform type action on the view with id as password.
        //Note it is important to close the softkeyboard otherwise the login button may not be visible
        onView(withId(R.id.password)).perform(typeText("password"), closeSoftKeyboard());
        //Perform click action of view with id as login_btn and text as "Login" which is a descedant of view with id frame_login_container
        onView(allOf(withId(R.id.login), withText("Login")));

    }

}