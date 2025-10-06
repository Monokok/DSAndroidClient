package com.yeah.dsapp.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yeah.dsapp.R
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class RegistrationFragment : Fragment() {
    private val viewModel: RegistrationViewModel by inject()
    private lateinit var roleSpinner: Spinner
    private var userRole = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {

            roleSpinner = view.findViewById(R.id.spinner_role);
            // Устанавливаем адаптер с массивом из ресурсов
            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.roles_array,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roleSpinner.adapter = adapter

            // Обработчик выбора роли
            roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedRole = parent?.getItemAtPosition(position).toString()

                    // Привязываем к модели или используем в логике
                    when (selectedRole) {
                        "Обучающийся" -> {
                            // Роль: student
                            userRole = "student"
                        }
                        "Преподаватель" -> {
                            // Роль: teacher
                            userRole = "teacher"
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Обработка, если ничего не выбрано
                }
            }


            viewModel.errorEvent.observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show() //подписка на ошибки внутри VModel
            }
            viewModel.successEvent.observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                //if (viewModel.successEvent.toString() == "Регистрация успешна")
                    findNavController().popBackStack();
            }

            val firstNameEditText  = view.findViewById<EditText>(R.id.et_first_name)
            val  lastNameEditText  = view.findViewById<EditText>(R.id.et_last_name)
            val  middleNameEditText  = view.findViewById<EditText>(R.id.et_middle_name)
            var phoneNumberEditText = view.findViewById<EditText>(R.id.et_phone_number)
            var emailEditText = view.findViewById<EditText>(R.id.et_email)
            var passwordEditText = view.findViewById<EditText>(R.id.et_password)
            var passwordConfirmEditText = view.findViewById<EditText>(R.id.et_confirm_password)
            var registrationButton = view.findViewById<Button>(R.id.btn_register)

            registrationButton.setOnClickListener{
                lifecycleScope.launch {
                    try {
                        viewModel.register(
                            firstNameEditText.text.toString(),
                            lastNameEditText.text.toString(),
                            middleNameEditText.text.toString(),
                            phoneNumberEditText.text.toString(),
                            emailEditText.text.toString(),
                            passwordEditText.text.toString(),
                            passwordConfirmEditText.text.toString(),
                            userRole
                        )

                    }
                    catch (e: Exception){
                        Toast.makeText(context, "Произошла ошибка" + e.message, Toast.LENGTH_SHORT).show()
                    }
                }

            }


        }
        catch (e: Exception) {
            Toast.makeText(context, "Произошла ошибка" + e.message, Toast.LENGTH_SHORT).show()

        }
    }

        companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}