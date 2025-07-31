export interface MenuLink {
  url: string;
  text: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse extends TokenResponse {
  username: string;
}

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}

export interface UserDto {
  userid: number;
  username: string;
  firstName: string;
  lastName: string;
  emailAddress: string;
  active: boolean;
}

export interface PermissionDto {
  permissionId: number;
  permissionCode: string;
  description: string;
  active: boolean;
}

export interface MergeRequestDto {
  mergeRequestId: number;
  link: string;
  issueNumber: string;
  owner: UserDto;
  active: boolean;
  relatedRequests: MergeRequestDto[];
}
