package com.yeah.dsapp.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yeah.dsapp.R
import com.yeah.dsapp.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val loginEditText = view.findViewById<EditText>(R.id.editTextLogin)
            val passwordEditText = view.findViewById<EditText>(R.id.editTextPassword)
            val errorTextView = view.findViewById<TextView>(R.id.loginErrorField)

            //view.findViewById<Button>(R.id.buttonLogin).visibility = View.VISIBLE;
            view.findViewById<Button>(R.id.buttonLogin).setOnClickListener {
                lifecycleScope.launch {
                    try {
                        var result: Pair<String, String> = viewModel.loginIn(
                            loginEditText?.text.toString(),
                            passwordEditText?.text.toString()
                        )
                        errorTextView.text = result.second;
                        if (result.second === "Login success"){
                            //возврат в профиль
                            Toast.makeText(context, "Вход выполнен", Toast.LENGTH_SHORT).show();
                            findNavController().popBackStack();
                        }
                    } catch (e: Exception) {
                        // Обработка ошибки
                        handleLoginError(e)
                    }
                }
            }
        } catch (e: Exception)
        {
            Log.e("LoginFragment", e.message.toString())
            view.findViewById<TextView>(R.id.errorTextMessage).text = e.message.toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun handleLoginError(e: Exception) {
        Log.e("LoginFragment", e.message.toString() + ' ' + e.toString())
    }
}