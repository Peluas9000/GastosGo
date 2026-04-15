#  GastosGo - Gestión de Gastos Personales.

GastosGo es una aplicación móvil Android desarrollada en Kotlin que permite a los usuarios gestionar sus gastos personales de forma eficiente y organizada. La aplicación utiliza Room Database para el almacenamiento local de datos y ofrece una interfaz intuitiva siguiendo los principios de Material Design

---

##  Características Principales

* **Autenticación Segura**: Sistema completo de autenticación y registro de usuarios[cite: 749].
* **Gestión de Gastos**: Registro y seguimiento de gastos clasificados por categorías predefinidas[cite: 749, 1360].
* **Dashboard Visual**: Visualización de la distribución de gastos mediante gráficos de pastel interactivos[cite: 749, 1017].
* **Control de Presupuesto**: Alertas visuales basadas en el saldo restante (Verde para finanzas saludables, Naranja para saldo bajo, y Rojo para presupuesto superado) .
* **Exportación de Facturas**: Generación automática de facturas en formato PDF, guardadas directamente en el dispositivo[cite: 749, 1293].
* **Historial Detallado**: Visualización del historial completo de movimientos con opciones de gestión[cite: 749, 1286].

---

##  Flujo de la Aplicación

El proyecto sigue un flujo de navegación lógico e intuitivo [cite: 843, 846-861]:

1. **LoginActivity**: Pantalla de inicio con validación de credenciales contra la base de datos[cite: 883, 886].
2. **RegistrarActivity**: Creación de cuentas nuevas, asignando un saldo inicial automático de 2500€[cite: 946, 949].
3. **MainActivity**: Panel de control principal que muestra el resumen financiero actualizado en tiempo real y el gráfico de pastel[cite: 1009, 1013, 1017].
4. **NuevoGastoActivity**: Formulario dinámico para registrar transacciones con cantidad, concepto, categoría, fecha y hora[cite: 1129, 1132, 1133].
5. **ListadoGastosActivity**: Interfaz de tipo ListView que muestra todos los gastos del usuario[cite: 1234, 1235].
6. **DetalleGastoActivity**: Vista detallada de una transacción específica, permitiendo su eliminación o la descarga de la factura en PDF[cite: 1286, 1293].

---

##  Arquitectura de Base de Datos (Room)

La aplicación implementa el patrón Singleton para la base de datos y ejecuta todas sus operaciones de manera asíncrona[cite: 759, 1347, 1357]. [cite_start]Cuenta con 4 entidades principales:

* **Usuario**: Almacena credenciales e identificadores.
* **Banco**: Gestiona el presupuesto o saldo mensual del titular.
* **Categoria**: Clasificación de los gastos (ej. Alimentación, Transporte, Ocio).
* **Gasto**: Registro individual de cada transacción.

**Relaciones de Integridad:**
* `Banco` → `Usuario`: Relación 1:1 con `CASCADE DELETE`.
* `Gasto` → `Usuario`: Relación N:1 con `CASCADE DELETE`.
* `Gasto` → `Categoria`: Relación N:1 con `SET NULL`.

---

## 🛠️ Stack Tecnológico y Herramientas

* **Lenguaje**: Kotlin[cite: 709, 745].
* **Base de Datos Local**: Room Database y KSP (Kotlin Symbol Processing).
* **Asincronía**: Kotlin Coroutines (`lifecycleScope.launch`) para no bloquear la UI.
* **Diseño UI**: Material Design y `MPAndroidChart` para gráficos.
* **Gestión de Archivos**: Android `PdfDocument` y MediaStore API (Android 10+) para exportación segura de documentos.

---

## 🚀 Posibles Mejoras Futuras

* Implementación de cifrado avanzado para contraseñas.
* Exportación de datos masivos a CSV o Excel.
* Filtros de búsqueda por fecha, categoría o rango de precios.
* Sincronización en la nube y notificaciones push.
* Implementación de Modo Oscuro nativo.
