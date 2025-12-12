// Exam entity types for Team Delta
export interface Exam {
  id: string;
  title: string;
  date: string;
  createdAt?: string;
}

export interface CreateExamRequest {
  title: string;
  date: string;
}

export interface ExamCreatedEvent {
  examId: string;
  title: string;
  date: string;
}

// Notification types
export interface Notification {
  id: string;
  type: 'EMAIL' | 'SMS' | 'PUSH';
  recipient: string;
  message: string;
  status: 'PENDING' | 'SENT' | 'FAILED';
  createdAt: string;
}
