# Architecture du Projet Menus - Pourquoi et Comment

## 🍽️ Contexte Métier

Ce projet gère les **menus d'un restaurant**. Chaque menu :
- Regroupe plusieurs plats
- A un responsable (auteur)
- A un prix total calculé
- Doit être validé avant création
- Doit être enrichi avec les détails complets des plats

**Exemple concret** :
```
Menu "Déjeuner d'Affaires"
  ├─ Plat ID #3 (Salade César)
  ├─ Plat ID #7 (Poulet Rôti)
  └─ Plat ID #12 (Tiramisu)
→ Prix total : 42,50€
```

Pour afficher ce menu à un client, il faut :
1. ✅ Chercher le menu en BD
2. ✅ Récupérer chaque plat (nom, description, prix)
3. ✅ Calculer le prix total
4. ✅ Retourner le tout en JSON

## 📐 Diagramme UML - Architecture en Couches

```plantuml
@startuml MeUnusArchitecture
skinparam backgroundColor #FEFEFE
skinparam classBackgroundColor #F8F8F8
skinparam classBorderColor #333333
skinparam packageBackgroundColor #E8E8E8
skinparam arrowColor #666666

title Menus API - Architecture Clean

' ============================================
' COUCHE 4 : PRESENTATION (REST)
' ============================================
package "4️⃣ PRESENTATION LAYER (REST API)" <<frame>> #FFA07A {
    class MenuResource {
        - service: MenuService
        --
        + getAllMenus(): String
        + getMenu(id: int): String
        + createMenu(menu: Menu): Response
        + updateMenu(id: int, menu: Menu): Response
        + deleteMenu(id: int): Response
    }
}

' ============================================
' COUCHE 3 : APPLICATION (BUSINESS LOGIC)
' ============================================
package "3️⃣ APPLICATION LAYER (USE CASES)" <<frame>> #87CEEB {
    class MenuService {
        - menuRepo: MenuRepositoryInterface
        - platsRepo: PlatsRepositoryInterface
        --
        + getAllMenusEnrichis(): ArrayList<MenuEnrichi>
        + getMenuEnrichi(id: int): MenuEnrichi
        + addMenu(menu: Menu): boolean
        + updateMenu(id: int, menu: Menu): boolean
        + deleteMenu(id: int): boolean
        - enrichirMenu(menu: Menu): MenuEnrichi
        - validerPlats(platsIds: List): boolean
        - getPlatsEnrichis(platsIds: List): ArrayList<Plat>
    }
}

' ============================================
' COUCHE 2 : DOMAIN (CORE BUSINESS)
' ============================================
package "2️⃣ DOMAIN LAYER (CORE - POJO PURE)" <<frame>> #90EE90 {
    package "📦 Entities" {
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
    
    package "🔌 Repository Interfaces" {
        interface MenuRepositoryInterface {
            {abstract} + getMenu(id: int): Menu
            {abstract} + getAllMenus(): ArrayList<Menu>
            {abstract} + addMenu(menu: Menu): boolean
            {abstract} + updateMenu(...): boolean
            {abstract} + deleteMenu(id: int): boolean
            {abstract} + close(): void
        }
        
        interface PlatsRepositoryInterface {
            {abstract} + getPlat(id: int): Plat
            {abstract} + getAllPlats(): ArrayList<Plat>
            {abstract} + close(): void
        }
    }
}

' ============================================
' COUCHE 1 : INFRASTRUCTURE (DRIVERS)
' ============================================
package "1️⃣ INFRASTRUCTURE LAYER (DRIVERS & ADAPTERS)" <<frame>> #FFD700 {
    package "🗄️ Database" {
        class MenuRepositoryMariadb {
            - dbConnection: Connection
            --
            + getMenu(id: int): Menu
            + getAllMenus(): ArrayList<Menu>
            + addMenu(menu: Menu): boolean
            + updateMenu(...): boolean
            + deleteMenu(id: int): boolean
            + close(): void
        }
    }
    
    package "📄 Data Sources" {
        class PlatsRepositoryJSON {
            - plats: ArrayList<Plat>
            - filePath: String
            --
            + getPlat(id: int): Plat
            + getAllPlats(): ArrayList<Plat>
            + close(): void
        }
        
        class PlatsRepositoryAPI {
            - url: String
            --
            + getPlat(id: int): Plat
            + getAllPlats(): ArrayList<Plat>
            + close(): void
        }
    }
}

' ============================================
' ENTRY POINT : CDI + REST
' ============================================
package "🎯 APPLICATION BOOTSTRAP" <<frame>> #DDA0DD {
    class MenuApplication {
        --
        +@Produces+ produceMenuRepository(): MenuRepositoryInterface
        +@Disposes+ disposeMenuRepository(repo): void
        +@Produces+ producePlatsRepository(): PlatsRepositoryInterface
        +@Disposes+ disposePlatsRepository(repo): void
    }
    
    note on right of MenuApplication
        Configuration CDI pour injection
        des dépendances + Point d'entrée
        REST (@ApplicationPath("/api"))
    end note
}

' ============================================
' DÉPENDANCES (flèches dirigées)
' ============================================

' Presentation -> Application
MenuResource --> MenuService: uses\n(injection CDI)

' Application -> Domain (interfaces)
MenuService --> MenuRepositoryInterface: depends on
MenuService --> PlatsRepositoryInterface: depends on

' Application -> Domain (entities)
MenuService --> Menu: uses
MenuService -.-> MenuEnrichi: creates
MenuService -.-> Plat: uses

' Presentation -> Domain (working with)
MenuResource -.-> Menu: consumes
MenuResource -.-> MenuEnrichi: produces JSON

' Implementation -> Interface (realizes)
MenuRepositoryMariadb ..|> MenuRepositoryInterface: implements
PlatsRepositoryJSON ..|> PlatsRepositoryInterface: implements
PlatsRepositoryAPI ..|> PlatsRepositoryInterface: implements

' Infrastructure -> Domain
MenuRepositoryMariadb --> Menu: reads/writes

' CDI Production
MenuApplication -.->|produces| MenuRepositoryInterface

' Data sources
PlatsRepositoryJSON -.-> Plat: loads from JSON
PlatsRepositoryAPI -.-> Plat: fetches from API

@enduml
```

