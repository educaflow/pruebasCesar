const { protocol, hostname, port, pathname } = window.location;
const baseUrl = `${protocol}//${hostname}${port ? `:${port}` : ''}${pathname.replace(/\/[^/]*$/, '')}`;

let expedienteContext = {};

async function signDocument(context, sourceKey, targetKey, modelName, sufijo) {
    expedienteContext = {};

    const updatedContext = await refreshContext(modelName, context.id);

    const source = updatedContext[sourceKey];
    const idSource = source.id;
    const versionSource = source.$version;
    const originalFileName = source.fileName;

    console.log("Contexto de firma:", updatedContext);

    expedienteContext.id = updatedContext.id;
    expedienteContext.version = updatedContext.version;
    expedienteContext.model = modelName;
    expedienteContext.sufijo = sufijo;
    expedienteContext.source = {};
    expedienteContext.source.id = idSource;
    expedienteContext.source.version = versionSource;
    expedienteContext.source.fileName = originalFileName;
    expedienteContext.source.field = sourceKey;
    expedienteContext.target = {};
    expedienteContext.target.field = targetKey;
    console.log("Contexto del expediente:", expedienteContext);

    const urlPdf = `${baseUrl}/ws/rest/com.axelor.meta.db.MetaFile/${idSource}/content/download?version=${versionSource}`;
    fetchPdfBase64(urlPdf)
        .then(base64Pdf => {
            console.log('PDF base64 descargado');
            //return firmarBase64(base64Pdf, originalFileName, targetKey);
            return firmarBase64(base64Pdf);
        })
        .catch(console.error);
    //signDocument(urlPdf);*/
}

async function refreshContext(modelName, id) {
    const url = `${baseUrl}/ws/rest/${modelName}/${id}`;
    const csrfToken = getCookie('CSRF-TOKEN');
    const response = await fetch(url, {
        method: 'GET',
        headers: { "Accept": "application/json", "X-CSRF-TOKEN": csrfToken },
        credentials: 'include'
    });
    if (!response.ok) throw new Error('Error al obtener la entidad actualizada');
    const json = await response.json();
    return json.data;  // o según la estructura que te devuelva el backend
}


async function fetchPdfBase64(url) {
    const response = await fetch(url);
    if (!response.ok) throw new Error('Error al descargar PDF');

    const blob = await response.blob();

    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onloadend = () => {
            const base64data = reader.result.split(',')[1];
            resolve(base64data);
        };
        reader.onerror = reject;
        reader.readAsDataURL(blob);
    });
}


function firmarBase64(base64Pdf) {
    AutoScript.cargarAppAfirma();
    const numeroDNI = "52648745C"; // Reemplaza con el número de DNI real

    var servletsBase = window.location.href.indexOf("/firmaMovil/") > 0
        ? window.location.href.substring(0, window.location.href.indexOf("/firmaMovil/") + "/firmaMovil/".length - 1)
        : window.location.origin;

    var params = 	"mode=explicit\n" +
        "serverUrl=" + servletsBase + "/afirma-server-triphase-signer/SignatureService";

    params=params + "\n"
        + "signaturePositionOnPageLowerLeftX=150\n"
        + "signaturePositionOnPageLowerLeftY=75\n"
        + "signaturePositionOnPageUpperRightX=400\n"
        + "signaturePositionOnPageUpperRightY=125\n"
        + "signaturePage=6"

    params=params + "\n" + "headless=true\nfilters.1=subject.rfc2254:(SERIALNUMBER=*" + numeroDNI + "*);nonexpired:\nfilters.2=subject.rfc2254:(CN=*" + numeroDNI + "*);nonexpired:";


    AutoScript.sign(
        base64Pdf,               // Datos en base64
        "SHA256withRSA",         // Algoritmo
        "PAdES",                   // Formato de firma
        params,                  // Parámetros JSON string
        function(signatureB64, certB64, extraData) {
            console.log("Firma OK");
            subirAFCTComoMetaFile(signatureB64);
            //subirAFCTComoMetaFile(signatureB64, 1, originalFileName, targetKey);
            // Crear blob y descargar PDF firmado
            /*const bin = atob(signatureB64);
            const buffer = new Uint8Array(bin.length);
            for (let i = 0; i < bin.length; i++) {
                buffer[i] = bin.charCodeAt(i);
            }
            const blob = new Blob([buffer], { type: "application/pdf" });
            const link = document.createElement("a");
            link.href = URL.createObjectURL(blob);
            link.download = "documento_firmado.pdf";
            link.click();*/
        },
        function(errorType, errorMessage) {
            console.error("Error al firmar:", errorType, errorMessage);
            alert("Error al firmar: " + errorMessage);
        }
    );
}

