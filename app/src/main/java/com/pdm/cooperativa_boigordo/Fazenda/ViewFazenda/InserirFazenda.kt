package com.pdm.cooperativa_boigordo.Fazenda.ViewFazenda

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.pdm.cooperativa_boigordo.Fazenda.ViewFazenda.ui.theme.Cooperativa_boigordoTheme
import com.pdm.cooperativa_boigordo.Shared.limparCampos

class InserirFazenda : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        val codigoParaEditar = intent.getStringExtra("codigoParaEditar")
        val isEdit = intent.getBooleanExtra("edit",false)

        setContent {
            Cooperativa_boigordoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    elementosInserirFazenda(codigoParaEditar,isEdit)
                }
            }
        }
    }
}

@Composable
fun elementosInserirFazenda(codigoParaEditar:String?,isEdit:Boolean?){

    val contexto = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val codigo =  remember{mutableStateOf(TextFieldValue())}
    if(isEdit == true){
        codigo.value = TextFieldValue(codigoParaEditar.toString())
    }
    val nome =  remember{mutableStateOf(TextFieldValue())}
    val valorPropriedade =  remember{mutableStateOf(TextFieldValue())}
    val qtdFuncionarios =  remember{mutableStateOf(TextFieldValue())}

    val activity=(LocalContext.current as? Activity)

    Column(
        Modifier.padding(40.dp)
    ) {
        Text(text = "Inserir Fazenda", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(25.dp))
        if (isEdit == true){
            TextField(
                value =codigo.value ,
                onValueChange ={codigo.value = it},
                placeholder = { Text(text = "ID Fazenda")},
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = true),
                textStyle = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif),
                maxLines = 1,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }else{
            TextField(
                value =codigo.value ,
                onValueChange ={codigo.value = it},
                placeholder = { Text(text = "ID Fazenda")},
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = true),
                textStyle = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif),
                maxLines = 1,
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value =nome.value ,
            onValueChange ={nome.value = it},
            placeholder = { Text(text = "Nome da Fazenda")},
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = true),
            textStyle = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif),
            maxLines = 1,
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value =valorPropriedade.value ,
            onValueChange ={valorPropriedade.value = it},
            placeholder = { Text(text = "Valor da Propriedade")},
            //keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = true),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            textStyle = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif),
            maxLines = 1,
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(15.dp))
        TextField(
            value =qtdFuncionarios.value ,
            onValueChange ={qtdFuncionarios.value = it},
            placeholder = { Text(text = "Quantidade de Funcionários")},
            //keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrect = true),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif),
            maxLines = 1,
            modifier = Modifier.fillMaxWidth())


        Button(
            onClick = {

                val fazenda = hashMapOf(
                    "codigo" to codigo.value.text,
                    "nome" to nome.value.text,
                    "valorPropriedade" to valorPropriedade.value.text.toDouble(),
                    "qtdFuncionarios" to qtdFuncionarios.value.text,
                )

                db.collection("fazenda").document(codigo.value.text).set(fazenda)
                    .addOnSuccessListener {
                        Toast.makeText(contexto, "Inserção realizada com sucesso", Toast.LENGTH_LONG).show()

                    }
                    .addOnFailureListener {
                        Toast.makeText(contexto, "Inserção não realizada", Toast.LENGTH_LONG).show()
                    }

                limparCampos(codigo, nome, valorPropriedade, qtdFuncionarios)

                if(isEdit == true){
                    Toast.makeText(contexto, "Fazenda atualizada com sucesso", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                    contexto.startActivity(Intent(contexto, GerenciaFazenda::class.java))
                }else
                {
                    Toast.makeText(contexto, "Fazenda inserida com sucesso", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if(isEdit==true){
                Text(text = "Atualizar")
            }else
            {
                Text(text = "Inserir")
            }
        }
        Spacer(modifier = Modifier.weight(1f, true))
        Button(onClick = {
            activity?.finish()
            contexto.startActivity(Intent(contexto, GerenciaFazenda::class.java))
         }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Voltar")
        }
    }

}