## 📁 Structure du Projet

```
src/main/java/menu/menus/
│
├── MenuApplication.java              ✨ CDI Entry Point + Config
│                                      (@ApplicationPath, @Produces, @Disposes)
│
├── domain/                            ✅ DOMAIN LAYER (CORE - Independent)
│   ├── entities/
│   │   ├── Menu.java                 (simple POJO - no framework)
│   │   ├── Plat.java                 (simple POJO - no framework)
│   │   └── MenuEnrichi.java          (simple POJO - no framework)
│   │
│   └── repositories/ (Interfaces)
│       ├── MenuRepositoryInterface.java   (contract)
│       └── PlatsRepositoryInterface.java  (contract)
│
├── application/                       🔄 APPLICATION LAYER (Business Logic)
│   └── services/
│       └── MenuService.java           (use cases, validation, enrichment)
│
├── infrastructure/                    🔌 INFRASTRUCTURE LAYER (Implementations)
│   └── repositories/
│       ├── MenuRepositoryMariadb.java (MariaDB implementation)
│       ├── PlatsRepositoryJSON.java   (JSON file implementation)  ← NEW
│       └── PlatsRepositoryAPI.java    (REST API implementation)
│
└── presentation/                      📡 PRESENTATION LAYER (REST)
    └── resources/
        └── MenuResource.java          (@Path("/menus"), @GET, @POST, etc.)
```

## 🔄 Flux Réel d'une Requête GET /api/menus

```
1. Client HTTP
   ↓
2. MenuResource.getAllMenus()
   ├─ Appelle MenuService.getAllMenusEnrichis()
   │
3. MenuService.getAllMenusEnrichis()
   ├─ Récup tous les menus en BD
   │  └─ menuRepo.getAllMenus() → MenuRepositoryMariadb ↔ MariaDB
   │
   ├─ Pour chaque menu:
   │  ├─ Récup les plats (IDs)
   │  │  └─ platsRepo.getPlat(id) → PlatsRepositoryJSON ↔ plats-utilisateurs.json
   │  ├─ Calcul prix total
   │  └─ Crée MenuEnrichi avec tous les détails
   │
   └─ Retour: List<MenuEnrichi>
       ↓
4. MenuResource convertit en JSON
   ↓
5. Response 200 OK + JSON au client
```

**Points clés** :
- MenuService **ne sait pas** que les menus viennent de MariaDB
- MenuService **ne sait pas** que les plats viennent d'un JSON
- MenuService **fait juste** le métier : enrichir et valider
- Le reste est "branché" via interfaces

## ✨ Pourquoi Cette Architecture Pour Ce Projet ?

