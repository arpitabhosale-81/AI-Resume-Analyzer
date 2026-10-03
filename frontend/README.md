# 🤖 AI Resume Analyzer

An AI-powered web application that analyzes resumes against a selected job role and job description. It provides an ATS score, job-match score, matching keywords, missing skills, strengths, and personalized suggestions to help make a resume more job-ready.

## 🚀 Features

* 📄 PDF Resume Upload
* 📝 PDF Text Extraction
* 🎯 Multiple Job Role Selection
* 📋 Job Description Matching
* 📊 ATS Resume Score
* 🎯 Job Match Score
* 🔑 Matching & Missing Job Keywords
* 🟢 Skills Found
* 🔴 Missing Skills
* 💚 Resume Strengths
* 💡 Resume Improvement Suggestions
* 🔄 Loading Animation
* 📱 Mobile Responsive Design
* 🔄 Analyze Another Resume

## 🛠️ Technologies Used

### Frontend

* React.js
* Vite
* HTML
* CSS
* JavaScript

### Backend

* Java
* Spring Boot
* Maven
* Apache PDFBox

## 📂 Project Structure

```text
AI - Resume - Analyzer
│
├── src
│   ├── UploadResume.jsx
│   └── UploadResume.css
│
├── backend
│   └── src
│       └── main
│           └── java
│
├── public
├── package.json
├── package-lock.json
├── vite.config.js
└── README.md
```

## ▶️ How to Run

### 1. Start the Backend

Open a terminal and run:

```bash
cd "C:\AI - Resume - Analyzer\backend"
mvn spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

### 2. Start the Frontend

Open another terminal and run:

```bash
cd "C:\AI - Resume - Analyzer"
npm run dev
```

The frontend runs on:

```text
http://localhost:5173
```

Open the frontend URL in your browser.

## 🔄 Application Flow

```text
Upload Resume
      ↓
Select Job Role
      ↓
Enter Job Description
      ↓
Analyze Resume
      ↓
Extract PDF Text
      ↓
Analyze Resume Content
      ↓
Calculate ATS & Job Match Scores
      ↓
Show Skills, Keywords, Strengths & Suggestions
```

## 🎯 Purpose

The purpose of this project is to help students and job seekers understand how well their resume matches a target job and identify areas that can be improved.

## 🔮 Future Enhancements

* AI/LLM-powered resume analysis
* Resume improvement using Generative AI
* Resume keyword optimization
* Downloadable analysis report
* User authentication
* Resume history
* Cloud deployment
* AI-powered resume rewriting

## 👩‍💻 Project Status

**Working Prototype — Frontend and Backend integrated successfully.**