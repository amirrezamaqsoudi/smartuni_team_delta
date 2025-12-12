// API Configuration for Smart University Platform
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// Helper to get JWT token from localStorage
export const getAuthToken = (): string | null => {
  return localStorage.getItem('jwt_token');
};

// Helper to set JWT token
export const setAuthToken = (token: string): void => {
  localStorage.setItem('jwt_token', token);
};

// Helper to remove JWT token
export const removeAuthToken = (): void => {
  localStorage.removeItem('jwt_token');
};

// Generic fetch wrapper with auth
export const apiFetch = async <T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> => {
  const token = getAuthToken();
  
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: 'An error occurred' }));
    throw new Error(error.message || `HTTP error! status: ${response.status}`);
  }

  return response.json();
};

// Exam Service API (Port 8084 via Gateway 8080)
export const examApi = {
  createExam: (data: { title: string; date: string }) =>
    apiFetch<{ id: string; title: string; date: string }>('/api/exams', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
  
  getExams: () =>
    apiFetch<Array<{ id: string; title: string; date: string }>>('/api/exams'),
};

// Auth Service API (Port 8081 via Gateway 8080)
export const authApi = {
  login: (data: { email: string; password: string }) =>
    apiFetch<{ token: string }>('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
  
  register: (data: { email: string; password: string; name: string }) =>
    apiFetch<{ id: string; email: string }>('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    }),
};
