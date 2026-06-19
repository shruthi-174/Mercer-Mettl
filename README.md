# 🚀 Mercer Mettl-like Assessment Platform

A scalable **multi-tenant online assessment system** inspired by Mercer Mettl, built using **Spring Boot (microservices-style backend) and React frontend**, with **AI-powered question generation and modular architecture**.

---

# 📌 Project Overview

This project is an **online assessment platform** that allows organizations to:

- Create and manage organizations (multi-tenant system)
- Manage users (Admins, Org Admins, Recruiters, Candidates)
- Create and assign MCQ-based tests
- Generate AI-based questions using LLM
- Conduct assessments and track results

The system is designed with **scalability, modularity, and future extensibility in mind**.

---

# 🏗️ System Architecture

## 🔌 API Gateway (Design/Layered Approach)
- Central entry point for all client requests
- Routes requests to appropriate backend services
- Supports scalable microservice-ready architecture

---

## 🔐 Authentication Service
- JWT-based authentication system
- Access Token + Refresh Token mechanism
- Role-Based Access Control (RBAC)

### Roles:
- Platform Admin
- Organization Admin
- Recruiter
- Candidate

---

## 🏢 Multi-Tenant Organization System
- Platform Admin can create multiple Org Admins
- Org Admin can create and manage organizations
- Each organization maintains isolated data using `orgId`
- Org Admin can manage:
  - Recruiters
  - Candidates

---

## 📚 Question Bank Service
- CRUD operations for MCQ questions
- Tag-based categorization (Java, DSA, etc.)
- Difficulty levels (Easy / Medium / Hard)
- AI-generated questions integration

---

## 🤖 AI Question Generation Service (Port: 8083)
- Separate microservice using Ollama LLM
- Generates MCQ questions based on:
  - Topic
  - Difficulty
  - Number of questions
- Stores generated questions into question bank

---

## 🧪 Assessment Service (Core Backend - Port: 8080)
Handles complete test lifecycle:

### Test Lifecycle:
- ASSIGNED
- STARTED
- COMPLETED

### Features:
- Test creation and assignment
- Candidate attempt tracking
- Score calculation
- Result generation (pass/fail)

---

## 👨‍💼 Recruiter Module
Recruiters can:
- Create tests
- Select questions from question bank
- Assign tests to candidates
- View candidate results

---

## 👩‍💻 Candidate Module
Candidates can:
- View assigned tests
- Start / resume tests
- Submit answers
- View results (score, status, performance)

---

## 📩 Notification System
- SMTP-based email integration
- Sends:
  - Test assignment emails
  - Test updates

---

## 🔌 Service Communication
- Backend service runs on `8080`
- AI service runs on `8083`
- Frontend uses separate API clients (Axios instances) for each service

---

# ⚙️ Tech Stack

### Backend:
- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Microservices-style architecture

### Frontend:
- React.js

### AI Service:
- Ollama LLM

### Database:
- MySQL / PostgreSQL (based on configuration)

---

# 📌 Key Features Implemented

✔ Multi-tenant architecture (organization-based isolation)  
✔ JWT authentication with access + refresh tokens  
✔ Role-based access control (RBAC)  
✔ MCQ question bank system  
✔ AI-based question generation (Ollama integration)  
✔ Test creation and lifecycle management  
✔ Candidate test attempt tracking  
✔ Score calculation and result generation  
✔ Recruiter and candidate dashboards  
✔ SMTP email notifications  
✔ Multi-service communication handling  

---

# 🚧 Current Limitations

- System currently supports **only MCQ-based assessments**
- Coding-based questions are not yet implemented
- Advanced proctoring and real-time monitoring not yet added

---

# 🚀 Future Enhancements

## 🤖 AI Improvements
- Smarter question generation (difficulty balancing, deduplication)
- AI-based answer evaluation and feedback

---

## 🧑‍💻 Test Experience
- Real-time test sessions with auto-save and resume support
- Timer synchronization and auto-submit
- Basic proctoring (tab switch / face detection)

---

## 💻 Coding Support
- Add coding-based questions
- Docker-based secure code execution sandbox
- Multi-language support with test cases

---

## 📊 Analytics
- Advanced recruiter dashboards
- Candidate performance insights
- Skill-based analytics

---

## 🔐 Scalability Improvements
- Full API Gateway implementation
- Kafka-based event-driven architecture
- Redis caching for optimization

---

## 🎨 UI/UX Improvements
- More responsive frontend design
- Improved dashboards for recruiter and candidate
- Better overall user experience

---

# 🧠 Project Summary

This project demonstrates a **real-world scalable assessment platform** with:

- Multi-tenant architecture
- Secure authentication system
- AI-powered question generation
- Modular backend services
- End-to-end test lifecycle management

---

# 🧾 One-Line Description

A scalable, multi-tenant AI-powered assessment platform similar to Mercer Mettl, supporting MCQ-based evaluations with role-based access control and modular microservice architecture.

---

# 👨‍💻 Author

**Shruthi Mohan**

---
