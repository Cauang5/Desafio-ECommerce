# Desafio - Ecommerce

Desafio 03 do programa de bolsas da Uol Compass, onde foi solicitado o desenvolvimento de um e-commerce. 
Este projeto foi desenvolvido utilizando a arquitetura layered e implementa uma API RESTful para gerenciar produtos e vendas. O sistema oferece funcionalidades como criação, leitura, atualização e exclusão de produtos e vendas, controle de estoque, inativação de produtos, geração de relatórios de vendas, e cache otimizado para operações de leitura. A aplicação inclui autenticação via JWT e autorização baseada em permissões, com regras específicas para usuários com privilégios de ADMIN.

Funcionalidades principais:
CRUD de Produtos: Permite que os usuários criem, leiam, atualizem e excluam produtos, com validações como preço positivo. Produtos associados a vendas não podem ser deletados, mas podem ser inativados.
Controle de Estoque: O sistema garante que produtos não possam ser vendidos quando o estoque estiver insuficiente.
CRUD de Vendas: Uma venda deve conter no mínimo 1 produto para ser concluída. Inclui controle de quantidade de produtos vendidos e atualização de estoque.
Relatórios de Vendas: Métodos para gerar relatórios de vendas por data, por mês e pela semana atual.
Gerenciamento de Cache: As operações de leitura de todos os produtos e vendas são otimizadas por meio de cache. O cache é invalidado automaticamente após operações de inserção para garantir a consistência dos dados.
Tratamento de Exceções: Todas as exceções são tratadas de maneira padronizada, com respostas uniformes.
Autenticação e Autorização: Implementação de autenticação via JWT, com autorização para garantir que apenas usuários autenticados acessem determinadas funcionalidades. Apenas usuários com permissão de ADMIN podem deletar ou cadastrar produtos e outros usuários com a mesma permissão.
Reset de Senha: Método para permitir que usuários redefinam suas senhas.
