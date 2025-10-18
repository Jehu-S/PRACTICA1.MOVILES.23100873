package dev.lchang.appue.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroDeActFisica(navController: NavController) {
    // Estados para los inputs del usuario
    val actividades = listOf("Correr", "Caminar", "Nadar", "Ciclismo", "Yoga")
    var actividadSeleccionada by remember { mutableStateOf(actividades[0]) }
    var duracion by remember { mutableStateOf("") }
    val intensidades = listOf("Baja", "Media", "Alta")
    var intensidadSeleccionada by remember { mutableStateOf(intensidades[1]) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Estados para los mensajes de resultado y error
    var resultado by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registro de Actividad Física",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Dropdown para Tipo de Actividad
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded }
        ) {
            TextField(
                value = actividadSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de actividad") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                actividades.forEach { actividad ->
                    DropdownMenuItem(
                        text = { Text(actividad) },
                        onClick = {
                            actividadSeleccionada = actividad
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        // Input para Duración
        OutlinedTextField(
            value = duracion,
            onValueChange = { duracion = it },
            label = { Text("Duración (en minutos)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = error?.contains("duración") == true
        )

        // RadioButtons para Intensidad
        Column {
            Text("Intensidad", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                intensidades.forEach { intensidad ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = (intensidad == intensidadSeleccionada),
                            onClick = { intensidadSeleccionada = intensidad }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (intensidad == intensidadSeleccionada),
                            onClick = { intensidadSeleccionada = intensidad }
                        )
                        Text(text = intensidad, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }
        }

        // Botón para Calcular
        Button(
            onClick = {
                error = null
                resultado = null

                val duracionInt = duracion.toIntOrNull()

                if (duracion.isBlank()) {
                    error = "Todos los campos son obligatorios."
                    return@Button
                }
                if (duracionInt == null || duracionInt <= 0) {
                    error = "La duración en minutos debe ser un número entero positivo."
                    return@Button
                }

                val caloriasPorMin = when (actividadSeleccionada) {
                    "Correr" -> 10
                    "Caminar" -> 5
                    "Nadar" -> 8
                    "Ciclismo" -> 7
                    else -> 4 // Yoga
                }

                val factorIntensidad = when (intensidadSeleccionada) {
                    "Baja" -> 0.8
                    "Media" -> 1.0
                    else -> 1.2 // Alta
                }

                val caloriasQuemadas = caloriasPorMin * duracionInt * factorIntensidad
                resultado = "Calorías quemadas: %.2f kcal".format(caloriasQuemadas)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular Calorías Quemadas")
        }

        // Mostrar mensaje de error o resultado
        error?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        }
        resultado?.let {
            Text(text = it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyLarge)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Regresar al Menú Principal")
        }
    }
}