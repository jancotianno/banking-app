# Banking Application

## Descrizione

Questa applicazione fornisce un'API REST per interagire con i servizi bancari, tra cui la consultazione del saldo, l'esecuzione di bonifici e la gestione delle transazioni. Utilizza l'API di **Fabrick** per comunicare con il sistema bancario.

## Funzionalità

1. **Richiesta Saldo**
    - **Endpoint**: `GET /accounts/{accountId}/balance`
    - Recupera il saldo di un determinato account bancario.

2. **Esecuzione Bonifico**
    - **Endpoint**: `POST /accounts/{accountId}/transfers`
    - Esegue un bonifico dal conto specificato.

3. **Richiesta Transazioni**
    - **Endpoint**: `POST /accounts/{accountId}/transactions`
    - Recupera le transazioni di un account bancario in un determinato intervallo di date.

## Setup

### Prerequisiti

- **JDK 11+**
- **Maven**
- **API Key** di Fabrick

### Installazione

1. Clona il repository:
   ```bash
   git clone https://github.com/jancotianno/banking-app.git
