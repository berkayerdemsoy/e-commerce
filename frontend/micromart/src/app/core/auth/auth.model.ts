// ──────────────────────────────────────
// Auth Models
// ──────────────────────────────────────

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  full_name: string;
  phone_number: string;
  address: string;
  dob: string;
  gender: string;
}

export interface AuthResponse {
  token: string;
  userId: number;
  username: string;
  email: string;
  roles: string[];
}

export interface UserResponse {
  id: number;
  username: string;
  email: string;
  roles: string[];
}

