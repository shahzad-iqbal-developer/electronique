import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ServiceService } from '../service.service';

@Component({
  selector: 'app-main-screen',
  templateUrl: './main-screen.component.html',
  styleUrls: ['./main-screen.component.css']
})
export class MainScreenComponent implements OnInit {

  constructor(private service:ServiceService) { }

  ngOnInit(): void {
  }

  products:any[]=[];

  profileForm = new FormGroup({
    name: new FormControl('',[Validators.required]),
    price: new FormControl('',[Validators.required]),
    description: new FormControl('',[Validators.required]),
   
  });

  onSubmit() {
    this.service.postdata(this.profileForm.value).subscribe(response =>{
      this.service.getdata().subscribe(response=>{
        this.products = response;
        this.profileForm.reset();
      })  
    });
  }

  
}
