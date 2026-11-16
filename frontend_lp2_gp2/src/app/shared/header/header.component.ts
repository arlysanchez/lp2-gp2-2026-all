import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CartService } from '../../core/services/cart.service';
import { AuthService } from '../../core/services/auth.service';
import { LoginComponent } from '../../features/login/login.component';
import { RegisterComponent } from '../../features/register/register.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule,RouterLink,LoginComponent,RegisterComponent],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  
  public cartService = inject(CartService);
  public authService = inject(AuthService);
  userName: string = '';
  isAdmin : boolean = false;
  

  ngOnInit(): void {
    this.authService.isLoggedIn$.subscribe(isLoggedIn =>{
      if(isLoggedIn){
        this.userName = localStorage.getItem('user_name') || 'Usuario';
        this.isAdmin = localStorage.getItem('user_role')==='ADMIN';
      }else{
        this.isAdmin = false;
        this.userName = '';
      }
    })
  }

  logout():void{
    this.authService.logout();
  }

}
