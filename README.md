# UniPass KMP (Android + Desktop)

Versión **Kotlin Multiplatform** de UniPass: una app para estudiantes que buscan pasantías o prácticas
profesionales. Es la misma app en **Android** y en **Desktop** (Windows, macOS, Linux) con la interfaz y
la lógica compartidas.

Taller 4 – Microproyecto KMP · Electiva Desarrollo de Aplicaciones Móviles · Universidad del Cauca.

## Cómo ejecutarla

### Requisitos
- Android Studio (reciente) con JDK 17 o superior (el que trae Android Studio sirve).
- Internet la primera vez (Gradle descarga las dependencias y la API de ofertas necesita conexión).

### Abrir el proyecto
1. En Android Studio: **File → Open** y elige esta carpeta (`UniPassKMP`).
2. Espera a que termine el *Gradle Sync*.
3. **Importante:** si Android Studio sugiere "Upgrade Android Gradle Plugin", **no lo aceptes**.
   El proyecto usa AGP 8.11.1, Gradle 8.14.3, Kotlin 2.2.20 y Compose Multiplatform 1.9.0.

### Android
1. Crea o abre un emulador (o conecta un teléfono con depuración USB).
2. En la barra superior elige la configuración **composeApp** y pulsa ▶.

### Desktop
En la terminal, desde la carpeta del proyecto:

```bash
# Windows
gradlew.bat :composeApp:run

# macOS / Linux
./gradlew :composeApp:run
```

Para generar el instalador del sistema donde estés:

```bash
gradlew.bat :composeApp:packageDistributionForCurrentOS
```

## Arquitectura modular

```
UniPassKMP/
├── shared/                  <- módulo KMP con TODA la lógica (no tiene UI)
│   └── src/
│       ├── commonMain/      <- código compartido por Android y Desktop
│       │   ├── dominio/        modelos y validaciones
│       │   ├── datos/          repositorios (SQLDelight) y DriverFactory
│       │   ├── red/            OfertaApiService (Ktor + kotlinx.serialization)
│       │   ├── navegacion/     Destino + Navegador (navegación en la capa compartida)
│       │   ├── presentacion/   ViewModels (StateFlow)
│       │   ├── plataforma/     expect: nombrePlataforma(), sha256()
│       │   ├── sqldelight/     tablas y consultas (.sq)
│       │   └── AppContainer.kt crea y conecta todo
│       ├── androidMain/     <- actual: SQLite de Android, SHA-256
│       └── desktopMain/     <- actual: SQLite por JDBC, SHA-256
└── composeApp/              <- módulo de interfaz (Compose Multiplatform)
    └── src/
        ├── commonMain/ui/      App, tema, componentes y las 10 pantallas
        ├── androidMain/        MainActivity, Application, manifiesto
        └── desktopMain/        main() con la ventana de escritorio
```

Flujo de datos:

```
Pantallas (Compose)  ->  ViewModels  ->  Repositorios  ->  SQLDelight (base de datos local)
                                     \->  OfertaApiService (Ktor) -> API de Remotive
```

## Requisitos del taller y dónde se cumplen

| Requisito | Dónde |
|---|---|
| Al menos 2 plataformas | Android y Desktop (`composeApp`) |
| Al menos 5 pantallas | 10 pantallas en `composeApp/.../ui/pantallas` |
| Persistencia | SQLDelight: `shared/.../sqldelight` (usuarios, perfil, ofertas, guardadas, postulaciones, preferencias) |
| Navegación en la capa compartida | `shared/.../navegacion/Navegador.kt` y `Destino.kt` |
| Arquitectura modular | Módulos `:shared` y `:composeApp` + paquetes por responsabilidad |
| Descripción y créditos | `AcercaScreen` |
| Código distinto por plataforma | `expect/actual`: `Plataforma.kt`, `ManejadorAtras.kt` |

## Funcionalidades extra
- Ofertas reales desde Internet (API de Remotive con Ktor), con fecha de publicación y orden por más recientes.
- La **sesión se recuerda** al cerrar la app, y el **modo oscuro** también (se guardan en la base de datos).
- Filtros combinados (texto, ubicación, modalidad, tipo y requisito de grado).
- Diseño adaptable: barra inferior en pantallas angostas y barra lateral en Desktop.
- Contraseñas guardadas como hash SHA-256.

## Dónde se guardan los datos
- Android: base de datos interna de la app (`unipass.db`).
- Desktop: `<carpeta del usuario>/.unipass/unipass.db`.

## Trabajo en equipo (lo que pide el taller)
- Repositorio **público**.
- Cada integrante trabaja en **su propia rama** y hace commits propios.
