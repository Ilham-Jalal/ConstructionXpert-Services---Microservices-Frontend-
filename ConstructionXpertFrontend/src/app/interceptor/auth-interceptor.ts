import { HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

// Define the interceptor function
export const AuthInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> => {
  let token: string | null = null;

  if (typeof localStorage !== 'undefined') {
    token = localStorage.getItem('jwt');
  }

  console.log("//////////////////////////////:");


  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('token/////:' + token);
    console.log("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    return next(cloned);
  }

  return next(req);
};
