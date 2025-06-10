Perfecto. Te puedo generar una batería de comandos `mvn exec:java` para que pruebes **todas las funcionalidades clave** de tu aplicación (alta, búsqueda, inscripción, errores). Aquí van ejemplos variados:

---

### ✅ **1. Alta de curso válido**

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-a 'Madrid' 'CursoNuevo1' '2025-07-01T10:00' 120.5 20"
```

---

### ❌ **2. Alta de curso con fecha inválida (menos de 15 días)**

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-a 'Madrid' 'CursoFallido' '2025-06-05T10:00' 150 10"
```

> Esperas: `InputValidationException` indicando problema de fecha.

---

### ✅ **3. Búsqueda de cursos por ciudad**

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-f 'Madrid'"
```

---

### ❌ **4. Alta con precio mal formado**

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-a 'Madrid' 'CursoPrecioMal' '2025-07-15T10:00' xyz 10"
```

> Esperas: error de parsing al convertir el precio.

---

### ✅ **5. Inscripción correcta a un curso (ajusta ID)**

Supón que el curso con ID `3` existe y tiene plazas:

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 3 'usuario@example.com' '1234567812345678'"
```

---

### ❌ **6. Inscripción a curso que no existe**

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 9999 'nadie@example.com' '1234567812345678'"
```

> Esperas: `InstanceNotFoundException`

---

### ❌ **7. Inscripción con tarjeta malformada (no 16 dígitos)**

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 3 'usuario@example.com' '1234'"
```

> Esperas: `InputValidationException`

---

Perfecto, aquí tienes una **batería extendida de casos de prueba** variados para cubrir lo más posible la lógica del cliente:

---

## 🟢 **Casos de éxito (esperas que todo funcione)**

### ✅ 1. Crear curso con datos normales

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-a 'Barcelona' 'SpringBootAvanzado' '2025-07-05T09:00' 200 25"
```

---

### ✅ 2. Buscar cursos en ciudad con varios cursos

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-f 'Madrid'"
```

---

### ✅ 3. Inscribirse en curso existente (ajusta ID real)

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 2 'jose@example.com' '1111222233334444'"
```

---

## 🔴 **Casos de error y validación**

### ❌ 4. Crear curso con número de plazas negativo

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-a 'Madrid' 'CursoMal' '2025-07-01T10:00' 150 -5"
```

> Esperas: `InputValidationException` (valor no permitido)

---

### ❌ 5. Crear curso con nombre vacío

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-a 'Madrid' '' '2025-07-01T10:00' 100 10"
```

> Esperas: `InputValidationException` (nombre requerido)

---

### ❌ 6. Inscribirse con email vacío

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 2 '' '1234567890123456'"
```

> Esperas: `InputValidationException` (email requerido)

---

### ❌ 7. Inscribirse dos veces al mismo curso

```bash
# Primera vez (éxito)
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 2 'ana@example.com' '1234567890123456'"

# Segunda vez (fallo esperado)
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 2 'ana@example.com' '1234567890123456'"
```

> Esperas: `RepeatedInscriptionException` o equivalente

---

### ❌ 8. Inscribirse en curso lleno (ajusta curso ya lleno)

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 5 'pepe@example.com' '9999888877776666'"
```

> Esperas: `FullCourseException` o mensaje equivalente

---

## 🔁 **Pruebas combinadas**

### 🔄 9. Crear curso y luego inscribirse inmediatamente

```bash
# Crear
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-a 'Sevilla' 'CursoExpress' '2025-07-10T15:00' 90 5"

# Inscribirse (ajusta ID tras crear)
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-i 4 'curso@ya.com' '1212121212121212'"
```

---

### 🆘 10. Opción inválida

```bash
mvn exec:java -Dexec.mainClass="es.udc.ws.app.client.ui.AppServiceClient" -Dexec.args="-z 'algo'"
```

> Esperas: mensaje `Opción no válida` y ayuda de uso

---
