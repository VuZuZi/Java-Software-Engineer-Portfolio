export interface UserProfile {
  id: number;
  email: string;
  name: string;
  avatar?: string | null;
  role: 'USER' | 'ADMIN';
}

export interface AuthPayload {
  email: string;
  password: string;
}

export interface RegisterPayload extends AuthPayload {
  name: string;
}

export interface AuthResponse {
  token: string;
  user: UserProfile;
}
