package dev.lchang.appue.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import dev.lchang.appue.data.model.AutoDeportivo
import java.text.NumberFormat
import java.util.Locale


// Mock Data (Datos harcodeados)
val listaDeAutos = listOf(
    AutoDeportivo("Porsche", "911 GT3 RS", 250000.0,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-9SNRtr2kHFMkDsA9ZzYS56NUOz6t3eWnUg&s"),
    AutoDeportivo("Ferrari", "SF90 Stradale", 500000.0,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9yud9_0sFu9sNdkmYAQWJMkzeZW3JegU8Cg&s"),
    AutoDeportivo("Lamborghini", "Huracán EVO", 280000.0,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3pfTqnjJTxqKRf3OYFrteh1L3objnJ5furw&s"),
    AutoDeportivo("McLaren", "720S", 310000.0,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTh-3bpT2GBknotWCYNq0Cop7l6bG-HwbJN7A&s"),
    AutoDeportivo("Audi", "R8 V10", 180000.0,
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4MPqwI0rDINefIGZrtz5hKjYlmNE7Te6yag&s")
)

@Composable
fun CatalogoDeAutosDep(navController: NavController) {
    val costoTotal = listaDeAutos.sumOf { it.precio }
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Catálogo de Autos Deportivos",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(listaDeAutos) { auto ->
                AutoCard(auto = auto)
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Regresar al Menú Principal")
        }

        Surface(shadowElevation = 8.dp) {
            Text(
                text = "Costo Total: ${formatter.format(costoTotal)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun AutoCard(auto: AutoDeportivo) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            AsyncImage(
                model = auto.imageUrl,
                contentDescription = "Imagen de ${auto.marca} ${auto.modelo}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${auto.marca} ${auto.modelo}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Precio Aprox: ${formatter.format(auto.precio)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}