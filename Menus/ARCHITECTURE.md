# Architecture Clean - Menus API

## Vue d'ensemble

```plantuml
@startuml CleanArchitecture
skinparam backgroundColor #FEFEFE
skinparam classBackgroundColor #F4F4F4
skinparam classBorderColor #333333

' Définition des couleurs pour chaque couche
package "4 PRESENTATION LAYER (REST API)" <<frame>> #FFA07A {
    class MenuResource {
        - service: MenuService
        + getAllMenus(): String
        + getMenu(id: int): String
        + createMenu(menu: Menu): Response
        + updateMenu(id: int, menu: Menu): Response
        + deleteMenu(id: int): Response
    }
}

package "3 APPLICATION LAYER (USE CASES)" <<frame>> #87CEEB {
    class MenuService {
        - menuRepo: MenuRepositoryInterface
        - platsRepo: PlatsRepositoryInterface
        + getAllMenusEnrichis(): ArrayList
        + getMenuEnrichi(id: int): MenuEnrichi
        + addMenu(menu: Menu): boolean
        + updateMenu(id: int, menu: Menu): boolean
        + deleteMenu(id: int): boolean
    }
}

package "2 DOMAIN LAYER (CORE - POJO PURE)" <<frame>> #90EE90 {
    package "Entities" {
        class Menu {
            - id: int
            - nom: String
            - platsIds: List<Integer>
            - auteur: String
            - dateCreation: String
            - dateModification: String
        }
        
        class Plat {
            - id: int
            - nom: String
            - description: String
            - prix: double
        }
        
        class MenuEnrichi {
            - id: int
            - nom: String
            - plats: List<Plat>
            - createurId: int
            - createurNom: String
            - dateCreation: String
            - dateModification: String
            - prixTotal: double
        }
    }
    
    package "Repositories (Interfaces)" {
        interface MenuRepositoryInterface {
            {abstract} getMenu(id: int): Menu
            {abstract} getAllMenus(): ArrayList
            {abstract} addMenu(menu: Menu): boolean
            {abstract} deleteMenu(id: int): boolean
            {abstract} updateMenu(...): boolean
            {abstract} close(): void
        }
        
        interface PlatsRepositoryInterface {
            {abstract} getPlat(id: int): Plat
            {abstract} getAllPlats(): ArrayList
            {abstract} close(): void
        }
    }
}

package "1 INFRASTRUCTURE LAYER (DRIVERS & ADAPTERS)" <<frame>> #FFD700 {
    package "Database" {
        class MenuRepositoryMariadb {
            - dbConnection: Connection
            + getMenu(id: int): Menu
            + getAllMenus(): ArrayList
            + addMenu(menu: Menu): boolean
            + deleteMenu(id: int): boolean
            + updateMenu(...): boolean
            - buildMenuFromResultSet(result): Menu
        }
    }
    
    package "API Client" {
        class PlatsRepositoryAPI {
            - url: String
            + getPlat(id: int): Plat
            + getAllPlats(): ArrayList
        }
    }
    
    package "Configuration" {
        class CDIConfiguration {
            + producePlatsRepository(): PlatsRepositoryInterface
            + disposePlatsRepository(repo): void
        }
    }
}

package "ENTRY POINT (CDI + REST)" <<frame>> #DDA0DD {
    class MenuApplication {
        + openDbConnection(): MenuRepositoryInterface
        + closeDbConnection(menuRepo): void
    }
}

' DÉPENDANCES (flèches qui vont VERS L'INTÉRIEUR - Clean Architecture)
MenuResource --> MenuService: uses
MenuService --> MenuRepositoryInterface: depends on
MenuService --> PlatsRepositoryInterface: depends on
MenuResource -.-> Menu: works with
MenuResource -.-> MenuEnrichi: returns

MenuRepositoryMariadb ..|> MenuRepositoryInterface: implements
PlatsRepositoryAPI ..|> PlatsRepositoryInterface: implements

MenuService --> Menu: uses
MenuService --> MenuEnrichi: creates
MenuService --> Plat: uses

MenuRepositoryMariadb --> Menu: reads/writes
PlatsRepositoryAPI --> Plat: consumes

MenuApplication --> MenuRepositoryInterface: produces
CDIConfiguration --> PlatsRepositoryInterface: produces


@enduml
```

## Structure du projet

```
source/java/menu/menus/
├── MenuApplication.java              (entry point, injection des repositories)
│
├── domain/
│   ├── entities/
│   │   ├── Menu.java                (simple POJO)
│   │   ├── Plat.java                (simple POJO)
│   │   └── MenuEnrichi.java         (simple POJO)
│   │
│   └── repositories/
│       ├── MenuRepositoryInterface.java
│       └── PlatsRepositoryInterface.java
│
├── application/services/
│   └── MenuService.java             (logique métier, les vrais use cases)
│
├── infrastructure/repositories/
│   ├── MenuRepositoryMariadb.java   (accès BD)
│   └── PlatsRepositoryAPI.java      (appelle l'API plats)
│
├── infrastructure/configuration/
│   └── CDIConfiguration.java        (production des PlatsRepository)
│
└── presentation/resources/
    └── MenuResource.java            (endpoints REST)
```


## Les dépendances

```
REST (MenuResource)
  ↓ uses
Service (MenuService)
  ↓ implements
Domain (interfaces)
  ↑ depends on
Infrastructure (MenuRepositoryMariadb, PlatsRepositoryAPI)
```

Les dépendances vont vers le Domain. C'est la clé.


## C'est clean architecture porque

- **Domain indépendant** - Menu.java/Plat.java sont juste des classes Java normales. Aucun framework dedans. Aucun import Jakarta, aucun import de BD.

- **Dépendances inversées** - MenuService dépend de MenuRepositoryInterface (abstraction), pas de MenuRepositoryMariadb (implémentation). Si on change de BD, on change juste l'implémentation.

- **Logique métier isolée** - MenuService ne connaît pas SQL, ne connaît pas HTTP, ne connaît pas JSON. Il connaît juste la logique des menus.

- **Facile à tester** - On peut tester MenuService avec des mocks. On peut tester le Domain en l'incluant dans n'importe quel projet Java.

- **Facile à changer** - Veux remplacer MariaDB par PostgreSQL? Change juste MenuRepositoryMariadb et crée PostgresRepositoryImplementation. Le reste ne change pas.


