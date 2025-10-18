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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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

@Composable
fun ConsumoDeAguaCal(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    val generos = listOf("Masculino", "Femenino", "Sin especificar")
    var generoSeleccionado by remember { mutableStateOf(generos[0]) }

    var resultado by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }


    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Calculadora de Consumo de Agua",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de la persona") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso corporal (en kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = error?.contains("peso") == true
        )

        Column {
            Text("Género", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                generos.forEach { genero ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = (genero == generoSeleccionado),
                            onClick = { generoSeleccionado = genero }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (genero == generoSeleccionado),
                            onClick = { generoSeleccionado = genero }
                        )
                        Text(
                            text = genero,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
        
        Button(
            onClick = {
                // Limpiar mensajes anteriores
                error = null
                resultado = null

                val pesoDouble = peso.toDoubleOrNull()

                // Validaciones
                if (nombre.isBlank() || peso.isBlank()) {
                    error = "Todos los campos son obligatorios"
                    return@Button
                }
                if (pesoDouble == null) {
                    error = "El formato del peso no es válido."
                    return@Button
                }
                if (pesoDouble !in 5.0..200.0) {
                    error = "El peso debe ser un número positivo entre 5 y 200."
                    return@Button
                }

                // Cálculo
                val factorGenero = when (generoSeleccionado) {
                    "Masculino" -> 1.02
                    "Femenino" -> 1.01
                    else -> 1.00
                }
                val litrosRecomendados = pesoDouble * 0.035 * factorGenero
                resultado = "$nombre debe beber aproximadamente %.2f litros de agua al día".format(litrosRecomendados)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular")
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