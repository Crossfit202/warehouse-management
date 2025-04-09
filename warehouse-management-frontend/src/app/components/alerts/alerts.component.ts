import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Alert } from '../../models/Alert';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alerts.component.html',
  styleUrl: './alerts.component.css'
})
export class AlertsComponent implements OnInit {

  alerts: Alert[] = [];

  constructor(private alertService: AlertService) { }

  ngOnInit(): void {
    this.loadAlerts();
  }

  loadAlerts(): void {
    this.alertService.getAllAlerts().subscribe(
      data => {
        this.alerts = data;
      }
    )
  }

}
