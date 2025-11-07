const apiBase = "http://localhost:8081";

// ========== LISTA DE ADMINS AUTORIZADOS ==========
const ADMINS_AUTORIZADOS = [
    "09006157104",
    "admin@padariarz.com",
    "01324431121"
];

// Sistema de armazenamento de sessão
const SessionStorage = {
    salvarUsuario(usuario) {
        localStorage.setItem('usuarioLogado', JSON.stringify(usuario));
    },
    obterUsuario() {
        const usuario = localStorage.getItem('usuarioLogado');
        return usuario ? JSON.parse(usuario) : null;
    },
    removerUsuario() {
        localStorage.removeItem('usuarioLogado');
    },
    isAdmin() {
        const usuario = this.obterUsuario();
        if (!usuario) return false;
        return ADMINS_AUTORIZADOS.includes(usuario.cpf) ||
            ADMINS_AUTORIZADOS.includes(usuario.email);
    }
};

const Carrinho = {
    obter() {
        const carrinho = localStorage.getItem('carrinho');
        return carrinho ? JSON.parse(carrinho) : [];
    },
    salvar(carrinho) {
        localStorage.setItem('carrinho', JSON.stringify(carrinho));
    },
    adicionar(produto) {
        const carrinho = this.obter();
        const itemExistente = carrinho.find(item => item.id === produto.id);
        if (itemExistente) {
            itemExistente.quantidade++;
        } else {
            carrinho.push({ ...produto, quantidade: 1 });
        }
        this.salvar(carrinho);
        this.atualizarContador();
        return carrinho;
    },
    remover(produtoId) {
        let carrinho = this.obter();
        carrinho = carrinho.filter(item => item.id !== produtoId);
        this.salvar(carrinho);
        this.atualizarContador();
    },
    limpar() {
        localStorage.removeItem('carrinho');
        this.atualizarContador();
    },
    atualizarContador() {
        const carrinho = this.obter();
        const total = carrinho.reduce((acc, item) => acc + item.quantidade, 0);
        const contador = document.getElementById('cartCount');
        if (contador) {
            contador.innerText = total;
            contador.style.display = total > 0 ? 'inline' : 'none';
        }
    }
};

// ========== GERENCIAMENTO DE PRODUTOS (ADMIN) ==========
let modoAdmin = false;
let produtoEditando = null;

const categorias = {
    paes: '🍞 Pães',
    doces: '🧁 Doces',
    salgados: '🥐 Salgados',
    bebidas: '☕ Bebidas',
    bolos: '🎂 Bolos',
    outros: '📦 Outros'
};

// Verificar se está na página de produtos
const isProductsPage = document.getElementById("productsContainer") !== null;

if (isProductsPage) {
    const btnAdmin = document.getElementById('btnAdmin');
    if (btnAdmin) {
        if (SessionStorage.isAdmin()) {
            btnAdmin.style.display = 'flex';
            btnAdmin.addEventListener('click', toggleModoAdmin);
        } else {
            btnAdmin.style.display = 'none';
        }
    }
    if (SessionStorage.isAdmin()) {
        adicionarPainelAdmin();
    }
}