### 1. **Pas de Dépendance sur la BD** ✅
**Avant**: Si MenuService importait `MenuRepositoryMariadb`
```java
// ❌ Mauvais - couplage fort
MenuRepositoryMariadb repo = new MenuRepositoryMariadb(connection);
repo.getMenu(1);
```

**Maintenant**: MenuService parle à une interface
```java
// ✅ Bon - MenuService injecté
MenuService reçoit "une chose qui récupère des menus"
// Peu importe d'où ils viennent (BD, JSON, API, Mock)
```

**Concrètement :** Demain si on veut charger les menus d'une API REST au lieu de MariaDB, on crée juste `MenuRepositoryAPI` sans toucher au code de MenuService.

### 2. **Facile à Tester Manuellement** ✅
```java
// Pour test unitaire:
MenuRepositoryInterface fakeRepo = new FakeMenuRepository();
MenuService service = new MenuService(fakeRepo, fakeRepo);
// Aucune BD requise, aucun JSON à charger
```

### 3. **Séparation des Responsabilités** ✅
| Fichier | Fait QUOI ? | Fait PAS QUOI ? |
|---------|-------------|------------------|
| `MenuService.java` | Logique métier, enrichissement, validation | SQL, REST, JSON parsing |
| `MenuResource.java` | Convertir requêtes HTTP → appels service → JSON | Logique métier complexe |
| `MenuRepositoryMariadb.java` | JDBC, SQL, transactions | Aucune logique métier |
| `PlatsRepositoryJSON.java` | Charger et parser le JSON | Aucune logique métier |
| `Menu.java`, `Plat.java` | Juste stocker les données | Aucun "comment c'est utilisé" |

### 4. **Quand Ça S'Ajoute, C'Est Facile** ✅
**Exemple: Ajouter "enrichissement avec images"**
```java
// Avant: Faudrait toucher à 5 classes
// Maintenant: 1 seule modification
MenuService.enrichirMenu() {
    // ... code existant ...
    menu.images = platsRepo.getPlatsWithImages(ids);  // ← Juste ça
    return menu;
}
```

### 5. **Règles Métier Visibles** ✅
Tous les "comment on valide un menu" est dans `MenuService` (classe métier).
Non dispersé entre REST endpoints et code DB.

Exemple: "Un menu doit avoir au moins 1 plat"
```java
// C'est ici, facile à trouver
MenuService.validerPlats(platsIds) {
    if (platsIds.isEmpty()) {
        throw new IllegalArgumentException("Au moins 1 plat requis");
    }
    // ...
}
```

## 🔧 Design Decisions Expliquées

### Pourquoi MenuApplication gère la CDI ?
**MenuApplication = point d'entrée de toute l'app**
```java
@ApplicationPath("/api")  // ← Première classe chargée par GlassFish
@ApplicationScoped        // ← Vit pendant toute la vie de l'app
public class MenuApplication extends Application {
    @Produces               // ← Les "producteurs" de beans
    public MenuRepositoryInterface produceMenuRepository() {
        return new MenuRepositoryMariadb(dbConnection);
    }
    // ...
}
```

**Bénéfice** : Toute la config "qui crée/détruit quoi" est au même endroit.

### Pourquoi PlatsRepositoryJSON ?
**Situation réelle** : La BD des plats n'était pas prête.
```
Options :
  ❌ Attendre la BD     → Bloque le développement
  ❌ Hardcoder plats   → Pas professionnel
  ✅ Charger JSON      → Travaille, facile de changer
```

Demain quand l'API plats sera prête, on change 1 ligne :
```java
@Produces
public PlatsRepositoryInterface producePlats() {
    // return new PlatsRepositoryJSON();  // ← ancienne ligne
    return new PlatsRepositoryAPI();       // ← nouvelle ligne
}
```

### Pourquoi MenuEnrichi ?
**Menu en BD** : `{ id, nom, platsIds, auteur, dates }`
**Menu enrichi (ce qu'on retourne)** :
```json
{
  "id": 1,
  "nom": "Déjeuner Affaires",
  "createurNom": "Chef Pierre",
  "plats": [                          // ← Enrichi
    { "id": 3, "nom": "Salade César", "description": "...", "prix": 12 },
    { "id": 7, "nom": "Poulet Rôti", "description": "...", "prix": 18 }
  ],
  "prixTotal": 30,                   // ← Calculé
  "dateCreation": "2026-04-09"
}
```

**Pourquoi deux classes** ?
- `Menu` = ce qu'on a en BD, léger, rapide
- `MenuEnrichi` = ce qu'on renvoie au client, riche, prêt à afficher
- Les séparer = facile de changer l'un sans l'autre




