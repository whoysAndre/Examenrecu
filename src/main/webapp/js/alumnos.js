document.addEventListener('DOMContentLoaded', function () {

  let table = new DataTable('#example', {
    ajax: {  
      url: 'medico'
    },
    columns: [
      { data: 'codigo' },
      { data: 'nombre' },
      { data: 'appa' },
      { data: 'apma' },
      { data: 'ndni' },
      {
        data: 'fechnaci',
        render: function (data) {
          return moment(data, 'ddd MMM DD HH:mm:ss z YYYY').format('YYYY-MM-DD');
        }
      },
      { data: 'username' },
      {
        data: null,
        render: function (data, type, row) {
          return `<button class="btn-eliminar btn btn-danger" data-id='${row.codigo}'>Eliminar</button>`;
        }
      },
      {
        data: null,
        render: function (data, type, row) {
          return `<button class="btn-editar btn btn-primary" data-toggle="modal" data-target="#exampleModal" data-alumno='${JSON.stringify(data)}'>Editar</button>`;
        }
      }
    ]
  });


  const btnGenerarReporte = document.getElementById('generar-reportes');
  btnGenerarReporte.addEventListener('click', async () => {
    // 2. Hacer la petición al servlet

    const response = await fetch('http://localhost:8080/ExamenRecuperacion/report', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    // 3. Verificar si la respuesta es exitosa
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.error || `Error ${response.status}: ${response.statusText}`);
    }

    // 4. Verificar que sea un PDF
    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/pdf')) {
      throw new Error('La respuesta no es un archivo PDF válido');
    }

    // 5. Convertir a blob y abrir/descargar el PDF
    const pdfBlob = await response.blob();
    const pdfUrl = URL.createObjectURL(pdfBlob);

    // Opción 1: Abrir en nueva pestaña
    window.open(pdfUrl, '_blank');
  });


  //AGREGAR ALUMNO
  agregarAlumno(table);

  //EDITAR
  document.querySelector('#example tbody').addEventListener('click', async function (e) {
    if (e.target.classList.contains('btn-editar')) {

      let alumno = JSON.parse(e.target.getAttribute('data-alumno'));
      document.querySelector('#newnombre').value = alumno.nombre;
      document.querySelector('#newappa').value = alumno.appa;
      document.querySelector('#newapma').value = alumno.apma;
      document.querySelector('#newndni').value = alumno.ndni;
      //FECHA
      const fechaFormateada = moment(alumno.fechnaci, 'ddd MMM DD HH:mm:ss z YYYY').format('YYYY-MM-DD');

      document.querySelector('#newfechnaci').value = fechaFormateada;
      document.querySelector('#newusername').value = alumno.username;


      document.getElementById("frm-actualizar-alumno").onsubmit = async (e) => {
        e.preventDefault();

        const alumNuevo = {
          codigo: alumno.codigo,
          nombre: document.querySelector('#newnombre').value,
          appa: document.querySelector('#newappa').value,
          apma: document.querySelector('#newapma').value,
          ndni: document.querySelector('#newndni').value,
          fechnaci: document.querySelector('#newfechnaci').value,
          username: document.querySelector('#newusername').value
        };

        const resp = await fetch("http://localhost:8080/ExamenRecuperacion/medico", {
          method: "PUT",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(alumNuevo)
        });

        const result = await resp.json();
        if (!result.success) {
          return;
        }
        alert("Registro actualizado");
        document.querySelector("#btn-close-editar").click();
        table.ajax.reload();
      };
    }
  });

  //ELIMINAR
  document.querySelector('#example tbody').addEventListener("click", async (e) => {
    if (e.target.classList.contains('btn-eliminar')) {

      let codigo = e.target.getAttribute('data-id');

      const idCliente = {
        codigo: parseInt(codigo)
      };

      const resp = await fetch("http://localhost:8080/ExamenRecuperacion/medico", {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(idCliente)
      });

      const result = await resp.json();

      if (!result.success) {
        alert("ERROR");
        return;
      }

      alert("Eliminado");
      table.ajax.reload();
    }
  });

});

function agregarAlumno(table) {
  const frmCreate = document.querySelector('#frm-crear');

  frmCreate.addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData(frmCreate);
    const data = Object.fromEntries(formData.entries());
    ;

    const resp = await fetch("http://localhost:8080/ExamenRecuperacion/medico", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    });

    const result = await resp.json();
    if (!result.success) {
      return;
    }
    alert("Registro creado");
    frmCreate.reset();
    table.ajax.reload();
  });

}