function adicionarPainelAdmin() {
    const productsSection = document.querySelector('.products-section');
    if (!productsSection) return;
    if (document.getElementById('adminPanel')) return;

    const adminPanel = document.createElement('div');
    adminPanel.id = 'adminPanel';
    adminPanel.className = 'admin-panel';
    adminPanel.style.display = 'none';
    adminPanel.innerHTML = `
        <div class="admin-header">
            <h2>📦 Painel Administrativo</h2>
            <button type="button" class="btn-admin-action" id="btnNovoProduto">➕ Novo Produto</button>
        </div>
        <div class="form-container" id="formContainer" style="display: none;">
            <h3 id="formTitle">Adicionar Novo Produto</h3>
            <form id="produtoForm">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Nome *</label>
                        <input type="text" id="prodNome" required placeholder="Ex: Pão Francês">
                    </div>
                    <div class="form-group">
                        <label>Preço (R$) *</label>
                        <input type="number" id="prodPreco" step="0.01" required placeholder="0.00">
                    </div>
                    <div class="form-group">
                        <label>Categoria</label>
                        <select id="prodCategoria">
                            <option value="paes">🍞 Pães</option>
                            <option value="doces">🧁 Doces</option>
                            <option value="salgados">🥐 Salgados</option>
                            <option value="bebidas">☕ Bebidas</option>
                            <option value="bolos">🎂 Bolos</option>
                            <option value="outros">📦 Outros</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Estoque</label>
                        <input type="number" id="prodEstoque" placeholder="0">
                    </div>
                    <div class="form-group full-width">
                        <label>Descrição</label>
                        <textarea id="prodDescricao" rows="3" placeholder="Descrição do produto..."></textarea>
                    </div>
                </div>
                <button type="submit" class="btn-salvar">Salvar Produto</button>
            </form>
        </div>
    `;
    productsSection.insertBefore(adminPanel, productsSection.firstChild);
    document.getElementById('btnNovoProduto').addEventListener('click', mostrarFormulario);
    document.getElementById('produtoForm').addEventListener('submit', salvarProduto);
}

function toggleModoAdmin() {
    if (!SessionStorage.isAdmin()) {
        mostrarNotificacao('Você não tem permissão para acessar o modo admin!', 'error');
        return;
    }
    modoAdmin = !modoAdmin;
    const adminPanel = document.getElementById('adminPanel');
    const btnAdmin = document.getElementById('btnAdmin');

    if (modoAdmin) {
        adminPanel.style.display = 'block';
        btnAdmin.classList.add('active');
        btnAdmin.textContent = '🔒 Sair Admin';
        mostrarNotificacao('Modo Admin ativado! 🔓');
    } else {
        adminPanel.style.display = 'none';
        btnAdmin.classList.remove('active');
        btnAdmin.textContent = '👤 Admin';
        document.getElementById('formContainer').style.display = 'none';
        limparFormulario();
        mostrarNotificacao('Modo Admin desativado! 🔒');
    }
    carregarProdutos();
}

function mostrarFormulario() {
    const formContainer = document.getElementById('formContainer');
    const formTitle = document.getElementById('formTitle');
    if (formContainer.style.display === 'none') {
        formContainer.style.display = 'block';
        formTitle.textContent = produtoEditando ? 'Editar Produto' : 'Adicionar Novo Produto';
        formContainer.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    } else {
        formContainer.style.display = 'none';
        limparFormulario();
    }
}

function limparFormulario() {
    document.getElementById('prodNome').value = '';
    document.getElementById('prodPreco').value = '';
    document.getElementById('prodCategoria').value = 'paes';
    document.getElementById('prodEstoque').value = '';
    document.getElementById('prodDescricao').value = '';
    produtoEditando = null;
}

