import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {map} from 'rxjs/operators';
import { ProductInterface } from './productInterface';

@Injectable({
  providedIn: 'root'
})
export class ServiceService {

  baseUrl = environment.baseUrl;

  constructor(public http:HttpClient) { }

  public postdata(obj):Observable<any>{
    return this.http.post(this.baseUrl+"product", obj);
  
  }

  public getdata():Observable<ProductInterface[]>{
    return this.http.get<ProductInterface[]>(this.baseUrl+"product").pipe(
      map((products:ProductInterface[])=>{
        return products.map(product =>({
            id:product.id,
            name:product.name,
            price:`${product.price}$`,
            description:product.description
        }))
      })
      );
  }

  public getdataid(id):Observable<any>{
    return this.http.get(this.baseUrl+"product/"+id);
  }

  public putdata(id,obj):Observable<any>{
    return this.http.put(this.baseUrl+"product/"+id, obj);
  }

  public deletedata(id):Observable<any>{
    return this.http.delete(this.baseUrl+"product/"+id);
  }


}
