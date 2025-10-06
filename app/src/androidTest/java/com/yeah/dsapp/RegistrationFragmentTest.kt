package com.yeah.dsapp

import android.widget.EditText
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`

@RunWith(AndroidJUnit4::class)
class RegistrationFragmentTest {

    @Before
    fun setup() {
        // Запускаем активити перед каждым тестом
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testRegistrationLayout() {
        // Проверка, что заголовок экрана присутствует
        onView(withId(R.id.tv_register_title)).check(matches(isDisplayed()))

        // Проверка, что поле "Имя" отображается
        onView(withId(R.id.et_first_name)).check(matches(isDisplayed()))

        // Проверка, что поле "Фамилия" отображается
        onView(withId(R.id.et_last_name)).check(matches(isDisplayed()))

        // Проверка, что поле "Отчество" отображается
        onView(withId(R.id.et_middle_name)).check(matches(isDisplayed()))

        // Проверка, что поле "Телефон" отображается
        onView(withId(R.id.et_phone_number)).check(matches(isDisplayed()))

        // Проверка, что поле "Почта" отображается
        onView(withId(R.id.et_email)).check(matches(isDisplayed()))

        // Проверка, что поле "Пароль" отображается
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))

        // Проверка, что поле "Подтверждение пароля" отображается
        onView(withId(R.id.et_confirm_password)).check(matches(isDisplayed()))

        // Проверка, что выпадающий список "Роль" отображается
        onView(withId(R.id.spinner_role)).check(matches(isDisplayed()))

        // Проверка, что кнопка "Зарегистрироваться" отображается
        onView(withId(R.id.btn_register)).check(matches(isDisplayed()))
    }

    @Test
    fun testFillFormAndSubmit() {
        // Заполнение всех полей
        onView(withId(R.id.et_first_name)).perform(typeText("Иван"), closeSoftKeyboard())
        onView(withId(R.id.et_last_name)).perform(typeText("Иванов"), closeSoftKeyboard())
        onView(withId(R.id.et_middle_name)).perform(typeText("Иванович"), closeSoftKeyboard())
        onView(withId(R.id.et_phone_number)).perform(typeText("1234567890"), closeSoftKeyboard())
        onView(withId(R.id.et_email)).perform(typeText("ivanov@example.com"), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.et_confirm_password)).perform(typeText("password123"), closeSoftKeyboard())

        // Выбор роли в Spinner
        onView(withId(R.id.spinner_role)).perform(click())
        // Допустим, выберем первый элемент (это зависит от данных в адаптере Spinner)
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Студент"))).perform(click())

        // Нажимаем кнопку "Зарегистрироваться"
        onView(withId(R.id.btn_register)).perform(click())

        // Проверка, что после нажатия кнопки регистрации произошел переход
        // (например, переходим на новый экран, и проверяем, что новый экран отображается)
        // Для этого вам нужно проверить, что ожидаемый элемент на новом экране присутствует.
    }
}
