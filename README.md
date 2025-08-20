
# Aplicacion ; Simuladores Inercia

Aplicación Android para la comunidad Simuladores Inercia: reserva rigs, gestion de cuenta consultar estadísticas de piloto con métricas avanzadas y participa en eventos o campeonatos. Pensada para producción, con arquitectura limpia.

## Vista rápida

Reservas: disponibilidad en tiempo real, detección de solapes, lista de espera, recordatorios, check-in por whatsapp, políticas de cancelación configurables.

Sistema de cuenta: email/Google sign-in, roles (admin/staff/piloto), perfiles verificados, dispositivos de confianza, recuperación de contraseña.

Estadísticas de piloto: mejores vueltas, consistencia, deltas de ritmo, análisis por sectores, incidentes, rating tipo ELO/iRating, tablas públicas y privadas.

Eventos: contrarreloj, sprint/feature, ligas por temporadas, sistema de puntos, inscripciones con cupo, parrillas y resultados.

Este repo es el cliente Android. Funciona con backend simulado (mock) o conectado a la API propia.

## Stack Tecnico
Lenguaje: Kotlin 1.9+
UI: Jetpack Compose, Material 3, Accompanist
Navegación: Jetpack Navigation (UDF donde conviene)
DI: Hilt
Datos locales: Room (SQLite), DataStore Proto para settings
Red: Retrofit + OkHttp (Kotlinx Serialization o Moshi)
Concurrencia: Coroutines + Flow
Trabajo en 2° plano: WorkManager (sync/recordatorios)
Imágenes: Coil
Logs: Timber (redacción de PII)
QA: JUnit4, Turbine (Flow), MockK, Robolectric, Espresso
Estilo: ktlint/Detekt
CI/CD: GitHub Actions + (opcional) Fastlane/Play Console

### Reservas

Vista de calendario por rig y por sede.

Detección de solape:

`fun overlaps(a: Interval, b: Interval) = a.start < b.end && b.start < a.end`
Lista de espera con ascenso opcional si hay cancelaciones.
Recordatorios push/email, ICS export.
Políticas: pago anticipado opcional, agregado a lista por no-show


### Sistema de cuenta

Autenticación: email+password y Google (Firebase Auth opcional).

Roles: PILOT, STAFF, ADMIN.

Perfiles: foto, equipo favorito, número de dorsal, preferencias.

Dispositivos de confianza, cierre de sesión global.

Gestión de PII: encriptación con EncryptedSharedPreferences.

### Estadísticas de piloto

Líderes por pista/cabina/semana.

Consistencia: desviación estándar de tiempos (menor = mejor).

Delta de ritmo: diferencia vs. mejor vuelta y vs. media del evento.

Sectores: extracción desde CSV/telemetría o split manual.

Incidentes: fuera de pista, toques, penalizaciones.

Rating ELO (simplificado)

### Eventos

Tipos: Time Attack, carreras sprint/doble manga, campeonatos.

Inscripciones con cupo, listas de reserva.

Seeding por rating/tiempos.

Puntuación configurable (ej. 25-18-15-… + vuelta rápida/pole).

Clasificaciones por fecha, categoría, temporada.

Publicación de resultados y protestas.
## Autores

- [@cesarvigils](https://www.github.com/cesarvigils) (Cesar Vigil)

