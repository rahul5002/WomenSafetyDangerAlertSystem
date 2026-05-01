# 🛡️ Women Safety Danger Alert System

A comprehensive **Object-Oriented Java application** designed to enhance women's safety by providing real-time danger zone alerts, crime area notifications, and emergency SOS features that instantly notify police and emergency contacts.

---

## 📌 Features

| Feature | Description |
|---|---|
| 🚨 **SOS Emergency Alert** | One-tap trigger notifies emergency contacts + nearest police station instantly |
| ⚠️ **Danger Zone Warnings** | Alerts users when entering areas with reported crime incidents |
| 📍 **Crime Area Map** | Browse reported crime hotspots by city and severity |
| 🗂️ **Crime Reporting** | Submit and verify crime reports (anonymous or named) |
| 👮 **Police Notification** | Direct dispatch to nearest police station with case number |
| 👥 **Emergency Contacts** | Up to 5 contacts notified via SMS + Email on SOS |
| 📊 **Danger Zone Levels** | WATCH / WARNING / DANGER / EXTREME zone classification |
| 🔐 **User Authentication** | Secure registration and login with hashed passwords |

---

## 🗂️ Project Structure

```
WomenSafetySystem/
└── src/
    └── com/womensafety/
        ├── Main.java
        ├── model/
        │   ├── Alert.java               # Abstract base alert class
        │   ├── SOSAlert.java            # SOS emergency (extends Alert)
        │   ├── DangerZoneAlert.java     # Proximity warning (extends Alert)
        │   ├── PoliceAlert.java         # Police dispatch (extends Alert)
        │   ├── CrimeReport.java
        │   ├── DangerZone.java
        │   ├── User.java
        │   ├── EmergencyContact.java
        │   ├── Location.java
        │   └── PoliceStation.java
        ├── alert/
        │   ├── AlertService.java
        │   └── NotificationDispatcher.java
        ├── service/
        │   ├── UserService.java
        │   ├── CrimeDataService.java
        │   └── PoliceStationService.java
        ├── database/
        │   └── DatabaseManager.java     # Singleton in-memory store
        ├── exception/
        │   ├── AuthenticationException.java
        │   └── UserNotFoundException.java
        ├── util/
        │   ├── Logger.java              # Singleton logger
        │   ├── PasswordUtil.java
        │   └── InputValidator.java
        └── ui/
            └── ApplicationMenu.java
```

---

## 🧱 OOP Concepts Used

- **Inheritance** — `SOSAlert`, `DangerZoneAlert`, `PoliceAlert` extend abstract `Alert`
- **Encapsulation** — All models use private fields with getters/setters
- **Abstraction** — `Alert` defines abstract `dispatch()` and `buildNotificationPayload()`
- **Polymorphism** — Alert types dispatched polymorphically via `AlertService`
- **Singleton** — `DatabaseManager` and `Logger` use the Singleton pattern
- **Enums** — `CrimeType`, `Severity`, `AlertPriority`, `DangerLevel` etc.

---

## 🚀 How to Run

### Prerequisites
- Java 11 or higher
- No external dependencies — pure Java

### Compile

```bash
find src -name "*.java" > sources.txt
javac -d out @sources.txt
```

### Run

```bash
java -cp out com.womensafety.Main
```

### Or use the pre-built JAR

```bash
java -jar WomenSafetySystem.jar
```

---

## 📦 Alert Types

| Alert | Priority | Triggers |
|---|---|---|
| `SOSAlert` | CRITICAL | Manual SOS button |
| `DangerZoneAlert` | Varies | Entering crime zone radius |
| `PoliceAlert` | CRITICAL | Direct police dispatch |

---

## 🏗️ Architecture

```
UI Layer        →  ApplicationMenu
Service Layer   →  AlertService, UserService, CrimeDataService
Model Layer     →  User, CrimeReport, Alert subclasses, DangerZone
Data Layer      →  DatabaseManager (Singleton)
```

---

## 🔮 Future Enhancements

- Android/iOS mobile integration
- Real SMS via Twilio / MSG91
- Live GPS tracking on Google Maps
- Firebase real-time backend
- ML-based crime hotspot prediction
- Voice-activated SOS
- Integration with 112 India police API

---

## 📄 License

Open-source under the [MIT License](LICENSE).
