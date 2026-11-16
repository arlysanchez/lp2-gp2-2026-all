import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { AlertService } from '../../core/services/alert.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
private authService = inject(AuthService);
private alertService = inject(AlertService);

 userData = {
  nombre: '',
  email: '',
  telefono: '',
  direccion: '',
  usuario: '',
  password: ''
 };

 isLoading = false;

 registrar():void{
if(this.isLoading) return;
this.isLoading = true;

this.authService.registrar(this.userData)
.pipe(finalize(()=>this.isLoading = false))
.subscribe({
  next: () =>{
    //1. cerrar el modal del registro
    document.getElementById('closeRegisterModal')?.click();
    //alert de éxito
    this.alertService.success('Tu cuenta ha sido creada','¡Registro Exitoso!');
     
    setTimeout(()=>{
      const btnLogin = document.querySelector('[data-bs-target="#loginModal"]')as HTMLElement;
      btnLogin?.click;
    },600);
    this.resetForm();
  },error:(err)=>{
    this.alertService.error(err.error.message || 'Error al registrarse');
  }
});

 }

 resetForm(){
  this.userData= {
    nombre: '',
  email: '',
  telefono: '',
  direccion: '',
  usuario: '',
  password: ''
  }
 }

}
