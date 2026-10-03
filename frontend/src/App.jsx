import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import "./App.css";
import UploadResume from "./pages/UploadResume";

function Home() {
  return (
    <div className="app">

      {/* Navigation Bar */}
      <nav className="navbar">

        <div className="logo">
          Resume<span>AI</span>
        </div>

        <div className="nav-links">
          <a href="#features">Features</a>
          <a href="#how-it-works">How It Works</a>

          <Link to="/upload">
            <button className="login-btn">
              Login
            </button>
          </Link>
        </div>

      </nav>

      {/* Hero Section */}
      <section className="hero">

        <div className="hero-content">

          <p className="tagline">
            ✨ AI-POWERED RESUME ANALYZER
          </p>

          <h1>
            Make Your Resume
            <span> Job-Ready.</span>
          </h1>

          <p className="hero-description">
            Upload your resume and let AI analyze your skills,
            identify improvement areas, and match your resume
            with your dream job.
          </p>

          <Link to="/upload">
            <button className="analyze-btn">
              Analyze My Resume →
            </button>
          </Link>

          <p className="small-text">
            Free analysis • PDF & DOCX supported
          </p>

        </div>

        {/* Resume Preview */}
        <div className="resume-card">

          <div className="resume-header">

            <div className="profile-circle"></div>

            <div>
              <h3>Your Resume</h3>
              <p>AI Analysis Preview</p>
            </div>

          </div>

          <div className="score-section">

            <div className="score">
              <strong>82</strong>
              <span>/100</span>
            </div>

            <div>
              <h4>Resume Score</h4>
              <p>Good Resume</p>
            </div>

          </div>

          <div className="skills">
            <span>Java</span>
            <span>React</span>
            <span>SQL</span>
            <span>Spring Boot</span>
          </div>

          <div className="analysis-item">
            ✓ Strong technical skills
          </div>

          <div className="analysis-item warning">
            ! Add more measurable achievements
          </div>

        </div>

      </section>

      {/* Features */}
      <section id="features" className="features">

        <h2>Everything You Need to Improve Your Resume</h2>

        <p className="section-description">
          Get AI-powered insights designed to help you build a stronger resume.
        </p>

        <div className="feature-grid">

          <div className="feature-card">
            <div className="icon">📊</div>
            <h3>Resume Score</h3>
            <p>
              Get an AI-generated score based on your resume quality.
            </p>
          </div>

          <div className="feature-card">
            <div className="icon">🎯</div>
            <h3>Job Matching</h3>
            <p>
              Compare your resume with a job description.
            </p>
          </div>

          <div className="feature-card">
            <div className="icon">💡</div>
            <h3>AI Suggestions</h3>
            <p>
              Get personalized recommendations to improve your resume.
            </p>
          </div>

          <div className="feature-card">
            <div className="icon">🤖</div>
            <h3>Interview Questions</h3>
            <p>
              Generate interview questions based on your resume.
            </p>
          </div>

        </div>

      </section>

      {/* How It Works */}
      <section id="how-it-works" className="how-it-works">

        <h2>How It Works</h2>

        <div className="steps">

          <div className="step">
            <div className="step-number">1</div>
            <h3>Upload Resume</h3>
            <p>Upload your PDF or DOCX resume.</p>
          </div>

          <div className="step">
            <div className="step-number">2</div>
            <h3>AI Analysis</h3>
            <p>AI analyzes your skills and experience.</p>
          </div>

          <div className="step">
            <div className="step-number">3</div>
            <h3>Get Results</h3>
            <p>Receive your score and suggestions.</p>
          </div>

        </div>

      </section>

      {/* Footer */}
      <footer>
        <p>© 2026 ResumeAI. AI-powered career assistance.</p>
      </footer>

    </div>
  );
}

function App() {
  return (
    <BrowserRouter>

      <Routes>

        <Route path="/" element={<Home />} />

        <Route
          path="/upload"
          element={<UploadResume />}
        />

      </Routes>

    </BrowserRouter>
  );
}

export default App;