async function salvarProduto(e) {
    e.preventDefault();
    if (!SessionStorage.isAdmin()) {
        mostrarNotificacao('Você não tem permissão para esta ação!', 'error');
        return;
    }

    const produto = {
        nome: document.getElementById('prodNome').value,
        preco: parseFloat(document.getElementById('prodPreco').value),
        categoria: document.getElementById('prodCategoria').value,
        quantidade: parseInt(document.getElementById('prodEstoque').value) || 0,
        descricao: document.getElementById('prodDescricao').value
    };

    try {
        let response;
        if (produtoEditando) {
            response = await fetch(`${apiBase}/padaria/${produtoEditando.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(produto)
            });
        } else {
            response = await fetch(`${apiBase}/padaria`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(produto)
            });
        }

        if (response.ok) {
            mostrarNotificacao(produtoEditando ? '✅ Produto atualizado com sucesso!' : '✅ Produto adicionado com sucesso!');
            limparFormulario();
            document.getElementById('formContainer').style.display = 'none';
            await carregarProdutos();
        } else {
            const errorText = await response.text();
            mostrarNotificacao(`❌ Erro: ${errorText}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        mostrarNotificacao('❌ Erro ao salvar produto!', 'error');
    }
}

function editarProduto(produto) {
    if (!SessionStorage.isAdmin()) {
        mostrarNotificacao('Você não tem permissão para esta ação!', 'error');
        return;
    }

    produtoEditando = produto;
    document.getElementById('prodNome').value = produto.nome;
    document.getElementById('prodPreco').value = produto.preco;
    document.getElementById('prodCategoria').value = produto.categoria || 'paes';
    document.getElementById('prodEstoque').value = produto.quantidade;
    document.getElementById('prodDescricao').value = produto.descricao || '';

    const formContainer = document.getElementById('formContainer');
    formContainer.style.display = 'block';
    document.getElementById('formTitle').textContent = 'Editar Produto';
    formContainer.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

async function removerProduto(id, nome) {
    if (!SessionStorage.isAdmin()) {
        mostrarNotificacao('Você não tem permissão para esta ação!', 'error');
        return;
    }
    if (!confirm(`⚠️ Tem certeza que deseja remover "${nome}"?\n\nEsta ação não pode ser desfeita!`)) return;

    try {
        const response = await fetch(`${apiBase}/padaria/${id}`, { method: 'DELETE' });
        if (response.ok) {
            mostrarNotificacao('🗑️ Produto removido com sucesso!');
            await carregarProdutos();
        } else {
            const errorText = await response.text();
            mostrarNotificacao(`❌ Erro: ${errorText}`, 'error');
        }
    } catch (error) {
        console.error('Erro:', error);
        mostrarNotificacao('❌ Erro ao remover produto!', 'error');
    }
}

async function ajustarEstoque(id, quantidade) {
    if (!SessionStorage.isAdmin()) {
        mostrarNotificacao('Você não tem permissão para esta ação!', 'error');
        return;
    }

    try {
        const response = await fetch(`${apiBase}/padaria/${id}/ajustar-estoque?quantidade=${quantidade}`, {
            method: 'PUT'
        });
        if (response.ok) {
            await carregarProdutos();
            mostrarNotificacao(`📦 Estoque ajustado: ${quantidade > 0 ? '+' : ''}${quantidade}`);
        } else {
            throw new Error('Erro ao ajustar estoque');
        }
    } catch (error) {
        console.error('Erro:', error);
        mostrarNotificacao('❌ Erro ao ajustar estoque!', 'error');
    }
}

// ========== CARREGAMENTO DE PRODUTOS ==========
async function carregarProdutos() {
    const container = document.getElementById("productsContainer");
    if (!container) return;

    try {
        const response = await fetch(`${apiBase}/padaria`);
        if (!response.ok) throw new Error("Erro ao buscar produtos");
        const produtos = await response.json();

        container.innerHTML = "";

        if (produtos.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <div class="empty-icon">📦</div>
                    <p class="empty-text">Nenhum produto cadastrado ainda.</p>
                    ${modoAdmin ? '<p class="empty-subtext">Clique em "Novo Produto" para adicionar o primeiro produto.</p>' : ''}
                </div>
            `;
            return;
        }

        const produtosPorCategoria = produtos.reduce((acc, produto) => {
            const cat = produto.categoria || 'outros';
            if (!acc[cat]) acc[cat] = [];
            acc[cat].push(produto);
            return acc;
        }, {});

        Object.entries(produtosPorCategoria).forEach(([categoria, produtosCategoria]) => {
            const categoriaSection = document.createElement('div');
            categoriaSection.className = 'categoria-section';

            const titulo = document.createElement('h3');
            titulo.className = 'categoria-titulo';
            titulo.textContent = categorias[categoria] || categoria;
            categoriaSection.appendChild(titulo);

            const grid = document.createElement('div');
            grid.className = 'products-grid';

            produtosCategoria.forEach(produto => {
                const card = criarCardProduto(produto);
                grid.appendChild(card);
            });

            categoriaSection.appendChild(grid);
            container.appendChild(categoriaSection);
        });

        setTimeout(() => {
            document.querySelectorAll(".product-card").forEach((card, index) => {
                setTimeout(() => {
                    card.classList.add("reveal");
                }, index * 50);
            });
        }, 100);

    } catch (error) {
        console.error("Erro ao carregar produtos:", error);
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">❌</div>
                <p class="empty-text">Erro ao carregar produtos.</p>
                <p class="empty-subtext">Tente novamente mais tarde.</p>
            </div>
        `;
    }
}

function criarCardProduto(produto) {
    const card = document.createElement("div");
    card.classList.add("product-card");

    const estoque = produto.quantidade || 0;
    const classeEstoque = estoque > 10 ? 'estoque-alto' : estoque > 0 ? 'estoque-medio' : 'estoque-baixo';

    if (modoAdmin) {
        // Card de administrador - CORRIGIDO: IDs como strings
        card.innerHTML = `
            <h3>${produto.nome}</h3>
            <p class="produto-descricao">${produto.descricao || 'Sem descrição'}</p>
            <div class="produto-info">
                <span class="produto-preco">R$ ${produto.preco?.toFixed(2) || "0.00"}</span>
                <span class="produto-estoque ${classeEstoque}">Estoque: ${estoque}</span>
            </div>
            
            <div class="admin-actions">
                <div class="estoque-controls">
                    <button class="btn-estoque btn-estoque-menos" onclick="ajustarEstoque('${produto.id}', -1)">-1</button>
                    <button class="btn-estoque btn-estoque-mais" onclick="ajustarEstoque('${produto.id}', 1)">+1</button>
                    <button class="btn-estoque btn-estoque-mais10" onclick="ajustarEstoque('${produto.id}', 10)">+10</button>
                </div>
                <div class="produto-actions">
                    <button class="btn-editar" onclick="editarProdutoGlobal(${JSON.stringify(produto).replace(/"/g, '&quot;')})">✏️ Editar</button>
                    <button class="btn-remover" onclick="removerProduto('${produto.id}', '${produto.nome.replace(/'/g, "\\'")}')">🗑️ Remover</button>
                </div>
            </div>
        `;
    } else {
        // Card de usuário normal - CORRIGIDO: ID como string
        card.innerHTML = `
            <h3>${produto.nome}</h3>
            <p><strong>Preço:</strong> R$ ${produto.preco?.toFixed(2) || "0.00"}</p>
            <p><strong>Descrição:</strong> ${produto.descricao || "Sem descrição"}</p>
            <p><strong>Estoque:</strong> <span class="produto-estoque ${classeEstoque}">${estoque} unidades</span></p>
            <button 
                onclick="adicionarAoCarrinho('${produto.id}', '${produto.nome.replace(/'/g, "\\'")}', ${produto.preco})"
                ${estoque === 0 ? 'disabled style="background: #ccc; cursor: not-allowed;"' : ''}
            >
                ${estoque > 0 ? '🛒 Adicionar ao Carrinho' : '❌ Indisponível'}
            </button>
        `;
    }

    return card;
}

// ========== CADASTRO ==========
const cadastroForm = document.getElementById("cadastroForm");

if (cadastroForm) {
    cadastroForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        // Remove pontos, traços e espaços do CPF antes de enviar
        const cpfLimpo = document.getElementById("cpf").value.replace(/\D/g, '');

        const usuario = {
            nome: document.getElementById("nome").value,
            cpf: cpfLimpo,
            dataNascimento: document.getElementById("dataNascimento").value,
            email: document.getElementById("email").value,
            telefone: document.getElementById("telefone").value,
        };

        try {
            const response = await fetch(`${apiBase}/cadastro`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(usuario)
            });

            const mensagemElement = document.getElementById("mensagem");

            if (response.ok) {
                mensagemElement.innerText = "✅ Cadastro realizado com sucesso!";
                mensagemElement.style.color = "green";

                SessionStorage.salvarUsuario(usuario);

                setTimeout(() => {
                    window.location.href = "Padaria.html";
                }, 1500);
            } else {
                const errorText = await response.text();
                mensagemElement.innerText = `❌ Erro ao cadastrar: ${errorText}`;
                mensagemElement.style.color = "red";
            }
        } catch (error) {
            console.error("Erro completo:", error);
            document.getElementById("mensagem").innerText = "❌ Erro de conexão com o servidor.";
            document.getElementById("mensagem").style.color = "red";
        }
    });
}

// ========== PERFIL ==========
const profileBtn = document.getElementById("profileBtn");
const profileMenu = document.getElementById("profileMenu");
const userName = document.getElementById("userName");
const userEmail = document.getElementById("userEmail");
const userCpf = document.getElementById("userCpf");
const userTelefone = document.getElementById("userTelefone");
const logoutBtn = document.getElementById("logoutBtn");

function carregarPerfilUsuario() {
    const usuario = SessionStorage.obterUsuario();

    if (userName) {
        if (usuario) {
            userName.innerText = usuario.nome;
            if (userEmail) userEmail.innerText = usuario.email;
            if (userCpf) userCpf.innerText = usuario.cpf;
            if (userTelefone) userTelefone.innerText = usuario.telefone;

            // Adicionar badge de admin se for admin
            if (SessionStorage.isAdmin()) {
                userName.innerHTML = `${usuario.nome} <span style="background: linear-gradient(135deg, #8B5CF6, #7C3AED); color: white; padding: 2px 8px; border-radius: 6px; font-size: 0.7em; margin-left: 8px;">ADMIN</span>`;
            }
        } else {
            userName.innerText = "Visitante";
            if (userEmail) userEmail.innerText = "Não logado";
        }
    }
}

if (profileBtn) {
    profileBtn.addEventListener("click", () => {
        if (profileMenu) profileMenu.classList.toggle("hidden");
    });

    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            SessionStorage.removerUsuario();
            Carrinho.limpar();
            mostrarNotificacao('👋 Logout realizado com sucesso!');
            setTimeout(() => {
                window.location.href = "index.html";
            }, 1000);
        });
    }
}