function subirAFCTComoMetaFile(base64Firmado) {
    const blob = base64ToBlob(base64Firmado, "application/pdf");
    const fileName = `${expedienteContext.source.fileName}${expedienteContext.sufijo}.pdf`
    const fileType = "application/pdf";
    const fileSize = blob.size;

    const formData = new FormData();
    formData.append("file", blob, fileName);
    formData.append("field", expedienteContext.target.field);

    // Construye el objeto JSON para el campo 'request'
    const requestPayload = {
        data: {
            fileName: fileName,
            fileType: fileType,
            fileSize: fileSize,
            "$upload": {
                file: {}
            }
        }
    };
    formData.append("request", JSON.stringify(requestPayload));

    // Obtener el CSRF-TOKEN (como lo estábamos haciendo)
    const csrfToken = getCookie('CSRF-TOKEN');
    if (!csrfToken) {
        console.error("CSRF-TOKEN no encontrado. No se puede subir el archivo.");
        alert("Error: Token de seguridad no encontrado. Por favor, recarga la página.");
        return;
    }

    //fetch("http://localhost:8080/pruebasCesar/ws/rest/com.axelor.meta.db.MetaFile/upload", {
    fetch(`${baseUrl}/ws/rest/com.axelor.meta.db.MetaFile/upload`, {
        method: "POST",
        body: formData,
        credentials: "include",
        headers: {
            "X-CSRF-TOKEN": csrfToken
        }
    })
        .then(res => {
            if (!res.ok) {
                return res.text().then(text => { throw new Error(`Error subiendo a MetaFile: ${res.status} ${res.statusText} - ${text}`); });
            }
            return res.json();
        })
        .then(metaFileData => {
            console.log("Respuesta de MetaFile/upload:", metaFileData);

            const metaFileId = metaFileData && metaFileData.data && metaFileData.data[0] && metaFileData.data[0].id;

            if (!metaFileId) {
                throw new Error(`MetaFile ID no obtenido. Estructura de respuesta inesperada: ${JSON.stringify(metaFileData)}`);
            }
            console.log("MetaFile subido con ID:", metaFileId);
            return actualizarFCTConMetaFile(metaFileId);
        })
        .catch(err => {
            console.error("Error durante la subida del documento firmado:", err);
            alert("Error al guardar el documento firmado: " + err.message);
        });
}

//function actualizarFCTConMetaFile(metaFileId, fctId) {
function actualizarFCTConMetaFile(metaFileId) {
    // La URL debe apuntar al registro específico de FCT por su ID.
    //const url = `http://localhost:8080/pruebasCesar/ws/rest/com.educaflow.apps.expedientes.db.FormacionCentroTrabajo/${fctId}`;
    const url = `${baseUrl}/ws/rest/${expedienteContext.model}/${expedienteContext.id}`;

    const payload = {
        data:
            {
                id: expedienteContext.id,
                version: expedienteContext.version, // Asegúrate de que este es el valor correcto para la versión
                a2_firmado: {
                    id: metaFileId,
                    version: 0
                }
            }
    };


    const csrfToken = getCookie('CSRF-TOKEN');

    if (!csrfToken) {
        console.error("CSRF-TOKEN no encontrado. No se puede actualizar el FCT.");
        alert("Error: Token de seguridad no encontrado. Por favor, recarga la página.");
        // Es mejor rechazar la promesa para que la cadena .catch() funcione correctamente
        return Promise.reject(new Error("CSRF-TOKEN no encontrado."));
    }

    return fetch(url, {
        method: "POST",
        // Todas las cabeceras van directamente en este objeto 'headers'
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "X-CSRF-TOKEN": csrfToken // ¡Aquí va tu token CSRF!
        },
        body: JSON.stringify(payload),
        // 'credentials' va aquí, al mismo nivel que 'method' y 'headers'
        credentials: "include"
    })
        .then(res => {
            if (!res.ok) {
                // Intenta leer el texto del error del servidor para depuración
                return res.text().then(text => {
                    throw new Error(`Error actualizando el FCT con el MetaFile: ${res.status} ${res.statusText} - ${text}`);
                });
            }
            return res.json();
        })
        .then(data => {
            console.log("FCT actualizado con documento firmado:", data);
            if (data && data.data && data.data.version !== undefined) {
                expedienteContext.version = data.data.version;
                console.log("Nueva versión guardada en expedienteContext:", expedienteContext.version);
            }
            alert("Documento firmado guardado correctamente en la ficha");
        })
        .catch(err => {
            console.error("Error al actualizar el FCT con el MetaFile:", err);
            // Propaga el error para que el catch superior en la cadena de promesas lo capture
            throw err;
        });
}

function base64ToBlob(base64, mime) {
    const byteChars = atob(base64);
    const byteNumbers = new Array(byteChars.length);
    for (let i = 0; i < byteChars.length; i++) {
        byteNumbers[i] = byteChars.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    return new Blob([byteArray], { type: mime });
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}