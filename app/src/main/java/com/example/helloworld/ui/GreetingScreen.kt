package com.example.helloworld.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helloworld.ui.theme.HelloWorldTheme
import kotlinx.coroutines.launch

/**
 * MINGGU 1 — Pemrograman Mobile Android
 *
 * GreetingScreen telah dimodifikasi menjadi Form Data Mahasiswa/Pegawai.
 */
@Composable
fun GreetingScreen() {
    // Context untuk Toast dan Snackbar
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // ==========================================
    // STATE MANAGEMENT
    // ==========================================
    // Menggunakan rememberSaveable agar data tetap ada saat rotasi layar
    var fullName by rememberSaveable { mutableStateOf("") }
    var idNumber by rememberSaveable { mutableStateOf("") }
    
    // State untuk hasil yang akan ditampilkan setelah submit
    var submittedName by rememberSaveable { mutableStateOf("") }
    var submittedId by rememberSaveable { mutableStateOf("") }
    var isSubmitted by rememberSaveable { mutableStateOf(false) }

    // State untuk error validation
    var nameError by remember { mutableStateOf<String?>(null) }
    var idError by remember { mutableStateOf<String?>(null) }

    // Fungsi validasi
    fun validate(): Boolean {
        var isValid = true
        
        if (fullName.isBlank()) {
            nameError = "Nama tidak boleh kosong"
            isValid = false
        } else {
            nameError = null
        }

        if (idNumber.isBlank()) {
            idError = "NIM/NIP tidak boleh kosong"
            isValid = false
        } else if (!idNumber.all { it.isDigit() }) {
            idError = "NIM/NIP hanya boleh berisi angka"
            isValid = false
        } else {
            idError = null
        }

        return isValid
    }

    // Scaffold digunakan untuk mempermudah penempatan SnackbarHost
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        // Column Utama dengan dukungan Scroll jika layar kecil
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Header Icon & Title ──
            Icon(
                imageVector = Icons.Default.AssignmentInd,
                contentDescription = "Form Icon",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Form Data Mahasiswa",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White // Paksa Putih agar mudah dibaca di atas Merah
            )

            Text(
                text = "Silakan lengkapi informasi di bawah ini",
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            // ── Form Card ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Input Nama Lengkap
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { 
                            fullName = it
                            if (nameError != null) nameError = null 
                        },
                        label = { Text("Nama Lengkap") },
                        placeholder = { Text("Contoh: Budi Santoso") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        isError = nameError != null,
                        supportingText = { nameError?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Input NIM/NIP
                    OutlinedTextField(
                        value = idNumber,
                        onValueChange = { input ->
                            // Hanya menerima input angka
                            if (input.all { it.isDigit() }) {
                                idNumber = input
                                if (idError != null) idError = null
                            }
                        },
                        label = { Text("NIM/NIP") },
                        placeholder = { Text("Contoh: 220101001") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = idError != null,
                        supportingText = { idError?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Tombol Submit
                    Button(
                        onClick = {
                            if (validate()) {
                                // Simpan data ke state hasil
                                submittedName = fullName
                                submittedId = idNumber
                                isSubmitted = true
                                
                                // Tutup keyboard
                                focusManager.clearFocus()
                                
                                // Tampilkan Snackbar
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Data berhasil disimpan",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        // Tombol hanya aktif jika kedua field terisi
                        enabled = fullName.isNotBlank() && idNumber.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Submit Data", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Display Result Card ──
            AnimatedVisibility(
                visible = isSubmitted,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✅ Data Berhasil Disimpan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f)
                        )

                        ResultItem(label = "Nama Lengkap", value = submittedName)
                        Spacer(modifier = Modifier.height(12.dp))
                        ResultItem(label = "NIM/NIP", value = submittedId)
                    }
                }
            }
        }
    }
}

@Composable
fun ResultItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview(showBackground = true, name = "Form Mahasiswa Preview")
@Composable
fun GreetingScreenPreview() {
    HelloWorldTheme(darkTheme = false) {
        GreetingScreen()
    }
}

@Preview(showBackground = true, name = "Form Mahasiswa Dark Preview")
@Composable
fun GreetingScreenDarkPreview() {
    HelloWorldTheme(darkTheme = true) {
        GreetingScreen()
    }
}
