package com.yeah.dsapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yeah.domain.model.User
import com.yeah.dsapp.R
import com.yeah.utils.ISharedPreferencesManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by inject()
    private val spManager: ISharedPreferencesManager by inject()

    private lateinit var firstNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var userRoleTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onResume() {
        super.onResume()
        getUserFromDb();
    }

    fun getUserFromDb(){
        // Подгрузка данных из БД
        viewLifecycleOwner.lifecycleScope.launch {
            var user: User? = viewModel.loadProfileData();

            if (user != null) {
                val fullName: String = user.firstName + " " + user.middleName + " " + user.lastName

                // Удаляем квадратные скобки и разделяем строку по запятым
                val roles = user.userRole
                    .removePrefix("[")
                    .removeSuffix("]")
                    .split(",")
                    .map { it.trim().removeSurrounding("\"") }

                // Обработка списка строк
                val role = roles.map {
                    when (it) {
                        "student" -> "Обучающийся"
                        "teacher" -> "Преподаватель"
                        else -> ""
                    }
                }.filter { it.isNotEmpty() }.joinToString(" ")

                firstNameTextView.text =  fullName
                firstNameTextView.visibility = View.VISIBLE
                emailTextView.text = user.email
                emailTextView.visibility = View.VISIBLE
                phoneTextView.text = user.phoneNumber
                phoneTextView.visibility = View.VISIBLE
                userRoleTextView.text = role
                userRoleTextView.visibility = View.VISIBLE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstNameTextView = view.findViewById(R.id.tv_user_name)
        emailTextView = view.findViewById(R.id.tv_user_email)
        phoneTextView = view.findViewById(R.id.tv_user_phone)
        userRoleTextView = view.findViewById(R.id.userUserRoleTextView)

        //если токена нет в sp - вывести текст "Войдите в свою учетную запись" и кнопку
        //если токен есть в sp - вывести из бд данные о юзере - он там будет один хранится
        val token = spManager.getAuthToken()
        getUserFromDb()
        if (token === null) {


            //кнопка войти
            view.findViewById<Button>(R.id.LoginButton).visibility = View.VISIBLE
            view.findViewById<Button>(R.id.LoginButton).setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }

            //кнопка зарегистрироваться
            view.findViewById<Button>(R.id.RegisterButton).visibility = View.VISIBLE;
            view.findViewById<Button>(R.id.RegisterButton).setOnClickListener {
//                Toast.makeText(context, "Регистрация пока не реализована", Toast.LENGTH_SHORT).show();
                findNavController().navigate(R.id.action_profileFragment_to_registrationFragment)
            }
        }
        else {

            view.findViewById<Button>(R.id.LogoutButton).visibility = View.VISIBLE
            view.findViewById<Button>(R.id.LogoutButton).setOnClickListener{
                //spManager.clearAuthToken(); <26.10.2024> - заменено на вызов метода из сервиса

                viewLifecycleOwner.lifecycleScope.launch() {
                    val result = async { viewModel.logout() }.await();

                    if (result) {
                        Toast.makeText(context, "Выход успешен", Toast.LENGTH_SHORT).show()

                        firstNameTextView.visibility = View.GONE

                        emailTextView.visibility = View.GONE

                        phoneTextView.visibility = View.GONE
                        //Навигация только после успешного logout!
                        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                    }
                    else
                        Toast.makeText(context, "Ошибка выхода", Toast.LENGTH_SHORT).show();
                }


            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}