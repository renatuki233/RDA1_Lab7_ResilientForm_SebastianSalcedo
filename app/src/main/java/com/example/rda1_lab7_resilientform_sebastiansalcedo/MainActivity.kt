package com.example.rda1_lab7_resilientform_sebastiansalcedo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.ui.Alignment
import androidx.savedstate.SavedStateRegistryOwner
import com.example.rda1_lab7_resilientform_sebastiansalcedo.data.UserPreferences
import com.example.rda1_lab7_resilientform_sebastiansalcedo.ui.FormViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences =
            UserPreferences(applicationContext)

        setContent {

            MaterialTheme {

                val factory =
                    FormViewModelFactory(
                        owner = this@MainActivity,
                        userPreferences = userPreferences
                    )

                val formViewModel: FormViewModel =
                    viewModel(factory = factory)

                ResilientFormScreen(
                    viewModel = formViewModel
                )


            }
        }
    }
}

class FormViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val userPreferences: UserPreferences
) : AbstractSavedStateViewModelFactory(
    owner,
    null
) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {

        @Suppress("UNCHECKED_CAST")
        return FormViewModel(
            stateHandle = handle,
            userPrefs = userPreferences
        ) as T
    }
}

@Composable
fun ResilientFormScreen(
    viewModel: FormViewModel
) {

    val nameDisk by
    viewModel.nameFromDisk.observeAsState("")

    var nameInput by remember {
        mutableStateOf("")
    }

    LaunchedEffect(nameDisk) {
        if (nameInput.isEmpty()) {
            nameInput = nameDisk
        }
    }

    BackHandler(
        enabled =
        nameInput.isNotEmpty() ||
                viewModel.email.isNotEmpty()
    ) {
        Log.d(
            "NAV",
            "El usuario intentó retroceder con datos en el formulario"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Borrador de Perfil",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // NOMBRE - DataStore
        OutlinedTextField(
            value = nameInput,
            onValueChange = {
                nameInput = it
                viewModel.saveName(it)
            },
            label = {
                Text("Nombre (Persiste al cerrar app)")
            }
        )

        // INDICADOR DE GUARDADO
        if (viewModel.showSavedIndicator) {
            Text(
                text = "☁ Guardado en DataStore",
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // EMAIL - SavedStateHandle
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = {
                viewModel.updateEmail(it)
            },
            label = {
                Text("Email (Persiste al rotar/muerte proceso)")
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // NOTIFICACIONES - ViewModel
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Notificaciones",
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = viewModel.notificationsEnabled,
                onCheckedChange = {
                    viewModel.updateNotifications(it)
                }
            )
        }
    }

}