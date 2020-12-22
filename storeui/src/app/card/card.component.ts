import { Component, OnInit, Input, Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { ServiceService } from '../service.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {

   

  temp:any;
  @Input() view;
  

  profileForm = new FormGroup({
    name: new FormControl('',[Validators.required]),
    price: new FormControl('',[Validators.required]),
    description: new FormControl('',[Validators.required]),
   
  });

  onSubmit() {
    this.service.putdata(this.temp.id,this.profileForm.value).subscribe(response =>{
      this.getProducts();
      
      
    });


  }

  update(id){
    this.service.getdataid(id).subscribe(response=>{
      this.temp = response;
      this.profileForm.setValue({
        name: this.temp.name,
        price:this.temp.price,
        description:this.temp.description
      });
    })


  }

  delete(id){
    this.service.deletedata(id).subscribe(response=>{
      this.getProducts();
    });
    
  }

  

  constructor(private service:ServiceService) { }

  ngOnInit(): void {
    this.getProducts();  
  }

  getProducts(){
    this.service.getdata().subscribe(response=>{
      this.view = response;
    })
  }

}
