import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, of } from 'rxjs';
import { ApiAuthResponse, ApiService, ApiUser } from '../shared/api.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly userSubject = new BehaviorSubject<ApiUser | null>(null);
    private readonly authenticatedSubject = new BehaviorSubject<boolean>(false);

    readonly user$ = this.userSubject.asObservable();
    readonly authenticated$ = this.authenticatedSubject.asObservable();
    readonly userName$ = this.user$.pipe(map((user) => user?.name || user?.email || ''));
    readonly userAvatar$ = this.user$.pipe(map((user) => user?.picture || ''));

    constructor(private readonly api: ApiService) {
        this.refresh();
    }

    refresh(): void {
        this.api.me().pipe(catchError(() => of({ authenticated: false } as ApiAuthResponse))).subscribe((response) => {
            const authenticated = response.authenticated || !!response.user;
            this.authenticatedSubject.next(authenticated);
            this.userSubject.next(response.user ?? null);
        });
    }
}
