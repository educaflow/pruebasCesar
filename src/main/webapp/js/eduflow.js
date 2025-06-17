/*console.log("¡Eduflow JavaScript inicializado usando originalConsole!");

alert("¡Hola desde un ALERT de Eduflow!");

document.addEventListener('DOMContentLoaded', () => {
    console.log("¡Eduflow JavaScript inicializado (DOMContentLoaded) usando originalConsole!");
});*/


alert("¡Hola desde un ALERT de Eduflow!");
console.log.toString();

document.addEventListener('DOMContentLoaded', () => {
    alert("¡DOM cargado y listo!");

    // Intenta modificar un elemento del DOM
    const bodyElement = document.body;
    console.log("Elemento <body> encontrado:", bodyElement);
    console.warn("Advertencia: Esto es un mensaje de advertencia en la consola.");
    console.error("Error: Esto es un mensaje de error en la consola.");
    if (bodyElement) {
        bodyElement.style.color = 'red'; // Cambia el color del texto del cuerpo a rojo
        alert("¡Color del texto cambiado a rojo!");
    } else {
        alert("Error: No se encontró el elemento <body>.");
    }
});