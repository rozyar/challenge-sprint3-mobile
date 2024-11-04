# AI Credit Analyzer App

## Membros da Equipe

- **Razyel Ferrari** - RM551875 - [irazyel](https://github.com/irazyel)
- **Rayzor Anael** - RM551832 - [rozyar](https://github.com/rozyar)
- **Derick Araújo** - RM551007 - [dericki](https://github.com/dericki)
- **Kalel Schlichting** - RM550620 - [K413l](https://github.com/K413l)

## Descrição do Projeto

O **AI Credit Analyzer** é um aplicativo que fornece análises preditivas de crédito utilizando a API GPT-3.5 da OpenAI. O app auxilia os usuários a prever a probabilidade de inadimplência e a aprovação de crédito com base em suas informações financeiras.

## Funcionalidades

- **Análise de Crédito**: Prediz a probabilidade de inadimplência com base no score do Serasa.
- **Predição de Crédito**: Analisa a probabilidade de aprovação de crédito com base na renda e no valor solicitado.
- **Autenticação**: Sistema de login e registro de usuários utilizando Firebase Authentication.
- **Operações CRUD**: Usuários podem criar, ler, atualizar e deletar suas análises e previsões de crédito.
- **Armazenamento Assíncrono**: Implementa armazenamento e recuperação de dados de forma assíncrona com Firebase Realtime Database.
- **Navegação Simples**: Navegação intuitiva entre as telas de análise, predição e autenticação.

### Explicação da Estrutura de Pastas

- **activity/**: Contém todas as classes de atividade responsáveis pelas telas da interface do usuário.
- **adapter/**: Contém classes adaptadoras personalizadas para as listas.
- **api/**: Inclui classes responsáveis por se comunicar com APIs externas, como a OpenAI.
- **model/**: Contém classes de dados (modelos) que representam as estruturas de dados do aplicativo.
- **service/**: Inclui classes de serviço que lidam com a lógica de negócios, como a autenticação.

Organizando os arquivos nessas pastas, melhoramos a legibilidade e a manutenção do código.

## Tecnologias Utilizadas

- **Kotlin**: Linguagem principal para o desenvolvimento do app.
- **Firebase Authentication**: Usado para o sistema de autenticação de usuários.
- **Firebase Realtime Database**: Armazenamento e recuperação assíncrona de dados dos usuários.
- **OpenAI API**: Fornece análises preditivas baseadas em inteligência artificial.
- **OkHttp**: Biblioteca para realizar as requisições HTTP à API da OpenAI.
- **Gson**: Biblioteca para serialização e desserialização de JSON.

## Configuração do Projeto

### Requisitos:

- **Android Studio**: IDE para o desenvolvimento do app Android.
- **Configuração do Firebase**: O projeto deve ser configurado com o Firebase (adicione o `google-services.json`).
- **Chave da API OpenAI**: Uma chave de API da OpenAI é necessária (fornecida no arquivo `api_key.txt`).

### Configurando o Firebase

1. **Adicionar Firebase ao Projeto**: Inclua o arquivo `google-services.json` no diretório `app/`.
2. **Regras do Firebase**: Configure as regras do banco de dados para proteger os dados dos usuários:

   ```json
   {
     "rules": {
       ".read": false,
       ".write": false,
       "credit_analyses": {
         "$userId": {
           ".read": "auth != null && auth.uid === $userId",
           ".write": "auth != null && auth.uid === $userId"
         }
       },
       "credit_predictions": {
         "$userId": {
           ".read": "auth != null && auth.uid === $userId",
           ".write": "auth != null && auth.uid === $userId"
         }
       }
     }
   }
   ```

## Implementação de Armazenamento Assíncrono com Firebase

Utilizamos o Firebase Realtime Database para armazenamento e recuperação de dados de forma assíncrona. As operações do Firebase, como leitura e escrita, são inerentemente assíncronas, garantindo que a interface do usuário não trave durante as operações.

### Exemplo de Operação de Leitura Assíncrona:

```kotlin
databaseRef.addValueEventListener(object : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        // Lida com o snapshot de dados de forma assíncrona
    }

    override fun onCancelled(error: DatabaseError) {
        // Lida com possíveis erros
    }
})
```

### Exemplo de Operação de Escrita Assíncrona:

```kotlin
databaseRef.setValue(data).addOnCompleteListener { task ->
    if (task.isSuccessful) {
        // Dados salvos com sucesso
    } else {
        // Lida com a falha
    }
}
```

Com operações assíncronas, garantimos que a interface do usuário permaneça responsiva enquanto os dados são processados.

## Atualizações e Mudanças

- **Implementação de Operações CRUD**: Usuários podem criar, ler, atualizar e deletar suas análises e previsões de crédito.
- **Manuseio de Dados Assíncrono**: Garantimos que todas as operações do Firebase sejam tratadas de forma assíncrona.
- **Navegação Melhorada**: Adicionamos botões nas telas de análise e predição para navegar para as respectivas listas.
- **Melhorias no Feedback ao Usuário**:
    - Exibição de mensagens informativas quando não há análises ou previsões encontradas, em vez de redirecionar o usuário.
    - Redirecionamento para a lista após atualizar ou deletar uma entrada.
- **Ordenação de Dados**: Listas de análises e previsões agora são ordenadas por data, mostrando as entradas mais recentes primeiro.
- **Otimização da Estrutura de Pastas**: Reorganizamos as pastas do projeto para melhor clareza e para atender aos padrões acadêmicos.

## Como Usar o App

1. **Registro e Login**: Usuários podem se registrar ou fazer login com uma conta existente.
2. **Realizar Análise de Crédito**:
    - Navegue até a seção "Análise de Crédito".
    - Insira seu score Serasa e receba uma análise.
    - Salve as análises para referência futura.
3. **Realizar Predição de Crédito**:
    - Navegue até a seção "Predição de Crédito".
    - Insira o valor desejado do crédito e sua renda mensal.
    - Receba uma previsão da probabilidade de aprovação.
    - Salve as previsões para referência futura.
4. **Visualizar Análises e Previsões**:
    - Acesse análises e previsões salvas nas respectivas seções.
    - Atualize ou delete entradas conforme necessário.

## Testes e Validação

- **Fluxos de Autenticação**: Testamos os processos de registro e login para garantir o correto funcionamento da autenticação.
- **Operações de Dados**: Verificamos que as operações CRUD para análises e previsões funcionam conforme esperado.
- **Operações Assíncronas**: Garantimos que as operações de leitura e escrita do Firebase não bloqueiam a interface do usuário e são tratadas corretamente.
- **Tratamento de Erros**: Implementamos mensagens de erro amigáveis para falhas de rede e entradas inválidas.
- **Navegação**: Confirmamos a navegação suave entre as diferentes seções do app.

## Conclusão

O aplicativo **AI Credit Analyzer** integra com sucesso capacidades avançadas de IA com recursos amigáveis ao usuário para fornecer insights financeiros valiosos. Ao aderir às melhores práticas em programação assíncrona e estruturação de aplicativos, o app oferece uma solução responsiva e de fácil manutenção.
