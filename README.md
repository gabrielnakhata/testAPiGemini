# testAPiGemini

## ⚡ Configuração Rápida do Firebase para uso da API do Gemini

1. **Criar o App no [Firebase Console](https://console.firebase.google.com/)**:
    - Package Name: "Adicione o do seu projeto" (ex: `com.iatest.testapigemini`)

2. **Baixar e Colocar o `google-services.json`**:
    - Salvar em: `app/google-services.json`

3. **Ativar a API do Gemini**:
    - No menu lateral do Firebase, vá em **Build > Vertex AI in Firebase** e clique em **Get Started**.

4. **Liberar o App Check (Desenvolvimento)**:
    - Copie o token impresso no **Logcat** (filtre por `DebugAppCheckProvider`).
    - Cadastre o token em **App Check > Apps > (três pontinhos) > Manage debug tokens**.
      *(Ou na aba **APIs**, defina o **Vertex AI in Firebase** como **Unenforce**).*