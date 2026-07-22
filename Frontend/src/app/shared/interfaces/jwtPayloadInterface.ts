export interface JwtPayload {
  username: string;
  roles: string;
  iat: number;
  exp: number;
}