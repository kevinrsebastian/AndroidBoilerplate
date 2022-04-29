package ph.teko.app.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ph.teko.app.user_model.usecase.UserUseCase
import javax.inject.Inject

@HiltViewModel
internal class SplashActivityVm @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    // VM State: isLoading
    val isLoading = ObservableField(false)
    fun setLoading(value: Boolean) = isLoading.set(value)

    fun checkForLoggedInUser(navigateToHomeScreen: () -> Unit) {
        setLoading(true)
        if (userUseCase.getUserFromCache() != null) {
            navigateToHomeScreen()
        }
        setLoading(false)
    }
}
