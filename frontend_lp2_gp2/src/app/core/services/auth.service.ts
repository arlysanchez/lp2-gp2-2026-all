import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ILoginResponse } from '../interfaces/auth.interface';
import { BehaviorSubject, catchError, map, Observable, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
private http = inject(HttpClient);
private router = inject(Router);

private apiUrl = '/api/v1/auth';
private loggedIn = new BehaviorSubject<boolean>(!!localStorage.getItem('token_ecommerce'));
isLoggedIn$ = this.loggedIn.asObservable();

login(credentials:{usuario:string; password:string}):Observable<boolean>{
  return this.http.post<ILoginResponse>(`${this.apiUrl}/login`,credentials).pipe(
    tap(response =>{
      if(response && response.token){
        localStorage.setItem('token_ecommerce',response.token);
        localStorage.setItem('user_id_persona',response.idPersona.toString());
        localStorage.setItem('user_role',response.rol);
         localStorage.setItem('user_name',response.usuario);
         this.loggedIn.next(true);
         this.router.navigate(['/home']);
      }
    }),
    map(response => !! response.token),
    catchError(() => {
      this.logout();
       return of(false);
    })
  );
}
logout():void{
 localStorage.clear();
 this.loggedIn.next(false);
 this.router.navigate(['/login']);
}
getToken():string | null{
 return localStorage.getItem('token_ecommerce');
}
registrar(datos:any):Observable<any>{
  return this.http.post(`${this.apiUrl}/registro`,datos);
}

}