// ========== CARRINHO ==========
window.adicionarAoCarrinho = function(id, nome, preco) {
    const produto = { id, nome, preco };
    Carrinho.adicionar(produto);
    mostrarNotificacao(`🛒 ${nome} adicionado ao carrinho!`);
};

window.editarProdutoGlobal = editarProduto;
window.removerProduto = removerProduto;
window.ajustarEstoque = ajustarEstoque;

function mostrarNotificacao(mensagem, tipo = 'success') {
    const notificacao = document.createElement('div');
    notificacao.className = 'notificacao';
    notificacao.innerText = mensagem;

    const cor = tipo === 'success' ?
        'linear-gradient(135deg, #22C55E, #15803D)' :
        'linear-gradient(135deg, #EF4444, #DC2626)';

    notificacao.style.cssText = `
        position: fixed;
        top: 30px;
        right: 30px;
        background: ${cor};
        color: white;
        padding: 18px 28px;
        border-radius: 16px;
        box-shadow: 0 10px 40px rgba(0,0,0,0.3);
        z-index: 10000;
        animation: slideIn 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
        font-weight: 700;
        font-size: 1.05em;
        backdrop-filter: blur(10px);
        border: 2px solid rgba(255, 255, 255, 0.3);
    `;

    document.body.appendChild(notificacao);

    setTimeout(() => {
        notificacao.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            if (notificacao.parentNode) {
                notificacao.parentNode.removeChild(notificacao);
            }
        }, 300);
    }, 3500);
}

