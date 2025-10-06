package com.yeah.dsapp.ui.dashboard.createLesson

import CreatePracticeViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeah.domain.model.Lesson
import com.yeah.dsapp.R
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.component.getScopeId
import java.util.Calendar

class CreatePracticeFragment : BottomSheetDialogFragment() {
    private var lastCreatedLesson: Lesson? = null
    private val viewModel: CreatePracticeViewModel by inject()
    private lateinit var spTeacher: Spinner
    private lateinit var spCategory: Spinner
    private lateinit var etDate: EditText
    private lateinit var spTime: Spinner
    private lateinit var btnCreate: Button
    private var teacherId: String = ""
    private var selectedTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Инфлейтим XML макет
        val view = inflater.inflate(R.layout.bottom_sheet_add_lesson, container, false)

        spTeacher = view.findViewById(R.id.sp_teacher)

//        spCategory = view.findViewById(R.id.sp_category)
        spTime = view.findViewById(R.id.sp_time)
        etDate = view.findViewById(R.id.et_date)
        btnCreate = view.findViewById(R.id.btn_create)

        setupListeners()

        // Наблюдаем за списком преподавателей
        viewModel.teachers.observe(viewLifecycleOwner) { teachers ->
            // Обновляем адаптер для Spinner
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                teachers.map { it.firstName + " " + it.middleName + " " + it.middleName + " " + it.email}
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spTeacher.adapter = adapter
        }
        // Наблюдаем за списком доступных дат
        viewModel.times.observe(viewLifecycleOwner) { times ->
            // Обновляем адаптер для Spinner
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                times
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spTime.adapter = adapter
        }

        viewModel.loadTeachers();



        return view
    }

    private fun setupListeners() {
        // Обработка нажатия на DatePicker
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            //диалог выбора даты
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                //установка даты в поле
                etDate.setText(String.format("%02d.%02d.%d", dayOfMonth, month + 1, year))
                if (teacherId != "")
                    viewModel.loadTimes(teacherId, etDate.text.toString());
                //Toast.makeText(context, etDate.text.toString(), Toast.LENGTH_SHORT).show()
                //проверка вводимых значений
                validateInputs()
            }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()



        }

        // Обработка нажатия на время
        spTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                validateInputs()
                var obj = viewModel.times.value?.get(position)
                obj?.let{
                    selectedTime = obj;
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Валидация на заполненность
        //выбрали преподавателя - очистили список из времени
        spTeacher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                validateInputs()
                viewModel.clearTimes()//очистили времена
                // Сохраняем id выбранного преподавателя в переменную
                val selectedTeacher = viewModel.teachers.value?.get(position)//сохранили преподавателя
                // Если преподаватель выбран, сохраняем его id
                selectedTeacher?.let {
                    teacherId = it.id
                    // Проверка в логе
                    //Log.d("Selected Teacher ID", teacherId)
                    //Toast.makeText(context, teacherId + " " + selectedTeacher.firstName, Toast.LENGTH_SHORT).show()
                } ?: run{
                    teacherId = ""
                    //Log.d("Selected Teacher ID", "No teacher selected")
                    //Toast.makeText(context, "Преподаватель не выбран", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

//        spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                validateInputs()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }

        // Кнопка "Создать"
        btnCreate.setOnClickListener {
            // Логика создания записи
            //val teacher = spTeacher.selectedItem.toString()
            //val category = spCategory.selectedItem.toString()
            val date = etDate.text.toString()
            if (teacherId != "" && selectedTime != ""){
                Toast.makeText(requireContext(), "Отправка запроса на создание урока", Toast.LENGTH_SHORT).show()

                viewLifecycleOwner.lifecycleScope.launch {
                    lastCreatedLesson = async {
                        viewModel.createLesson(teacherId, etDate.text.toString(), selectedTime)
                    }.await() //дожидаемся окончания выполнения создания урока - он создастся в локальной бд.
                    if (lastCreatedLesson != null) {
                        dismiss()
                    }
                }
            }
        }
        //val time = spTime.selectedItem.toString()

    }

    private fun validateInputs() {
//        btnCreate.isEnabled = spTeacher.
//                && etDate.text.isNotEmpty()
//                && spTime.isSelected
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // Отправляем результат в родительский фрагмент
        val result = Bundle().apply {
            putString("key_result", "isClosed")
        }
        parentFragmentManager.setFragmentResult("requestKey", result)
    }


}