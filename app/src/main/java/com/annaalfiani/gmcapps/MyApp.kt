package com.annaalfiani.gmcapps

import android.app.Application
import com.annaalfiani.gmcapps.repositories.*
import com.annaalfiani.gmcapps.ui.detail_film.DetailFilmViewModel
import com.annaalfiani.gmcapps.ui.detail_movie.DetailViewModel
import com.annaalfiani.gmcapps.ui.login.SignInViewModel
import com.annaalfiani.gmcapps.ui.main.home.HomeViewModel
import com.annaalfiani.gmcapps.ui.main.profile.ProfileViewModel
import com.annaalfiani.gmcapps.ui.main.ticket.TicketViewModel
import com.annaalfiani.gmcapps.ui.order.OrderViewModel
import com.annaalfiani.gmcapps.ui.register.SignUpViewModel
import com.annaalfiani.gmcapps.ui.update_profil.UpdateProfilViewModel
import com.annaalfiani.gmcapps.webservices.ApiCllient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(retrofitModule, repositoryModule, viewModelModule))
        }
    }
}

val retrofitModule = module {
    single { ApiCllient.instance() }
}
val repositoryModule = module {
    factory { UserRepository(get()) }
    factory { SeatRepository(get()) }
    factory { MovieRepository(get()) }
    factory { OrderRepository(get()) }
    factory { SchedulleRepository(get()) }
}
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailFilmViewModel(get()) }


    viewModel { ProfileViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { OrderViewModel(get(), get(), get(), get()) }
    viewModel { TicketViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { UpdateProfilViewModel(get()) }
}