const cartBtn = document.getElementById("cartBtn");
if (cartBtn) {
    cartBtn.addEventListener("click", () => {
        window.location.href = "carrinho.html";
    });
}

// ========== FINALIZAR COMPRA (CORRIGIDO) ==========
async function finalizarCompra() {
    const usuario = SessionStorage.obterUsuario();
    const carrinho = Carrinho.obter();

    console.log("🔍 STEP 1: Verificando usuário e carrinho");
    console.log("Usuario:", usuario);
    console.log("Carrinho:", carrinho);

    if (!usuario) {
        mostrarNotificacao("⚠️ Faça login antes de finalizar a compra!", "error");
        return;
    }

    if (carrinho.length === 0) {
        mostrarNotificacao("🛒 Seu carrinho está vazio!", "error");
        return;
    }

    try {
        // Remove formatação do CPF
        const cpfLimpo = usuario.cpf.replace(/\D/g, '');
        const produtosIds = carrinho.map(item => item.id.toString());

        console.log("🔍 STEP 2: Preparando compra");
        console.log("CPF:", cpfLimpo);
        console.log("Produtos IDs:", produtosIds);

        // CORREÇÃO: Use usuarioCpf em vez de usuarioId
        const compraRequest = {
            usuarioCpf: cpfLimpo,
            produtosIds: produtosIds
        };

        console.log("📦 STEP 3: Compra request:", compraRequest);

        // CORREÇÃO: Use endpoint /compras em vez de /api/compras
        const response = await fetch(`${apiBase}/compras`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(compraRequest)
        });

        console.log("📥 STEP 4: Response da compra:", response.status);

        if (!response.ok) {
            const errorText = await response.text();
            console.error("❌ Erro na compra:", errorText);
            throw new Error(`Erro ao finalizar compra: ${errorText}`);
        }

        const compraCriada = await response.json();
        console.log("✅ STEP 5: Compra criada com sucesso:", compraCriada);

        // Atualiza estoque dos produtos
        console.log("📦 STEP 6: Atualizando estoque...");
        for (const item of carrinho) {
            try {
                const produtoResponse = await fetch(`${apiBase}/padaria/${item.id}`);
                if (!produtoResponse.ok) {
                    console.warn(`⚠️ Produto ${item.id} não encontrado`);
                    continue;
                }

                const produto = await produtoResponse.json();
                const novoEstoque = Math.max(0, produto.quantidade - item.quantidade);

                await fetch(`${apiBase}/padaria/${item.id}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ ...produto, quantidade: novoEstoque })
                });

                console.log(`✅ Estoque atualizado - ${produto.nome}: ${novoEstoque}`);
            } catch (erroEstoque) {
                console.error(`❌ Erro estoque ${item.id}:`, erroEstoque);
            }
        }

        // Limpa carrinho e mostra sucesso
        Carrinho.limpar();
        const valorTotal = carrinho.reduce((acc, item) => acc + item.preco * item.quantidade, 0);
        mostrarNotificacao(`✅ Compra finalizada! Total: R$ ${valorTotal.toFixed(2)}`);

        setTimeout(() => {
            window.location.href = "Padaria.html";
        }, 2000);

    } catch (error) {
        console.error("❌ ERRO FINAL:", error);
        mostrarNotificacao(`❌ ${error.message}`, "error");
    }
}

// ========== EFEITO HEADER AO ROLAR ==========
window.addEventListener("scroll", () => {
    const header = document.querySelector("header");
    if (header) header.classList.toggle("scrolled", window.scrollY > 50);
});

// ========== MÁSCARAS DE INPUT ==========

// CPF (000.000.000-00)
const cpfInput = document.getElementById('cpf');
if (cpfInput) {
    cpfInput.addEventListener('input', (e) => {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 11) value = value.slice(0, 11);
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        e.target.value = value;
    });
}

// Nome (apenas letras e espaços)
const nomeInput = document.getElementById('nome');
if (nomeInput) {
    nomeInput.addEventListener('input', (e) => {
        e.target.value = e.target.value
            .replace(/[^A-Za-zÀ-ÿ\s]/g, '')
            .replace(/\s{2,}/g, ' ');
    });
}

// Telefone ((00) 00000-0000)
const telefoneInput = document.getElementById('telefone');
if (telefoneInput) {
    telefoneInput.addEventListener('input', (e) => {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 11) value = value.slice(0, 11);
        if (value.length <= 10) {
            value = value.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
        } else {
            value = value.replace(/(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
        }
        e.target.value = value.trim();
    });
}

// Preço (somente números e ponto, até 2 casas decimais)
const precoInput = document.getElementById('preco');
if (precoInput) {
    precoInput.addEventListener('input', (e) => {
        let value = e.target.value.replace(/[^0-9.,]/g, '').replace(',', '.');
        const parts = value.split('.');
        if (parts.length > 2) parts.splice(2);
        if (parts[1]) parts[1] = parts[1].slice(0, 2);
        e.target.value = parts.join('.');
    });
}

// ========== INICIALIZAÇÃO ==========
document.addEventListener('DOMContentLoaded', () => {
    carregarPerfilUsuario();
    carregarProdutos();
    Carrinho.atualizarContador();
});

// ========== ESTILOS DAS ANIMAÇÕES ==========
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from { transform: translateX(400px); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(400px); opacity: 0; }
    }
`;
document.head.appendChild(style);