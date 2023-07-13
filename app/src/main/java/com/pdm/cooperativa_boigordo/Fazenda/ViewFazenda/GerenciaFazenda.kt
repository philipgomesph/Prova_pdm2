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
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.pdm.cooperativa_boigordo.Fazenda.ModelFazenda
import com.pdm.cooperativa_boigordo.Fazenda.ViewFazenda.ui.theme.Cooperativa_boigordoTheme

class GerenciaFazenda : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Cooperativa_boigordoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    elementosGerenciaFazenda()
                }
            }
        }
    }
}

@Composable
fun elementosGerenciaFazenda(){

    val contexto = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val activity=(LocalContext.current as? Activity)

    val fazenda = remember {
        mutableStateListOf<ModelFazenda>()
    }
    val valorTotal= remember{  mutableStateListOf<Double>()}
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
        item{
            Text(
                text = "Gerenciar Fazendas",
                style = TextStyle(fontSize = 20.sp,fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(fazenda.size) { index ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)

            ) {
                Text(
                    text = "${fazenda[index].nome}",

                    style = TextStyle(fontSize = 16.sp,fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Codigo: ${fazenda[index].codigo}",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth()
                )


                Text(
                    text = "Propriedade: ${fazenda[index].valorPropriedade}",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth() ,

                )
                Text(
                    text = "Funcionarios: ${fazenda[index].qtdFuncionarios}",
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.fillMaxWidth()
                )
                valorTotal.add(fazenda[index].valorPropriedade)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(
                        onClick = {
                            var codigoFazenda = fazenda[index].codigo
                            val intent = Intent(contexto, InserirFazenda::class.java)
                            intent.putExtra("codigoParaEditar", codigoFazenda)
                            intent.putExtra("edit", true)
                            contexto.startActivity(intent)
                            activity?.finish()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Editar")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            db.collection("fazenda").document( fazenda[index].codigo).delete()
                                .addOnSuccessListener {
                                    Toast.makeText(contexto, "Fazenda excluída com sucesso", Toast.LENGTH_LONG).show()
                                    activity?.finish()
                                    contexto.startActivity(Intent(contexto, GerenciaFazenda::class.java))
                                }
                                .addOnFailureListener {
                                    Toast.makeText(contexto, "Falha ao excluir a fazenda: $it", Toast.LENGTH_LONG).show()
                                    Log.d("Exclusão da fazenda", it.toString())
                                }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Deletar")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                }
            }

        }

        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Button(onClick = { activity?.finish() }, modifier = Modifier.weight(1f)) {
                    Text(text = "Voltar")
                }
                var mostraValorTotal = 0.0
                valorTotal.forEach {
                    mostraValorTotal += it
                }

                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
                    Toast.makeText(contexto,"Valor total: R$ ${mostraValorTotal}" , Toast.LENGTH_SHORT).show() },
                    modifier = Modifier.weight(1f))
                {
                    Text(text = "Calcular valor total")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

        }


    }
}




