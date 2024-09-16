
# AI Credit Analyzer App

## Descrição do Projeto
O **AI Credit Analyzer** é um aplicativo que fornece análises preditivas de crédito utilizando a API GPT-3.5 da OpenAI. O app auxilia os usuários a prever a probabilidade de inadimplência e a aprovação de crédito com base em suas informações financeiras.

## Funcionalidades
- **Análise de crédito**: Previsão da probabilidade de inadimplência baseada no score do Serasa.
- **Predição de crédito**: Análise da probabilidade de aprovação de crédito com base na renda e valor solicitado.
- **Autenticação**: Sistema de login e registro de usuários utilizando Firebase.
- **Navegação Simples**: Navegação intuitiva entre as telas de análise, predição e autenticação.

## Estrutura de Pastas

```bash
app/
└── manifests/
└── kotlin+java/
    └── com.example.aicreditanalyzer/
        ├── activity/
        │   ├── CreditAnalysisActivity.kt
        │   ├── CreditPredictionActivity.kt
        │   ├── HomeActivity.kt
        │   ├── LoginActivity.kt
        │   ├── MyApplication.kt
        │   └── RegisterActivity.kt
        ├── api/
        │   └── GPTApiService.kt
        ├── model/
        │   ├── GPTResponse.kt
        │   └── User.kt
        └── service/
            └── AuthService.kt
res/
├── drawable/
│   ├── custom_input_style.xml
│   ├── ic_analysis.xml
│   ├── ic_launcher_background.xml
│   ├── ic_launcher_foreground.xml
│   └── ic_prediction.xml
├── layout/
│   ├── activity_credit_analysis.xml
│   ├── activity_credit_prediction.xml
│   ├── activity_home.xml
│   ├── activity_login.xml
│   └── activity_register.xml
└── values/
    ├── colors.xml
    ├── strings.xml
    ├── styles.xml
    └── themes.xml
```

## Tecnologias Utilizadas
- **Kotlin**: Linguagem principal do desenvolvimento do app.
- **Firebase Authentication**: Usado para o sistema de autenticação de usuários.
- **Firebase Realtime Database**: Armazenamento de informações de usuários.
- **OpenAI API**: Fornece análises preditivas baseadas em inteligência artificial.
- **OkHttp**: Biblioteca para realizar as requisições HTTP à API da OpenAI.

## Configuração do Projeto
### Requisitos:
- Android Studio
- Firebase configurado no projeto
- Chave de API da OpenAI
