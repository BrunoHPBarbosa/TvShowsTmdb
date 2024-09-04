# Tv Shows App

Create a native android app, using the TheMovieDb API.


## :movie_camera: Video
<img src="https://github.com/user-attachments/assets/ad14a11f-0152-485e-9b44-c3a3b8d7e5f1" width="250">&emsp;<img src="https://github.com/user-attachments/assets/b78beb0b-35da-4517-b19a-979558485dc5" width="250">&emsp;<img src="https://github.com/user-attachments/assets/18f16fed-ef33-4a1a-bde0-b5473f7d5e32" width="250">&emsp;<img src="https://github.com/user-attachments/assets/dcc701e6-145b-46be-973b-54cf01181448" width="250">

## :camera_flash: Screenshots
<img src="https://github.com/user-attachments/assets/43c890b6-823a-4637-bf43-58737d208b96" width="250">&emsp;
<img src="https://github.com/user-attachments/assets/d79eb15c-c8cd-45b4-9065-9c2f4b12e087" width="250">&emsp;
<img src="https://github.com/user-attachments/assets/a59704d1-e933-4bf0-a407-e4288aabbb02" width="250">&emsp;



🛠️ Technologies used

- Kotlin
- Clean Architecture
- MVVM
- Retrofit
- Dagger Hilt
- Coroutines
- Glide
- Flow
- State Flow
- Room Database
- Jetpack Components (ViewBinding, Navigation Components)

  * Clean Architecture:
    - Separação de conceitos;
    - Interface com base em modelos de dados;
    - Única fonte de informações;
    - Fonte: https://developer.android.com/topic/architecture?hl=pt-br
   
      * MVVM:
    - Separação de responsabilidades;
    - Facilidade de manutenção;
    - Testabilidade.
   
      * Room Database
    - A biblioteca de persistência Room oferece uma camada de abstração sobre o SQLite para permitir acesso fluente ao banco de dados, aproveitando toda a capacidade do SQLite.A persistência de dados local pode ser muito útil para apps que processam quantidades não triviais de dados estruturados. O caso de uso mais comum é armazenar em cache partes importantes de dados para que, quando o dispositivo não puder acessar a rede, o usuário ainda consiga ter acesso a esse conteúdo off-line.
   
      * Retrofit:
  - Biblioteca para se conectar a um serviço REST da Web e receber uma resposta.
  - Fonte: https://square.github.io/retrofit.
 
    * Serialization
  - Analisa a resposta JSON em um objeto de dado
  - Fonte: https://kotlinlang.org/docs/serialization.html#0
 
    * Dagger Hilt
  - Biblioteca de injeção de dependência para Android que reduz a injeção manual de código boilerplate no projeto, oferecendo contêineres para cada classe do Android e gerenciando os ciclos de vida de cada uma automaticamente. 
  - Fonte: https://developer.android.com/training/dependency-injection/hilt-android?hl=pt-br
 
    * Coroutines
  - Padrão de projeto de simultaneidade para simplificar o código que é executado de forma assíncrona. As corrotinas ajudam a gerenciar tarefas de longa duração que podem bloquear a linha de execução principal e fazer com que seu app pare de responder.
  - Fonte: https://developer.android.com/kotlin/coroutines?hl=pt-br
 
* Flow 
  - Em corrotinas, um fluxo é um tipo que pode emitir vários valores sequencialmente. Conceitualmente, um fluxo é um stream de dados que pode ser computado de forma assíncrona.
  - Fonte:https://developer.android.com/kotlin/flow?hl=pt-br
  - 
* StateFlow
  - É um fluxo observável detentor de estado que emite as atualizações de estado novas e atuais para os coletores. No Android, StateFlow é uma ótima opção para classes que precisam manter um estado mutável observável.
  - Fonte: https://developer.android.com/kotlin/flow/stateflow-and-sharedflow?hl=pt-br
 
* Data Binding(ViewBinding)
  - É uma biblioteca de suporte que permite vincular componentes de interface nos seus layouts a fontes de dados no app usando um formato declarativo em vez de programaticamente.
  - Fonte:https://developer.android.com/topic/libraries/data-binding?hl=pt-br
 
 * Glide 
  - É uma estrutura de gerenciamento de mídia e carregamento de imagens de código aberto rápida e eficiente para Android que envolve decodificação de mídia, cache de memória e disco e pool de recursos em uma interface simples e fácil de usar.
  - Fonte:https://github.com/bumptech/glide
  - 
* Timber 
  - Um logger com uma API pequena e extensível que fornece utilidade além da Logclasse normal do Android.
  - Fonte:https://github.com/JakeWharton/timber
