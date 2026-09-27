# Transporte Escolar - Swing + SQLite

Projeto Maven pronto para abrir no NetBeans.

## Como abrir no NetBeans

1. Extraia o zip em uma pasta no seu computador.
2. No NetBeans: **File > Open Project...**
3. Selecione a pasta `transporte-escolar-swing` (a que contém o `pom.xml`).
4. O NetBeans reconhece automaticamente como projeto Maven e baixa a dependência do SQLite (`org.xerial:sqlite-jdbc`) sozinho.
5. Clique com o botão direito no projeto > **Run**, ou rode a classe `com.transporteescolar.Main` diretamente.

Na primeira execução, o arquivo `transporteescolar.db` é criado automaticamente na pasta do projeto, com todas as tabelas.

## Estrutura

```
src/main/java/com/transporteescolar/
├── Main.java                    # cria as tabelas (se não existirem) e abre a TelaLogin
├── model/
│   ├── Van.java                 # id, placa, modelo, capacidade, motorista responsável
│   ├── Motorista.java           # id, nome, CPF, CNH, login, senha
│   ├── Aluno.java                # id, nome, endereço, escola, responsável, telefone
│   └── Rota.java                 # id, nome/descrição, horário, van vinculada, lista de alunos
├── dao/
│   ├── Conexao.java             # abre a conexão SQLite e cria as tabelas
│   ├── VanDAO.java
│   ├── MotoristaDAO.java
│   ├── AlunoDAO.java
│   └── RotaDAO.java             # cuida também da tabela de ligação rota_aluno
└── view/
    ├── TelaLogin.java           # admin/admin = Administrador; login/senha do motorista cadastrado = Motorista
    ├── TelaPrincipal.java       # (Administrador) menu de acesso a todas as funcionalidades
    ├── TelaVan.java             # (Administrador) incluir, listar, editar, excluir van
    ├── TelaMotorista.java       # (Administrador) incluir, listar, editar, excluir motorista
    ├── TelaAluno.java           # (Administrador) incluir, listar, editar, excluir aluno
    ├── TelaRota.java            # (Administrador) incluir, listar, editar, excluir rota, vinculando van e alunos
    ├── TelaRotasMotorista.java  # (Motorista) vê apenas as rotas da van em que é o motorista responsável
    └── TelaEmbarque.java        # (Motorista) confirma a presença dos alunos ao iniciar a rota
```

## Banco de dados

Tabelas criadas automaticamente no SQLite (`transporteescolar.db`):

- `motorista` (id, nome, cpf, cnh, login, senha)
- `van` (id, placa, modelo, capacidade, motorista_id)
- `aluno` (id, nome, endereco, escola, responsavel, telefone)
- `rota` (id, nome, horario, van_id)
- `rota_aluno` (rota_id, aluno_id) — tabela de ligação entre Rota e Aluno

## Regras de negócio

- Uma Van tem um Motorista responsável.
- Uma Rota está vinculada a uma Van e tem vários Alunos.
- O Administrador (login `admin` / senha `admin`) tem acesso a todos os cadastros.
- Cada Motorista tem seu próprio login/senha (cadastrados pelo Administrador) e, ao entrar, só vê as rotas da van em que é responsável, podendo confirmar a presença dos alunos.

## Requisitos

- JDK 17 ou superior
- NetBeans com suporte a projetos Maven (padrão desde o NetBeans 8)
- Conexão com a internet na primeira abertura, para o Maven baixar o driver do SQLite
