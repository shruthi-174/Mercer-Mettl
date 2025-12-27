import React, { useState } from 'react';
import { User, Users, Eye, Award, ArrowRight, CheckCircle, AlertTriangle, Play, Lock, FileText, Code, Camera, BarChart3, Mail, Clock, Database, Server } from 'lucide-react';

const EndToEndFlowDiagram = () => {
  const [selectedUser, setSelectedUser] = useState('recruiter');

  const userTypes = {
    recruiter: { 
      name: 'Recruiter / Org Admin', 
      icon: Users, 
      color: 'blue',
      steps: [
        {
          phase: 'Setup & Configuration',
          steps: [
            { id: 1, title: 'Login to Platform', service: 'Auth Service', action: 'POST /api/v1/auth/login', db: 'Validate credentials → Generate JWT', icon: Lock },
            { id: 2, title: 'Dashboard Access', service: 'User Service', action: 'GET /api/v1/orgs/{orgId}/dashboard', db: 'Fetch org details, user stats', icon: BarChart3 },
            { id: 3, title: 'Create Question Bank', service: 'Question Service', action: 'POST /api/v1/orgs/{orgId}/questions', db: 'Store in PostgreSQL, Index in ElasticSearch', icon: FileText },
            { id: 4, title: 'Upload Media (Optional)', service: 'Question Service', action: 'POST /api/v1/questions/{id}/media', db: 'Upload to S3, Link to question', icon: FileText },
            { id: 5, title: 'Create Test', service: 'Test Orchestration', action: 'POST /api/v1/orgs/{orgId}/tests', db: 'Create test entity, Link questions', icon: FileText },
            { id: 6, title: 'Configure Settings', service: 'Test Orchestration', action: 'PUT /api/v1/tests/{testId}/settings', db: 'Set duration, proctoring, scoring rules', icon: FileText }
          ]
        },
        {
          phase: 'Candidate Management',
          steps: [
            { id: 7, title: 'Bulk Upload Candidates', service: 'User Service', action: 'POST /api/v1/tests/{testId}/candidates/bulk', db: 'Parse CSV, Create candidate records', icon: Users },
            { id: 8, title: 'Schedule Test', service: 'Test Orchestration', action: 'POST /api/v1/tests/{testId}/schedule', db: 'Set time window, Publish event', icon: Clock },
            { id: 9, title: 'Send Invitations', service: 'Notification Service', action: 'Kafka: test.scheduled', db: 'Queue emails with unique tokens', icon: Mail },
            { id: 10, title: 'Email Delivery', service: 'Notification Service', action: 'Process email queue', db: 'Send via SMTP, Log delivery status', icon: Mail }
          ]
        },
        {
          phase: 'Monitoring & Review',
          steps: [
            { id: 11, title: 'Monitor Live Sessions', service: 'Test Orchestration', action: 'GET /api/v1/tests/{testId}/sessions/live', db: 'Fetch active sessions from Redis', icon: Eye },
            { id: 12, title: 'Review Proctor Flags', service: 'Proctoring Service', action: 'GET /api/v1/sessions/{sessionId}/flags', db: 'Fetch flagged events, clips from S3', icon: AlertTriangle },
            { id: 13, title: 'Manual Grading', service: 'Reporting Service', action: 'POST /api/v1/submissions/{id}/grade', db: 'Score descriptive answers', icon: Award },
            { id: 14, title: 'View Reports', service: 'Reporting Service', action: 'GET /api/v1/tests/{testId}/report', db: 'Aggregate scores, analytics', icon: BarChart3 },
            { id: 15, title: 'Export Results', service: 'Reporting Service', action: 'GET /api/v1/tests/{testId}/export', db: 'Generate PDF/CSV from PostgreSQL', icon: BarChart3 }
          ]
        }
      ]
    },
    candidate: { 
      name: 'Candidate (Test Taker)', 
      icon: User, 
      color: 'green',
      steps: [
        {
          phase: 'Pre-Test Setup',
          steps: [
            { id: 1, title: 'Receive Email Invite', service: 'Email Client', action: 'Click unique test link', db: 'Token embedded in URL', icon: Mail },
            { id: 2, title: 'Token Validation', service: 'Auth Service', action: 'GET /api/v1/tests/{testId}/validate', db: 'Verify token, Check expiry', icon: Lock },
            { id: 3, title: 'System Check', service: 'Browser', action: 'Check camera, mic, bandwidth', db: 'Local browser APIs', icon: Camera },
            { id: 4, title: 'Consent & Instructions', service: 'Test Orchestration', action: 'GET /api/v1/tests/{testId}/instructions', db: 'Fetch test rules, consent form', icon: FileText },
            { id: 5, title: 'Identity Verification', service: 'Proctoring Service', action: 'POST /api/v1/sessions/verify', db: 'Upload photo, ID (optional)', icon: Camera }
          ]
        },
        {
          phase: 'Test Execution',
          steps: [
            { id: 6, title: 'Start Test Session', service: 'Test Orchestration', action: 'POST /api/v1/tests/{testId}/start', db: 'Create session in PostgreSQL & Redis', icon: Play },
            { id: 7, title: 'WebSocket Connection', service: 'Proctoring Service', action: 'WS /ws/proctor/{sessionToken}', db: 'Establish real-time channel', icon: Camera },
            { id: 8, title: 'Load Questions', service: 'Test Orchestration', action: 'GET /api/v1/sessions/{sessionId}/questions', db: 'Fetch questions from PostgreSQL', icon: FileText },
            { id: 9, title: 'Answer MCQ Questions', service: 'Test Orchestration', action: 'POST /api/v1/sessions/{sessionId}/submit', db: 'Save answer, timestamp', icon: CheckCircle },
            { id: 10, title: 'Submit Code Solution', service: 'Code Execution', action: 'POST /api/v1/execution/run', db: 'Publish to Kafka queue', icon: Code },
            { id: 11, title: 'Continuous Proctoring', service: 'Proctoring AI', action: 'Stream frames via WebSocket', db: 'AI analyzes, flags events', icon: Camera },
            { id: 12, title: 'Write Descriptive Answer', service: 'Test Orchestration', action: 'POST /api/v1/sessions/{sessionId}/submit', db: 'Save text answer', icon: FileText }
          ]
        },
        {
          phase: 'Completion & Results',
          steps: [
            { id: 13, title: 'Submit Test', service: 'Test Orchestration', action: 'POST /api/v1/sessions/{sessionId}/finish', db: 'Mark session COMPLETED', icon: CheckCircle },
            { id: 14, title: 'Auto-Grading Triggered', service: 'Reporting Service', action: 'Kafka: session.completed', db: 'Calculate MCQ & code scores', icon: Award },
            { id: 15, title: 'Results Published', service: 'Reporting Service', action: 'Calculate final score', db: 'Store in PostgreSQL', icon: BarChart3 },
            { id: 16, title: 'Email Notification', service: 'Notification Service', action: 'Send result summary', db: 'Template with score', icon: Mail },
            { id: 17, title: 'View Results', service: 'Reporting Service', action: 'GET /api/v1/sessions/{sessionId}/result', db: 'Fetch score breakdown', icon: BarChart3 }
          ]
        }
      ]
    },
    proctor: { 
      name: 'Proctor / Reviewer', 
      icon: Eye, 
      color: 'purple',
      steps: [
        {
          phase: 'Live Monitoring',
          steps: [
            { id: 1, title: 'Login to Proctor Dashboard', service: 'Auth Service', action: 'POST /api/v1/auth/login', db: 'Validate proctor role', icon: Lock },
            { id: 2, title: 'View Active Sessions', service: 'Test Orchestration', action: 'GET /api/v1/proctor/sessions/active', db: 'Fetch live sessions from Redis', icon: Eye },
            { id: 3, title: 'Real-time Alerts', service: 'Proctoring Service', action: 'WS /ws/proctor/alerts', db: 'Subscribe to flag events', icon: AlertTriangle },
            { id: 4, title: 'View Candidate Feed', service: 'Proctoring Service', action: 'GET /api/v1/sessions/{sessionId}/stream', db: 'Low-latency video stream', icon: Camera }
          ]
        },
        {
          phase: 'Event Review',
          steps: [
            { id: 5, title: 'Filter Flagged Events', service: 'Proctoring Service', action: 'GET /api/v1/proctor/events?severity=HIGH', db: 'Query PostgreSQL for flags', icon: AlertTriangle },
            { id: 6, title: 'Review Video Clips', service: 'Proctoring Service', action: 'GET /api/v1/events/{eventId}/clip', db: 'Fetch clip from S3', icon: Camera },
            { id: 7, title: 'Annotate Event', service: 'Proctoring Service', action: 'POST /api/v1/events/{eventId}/annotate', db: 'Add notes, adjust severity', icon: FileText },
            { id: 8, title: 'Approve/Reject Session', service: 'Test Orchestration', action: 'PUT /api/v1/sessions/{sessionId}/status', db: 'Mark as VALID or INVALID', icon: CheckCircle }
          ]
        },
        {
          phase: 'Post-Test Analysis',
          steps: [
            { id: 9, title: 'View Session Timeline', service: 'Proctoring Service', action: 'GET /api/v1/sessions/{sessionId}/timeline', db: 'All events chronologically', icon: Clock },
            { id: 10, title: 'Generate Proctor Report', service: 'Reporting Service', action: 'GET /api/v1/sessions/{sessionId}/proctor-report', db: 'Compile flags, clips, notes', icon: BarChart3 },
            { id: 11, title: 'Share with Recruiter', service: 'Notification Service', action: 'Send report link', db: 'Email or dashboard notification', icon: Mail }
          ]
        }
      ]
    }
  };

  const selectedData = userTypes[selectedUser];
  const ColorIcon = selectedData.icon;

  const getColorClasses = (color) => {
    const colors = {
      blue: {
        bg: 'bg-blue-50',
        border: 'border-blue-300',
        text: 'text-blue-700',
        button: 'bg-blue-600 hover:bg-blue-700',
        gradient: 'from-blue-100 to-blue-50'
      },
      green: {
        bg: 'bg-green-50',
        border: 'border-green-300',
        text: 'text-green-700',
        button: 'bg-green-600 hover:bg-green-700',
        gradient: 'from-green-100 to-green-50'
      },
      purple: {
        bg: 'bg-purple-50',
        border: 'border-purple-300',
        text: 'text-purple-700',
        button: 'bg-purple-600 hover:bg-purple-700',
        gradient: 'from-purple-100 to-purple-50'
      }
    };
    return colors[color];
  };

  const currentColors = getColorClasses(selectedData.color);

  return (
    <div className="w-full min-h-screen bg-gradient-to-br from-slate-50 to-slate-100 p-6 overflow-auto">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="mb-6">
          <h1 className="text-3xl font-bold text-gray-800 mb-2">MercerMettl Platform - Complete End-to-End Flow</h1>
          <p className="text-gray-600">Detailed journey from login to completion for each user type</p>
        </div>

        {/* User Type Selector */}
        <div className="flex gap-3 mb-6">
          {Object.entries(userTypes).map(([key, data]) => {
            const Icon = data.icon;
            const colors = getColorClasses(data.color);
            return (
              <button
                key={key}
                onClick={() => setSelectedUser(key)}
                className={`flex items-center gap-2 px-6 py-3 rounded-lg font-medium transition-all ${
                  selectedUser === key
                    ? `${colors.button} text-white shadow-lg scale-105`
                    : 'bg-white text-gray-700 hover:bg-gray-50 border-2 border-gray-200'
                }`}
              >
                <Icon className="w-5 h-5" />
                {data.name}
              </button>
            );
          })}
        </div>

        {/* Flow Container */}
        <div className={`bg-gradient-to-br ${currentColors.gradient} rounded-xl shadow-xl p-6 border-2 ${currentColors.border}`}>
          <div className="flex items-center gap-3 mb-6">
            <ColorIcon className={`w-8 h-8 ${currentColors.text}`} />
            <h2 className="text-2xl font-bold text-gray-800">{selectedData.name} Journey</h2>
          </div>

          {/* Phases */}
          {selectedData.steps.map((phase, phaseIdx) => (
            <div key={phaseIdx} className="mb-8 last:mb-0">
              <div className="flex items-center gap-2 mb-4">
                <div className={`px-4 py-2 ${currentColors.bg} ${currentColors.border} border-2 rounded-lg`}>
                  <h3 className={`text-lg font-bold ${currentColors.text}`}>
                    Phase {phaseIdx + 1}: {phase.phase}
                  </h3>
                </div>
              </div>

              <div className="space-y-3">
                {phase.steps.map((step, stepIdx) => {
                  const StepIcon = step.icon;
                  return (
                    <div key={step.id} className="relative">
                      {/* Connector Line */}
                      {stepIdx < phase.steps.length - 1 && (
                        <div className={`absolute left-6 top-12 w-0.5 h-8 ${currentColors.border} border-l-2 border-dashed`}></div>
                      )}

                      {/* Step Card */}
                      <div className="flex gap-4 bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow">
                        {/* Step Number & Icon */}
                        <div className="flex flex-col items-center gap-2">
                          <div className={`w-12 h-12 rounded-full ${currentColors.bg} ${currentColors.border} border-2 flex items-center justify-center font-bold ${currentColors.text}`}>
                            {step.id}
                          </div>
                          <StepIcon className={`w-5 h-5 ${currentColors.text}`} />
                        </div>

                        {/* Step Details */}
                        <div className="flex-1">
                          <h4 className="font-bold text-gray-800 mb-1">{step.title}</h4>
                          <div className="grid grid-cols-3 gap-4 text-sm">
                            <div>
                              <p className="text-gray-500 text-xs font-semibold mb-1">Microservice</p>
                              <div className="flex items-center gap-1">
                                <Server className="w-3 h-3 text-blue-500" />
                                <p className="text-gray-700 font-medium">{step.service}</p>
                              </div>
                            </div>
                            <div>
                              <p className="text-gray-500 text-xs font-semibold mb-1">API / Action</p>
                              <p className="text-gray-700 font-mono text-xs bg-gray-50 px-2 py-1 rounded">{step.action}</p>
                            </div>
                            <div>
                              <p className="text-gray-500 text-xs font-semibold mb-1">Data Operation</p>
                              <div className="flex items-center gap-1">
                                <Database className="w-3 h-3 text-teal-500" />
                                <p className="text-gray-700 text-xs">{step.db}</p>
                              </div>
                            </div>
                          </div>
                        </div>

                        {/* Arrow */}
                        {stepIdx < phase.steps.length - 1 && (
                          <div className="flex items-center">
                            <ArrowRight className={`w-5 h-5 ${currentColors.text} opacity-30`} />
                          </div>
                        )}
                      </div>
                    </div>
                  );
                })}
              </div>

              {/* Phase Completion Indicator */}
              {phaseIdx < selectedData.steps.length - 1 && (
                <div className="flex items-center justify-center my-6">
                  <div className={`flex items-center gap-2 px-6 py-2 ${currentColors.bg} ${currentColors.border} border-2 rounded-full`}>
                    <CheckCircle className={`w-5 h-5 ${currentColors.text}`} />
                    <span className={`font-semibold ${currentColors.text}`}>Phase {phaseIdx + 1} Complete</span>
                  </div>
                </div>
              )}
            </div>
          ))}

          {/* Journey Complete */}
          <div className="flex items-center justify-center mt-8 pt-6 border-t-2 border-gray-200">
            <div className={`flex items-center gap-3 px-8 py-4 ${currentColors.button} text-white rounded-xl shadow-lg`}>
              <Award className="w-6 h-6" />
              <span className="font-bold text-lg">{selectedData.name} Journey Complete!</span>
            </div>
          </div>
        </div>

        {/* System Architecture Overview */}
        <div className="mt-8 bg-white rounded-xl shadow-lg p-6 border-2 border-gray-300">
          <h2 className="text-xl font-bold text-gray-800 mb-4">System Architecture Components</h2>
          <div className="grid grid-cols-4 gap-4">
            <div className="bg-blue-50 p-4 rounded-lg border border-blue-300">
              <Lock className="w-6 h-6 text-blue-600 mb-2" />
              <h3 className="font-semibold text-sm">Auth Service</h3>
              <p className="text-xs text-gray-600">Port: 8081</p>
            </div>
            <div className="bg-purple-50 p-4 rounded-lg border border-purple-300">
              <Users className="w-6 h-6 text-purple-600 mb-2" />
              <h3 className="font-semibold text-sm">User Service</h3>
              <p className="text-xs text-gray-600">Port: 8082</p>
            </div>
            <div className="bg-green-50 p-4 rounded-lg border border-green-300">
              <FileText className="w-6 h-6 text-green-600 mb-2" />
              <h3 className="font-semibold text-sm">Question Service</h3>
              <p className="text-xs text-gray-600">Port: 8083</p>
            </div>
            <div className="bg-orange-50 p-4 rounded-lg border border-orange-300">
              <Server className="w-6 h-6 text-orange-600 mb-2" />
              <h3 className="font-semibold text-sm">Test Orchestration</h3>
              <p className="text-xs text-gray-600">Port: 8084</p>
            </div>
            <div className="bg-red-50 p-4 rounded-lg border border-red-300">
              <Camera className="w-6 h-6 text-red-600 mb-2" />
              <h3 className="font-semibold text-sm">Proctoring AI</h3>
              <p className="text-xs text-gray-600">Port: 8085 (Python)</p>
            </div>
            <div className="bg-teal-50 p-4 rounded-lg border border-teal-300">
              <Code className="w-6 h-6 text-teal-600 mb-2" />
              <h3 className="font-semibold text-sm">Code Execution</h3>
              <p className="text-xs text-gray-600">Port: 8086</p>
            </div>
            <div className="bg-yellow-50 p-4 rounded-lg border border-yellow-300">
              <BarChart3 className="w-6 h-6 text-yellow-600 mb-2" />
              <h3 className="font-semibold text-sm">Reporting Service</h3>
              <p className="text-xs text-gray-600">Port: 8087</p>
            </div>
            <div className="bg-pink-50 p-4 rounded-lg border border-pink-300">
              <Mail className="w-6 h-6 text-pink-600 mb-2" />
              <h3 className="font-semibold text-sm">Notification Service</h3>
              <p className="text-xs text-gray-600">Port: 8088</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EndToEndFlowDiagram;