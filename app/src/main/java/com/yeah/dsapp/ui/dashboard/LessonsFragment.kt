package com.yeah.dsapp.ui.dashboard

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yeah.domain.model.Lesson
import com.yeah.dsapp.R
import com.yeah.dsapp.databinding.LessonItemBinding
import com.yeah.dsapp.ui.dashboard.createLesson.CreatePracticeFragment
import com.yeah.dsapp.ui.dashboard.createLesson.TeacherCreatePracticeFragment
import com.yeah.dsapp.ui.profile.ProfileViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LessonsFragment : Fragment() {
    private var userRole: String = ""
    private val viewModel: LessonsViewModel by inject()
    private lateinit var addLessonButton: FloatingActionButton
    private lateinit var lessonsRecyclerView: RecyclerView
    private lateinit var lessonsAdapter: LessonsAdapter


    private var _binding: LessonItemBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        lifecycleScope.launch {
//            try {
//                var role = viewModel.getUserRole()
//                if (role != null){
//                    userRole = role;
//                }
//            }
//            catch (e: Exception){ }
//        }



//        val lessonsViewModel =
//            ViewModelProvider(this).get(LessonsViewModel::class.java)

//        _binding = LessonItemBinding.inflate(inflater, container, false)

//
//        val textView: TextView = binding.tvLessonTitle
//        lessonsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        val root: View = binding.root
//        return root
        return inflater.inflate(R.layout.fragment_lessons, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lessonsRecyclerView = view.findViewById(R.id.rv_lessons)
        lessonsRecyclerView.layoutManager = LinearLayoutManager(context)

        addLessonButton = view.findViewById(R.id.fab_add_lesson)

        //основной блок заполнения списка с занятиями:
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Ожидаем результат от getUserRole()
                val role = async {   viewModel.getUserRole() }.await()

                // Используем полученную роль
                if (role != null) {
                    // Ваш код с ролью, например, логика отображения или выполнения действий
                    userRole = role//println("User role: $role")
                    Log.d("LessonsFragment", "Role is $userRole")
                } else {
                    // Обработка случая, если роль пустая
                    Log.d("LessonsFragment", "Role is null")
                }
            } catch (e: Exception) {
                // Ловим исключение, если что-то пошло не так
                e.printStackTrace()
            }

            // Создаем адаптер
            lessonsAdapter = LessonsAdapter(emptyList(), userRole, viewModel)
            // Устанавливаем адаптер для RecyclerView
            lessonsRecyclerView.adapter = lessonsAdapter


            // Подгружаем занятия
            viewLifecycleOwner.lifecycleScope.launch {
                val result = async { viewModel.isUserLoggedIn() }.await()

                if (result) { // если есть залогиненный юзер
                    addLessonButton.visibility = View.VISIBLE
                    if (userRole == "student") {//для юзера своё меню создания
                        addLessonButton.setOnClickListener {
                            val bottomSheetFragment = CreatePracticeFragment()
                            // Устанавливаем слушатель для получения результата от фрагмента
                            parentFragmentManager.setFragmentResultListener(
                                "requestKey",
                                viewLifecycleOwner
                            ) { requestKey, result ->
                                // Когда фрагмент передает результат, показываем сообщение
                                val message = result.getString("key_result")

                                // Запускаем асинхронную задачу внутри жизненного цикла фрагмента
                                viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getLessons()  // Здесь будет выполняться асинхронная операция
                                }
                            }
                            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                        }
                    } else if (userRole == "teacher"){
                        addLessonButton.setOnClickListener {
                            val bottomSheetFragment = TeacherCreatePracticeFragment()
                            // Устанавливаем слушатель для получения результата от фрагмента
                            parentFragmentManager.setFragmentResultListener(
                                "requestKey",
                                viewLifecycleOwner
                            ) { requestKey, result ->
                                // Когда фрагмент передает результат, показываем сообщение
                                val message = result.getString("key_result")

                                // Запускаем асинхронную задачу внутри жизненного цикла фрагмента
                                viewLifecycleOwner.lifecycleScope.launch {
                                    viewModel.getLessons()  // Здесь будет выполняться асинхронная операция
                                }
                            }
                            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
                        }
                    }



//                    addLessonButton.setOnClickListener {
//                        val bottomSheetFragment = CreatePracticeFragment()
//                        // Устанавливаем слушатель для получения результата от фрагмента
//                        parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) { requestKey, result ->
//                            // Когда фрагмент передает результат, показываем сообщение
//                            val message = result.getString("key_result")
//                        }
//                        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
//                    }

                    try {
                        // Подписываемся на изменения в LiveData
                        viewModel.lessons.observe(viewLifecycleOwner, Observer { lessons ->
                            lessonsAdapter.submitList(lessons)
                        })


                    } catch (e: Exception) {
                        Log.d("LessonsFragment", "Ошибка подписки на изменения")
                        //Toast.makeText(context, "Ошибка подписки на изменения", Toast.LENGTH_SHORT).show()
                    }

                    viewModel.getLessons()


                } else {
                    addLessonButton.visibility = View.GONE
                }

            }
        }





    }

}