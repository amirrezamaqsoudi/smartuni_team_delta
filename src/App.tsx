import { Suspense } from "react";
import { Routes, Route } from "react-router-dom";
import Home from "./components/home";
import ExamDashboard from "./components/exam/ExamDashboard";

function App() {
  return (
    <Suspense fallback={<p>Loading...</p>}>
      <>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/exams" element={<ExamDashboard />} />
        </Routes>
      </>
    </Suspense>
  );
}

export default App;
