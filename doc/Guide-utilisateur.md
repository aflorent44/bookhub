# 👤 Système de gestion de bibliothèque – Scénarios utilisateurs

---

## 1. 👤 Création de compte

### Étapes
- Accéder à la page d’accueil  
- Cliquer sur **"S’inscrire"**  
- Remplir le formulaire :
  - Prénom / Nom  
  - Pseudo
  - Email  
  - Mot de passe  
- Valider  

### Résultat attendu
- ✔ Compte créé  
- ✔ Redirection vers la page de connexion  

![alt text](doc/images/inscription.png)

---

## 🔐 2. Connexion

### Étapes
- Cliquer sur **"Connexion"**  
- Entrer email + mot de passe  
- Valider  

### Résultat attendu
- ✔ Accès à l’espace utilisateur  
- ✔ Redirection selon le rôle  

![alt text](doc/images/inscription.png)
![alt text](doc/images/profil.png)

---

## 📚 3. Consulter le catalogue

### Étapes
- Accéder à **"Catalogue"**  
- Parcourir la liste des livres  
- Cliquer sur un livre pour voir le détail  

### Fonctionnalités
- Pagination  
- Badge de disponibilité  
- Affichage couverture  

![alt text](web\public\assets\book_cover.jpg)

---

## 🔎 4. Rechercher un livre

### Étapes
- Utiliser la barre de recherche  
- Saisir titre / auteur / ISBN  
- Appliquer des filtres :
  - Auteur
  - Genre
  - Editeur
  - Annee
  - Pays
  - Langue  

### Résultat attendu
- ✔ Résultats filtrés instantanément  

![alt text](web\public\assets\book_cover.jpg)

---

## 📖 5. Emprunter un livre

### Conditions
- Livre disponible  
- Maximum 3 emprunts  
- Aucun retard  

### Étapes
- Ouvrir la fiche livre  
- Cliquer sur **"Emprunter ce livre"**  

### Résultat attendu
- ✔ Confirmation d’emprunt  
- ✔ Date de retour affichée  

![alt text](doc/images/emprunte.png)

---

## 🔄 6. Retourner un livre (Bibliothécaire)

### Étapes
- Accéder à la gestion des emprunts  
- Cliquer sur **"Retour"**  

### Résultat attendu
- ✔ Livre de nouveau disponible  
- ✔ Retard calculé automatiquement  

![alt text](web\public\assets\book_cover.jpg)

---

## 📌 7. Réserver un livre

### Étapes
- Ouvrir un livre indisponible  
- Cliquer sur **"Réserver"**  

### Résultat attendu
- ✔ Ajout à la file d’attente  
- ✔ Affichage de date de réservation  

![alt text](doc/images/reserve.png)

---

## ⭐ 8. Noter et commenter

### Conditions
- Avoir emprunté le livre  

### Étapes
- Accéder à la fiche livre  
- Ajouter :
  - Note (1 à 5)  
  - Commentaire  

### Résultat attendu
- ✔ Note moyenne mise à jour  
- ✔ Commentaire visible  

![alt text](web\public\assets\book_cover.jpg)

---

## 📊 9. Dashboard utilisateur

### Contenu
- Emprunts en cours  
- Retards  
- Réservations  
- Historique  

![alt text](doc/images/profil.png)