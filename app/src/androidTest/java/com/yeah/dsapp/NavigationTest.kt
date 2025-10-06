package com.yeah.dsapp
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Test
    fun testNavigationToRegistrationFragment() {
        // Шаг 1: Запускаем активити с нижней навигацией
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Шаг 2: Переключаемся на фрагмент профиля (кнопка профиля в нижней навигации)
        onView(withId(R.id.profileFragment)).perform(click())

        // Шаг 3: Проверяем, что фрагмент профиля отображается
        onView(withId(R.id.profileFragment))
            .check(matches(isDisplayed()))
    }
}