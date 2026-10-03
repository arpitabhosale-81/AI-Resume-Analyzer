import { useState } from "react";
import "./UploadResume.css";

function UploadResume() {
  const [file, setFile] = useState(null);
  const [jobRole, setJobRole] = useState("");
  const [jobDescription, setJobDescription] = useState("");
  const [uploading, setUploading] = useState(false);
  const [analysis, setAnalysis] = useState(null);
  const [error, setError] = useState("");

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];

    if (!selectedFile) {
      return;
    }

    if (!selectedFile.name.toLowerCase().endsWith(".pdf")) {
      setError("Please select a PDF file.");
      setFile(null);
      setAnalysis(null);
      return;
    }

    setFile(selectedFile);
    setError("");
    setAnalysis(null);
  };

  const handleAnalyze = async () => {
    setError("");
    setAnalysis(null);

    if (!file) {
      setError("Please upload your resume first.");
      return;
    }

    if (!jobRole) {
      setError("Please select a job role.");
      return;
    }

    const formData = new FormData();

    formData.append("file", file);
    formData.append("jobRole", jobRole);
    formData.append("jobDescription", jobDescription);

    setUploading(true);

    try {
      const response = await fetch(
        "http://localhost:8080/api/resume/upload",
        {
          method: "POST",
          body: formData,
        }
      );

      const result = await response.json();

      if (!response.ok) {
        throw new Error(
          result.message || "Resume analysis failed."
        );
      }

      setAnalysis(result);

    } catch (error) {
      console.error("Analysis error:", error);
      setError(error.message);

    } finally {
      setUploading(false);
    }
  };

  // ATS score status
  const getScoreStatus = (score) => {
    if (score >= 80) {
      return "Excellent Resume";
    }

    if (score >= 60) {
      return "Good Resume";
    }

    if (score >= 40) {
      return "Needs Improvement";
    }

    return "Needs Major Improvement";
  };

  return (
    <div className="upload-page">

      <div className="upload-container">

        {/* Header */}

        <div className="upload-header">

          <h1>Analyze Your Resume</h1>

          <p>
            Upload your resume and select your target job role.
            Our AI will analyze it for you.
          </p>

        </div>

        {/* Resume Upload */}

        <div className="upload-box">

          <div className="upload-icon">
            📄
          </div>

          <h2>
            {file ? file.name : "Upload Your Resume"}
          </h2>

          <p>
            PDF files only
          </p>

          <label className="choose-file">

            Choose Resume

            <input
              type="file"
              accept=".pdf,application/pdf"
              onChange={handleFileChange}
            />

          </label>

          {file && (
            <p className="file-selected">
              ✓ Resume selected
            </p>
          )}

        </div>

        {/* Job Role */}

        <div className="job-section">

          <label>
            Target Job Role
          </label>

          <select
            value={jobRole}
            onChange={(event) =>
              setJobRole(event.target.value)
            }
          >

            <option value="">
              Select a job role
            </option>

            <option value="Java Full Stack Developer">
              Java Full Stack Developer
            </option>

            <option value="Frontend Developer">
              Frontend Developer
            </option>

            <option value="Backend Developer">
              Backend Developer
            </option>

            <option value="Software Developer">
              Software Developer
            </option>

            <option value="GenAI Developer">
              GenAI Developer
            </option>

            <option value="Data Analyst">
              Data Analyst
            </option>

          </select>

        </div>

        {/* Job Description */}

        <div className="job-description-section">

          <label>
            Job Description
          </label>

          <textarea
            value={jobDescription}
            onChange={(event) =>
              setJobDescription(event.target.value)
            }
            placeholder="Paste the job description here..."
            rows="8"
          ></textarea>

          <p className="job-description-help">
            Paste the job description to get better resume
            matching results.
          </p>

        </div>

        {/* Error Message */}

        {error && (
          <div className="error-message">
            ❌ {error}
          </div>
        )}

        {/* Analyze Button */}

        <button
          className="analyze-button"
          onClick={handleAnalyze}
          disabled={uploading}
        >

          {uploading ? (
  <>
    <span className="loading-spinner"></span>
    Analyzing Resume...
  </>
) : (
  "Analyze Resume →"
)}

        </button>

        {/* Analysis Result */}

        {analysis && (

          <div className="resume-result">

            <h2>
              Resume Analysis Result
            </h2>

            {/* Target Role */}

            <p>
              Target Role:
              <strong> {analysis.jobRole}</strong>
            </p>

            {/* ATS Score */}

            <div className="ats-score">

              <h3>
                ATS Score
              </h3>

              <div
                className="score-circle"
              style={{
  "--score": `${analysis.atsScore * 3.6}deg`,
  "--score-color":
    analysis.atsScore >= 80
      ? "#15803d"
      : analysis.atsScore >= 60
      ? "#2563eb"
      : analysis.atsScore >= 40
      ? "#d97706"
      : "#dc2626",
}}
              >
                 <div className="score-number">
                   {analysis.atsScore}%
                 </div>
              </div>

              <p
  className={`score-status ${
    analysis.atsScore >= 80
      ? "score-excellent"
      : analysis.atsScore >= 60
      ? "score-good"
      : analysis.atsScore >= 40
      ? "score-average"
      : "score-poor"
  }`}
>
  {getScoreStatus(analysis.atsScore)}
</p>
{/* Job Match Score */}
<div className="job-match-score">
  <h3>🎯 Job Match Score</h3>

  <div className="job-match-bar">
    <div
      className="job-match-fill"
      style={{
        width: `${analysis.jobMatchScore || 0}%`,
      }}
    ></div>
  </div>

  <p>
    {analysis.jobMatchScore || 0}% match with the selected job
    description.
  </p>
</div>
            </div>

            {/* Skills Found */}

           {/* Job Keywords */}
<div className="job-keywords-section">

  <h3>🔑 Matching Keywords</h3>

  {analysis.matchingKeywords &&
  analysis.matchingKeywords.length > 0 ? (
    <ul>
      {analysis.matchingKeywords.map((keyword, index) => (
        <li key={index}>{keyword}</li>
      ))}
    </ul>
  ) : (
    <p>No matching keywords found.</p>
  )}

  <h3>⚠️ Missing Job Keywords</h3>

  {analysis.missingJobKeywords &&
  analysis.missingJobKeywords.length > 0 ? (
    <ul>
      {analysis.missingJobKeywords.map((keyword, index) => (
        <li key={index}>{keyword}</li>
      ))}
    </ul>
  ) : (
    <p>No missing job keywords.</p>
  )}

</div>
            <div className="skills-section">

              <h3>
                ✅ Skills Found
              </h3>

              {analysis.foundSkills &&
              analysis.foundSkills.length > 0 ? (

                <ul>

                  {analysis.foundSkills.map(
                    (skill, index) => (

                      <li key={index}>
                        {skill}
                      </li>

                    )
                  )}

                </ul>

              ) : (

                <p>
                  No matching skills found.
                </p>

              )}

            </div>

            {/* Missing Skills */}

            <div className="skills-section">

              <h3>
                ❌ Missing Skills
              </h3>

              {analysis.missingSkills &&
              analysis.missingSkills.length > 0 ? (

                <ul>

                  {analysis.missingSkills.map(
                    (skill, index) => (

                      <li key={index}>
                        {skill}
                      </li>

                    )
                  )}

                </ul>

              ) : (

                <p>
                  No missing skills detected.
                </p>

              )}

            </div>

            {/* Strengths */}

            <div className="skills-section">

              <h3>
                💪 Strengths
              </h3>

              {analysis.strengths &&
              analysis.strengths.length > 0 ? (

                <ul>

                  {analysis.strengths.map(
                    (strength, index) => (

                      <li key={index}>
                        {strength}
                      </li>

                    )
                  )}

                </ul>

              ) : (

                <p>
                  No specific strengths detected.
                </p>

              )}

            </div>

            {/* Suggestions */}

            <div className="skills-section suggestions-section">

              <h3 classname="suggestions-title">
                💡 Suggestions
              </h3>

              {analysis.suggestions &&
              analysis.suggestions.length > 0 ? (

                <ul>

                  {analysis.suggestions.map(
                    (suggestion, index) => (

                      <li key={index}>
                        {suggestion}
                      </li>

                    )
                  )}

                </ul>

              ) : (

                <p>
                  No suggestions available.
                </p>

              )}
<button
  className="reset-button"
  onClick={() => {
    setFile(null);
    setJobRole("");
    setJobDescription("");
    setAnalysis(null);
    setError("");
  }}
>
  🔄 Analyze Another Resume
</button>
            </div>

          </div>

        )}

      </div>

    </div>
  );
}

export default UploadResume;