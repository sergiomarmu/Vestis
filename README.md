# 👕 Vestis App

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.1.x-purple?style=for-the-badge&logo=kotlin" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-2026.05.00-green?style=for-the-badge&logo=jetpackcompose" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Architecture-MVI_/_Clean-blue?style=for-the-badge" alt="Architecture" />
  <img src="https://img.shields.io/badge/Modularized-Yes-orange?style=for-the-badge" alt="Modularized" />
</p>

<p align="center">
  <strong>Vestis</strong> is a modular Android application built with Clean Architecture and MVI. It consumes the <em>FakeStore API</em> to manage a clothing catalog with a reactive local favorite system.
</p>

---

## 🚀 Features

* **👕 Catalog:** Product listing.
* **❤️ Favorites:** Reactive local persistence system using Room DB.
* **👤 Profile:** Displays user data and total favorites count.

---

## 🏗️ Architecture

The project is **Modularized** by layers and features to ensure scalability and separation of concerns:

* **`:app`** ➔ Core orchestrator, dependency injection, and global navigation.

* **`:feature`** ➔ Isolated presentation screens and user flows (MVI / Compose).
    * **`:feature:products` / `:feature:favorite` / `:feature:profile`**

* **`:domain`** ➔ Pure Kotlin layers holding business logic, business entities, and Use Cases.
    * **`:domain-products` / `:domain-favorite` / `:domain-profile`**

* **`:data`** ➔ Infrastructure implementations, API data sources (Retrofit), local caching and error handling.
    * **`:data-products` / `:data-favorite` / `:data-profile`**

* **`:core`** ➔ Shared utilities
    * **`:core-common` / `:core-presentation` / `:core-domain` / `:core-data`**

```mermaid
graph TB
    app[":app"]

    subgraph Layer_2 ["2. :feature Presentation Layer"]
        direction TB
        feat_products[":feature:products"]
        feat_favorite[":feature:favorite"]
        feat_profile[":feature:profile"]
    end

    subgraph Layer_3 ["3. :domain (Business Logic)"]
        direction TB
        domain_products[":products"]
        domain_favorite[":favorite"]
        domain_profile[":profile"]
    end

    subgraph Layer_4 ["4. :data (Infrastructure)"]
        direction TB
        data_products[":products"]
        data_favorite[":favorite"]
        data_profile[":profile"]
    end

    subgraph Layer_5 ["5. :core (Core)"]
        direction LR
        core_common[":common"]
        core_presentation[":presentation"]
        core_domain[":domain"]
        core_data[":data"]
    end

    app --> Layer_2
    Layer_2 --> Layer_3
    Layer_4 --> Layer_3

    Layer_2 --> Layer_5
    Layer_3 --> Layer_5
    Layer_4 --> Layer_5

    feat_products ~~~ domain_products
    domain_products ~~~ data_products
```

---

## 🛠️ Tech Stack

* **UI:** Jetpack Compose.
* **Asynchrony:** Kotlin Coroutines & Flows.
* **DI:** Hilt / Dagger.
* **Network & Local:** Retrofit + OkHttp & Room Database.
* **Image Loading:** Coil 3.

---

## 🧪 Testing

* **🧪 Unit Tests (`test/`):** ViewModels, Use cases and Repositories verified using **Turbine** and **MockK**.
* **📱 Android Tests (`androidTest/`):** Database instrumented validation.

---
