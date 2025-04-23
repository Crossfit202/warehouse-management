import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../services/alert.service';
import { Alert } from '../../models/Alert';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit{

  alert: Alert[] = [];

    constructor(private alertService: AlertService) {}

    ngOnInit(): void {
      this.loadAlerts();
    }

    loadAlerts(): void {
      this.alertService.getAllAlerts().subscribe(data => {
        this.alert = data;
      })
    }

}
