document.getElementById("formUsuario").addEventListener("submit", async function (e) {
    e.preventDefault();

    const usuario = {
        nome: document.getElementById("nome").valueOf(),
        cpf: document.getElementById("cpf").valueOf(),
        dataNascimento: document.getElementById("dataNascimento").value,
        email: document.getElementById("email").value,
        telefone: document.getElementById("telefone").value
    };

    try {
        const response = await fetch("https://localhost:8081/usuario", {
            method: "POST",
            headers: {"Content-Type:": "application/json" },
            body: JSON.stringify(usuario)
        });

        if(response.status === 201) {
            alert("Usuario cadastro com sucesso!");
            document.getElementById("formUsuario").reset();
        }else{
            alert("Erro ao cadastrar usuario.")
        }
    } catch (error) {
        alert("Erro de conexao com o servidor.")
        console.error(error);
    }

});
async function buscarUsuario() {
    const cpf = document.getElementById("buscarCpf").value;

    try {
        const response = await fetch(`http://localhost:8081/usuario?cpf=${cpf}`);

        if (response.ok) {
            const usuario = await response.json();
            document.getElementById("resultado").textContent = JSON.stringify(usuario, null, 2);
        } else {
            document.getElementById("resultado").textContent = "Erro ao buscar usuario.";
        }
    }catch (error) {
        document.getElementById("resultado").textContent = "Erro ao buscar usuario.";
        console.error(error);
    }
}



