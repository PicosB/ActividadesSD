# ActividadesSD

##  Mensajero WebSocket (Java + HTML/JS)

Este proyecto es un sistema de mensajería instantánea usando **WebSockets**, con un servidor en Java y dos clientes: uno web (HTML + JavaScript) y otro de escritorio (Java Swing opcional). Fue desarrollado como parte de la materia **Sistemas Distribuidos**.

---

##  Tecnologías utilizadas

- **Java**: `21.0.2` (LTS)
- **Jakarta EE**: WebSocket API
- **Servidor**: [Apache TomEE 9 Plume](https://tomee.apache.org/)
- **Cliente Web**: HTML + JavaScript nativo
- (Opcional) **Cliente Escritorio**: Java Swing

---

##  Vista previa

### Usuario 1
![imagen](https://github.com/user-attachments/assets/fd3cb22e-f6bb-47d9-a9d4-e8d48b84d20e)

### Usuario 2
![imagen](https://github.com/user-attachments/assets/19fdf061-db17-4e17-8f33-46786b70416b)

---

##  ¿Cómo ejecutar el proyecto?

### 1. Preparar el servidor

- Asegúrate de tener **Apache TomEE 9 Plume** instalado.
- Despliega el proyecto como WAR o desde tu IDE (NetBeans, IntelliJ, Eclipse).

### 2. Configurar el WebSocket

En tu archivo HTML, asegúrate de que la URL del WebSocket sea correcta:

```js
const socket = new WebSocket("ws://localhost:8080/wSoket/mensajero");


