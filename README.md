# [cite_start]💰 GastosGo - Gestión de Gastos Personales [cite: 706, 707]

[cite_start]GastosGo es una aplicación móvil Android desarrollada en Kotlin que permite a los usuarios gestionar sus gastos personales de forma eficiente y organizada[cite: 745]. [cite_start]La aplicación utiliza Room Database para el almacenamiento local de datos y ofrece una interfaz intuitiva siguiendo los principios de Material Design[cite: 746].

---

## 🌟 Características Principales

* [cite_start]**Autenticación Segura**: Sistema completo de autenticación y registro de usuarios[cite: 749].
* [cite_start]**Gestión de Gastos**: Registro y seguimiento de gastos clasificados por categorías predefinidas[cite: 749, 1360].
* [cite_start]**Dashboard Visual**: Visualización de la distribución de gastos mediante gráficos de pastel interactivos[cite: 749, 1017].
* [cite_start]**Control de Presupuesto**: Alertas visuales basadas en el saldo restante (Verde para finanzas saludables, Naranja para saldo bajo, y Rojo para presupuesto superado) [cite: 749, 1013-1016].
* [cite_start]**Exportación de Facturas**: Generación automática de facturas en formato PDF, guardadas directamente en el dispositivo[cite: 749, 1293].
* [cite_start]**Historial Detallado**: Visualización del historial completo de movimientos con opciones de gestión[cite: 749, 1286].

---

## 📱 Flujo de la Aplicación

[cite_start]El proyecto sigue un flujo de navegación lógico e intuitivo [cite: 843, 846-861]:

1. [cite_start]**LoginActivity**: Pantalla de inicio con validación de credenciales contra la base de datos[cite: 883, 886].
2. [cite_start]**RegistrarActivity**: Creación de cuentas nuevas, asignando un saldo inicial automático de 2500€[cite: 946, 949].
3. [cite_start]**MainActivity**: Panel de control principal que muestra el resumen financiero actualizado en tiempo real y el gráfico de pastel[cite: 1009, 1013, 1017].
4. [cite_start]**NuevoGastoActivity**: Formulario dinámico para registrar transacciones con cantidad, concepto, categoría, fecha y hora[cite: 1129, 1132, 1133].
5. [cite_start]**ListadoGastosActivity**: Interfaz de tipo ListView que muestra todos los gastos del usuario[cite: 1234, 1235].
6. [cite_start]**DetalleGastoActivity**: Vista detallada de una transacción específica, permitiendo su eliminación o la descarga de la factura en PDF[cite: 1286, 1293].

---

## 🗄️ Arquitectura de Base de Datos (Room)

[cite_start]La aplicación implementa el patrón Singleton para la base de datos y ejecuta todas sus operaciones de manera asíncrona[cite: 759, 1347, 1357]. [cite_start]Cuenta con 4 entidades principales[cite: 758]:

* [cite_start]**Usuario**: Almacena credenciales e identificadores[cite: 762, 769, 773].
* [cite_start]**Banco**: Gestiona el presupuesto o saldo mensual del titular[cite: 777, 778].
* [cite_start]**Categoria**: Clasificación de los gastos (ej. Alimentación, Transporte, Ocio)[cite: 798, 800].
* [cite_start]**Gasto**: Registro individual de cada transacción[cite: 806, 807].

**Relaciones de Integridad:**
* [cite_start]`Banco` → `Usuario`: Relación 1:1 con `CASCADE DELETE`[cite: 840].
* [cite_start]`Gasto` → `Usuario`: Relación N:1 con `CASCADE DELETE`[cite: 841].
* [cite_start]`Gasto` → `Categoria`: Relación N:1 con `SET NULL`[cite: 842].

---

## 🛠️ Stack Tecnológico y Herramientas

* [cite_start]**Lenguaje**: Kotlin[cite: 709, 745].
* [cite_start]**Base de Datos Local**: Room Database y KSP (Kotlin Symbol Processing)[cite: 1370].
* [cite_start]**Asincronía**: Kotlin Coroutines (`lifecycleScope.launch`) para no bloquear la UI[cite: 1347, 1348, 1370].
* [cite_start]**Diseño UI**: Material Design y `MPAndroidChart` para gráficos[cite: 1370].
* [cite_start]**Gestión de Archivos**: Android `PdfDocument` y MediaStore API (Android 10+) para exportación segura de documentos[cite: 1364, 1370].

---

## 🚀 Posibles Mejoras Futuras

* [cite_start]Implementación de cifrado avanzado para contraseñas[cite: 1376].
* [cite_start]Exportación de datos masivos a CSV o Excel[cite: 1376].
* [cite_start]Filtros de búsqueda por fecha, categoría o rango de precios[cite: 1377].
* [cite_start]Sincronización en la nube y notificaciones push[cite: 1377].
* [cite_start]Implementación de Modo Oscuro nativo[cite: 1377].
