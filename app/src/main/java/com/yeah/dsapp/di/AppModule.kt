package com.yeah.dsapp.di


import CreatePracticeViewModel
import com.yeah.data.api.ApiService
import com.yeah.data.api.LessonApi
import com.yeah.data.api.UserApi
import com.yeah.data.db.AppDatabase
import com.yeah.data.repository.DbRepository
import com.yeah.data.repository.PracticeRepository
import com.yeah.data.repository.UserRepository
import com.yeah.data.services.UserService
import com.yeah.domain.model.Lesson
import com.yeah.domain.model.User
import com.yeah.domain.repository.IDatabaseRepository
import com.yeah.domain.repository.IRepository
import com.yeah.domain.services.IUserService
import com.yeah.dsapp.ui.dashboard.LessonsViewModel
import com.yeah.dsapp.ui.login.LoginViewModel
import com.yeah.dsapp.ui.profile.ProfileViewModel
import com.yeah.dsapp.ui.registration.RegistrationViewModel
import com.yeah.utils.ISharedPreferencesManager
import com.yeah.utils.SharedPreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module



val appModule = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().lessonDao() }

    single<UserApi> { ApiService.createUserApi(get()) }
    single<LessonApi> { ApiService.createLessonApi(get()) }


    single<ISharedPreferencesManager> { SharedPreferencesManager(androidContext()) }
    single<IDatabaseRepository> { DbRepository(get(), get(), get(),get(), get()) }

    single<IRepository<User>> { UserRepository(get(), get(), get()) }
    single<IRepository<Lesson>> {PracticeRepository(get(), get(), get())}

    single<IUserService> { UserService(get(), get(), get()) }

    //VMs:
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { LessonsViewModel(get()) }
    viewModel { CreatePracticeViewModel(get()) }

}
