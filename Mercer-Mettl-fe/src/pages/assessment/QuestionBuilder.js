import { useState } from "react";
import ExistingQuestions from "./ExistingQuestions";
import ManualQuestion from "./ManualQuestion";
import TestQuestions from "./TestQuestions";
import GenerateAIQuestions from "./GenerateAIQuestions";

export default function QuestionBuilder({ testId, tag }) {

  const [showExisting, setShowExisting] = useState(false);
  const [showManual, setShowManual] = useState(false);
  const [refresh, setRefresh] = useState(false);
  const [showAI, setShowAI] = useState(false);

  const reloadQuestions = () => {
    setRefresh(prev => !prev);
  };

  return (

    <div className="builder">

      <h2>Question Builder</h2>

      <div style={{ marginBottom: "20px" }}>
        <button onClick={() => setShowAI(true)}>
          Generate AI Questions
        </button>
        <button onClick={() => setShowExisting(true)}>
          Select Existing Questions
        </button>

        <button
          style={{ marginLeft: "10px" }}
          onClick={() => setShowManual(true)}
        >
          Create Question Manually
        </button>

      </div>

   {/* AI Popup */}
      {showAI && (
        <GenerateAIQuestions
          testId={testId}
          tag={tag}
          close={() => setShowAI(false)}
          onQuestionsAdded={reloadQuestions}
        />
      )}
      
      {/* Existing Questions Popup */}
      {showExisting && (

        <ExistingQuestions
          testId={testId}
          tag={tag}
          close={() => setShowExisting(false)}
          onQuestionsAdded={reloadQuestions}
        />

      )}
      
      {/* Manual Question Popup */}
      {showManual && (

        <ManualQuestion
          tag={tag}
          close={() => setShowManual(false)}
          onQuestionsAdded={reloadQuestions}
        />

      )}

      {/* Questions in Test */}
      <TestQuestions
        testId={testId}
        refresh={refresh}
      />

    </div>

  );
}