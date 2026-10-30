# Rúbricas FCCh

App móvil (Android, Kotlin + Jetpack Compose) para docentes e instructores de la Fundación Carabineros de Chile: carga de rúbricas, evaluación desde el teléfono y descarga en PDF.

**Asignatura:** DSY1105 – Desarrollo de Aplicaciones Móviles · Evaluación Parcial 2
**Estudiantes:** _(completar nombres)_

## Funcionalidades implementadas (avance 1)

- Login con correo Gmail y contraseña, consultando una base de datos Room (SQLite)
- Usuarios guardados localmente (nombre, correo único, rol) con contraseña protegida con hash SHA-256 y sal
- Validación por campo con ícono y mensaje visual
- Lógica de validación y de acceso separada de la pantalla (`LoginLogica.kt`)
- Animaciones: cambio de color del ícono (`animateColorAsState`) y aparición de mensajes (`AnimatedVisibility`)
- `rememberSaveable` para conservar lo escrito al girar el dispositivo
- Editar datos de usuario (`EditarActivity`): nombre y contraseña opcional
- Cargar documentos (selector de archivos del sistema), ver (abrir con otra app) y eliminar (con confirmación), guardados en Room
- Calificar alumnos: agregar alumnos, registrar notas (1,0 a 7,0) con comentario, promedio con color animado y eliminación con confirmación, guardados en Room (`CalificacionesUI.kt`, `CalificacionLogica.kt`)
- Botón "Llamar a soporte" con solicitud del permiso CALL_PHONE
- Vista de bienvenida (`InicioActivity`) con barra superior (inicio, editar datos, cerrar sesión) e inferior (cargar, ver, eliminar)
- Registro de usuarios (`RegistroActivity`): nombre, correo, contraseña, confirmación y rol, con validación por campo y correo único
- Pantalla de bienvenida (`BienvenidaActivity`) como primera pantalla, con acceso al login
- Navegación a una segunda pantalla (`InicioActivity`) con `Intent`

## Usuarios ficticios de prueba

| Correo | Contraseña |
|---|---|
| docente.demo@gmail.com | docente123 |
| instructor.demo@gmail.com | instructor123 |

## Arquitectura (MVVM)

La app separa **Modelo, Vista y ViewModel** para que la interfaz no contenga lógica:

- **Vista (`ui/`)**: solo dibuja el estado y avisa de las acciones del usuario. Las pantallas no validan ni consultan la base de datos.
- **ViewModel (`viewmodel/`)**: guarda el estado (campos, errores, listas) con `mutableStateOf`, sobrevive a giros de pantalla y llama a la lógica.
- **Modelo (`data/` y `logic/`)**: entidades y DAOs de Room, y las validaciones y reglas de negocio, sin depender de Compose.

```
app/src/main/java/com/example/appprueba/
├── BienvenidaActivity.kt   Primera pantalla (bienvenida)
├── MainActivity.kt         Activity del login (delgada)
├── RegistroActivity.kt     Activity del registro (delgada)
├── EditarActivity.kt       Activity de edición de datos (delgada)
├── InicioActivity.kt       Activity principal (crea los ViewModels)
├── data/                   MODELO: persistencia local con Room
│   ├── AppDatabase.kt      Base de datos (versión 4)
│   ├── BaseDatos.kt        Instancia única y migraciones
│   ├── Usuario.kt / UsuarioDAO.kt
│   ├── Documento.kt / DocumentoDAO.kt
│   └── Alumno.kt / Calificacion.kt / AlumnoDAO.kt
├── logic/                  MODELO: validaciones y reglas (sin Compose)
│   ├── LoginLogica.kt      Login, registro, hash de contraseñas
│   ├── DocumentoLogica.kt  Validación de documentos
│   └── CalificacionLogica.kt  Validación de notas y promedio
├── viewmodel/              VIEWMODEL: estado y acciones de cada pantalla
│   ├── LoginViewModel.kt
│   ├── RegistroViewModel.kt
│   ├── EditarViewModel.kt
│   ├── InicioViewModel.kt
│   ├── DocumentosViewModel.kt
│   └── CalificacionesViewModel.kt
└── ui/                     VISTA: solo interfaz
    ├── components/         Componentes reutilizables (CampoFormulario, tarjetas)
    ├── screens/            Pantallas (Login, Registro, Editar, Inicio, Documentos, Calificaciones)
    └── theme/              Colores, tema y tipografía
```

Pruebas unitarias de la lógica en `app/src/test/.../LogicaTest.kt` (se ejecutan sin emulador).

## Cómo ejecutar

1. Abrir la carpeta del proyecto en Android Studio (**File > Open**).
2. Esperar la sincronización de Gradle (mismas versiones que los ejemplos del curso).
3. Ejecutar en un emulador o dispositivo con Android 13 (API 33) o superior.
