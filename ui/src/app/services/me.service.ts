import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { PermissionDto, UserDto } from '@models/app.model';
import { AuthStore } from '@store/auth/auth.store';
import { map, Observable, take } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class MeService {
  constructor(
    private http: HttpClient,
    private authStore: AuthStore,
    @Inject('API_BASE_URL') private apiBaseUrl: string,
  ) {}

  public getMe(): Observable<UserDto> {
    const url = `${this.apiBaseUrl}me`;

    return this.http.get<UserDto>(url);
  }

  public getMyPermissions(): Observable<string[]> {
    const url = `${this.apiBaseUrl}me/permissions`;

    return this.http.get<PermissionDto[]>(url).pipe(
      take(1),
      map(response => response.map(permission => permission.permissionCode)),
    );
  }
}
