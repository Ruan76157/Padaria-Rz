const apiBase = "http://localhost:8081/produtos";

document.getElementById("produtoForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const produto = {
        nome: document.getElementById("nome").value,
        preco: parseFloat(document.getElementById("preco").value),
        descricao: document.getElementById("descricao").value,
        estoque: parseInt(document.getElementById("estoque").value),
        imagem: document.getElementById("imagem").value
    };

    try {
        const response = await fetch(apiBase, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(produto)
        });

        if (response.ok) {
            document.getElementById("mensagem").innerText = "Produto cadastrado com sucesso!";
            document.getElementById("produtoForm").reset();
        } else {
            document.getElementById("mensagem").innerText = "Erro ao cadastrar!";
        }
    } catch (error) {
        document.getElementById("mensagem").innerText = "Erro na conexão com o servidor!";
    }
});
