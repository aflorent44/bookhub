# 📄 5.4 Rapport Technique – BookHub

## 1. Choix d’architecture justifiés

### Architecture générale

Nous avons choisi une **architecture en couches (n-tier)** avec séparation claire :

- **Frontend :** Angular 25 (SPA)
- **Backend :** Spring Boot (API REST)
- **Base de données :** SQL Server

### Justifications

#### 🔹 Architecture REST

- Permet une séparation claire entre frontend et backend
- Facilite la maintenabilité et les évolutions
- Compatible avec Angular via HttpClient

#### 🔹 Spring Boot

- Framework robuste et standard en Java
- Intégration simplifiée avec :
  - Spring Security (authentification)
  - Spring Data JPA (accès aux données)
- Gain de temps grâce à l’auto-configuration

#### 🔹 Angular

- Framework structuré adapté aux applications complexes
- Gestion des composants réutilisables
- Bonne gestion des formulaires et validations

#### 🔹 JWT pour l’authentification

- Stateless → pas de session côté serveur
- Adapté aux API REST
- Facilement stockable côté client

#### 🔹 Base de données relationnelle

- Données fortement structurées (utilisateurs, livres, emprunts)
- Intégrité référentielle importante
- Utilisation de JPA pour abstraction

---

## 2. Difficultés rencontrées et solutions

### 🔸 Gestion des relations entre entités

**Problème :**
- Relations complexes (User ↔ Loan ↔ Book ↔ Reservation)
- Risque de boucles infinies (JSON serialization)

**Solution :**
- Utilisation de `@JsonIgnore`
- Mise en place de DTO pour découpler les entités

---

### 🔸 Sécurisation des endpoints

**Problème :**
- Gestion des rôles (USER, LIBRARIAN, ADMIN)
- Accès restreint aux routes sensibles

**Solution :**
- Configuration de Spring Security avec :
  - Filtres JWT
  - Annotations `@PreAuthorize`
- Middleware Angular pour gérer les routes protégées

---

### 🔸 Gestion des états (emprunt / disponibilité)

**Problème :**
- Synchronisation entre disponibilité des livres et emprunts
- Risque d’incohérence (ex : emprunter un livre déjà pris)

**Solution :**
- Vérifications côté backend :
  - Disponibilité
  - Nombre d’emprunts utilisateur
- Transactions pour garantir la cohérence

---

### 🔸 Communication Frontend / Backend

**Problème :**
- Gestion des erreurs API
- Format des données

**Solution :**
- Intercepteur Angular pour :
  - Ajouter le token JWT
  - Centraliser les erreurs
- Normalisation des réponses API

---

## 3. Améliorations possibles

### 🔹 Fonctionnelles

* Notifications pour les réservations et retards
* Système de recommandations
* Dimention communautaires
* Déploiement Docker

---

### 🔹 Techniques

- Mise en place de **tests automatisés plus complets**
- Passage à une **architecture microservices** (si montée en charge)
- Ajout de **Docker** pour faciliter le déploiement

---

### 🔹 Performance

- Mise en cache (ex : catalogue)
- Optimisation des requêtes SQL (index, pagination avancée)

---

### 🔹 UX/UI

- Amélioration de l’accessibilité (RGAA)
- Mode sombre
- Expérience mobile optimisée

---

## 4. Conclusion

Le projet BookHub repose sur une architecture moderne et robuste adaptée aux applications web actuelles.

Malgré certaines difficultés techniques, les solutions mises en place ont permis de garantir :
- La sécurité des données
- La cohérence métier
- Une bonne expérience utilisateur

Des améliorations restent possibles, notamment sur la performance, les tests et les fonctionnalités avancées, ouvrant la voie à des évolutions futures.