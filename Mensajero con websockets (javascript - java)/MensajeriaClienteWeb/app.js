// Conexión al WebSocket
const socket = new WebSocket('ws://localhost:8080/MesajeriaSocketWeb/chat');

// Elementos del DOM
const messageContainer = document.getElementById('message-container');
const messageInput = document.getElementById('message-input');
const sendButton = document.getElementById('send-button');

// Evento cuando se establece la conexión
socket.addEventListener('open', () => {
    console.log('Conectado al servidor WebSocket');
    addMessage('Te has conectado al chat.');
});

// Evento cuando se recibe un mensaje
socket.addEventListener('message', (event) => {
    console.log('📩 Mensaje recibido:', event.data);
    addMessage(event.data);
});

// Evento cuando se cierra la conexión
socket.addEventListener('close', () => {
    console.log('Conexión cerrada');
    addMessage('Se ha cerrado la conexión con el servidor.');
});

// Evento cuando hay un error
socket.addEventListener('error', (error) => {
    console.error('Error:', error);
    addMessage('Ha ocurrido un error.');
});

// Función para agregar mensajes al chat
function addMessage(message) {
    const messageElement = document.createElement('div');
    messageElement.textContent = message;
    messageContainer.appendChild(messageElement);
    messageContainer.scrollTop = messageContainer.scrollHeight;
}

// Enviar mensaje al hacer clic en el botón
sendButton.addEventListener('click', () => {
    if (messageInput.value.trim() !== '') {
        socket.send(messageInput.value);
        addMessage(`Tú: ${messageInput.value}`);
        messageInput.value = '';
    }
});

// Enviar mensaje al presionar Enter
messageInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter' && messageInput.value.trim() !== '') {
        socket.send(messageInput.value);
        addMessage(`Tú: ${messageInput.value}`);
        messageInput.value = '';
    }
});
