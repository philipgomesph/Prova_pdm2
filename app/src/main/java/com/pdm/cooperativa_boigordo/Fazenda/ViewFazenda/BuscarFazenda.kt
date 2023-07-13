package com.pdm.cooperativa_boigordo.Fazenda.ViewFazenda

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.pdm.cooperativa_boigordo.Fazenda.ModelFazenda
import com.pdm.cooperativa_boigordo.Fazenda.ViewFazenda.ui.theme.Cooperativa_boigordoTheme

class BuscarFazenda : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Cooperativa_boigordoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    elementosBuscarFazenda()
                }
            }
        }
    }
}
@Composable
fun elementosBuscarFazenda()  {
    val contexto = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val activity = (LocalContext.current as? Activity)

    val fazenda = remember {
        mutableStateListOf<ModelFazenda>()
    }
    val valorTotal = remember { mutableStateListOf<Double>() }
    val nomeBusca = remember { mutableStateOf(TextFieldValue()) }
    val codigoBusca = remember { mutableStateOf(TextFieldValue()) }

    db.collection("fazenda")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                val fazendaData = document.data
                val fazendaGet = ModelFazenda(
                    codigo = fazendaData["codigo"].toString(),
                    nome = fazendaData["nome"].toString(),
                    valorPropriedade = fazendaData["valorPropriedade"].toString().toDouble(),
                    qtdFuncionarios = fazendaData["qtdFuncionarios"].toString().toInt(),
                )
                fazenda.add(fazendaGet)
            }
        }
        .addOnFailureListener { erro ->
            println("Erro ao consultar Fazendo no banco: $erro")
        }

    LazyColumn(
        modifier = Modifier.padding(14.dp)
    ) {
        item {
            Text(
                text = "Gerenciar Fazendas",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(fazenda.size) { index ->
            val fazendaItem = fazenda[index]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)

            ) {
                Text(
                    text = fazendaItem.nome,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Código: ${fazendaItem.codigo}",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Propriedade: ${fazendaItem.valorPropriedade}",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "Funcionários: ${fazendaItem.qtdFuncionarios}",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth()
                )
                valorTotal.add(fazendaItem.valorPropriedade)
            }
        }

        item {
            TextField(
                value = nomeBusca.value,
                onValueChange = { nomeBusca.value = it },
                label = { Text(text = "Nome da Fazenda") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    fazenda.clear()
                    db.collection("fazenda")
                        .whereEqualTo("nome", nomeBusca.value.text)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val fazendaData = document.data
                                val fazendaGet = ModelFazenda(
                                    codigo = fazendaData["codigo"].toString(),
                                    nome = fazendaData["nome"].toString(),
                                    valorPropriedade = fazendaData["valorPropriedade"].toString()
                                        .toDouble(),
                                    qtdFuncionarios = fazendaData["qtdFuncionarios"].toString()
                                        .toInt(),
                                )
                                fazenda.add(fazendaGet)
                            }
                        }
                        .addOnFailureListener { erro ->
                            println("Erro ao consultar Fazendo no banco: $erro")
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Buscar por Nome")
            }

            TextField(
                value = codigoBusca.value,
                onValueChange = { codigoBusca.value = it },
                label = { Text(text = "Código da Fazenda") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    fazenda.clear()
                    db.collection("fazenda")
                        .whereEqualTo("codigo", codigoBusca.value.text)
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                val fazendaData = document.data
                                val fazendaGet = ModelFazenda(
                                    codigo = fazendaData["codigo"].toString(),
                                    nome = fazendaData["nome"].toString(),
                                    valorPropriedade = fazendaData["valorPropriedade"].toString()
                                        .toDouble(),
                                    qtdFuncionarios = fazendaData["qtdFuncionarios"].toString()
                                        .toInt(),
                                )
                                fazenda.add(fazendaGet)
                            }
                        }
                        .addOnFailureListener { erro ->
                            println("Erro ao consultar Fazendo no banco: $erro")
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Buscar por Código")
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { activity?.finish() }, modifier = Modifier.weight(1f)) {
                    Text(text = "Voltar")
                }
            }
        }
    }
}